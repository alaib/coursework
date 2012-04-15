function totalSum = normalized_cross_correlation( img1, img2 , startx, endx, starty, endy)
%normalized_cross_correlation - computes normalized cross-correlation

if nargin < 6
  disp('Please pass all the required parameters to normalized cross-correlation, Exiting');
  quit;
end

totalSum = 0.0;
x1 = startx;
y1 = starty;
x2 = x1 + 64 - 1;
y2 = y1 + 64 - 1;
while true    
  if x2 > endx
      x1 = startx;
      x2 = x1 + 64 - 1;
      y1 = y2 + 1;
      y2 = y1 + 64 - 1;      
  end
  if y2 > endy
      break;
  end  
  
  tempimg1 = img1(x1:x2, y1:y2);
  tempimg2 = img2(x1:x2, y1:y2);
  m1 = mean2(tempimg1);
  m2 = mean2(tempimg2);
  s1 = std2(tempimg1);
  s2 = std2(tempimg2);
  
  s = 0.0;
  count = 0;  
  for i = 1 : size(tempimg1,2)
      for j = 1 : size(tempimg1,1)
          s = s + ( ((tempimg1(j,i) - m1) * (tempimg2(j,i) - m2)) / (s1 * s2));             
          count = count + 1;
      end
  end  
  s = s / (count - 1);
  totalSum = totalSum + s*s;  
  x1 = x2 + 1;
  x2 = x1 + 64 - 1;
end
totalSum = 1 / totalSum;
%disp(totalSum);
end

