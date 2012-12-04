clear; clc;

load('hw1.mat');
X = X';
y = y';
% alpha = 1.0 means lasso and 0.0 means ridge regression
alpha = 0.5; 
maxIter = 100;

[B, FitInfo] = lasso(X, y, 'Alpha', alpha);
%lassoPlot(B, FitInfo, 'PlotType', 'CV');
%disp(FitInfo);

[beta0 beta logLikeArray] = elasticNet(y, X, alpha, 1-alpha, maxIter);

ourBeta = beta;
predBeta = B(:, 1);
