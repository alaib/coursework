clc;
clear;
%% load data
load('hw2.mat', 'X', 'y');

fprintf('Start\n');
%% prepare required variables
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

%% get ADMM method solution
w = solveLogRegFusedLasso(y1, X1, y2, X2);
index = length(w)/2;

%% (X1, y1)
beta0 = 0; % not used
beta = w(1:index);
predY1 = ones(length(y1), 1);
for i = 1 : length(y1)
    predY1(i) = predictY(X1(i,:), beta0, beta);
end
fprintf('Prediction Success of (X1, Y1) with ADMM = %.2f\n', getPredictionSuccess(predY1, y1));

%% (X2, y2)
beta0 = 0; % not used
beta = w(index+1:end);
predY2 = ones(length(y2), 1);
for i = 1 : length(y2)
    predY2(i) = predictY(X2(i,:), beta0, beta);
end
fprintf('Prediction Success of (X2, Y2) with ADMM = %.2f\n', getPredictionSuccess(predY2, y2));


%% get lassoglm solution
%% (X1, y1)
[B, FitInfo] = lassoglm(X1, y1, 'normal', 'Alpha', 1);
index = find(min(FitInfo.Deviance));
beta0 = FitInfo.Intercept(index);
beta = B(:, index);

predY1_glm = ones(length(y1), 1);
for i = 1 : length(y1)
    predY1_glm(i) = predictY(X1(i,:), beta0, beta);
end
fprintf('Prediction Success of (X1, Y1) with GLM = %.2f\n', getPredictionSuccess(predY1_glm, y1));

%% (X2, y2)
[B, FitInfo] = lassoglm(X2, y2, 'normal', 'Alpha', 1);
index = find(min(FitInfo.Deviance));
beta0 = FitInfo.Intercept(index);
beta = B(:, index);

predY2_glm = ones(length(y2), 1);
for i = 1 : length(y2)
    predY2_glm(i) = predictY(X2(i,:), beta0, beta);
end

fprintf('Prediction Success of (X2, Y2) with GLM = %.2f\n', getPredictionSuccess(predY2_glm, y2));
fprintf('Done\n');