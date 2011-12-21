% Image mismatch program
% Read the base image
baseimg = double(imread('../FixedImage.png'));
maxx = size(baseimg,1);
maxy = size(baseimg,2);

% Generate the intensity transformation
transform_choice = 1;
gauss_sigma = 10;
movimg1 = intensity_transformation(baseimg, 1, 0, gauss_sigma);
movimg2 = intensity_transformation(baseimg, 1, 20, gauss_sigma);
movimg3 = intensity_transformation(baseimg, -1, 1100, gauss_sigma);
movimg4 = intensity_transformation(baseimg, 'gauss', 0, gauss_sigma);
switch transform_choice
    case 1
        movimg = movimg1;
    case 2
        movimg = movimg2;
    case 3
        movimg = movimg3;
    case 4
        movimg = movimg4;
end
% Compute the moving image which is intensity transformed and translated
tempimg = zeros(maxx,maxy);
initval = 128;
startx = 128;
starty = 128;
deltax = randi([-100 100],1);
deltaxArr = zeros(1,initval*2+1);
mismatchArr = zeros(1,initval*2+1);
fprintf('Chosen deltax = %d\n',deltax);
%Compute mismatch at deltax
T = [1 0 0; 0 1 0; deltax 0 1];
tform = maketform('affine', T);    
[tempimg, xdata, ydata] = imtransform(movimg, tform, ...
                              'XData', [1 size(movimg,2)], 'YData', [1 size(movimg,1)]);
mismatch_val2 = mi(baseimg(startx+1:startx+256,starty+1:starty+256), ...
                                             tempimg(startx+1:startx+256,starty+1:starty+256));                              
deltaxArr(1) = deltax;
mismatchArr(1) = mismatch_val2;
%Compute mismatch at deltax-1
T = [1 0 0; 0 1 0; deltax-1 0 1];
tform = maketform('affine', T);    
[tempimg, xdata, ydata] = imtransform(movimg, tform, ...
                              'XData', [1 size(movimg,2)], 'YData', [1 size(movimg,1)]);
mismatch_val1 = mi(baseimg(startx+1:startx+256,starty+1:starty+256), ...
                                             tempimg(startx+1:startx+256,starty+1:starty+256));                              
%Compute mismatch at deltax+1
T = [1 0 0; 0 1 0; deltax+1 0 1];
tform = maketform('affine', T);    
[tempimg, xdata, ydata] = imtransform(movimg, tform, ...
                              'XData', [1 size(movimg,2)], 'YData', [1 size(movimg,1)]);
mismatch_val3 = mi(baseimg(startx+1:startx+256,starty+1:starty+256), ...
                                             tempimg(startx+1:startx+256,starty+1:starty+256));                                
skipFlag = false;                              
if mismatch_val3 > mismatch_val2 && mismatch_val2 > mismatch_val1
    step = -1;
    curr_val = mismatch_val1;
    deltax = deltax+step;
elseif mismatch_val3 < mismatch_val2 && mismatch_val2 < mismatch_val1
    step = 1;        
    curr_val = mismatch_val3;
    deltax = deltax+step;
else
    skipFlag = true;
    step = 1;
    curr_val = mismatch_val2;
end
if ~skipFlag
    deltaxArr(2) = deltax;
    mismatchArr(2) = curr_val;
end
deltax = deltax + step;
count = 3;
while true    
    T = [1 0 0; 0 1 0; deltax 0 1];
    tform = maketform('affine', T);    
    [tempimg, xdata, ydata] = imtransform(movimg, tform, ...
                                  'XData', [1 size(movimg,2)], 'YData', [1 size(movimg,1)]);
    next_val = mi(baseimg(startx+1:startx+256,starty+1:starty+256), ...
                                             tempimg(startx+1:startx+256,starty+1:starty+256));    
    if next_val > curr_val || deltax < -128 || deltax > 128
        %fprintf('deltax = %d, val = %f\n',deltax,next_val);
        deltax = deltax - step;
        count = count - 1;
        break
    end
    deltaxArr(count) = deltax;
    mismatchArr(count) = next_val;
    %fprintf('deltax = %d, val = %f\n',deltax,next_val);
    curr_val = next_val;
    deltax = deltax+step;
    count = count + 1;
end

x = zeros(1,count);
y = zeros(1,count);
for i = 1 : count
    x(i) = deltaxArr(i);
    y(i) = mismatchArr(i);
end
fprintf('startx = %d\n', deltaxArr(1));
fprintf('endx = %d\n', deltaxArr(count));
fprintf('mismatch = %e\n', mismatchArr(count));

figure;
axis on;
xlabel('Translation Value - deltax');
yl = char(strcat('Sum of Square Intensity Diff with Transform ',int2str(transform_choice)));
ylabel(yl);
hold on;
plot(x,y);
