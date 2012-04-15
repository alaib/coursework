%Bilinear Interpolation: function to perform bilinear interpolation

function [interpol] = bilinear(gradX, gradY, samplePoints)
    interpol = zeros(size(samplePoints));
    for i=1:size(samplePoints,1)
        
        f = floor(samplePoints(i,:));
        d = samplePoints(i,:) - f;
        
        g00 = [gradX(f(1),f(2)), gradY(f(1),f(2))];
        g01 = [gradX(f(1), f(2)+1), gradY(f(1), f(2))];        
        g10 = [gradX(f(1)+1,f(2)), gradY(f(1)+1,f(2))];
        g11 = [gradX(f(1)+1, f(2)+1), gradY(f(1)+1, f(2)+1)];                
        
        i1 = g00*(1-d(2)) + g01*d(2);
        i2 = g10*(1-d(2)) + g11*d(2);                        
        
        interpol(i,:) = i1*(1-d(1)) + i2*d(1);        
    end
end