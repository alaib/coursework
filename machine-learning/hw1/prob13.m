beta0 = 1; beta = [1 1 1 1 0 0 0 0]';
N=100;
p = length(beta);
X = rand(N,p);
y = beta0 + X*beta;
[dbeta0, dbeta] = dLogLikLogReg(y, X, beta0, beta);
d = dfLogLik(y, X, beta0, beta);
dn = zeros(1, size(beta, 1));
for i = 1 : length(dn)
    dn(i) = dfLogLikN(y, X, beta0, beta, i);
end

display(dbeta0);
display(d);
display(dbeta');
display(dn);
