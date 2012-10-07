clc
beta0 = 1; beta = [1 1 1 1 0 0 0 0]';
N=100;
p = length(beta);
x = rand(N,p);
y = beta0 + x*beta;
%[fitSigma,fitBeta0,fitBeta] = coordAscent(y,x,[])
k = size(x,2);
newBeta0 = mean(y);
newBeta = 1e-6*ones(k,1);
dLogLikLogReg1(y, x, newBeta0, newBeta);
%% print ground truth for noise-free
if 0
fprintf('Ground truth parameter values\n');
beta0
beta
[fitSigma,fitBeta0,fitBeta] = coordAscent(y,x,[])




%% fitting without noise
fprintf('Learned paramaters from noise-free synthetic data\n');
[fitSigma,fitBeta0,fitBeta] = coordAscent(y,x,[])

clc
%% add some noise
fprintf('Ground truth parameter values\n');
beta0
beta
sigma = sqrt(0.1)

fprintf('Fitting noisy synthetic data\n');
noise = sigma*randn(length(x),1);
y = y + noise;
fprintf('Learned parameters from noisy data\n')
[fitSigma,fitBeta0,fitBeta] = coordAscent(y,x,[])
end