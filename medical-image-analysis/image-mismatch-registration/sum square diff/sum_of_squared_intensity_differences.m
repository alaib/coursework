function sum = sum_of_squared_intensity_differences( img1, img2 , startx, endx, starty, endy)
%sum_of_intensity_differences - Computes the sum of squared intensity
%differences from (startx, starty) to (endx, endy)
if nargin < 6
  disp('Please pass all the required parameters to sum_of_squared_intensity_differences, Exiting');
  quit;
end

sum = 0.0;
t1 = 0.0;
t2 = 0.0;
term = 0.0;
maxterm = 0.0;
for i = starty : endy
    for j = startx : endx
        t1 = img1(j,i);
        t2 = img2(j,i);
        term = t1 - t2;
        term = term * term;
        if maxterm < term
            maxterm = term;
        end
        sum = sum + term;
    end
end
%disp(sum);
end

