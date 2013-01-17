clc;
clear;

% Ground truth params
beta0 = 1;
beta = [1 1 1 1 0 0 0 0]';
N=100;
p = length(beta);
x = rand(N,p);
y = beta0 + x*beta;


%Elastic Net
lambda = 0.5;
alpha = 0.5;
min = realmax;

for lambda = 0.0:0.1:1.0
    alpha = 1 - lambda;
    [fitBeta0,fitBeta, logLikArray] = elasticNet(y, x, lambda, alpha);
    diff = abs(beta0-fitBeta0) + sum(abs(beta-fitBeta));
    if(min > diff)
        min = diff;
        bestAlpha = alpha;
        bestLambda = lambda;
        bestBeta0 = beta0;
        bestBeta = beta;
        bestLogLikArray = logLikArray;
    end  
end

fprintf('Ground truth parameter values\n');
display(beta0);
display(beta);

y1 = bestBeta0 + x * bestBeta;
fprintf('Predicted parameter values\n');
display(bestBeta0);
display(bestBeta);
display(alpha);
display(lambda);
display(min);

figure;
title('Log Likelihood Plot');
plot(bestLogLikArray);