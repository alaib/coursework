function totalSum = quantile_function_diff( img1, img2 , startx, endx, starty, endy, normalized)
%normalized_cross_correlation - computes normalized cross-correlation

if nargin < 6
  disp('Please pass all the required parameters to normalized cross-correlation, Exiting');
  quit;
end

if nargin < 7
    normalized = 0;
end
quantDist = [0.0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0];
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
  % take transpose and reshape into a one dimension array
  tempimg1 = reshape(img1(x1:x2, y1:y2)', 1, 64*64);
  tempimg2 = reshape(img2(x1:x2, y1:y2)', 1, 64*64);
  quant1 = quantile(tempimg1, quantDist);
  quant2 = quantile(tempimg2, quantDist);
  if normalized
      % Normalize by (q - q(0.5)) / (q(0.7) - q(0.3))
      quant1 = (quant1 - quant1(6)) / (quant1(8) - quant1(4));
      quant2 = (quant2 - quant2(6)) / (quant2(8) - quant2(4));
  end
  term = sum(abs(quant1-quant2));
  totalSum = totalSum + term;
  
  x1 = x2 + 1;
  x2 = x1 + 64 - 1;
end
end
