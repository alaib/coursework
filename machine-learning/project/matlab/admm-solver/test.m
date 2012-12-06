load('hw2.mat', 'X', 'y');
X1 = X;
X2 = X * 0.5;
y1 = y;
y2 = zeros(length(y1), 1);
for i = 1 : length(y2)
    if(i < length(y2)/2)
        y2(i) = y1(i);
    else
        y2(i) = -1 * y1(i);
    end
    
end

w = solveLogRegFusedLasso(y1, X1, y2, X2);
