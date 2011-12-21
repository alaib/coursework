function output = intensity_transformation(img, alpha, beta, sigma)
%intensity_transformation - produces the intensity transformation image
if nargin < 4
  disp('Please pass all the required parameters to intensity_transformation function, Exiting');
  quit;
end
length = size(img, 1);
width = size(img, 2);
gauss_noise = imnoise(img, 'gaussian', 0, sigma*sigma);

output = zeros(length, width);
alphaType = class(alpha);
if ~strcmp(alphaType, 'char')
    for i = 1 : length
        for j = 1 : width
            output(j,i) = alpha * img(j,i) + beta + gauss_noise(j,i);
        end
    end
else
    g = fspecial('gaussian', [512 512], 300);
    img_gauss = imfilter(img, g, 'replicate');
    for i = 1 : length
        for j = 1 : width
            output(j,i) = img_gauss(j,i) * img(j,i) + beta + gauss_noise(j,i);
        end
    end
end
end

