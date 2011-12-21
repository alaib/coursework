% Read all the points
% pts contains all the 21 point sets (21x68x2)      
% (:,:,1) - contains x values
% (:,:,2) - contains y values
files=dir('./dat/*.pts');
for i = 1: length(files)
    fName = strcat('./dat/',files(i).name);
    pts(i,:,:) = readPoints(fName);    
end

% backup the original points read
orig_pts = pts;

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
drawFaceParts(mean_of_sets, 'g-');

% Overlap it with the translated set of original points
colors = {'r-', 'k-', 'y-', 'c-'};
for i = 1 : 1  
    drawFaceParts(squeeze(pts(i,:,:)), colors{i});
end

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

% Extract the first three eigen values and eigen modes of variation
eigen_values = latent(1:3);
eigen_modes = COEFF(:,1:3);

% Compute the 2 Standard deviations away face from 1st Eigen Vector
two_sigma_face = zeros(size(mean_of_sets,1), 2);
for i = 1 : size(mean_of_sets)       
    two_sigma_face(i,1) = mean_of_sets(i,1) + 2 * sqrt(eigen_values(1)) * eigen_modes(2*i-1,1);
    two_sigma_face(i,2) = mean_of_sets(i,2) + 2 * sqrt(eigen_values(1)) * eigen_modes(2*i,1);                          
end

% Plot the mean face and the face which is two standard deviations away
% from 1st eigen vector
figure;
drawFaceParts(mean_of_sets, 'g-');
drawFaceParts(two_sigma_face, 'r-');
