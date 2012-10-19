clc
beta0 = 1; beta = [1 1 1 1 0 0 0 0]';
N=100;
p = length(beta);
X = rand(N,p);
y = beta0 + X * beta;
for i = 1 : N
    y = exp(-1 * y(:,i)' * (beta0 + X * beta)); 
end
if 0
%% print ground truth for noise-free
fprintf('Ground truth parameter values\n');
beta0
beta
pause
%% fitting without noise
fprintf('Learned paramaters from noise-free synthetic data\n');
[fitSigma,fitBeta0,fitBeta] = coordAscent(y,x,[])
pause
clc
%% add some noise
fprintf('Ground truth parameter values\n');
beta0
beta
sigma = sqrt(0.1)
pause
fprintf('Fitting noisy synthetic data\n');
noise = sigma*randn(length(x),1);
y = y + noise;
fprintf('Learned parameters from noisy data\n')
[fitSigma,fitBeta0,fitBeta] = coordAscent(y,x,[])
end