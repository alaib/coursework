% Script to generate dark ellipses on white background given center, major
% axes and minor axes.

% initialize the dimensions of the image
ymax = 100;
xmax = 100;
zmax = 100;

% initialize the image with above dimensions
image = zeros(zmax, ymax, xmax);

% initialize center and axes of the ellipsoid
cx = 50;
cy = 40;
cz = 50;
rx = 20;
ry = 30;
rz = 20;

% for each pixel check,
%     if (((x-cx)/rx)^2 + ((y-cy)/ry)^2) + ((y-cz)/rz)^2) < 1,
%     yes -> mark that pixel as 0 (black)
%     no  -> mark that pixel as 255 (white)
for x = 1:xmax
    for y = 1:ymax
        for z = 1:zmax
            if (((x-cx)/rx)^2 + ((y-cy)/ry)^2 + ((z-cz)/rz)^2) < 1                
                image(z,y,x) = 0;
            else                            
                image(z,y,x) = 255;
            end
        end
    end
end

% write out the file
filename = 'generated_ellipsoid_01.mhd';
writeMETA(image, filename);