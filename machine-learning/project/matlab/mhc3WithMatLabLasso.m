
clc;
%clear;
% Load files
type = 'HLA-DRB10401';
trainMatFile = strcat('/home/ravikirn/mlcode/data/MHCBN-15mers/matfiles/', type, '_train_encoded.mat');
testMatFile = strcat('/home/ravikirn/mlcode/data/MHCBN-15mers/matfiles/', type, '_test_encoded.mat');
load(trainMatFile);
load(testMatFile);


alpha = 1.0;
maxIter = 1000;

% User cross-validation
[B, FitInfo] = lasso(trainX, trainY, 'Alpha', alpha);
index = find(min(FitInfo.MSE));
  
predBeta = B(:, index);
predBeta0 = FitInfo.Intercept(index);

predictY = predBeta0 + testX * predBeta;
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