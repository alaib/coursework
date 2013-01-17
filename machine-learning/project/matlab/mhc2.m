clc;
clear;
load('train_encoded.mat');
load('test_encoded.mat');
alpha = 0.01;
lambda = 1 - alpha;
maxIter = 1000;

[beta0 beta logLikeArray] = elasticNet(trainY, trainX, lambda, alpha, maxIter);

%figure;
%title('Log Likelihood');
%plot(logLikeArray);

predictY = beta0 + testX * beta;
match = 0;
pY = zeros(size(predictY, 1), 1);
for i = 1 : size(predictY, 1)
    if(predictY(i) > 0.5)
        pY(i) = 1;
    else
        pY(i) = 0;
    end
    if(pY(i) == testY(i))
        match = match + 1;
    end    
end

fprintf('Prediction Success Rate = %.2f\n', match/size(testY,1)*100);
fprintf('Final Log Likelihood Value = %.4f\n', max(logLikeArray));



