% Image mismatch program
% Read the base image
baseimg = double(imread('../FixedImage.png'));
maxx = size(baseimg,1);
maxy = size(baseimg,2);

% Generate the intensity transformation
gauss_sigma = 10;
movimg1 = intensity_transformation(baseimg, 1, 0, gauss_sigma);
movimg2 = intensity_transformation(baseimg, 1, 20, gauss_sigma);
movimg3 = intensity_transformation(baseimg, -1, 1100, gauss_sigma);
movimg4 = intensity_transformation(baseimg, 'gauss', 0, gauss_sigma);

% Compute the moving image which is intensity transformed and translated
tempimg = zeros(maxx,maxy);
initval = 128;
startx = 128;
starty = 128;
deltax = -initval:initval;
mismatch_arr = zeros(4,size(deltax,2));
fprintf('%s', 'Percentage done = ');
for i = 1:size(deltax,2) 
    T = [1 0 0; 0 1 0; deltax(i) 0 1];
    tform = maketform('affine', T);    
    [tempimg1, xdata1, ydata1] = imtransform(movimg1, tform, ...
                              'XData', [1 size(movimg1,2)], 'YData', [1 size(movimg1,1)]);    
    [tempimg2, xdata2, ydata2] = imtransform(movimg2, tform, ...
                              'XData', [1 size(movimg2,2)], 'YData', [1 size(movimg2,1)]);   
    [tempimg3, xdata3, ydata3] = imtransform(movimg3, tform, ...
                              'XData', [1 size(movimg3,2)], 'YData', [1 size(movimg3,1)]);   
    [tempimg4, xdata4, ydata4] = imtransform(movimg4, tform, ...
                              'XData', [1 size(movimg4,2)], 'YData', [1 size(movimg4,1)]);                           
    %figure;
    %imshow(tempimg,'XData',xdata,'YData',ydata);        
    mismatch_arr(1,deltax(i)+initval+1) = normalized_cross_correlation( baseimg, ...
                                  tempimg1, startx+1, startx+256, starty+1, starty+256);    
    mismatch_arr(2,deltax(i)+initval+1) = normalized_cross_correlation( baseimg, ...
                                  tempimg2, startx+1, startx+256, starty+1, starty+256);               
    mismatch_arr(3,deltax(i)+initval+1) = normalized_cross_correlation( baseimg, ...
                                  tempimg3, startx+1, startx+256, starty+1, starty+256);  
    mismatch_arr(4,deltax(i)+initval+1) = normalized_cross_correlation( baseimg, ...
                                  tempimg4, startx+1, startx+256, starty+1, starty+256);                                
    % Display percentage done    
    if mod(deltax(i),30) == 0        
        perc = uint16((i / size(deltax,2)) * 100);
        fprintf('%d%s',perc,'...');        
    end    
end
fprintf('%d\n', 100);

for i = 1 : 4
    s = char(strcat('Transform ',int2str(i),' Global Minimum = '));
    fprintf('%s %e\n', s,min(mismatch_arr(i,:)));
end

figure;
axis on;
xlabel('Translation Value - deltax');
ylabel('Normalized Cross Correlation with Transformation 1');
hold on;
plot(deltax, mismatch_arr(1,:));

figure;
axis on;
xlabel('Translation Value - deltax');
ylabel('Normalized Cross Correlation with Transformation 2');
hold on;
plot(deltax, mismatch_arr(2,:));

figure;
axis on;
xlabel('Translation Value - deltax');
ylabel('Normalized Cross Correlation with Transformation 3');
hold on;
plot(deltax, mismatch_arr(3,:));

figure;
axis on;
xlabel('Translation Value - deltax');
ylabel('Normalized Cross Correlation with Transformation 4');
hold on;
plot(deltax, mismatch_arr(3,:));

disp('Done');