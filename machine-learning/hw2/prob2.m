load('hw2_test.mat');
y = y';
X = X';
val = computeAL(y, X, D, lambda, mu, rho, w, z0, z1, z2, u0, u1, u2);