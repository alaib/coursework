% Script to generate dark ellipses on white background given center, major
% axes and minor axes.

% initialize image dimensions
maxx = 300;
maxy = 200;
maxz = 200;

% initialize the image with above dimensions
image = zeros(maxx, maxy, maxz);

% initialize center, major axes and minor axes of the ellipse
cx = 50;
cy = 80;
rx = 40;
ry = 30;

% for each pixel check,
%     if (((x-cx)/rx)^2 + ((y-cy)/ry)^2) < 1,
%     yes -> mark that pixel as 0 (black)
%     no  -> mark that pixel as 255 (white)
for x = 1:maxx
    for y = 1:maxy
            if ( ((x-cx)/rx)^2 + ((y-cy)/ry)^2 ) < 1
                % y - row, x - column
                image(x,y,:) = 0;
            else            
                %image(y,x) = randi([200, 255],1);
                image(x,y,:) = 255;
            end
        end
end

% write out the file
filename = 'generated_ellipse_01.mhd';
writeMETA(image, filename);