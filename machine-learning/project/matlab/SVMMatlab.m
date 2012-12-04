%clc;
clear;

% Load files
type = 'HLA-DR1';
trainMatFile = strcat('/home/ravikirn/mlcode/data/MHCBN-15mers/matfiles/', type, '_train_encoded.mat');
testMatFile = strcat('/home/ravikirn/mlcode/data/MHCBN-15mers/matfiles/', type, '_test_encoded.mat');
load(trainMatFile);
load(testMatFile);

SVMStruct = svmtrain(trainX, trainY, 'kernel_function', 'linear');
predY = svmclassify(SVMStruct, testX);

match = 0;
for i = 1 : size(testY, 1)
    if(predY(i) == testY(i))
        match = match + 1;
    end
end

fprintf('Prediction Success Rate = %.2f\n', match/size(testY,1)*100);
    