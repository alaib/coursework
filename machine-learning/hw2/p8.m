load('hw2.mat');
w = solveLogRegFusedLasso(y, X, D, lambda, mu);
display('w = ');
display(w);