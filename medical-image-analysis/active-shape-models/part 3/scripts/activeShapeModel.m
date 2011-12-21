% Read all the points
% pts contains all the 21 point sets (21x68x2)      
% (:,:,1) - contains x values
% (:,:,2) - contains y values
files=dir('../trainPDM/*.pts');

for i = 1: length(files)
    fName = strcat('../trainPDM/',files(i).name);    
    pts(i,:,:) = readPoints(fName);    
end

% backup the original points read
orig_pts = pts;
mean_orig_pts = squeeze(mean(pts));
% compute the mean of original points, used for translating back to mean
% and object space
mean_x = mean(mean_orig_pts(:,1));
mean_y = mean(mean_orig_pts(:,2));

% Compute Center of Gravity (Mean) of each point set and subtract each one
% of the points from the center of gravity of the same set
for i = 1 : size(pts, 1)
    cg_pts = squeeze(mean(pts(i,:,:)));
    for j = 1 : size(pts, 2)
        pts(i,j,1) = pts(i,j,1) - cg_pts(1);
        pts(i,j,2) = pts(i,j,2) - cg_pts(2);
    end
end

% Generate the mean for all the sets of points and draw it
mean_of_sets = squeeze(mean(pts));

%Subtract the mean_of_sets from each of the point sets
for i = 1 : size(pts, 1)    
    for j = 1 : size(pts, 2)
        pts(i,j,1) = pts(i,j,1) - mean_of_sets(j,1);
        pts(i,j,2) = pts(i,j,2) - mean_of_sets(j,2);
    end
end

% We have 21 sets of points and 68 points are present in each set ,
% in order to do principal component analysis, we need 21 x 136 (68x2) 
% matrix,  where, the matrix is as below:-
% 1 - [x1 y1 x2 y2 ..... x68 y68]
% 2 - [x1 y1 x2 y2 ..... x68 y68]
% ...............................
% 21- [x1 y1 x2 y2 ..... x68 y68]
mean_sub_set = zeros(size(pts,1), size(pts,2) * size(pts,3));
for i = 1 : size(pts, 1)
    for j = 1 : size(pts, 2)        
 % eg:- mean_sub_set(1,1) =  pts(1,1,1) and mean_sub_set(1,2) = pts(1,1,2);
        mean_sub_set(i,2*j-1) = pts(i,j,1);
        mean_sub_set(i,2*j) = pts(i,j,2);
    end
end

% Perform Principal Component Analysis on the above mean_subtracted set
% COEFF - A p-by-p matrix, each column containing coefficients for one 
% principal component. The columns are in order of decreasing component
% variance
% SCORE - the principal component scores
% latent - a vector containing the eigenvalues of the covariance matrix of X
[COEFF,SCORE,latent] = princomp(mean_sub_set);

% Extract the eigen values and eigen modes of variation
eigen_values = latent(1:2);
eigen_modes = COEFF(:,1:2);

%overlayPDM2MR('../trainPDM/92.pts','../trainImg/92.2d.mhd');
meanPDM = mean_of_sets;

%Compute the gradient of the target image
% Target Image
files=dir('../testImg/*.mhd');
for i = 1: length(files)
    meanPDM = mean_of_sets;
    fileName = files(i).name;
    pathName = strcat('../testImg/',fileName);           
    targImg = loadMETA(pathName);
    [gradX, gradY] = gradient(targImg);    
    flag = true;
    nIter = 1;
    maxIter = 1000;
    no_of_points = size(meanPDM,1);
    finalPDM = zeros(no_of_points,2);    
    % Iterate until maxIter is reached or no change occurs in PDM
    while flag
        % Compute the normals
        normals = lineNormals2D(meanPDM);          
        newPDM = zeros(no_of_points, 2);
        % Scale back to the object space
        scaledMeanPDM = cat(2,meanPDM(:,1) + mean_x, meanPDM(:,2) + mean_y);
        for i = 1 : no_of_points
            % Sample 13 points along the normal each 1 pixel apart             
            sample_points = zeros(13,2);            
            for j = 1 : 13   
                sample_points(j,:) = scaledMeanPDM(i,:) + (7-j) * normals(i,:);                                                            
            end
            % Use bilinear interpolation to estimate gradients at sample
            % points.
            % Compute geometry to image match i.e, 
            % directional derivative = dot product of interpolated value and
            % normals
            interpol = bilinear(gradX, gradY, sample_points);            
            dirDer = interpol * normals(i,:)';
            
            % Find the maximum directional derivative
            [val ind] = max(abs(dirDer));        
            
            % Replace the point in scaledMeanPDM with that point which had the
            % largest directional derivative (see computation of candidate
            % point)
            scaledMeanPDM(i,:) = scaledMeanPDM(i,:) + (7-ind) * normals(i,:);                          
        end
        
        % Scale it back to mean object space
        newPDM = cat(2,scaledMeanPDM(:,1) - mean_x, scaledMeanPDM(:,2) - mean_y);
        diff = newPDM - mean_of_sets;      

        % eigen_modes = (32x2) , i.e, each column vector is (x1 y1) .. (xn yn)
        % Taking the transpose, eige_modes' becomes 2x32 
        % reshape re-arranges the diff' matrix to 32x1 matrix 
        % On multiplying above you get the beta matrix of 2x1        
        beta = eigen_modes' * reshape(diff',32,1);    

        % Restrict beta values to -3*sigma and +3*sigma 
        %(sigma =sqrt(eigen_values)) , eigen_values = variance
        for i = 1 : 2
            sigma = sqrt(eigen_values(i));
            if beta(i) > 3 * sigma
                beta(i) = 3 * sigma;
            elseif beta(i) < -3 * sigma
                beta(i) = -3 * sigma;
            end
        end    
        
        % Convert shape to PDM (use original mean_of_sets and not meanPDM)
        shapeSpacePDM = mean_of_sets + reshape(eigen_modes*beta,2,16)';      

        % Check if shapeSpacePDM == meanPDM with tolerance level of 0.0001
        tolerance = 0.0001;
        index = abs(shapeSpacePDM - meanPDM) <= tolerance;
        hasNotMoved = all(all(index));

        % If the newPDM didn't change i.e, no point moved, then exit the loop
        % Also exit the loop if no. of iterations reached max.    
        if hasNotMoved || (nIter == maxIter)
            flag = false;   
            %disp(nIter);
            % Translate the result to the object space
            finalPDM = cat(2,shapeSpacePDM(:,1) + mean_x, shapeSpacePDM(:,2) + mean_y);
        else
            meanPDM = shapeSpacePDM;            
            nIter = nIter + 1;            
        end            
    end
    
    % Overlay the final result to the test MR image    
    %overlayPDM2MR(finalPDM,fName,1);
    writePDM(finalPDM,char(strcat('../asmSegImg/',fileName)),pathName);
    disp(fileName);
end
disp('Done');