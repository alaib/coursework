load('hw1.mat')
X = X';
y = y';
lambda = 0.5;
alpha = 0.5;

[beta0 beta logLikeArray iter] = elasticNet(y, X, lambda, alpha);
iterArray = (1:1:iter);

plot(logLikeArray);