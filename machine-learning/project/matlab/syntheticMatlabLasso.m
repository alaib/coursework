clc;
clear;

% Ground truth params
beta0 = 1;
beta = [1 1 1 1 -1 0 0 0]';
N=100;
p = length(beta);
X = rand(N,p);
y = beta0 + X*beta;
alpha = 1.0;

[B, FitInfo] = lasso(X, y, 'Alpha', alpha, 'CV', 10);
index = FitInfo.Index1SE;
predBeta = B(:, index);
predBeta0 = FitInfo.Intercept(index);

predictY = predBeta0 + X * predBeta;
match = 0;

for i = 1 : size(predictY, 1)
    diff = abs(predictY(i)-y(i));
    disp(diff);
    if(diff < 0.1)
        match = match + 1;
    end    
end

fprintf('Prediction Success Rate = %.2f\n', match/size(y,1)*100);