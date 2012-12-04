clc;
clear;
load('train_encoded.mat');
load('test_encoded.mat');
alpha = 1.00;
maxIter = 1000;

[B, FitInfo] = lasso(trainX, trainY, 'Alpha', alpha);

predBeta = B(:,1);
predictY = testX * predBeta;
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




