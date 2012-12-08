hla1 = 'HLA-DRB1-1101';
hla2 = 'HLA-DRB1-1302';

trainMatFile1 = strcat('/home/ravikirn/mlcode/data/wang-paper-data/matfiles/', hla1, '_train_encoded.mat');
testMatFile1 = strcat('/home/ravikirn/mlcode/data/wang-paper-data/matfiles/', hla1, '_test_encoded.mat');
load(trainMatFile1);
load(testMatFile1);

HLA1Data = struct('trainY', trainY, 'trainX', trainX, 'testY', testY, 'testX', testX);

trainMatFile2 = strcat('/home/ravikirn/mlcode/data/wang-paper-data/matfiles/', hla2, '_train_encoded.mat');
testMatFile2 = strcat('/home/ravikirn/mlcode/data/wang-paper-data/matfiles/', hla2, '_test_encoded.mat');
load(trainMatFile2);
load(testMatFile2);

HLA2Data = struct('trainY', trainY, 'trainX', trainX, 'testY', testY, 'testX', testX);

%clear variables
clear trainMatFile1 trainMatFile2 testMatFile1 testMatFile2 trainY trainX testY testX

fprintf('HLA1 = %s, HLA2 = %s\n', hla1, hla2);

%% get ADMM method solution
fprintf('Started Fused Lasso ADMM Solver\n');
w = solveLogRegFusedLasso(HLA1Data.trainY, HLA1Data.trainX, HLA2Data.trainY, HLA2Data.trainX);
fprintf('Ended Fused Lasso ADMM Solver\n');
index = length(w)/2;

%% (X1, y1)
beta0_hla1_admm = 0; % not used
beta_hla1_admm = w(1:index);
predY1_admm = ones(length(HLA1Data.testY), 1);
for i = 1 : length(HLA1Data.testY)
    predY1_admm(i) = predictY(HLA1Data.testX(i,:), beta0_hla1_admm, beta_hla1_admm);
end
fprintf('Prediction Success of HLA1 = %s with ADMM = %.2f\n', ... 
        hla1, getPredictionSuccess(predY1_admm, HLA1Data.testY));

%% (X2, y2)
beta0_hla2_admm = 0; % not used
beta_hla2_admm = w(index+1:end);
predY2_admm = ones(length(HLA2Data.testY), 1);
for i = 1 : length(HLA2Data.testY)
    predY2_admm(i) = predictY(HLA2Data.testX(i,:), beta0_hla2_admm, beta_hla2_admm);
end
fprintf('Prediction Success of HLA2 = %s with ADMM = %.2f\n', ... 
        hla2, getPredictionSuccess(predY2_admm, HLA2Data.testY));

%% get SVM solution
%% (X1, y1)
fprintf('Started SVM Solver for HLA1 Data\n');
options = optimset('maxiter', 1000);
kernel_func = 'linear';
SVMStruct_hla1 = svmtrain(HLA1Data.trainX, HLA1Data.trainY, ... 
                 'kernel_function', kernel_func, 'Method','QP', 'quadprog_opts',options);
predY1_svm = svmclassify(SVMStruct_hla1, HLA1Data.testX);

fprintf('Prediction Success of HLA1 = %s with SVM = %.2f\n', ...
        hla1, getPredictionSuccess(predY1_svm, HLA1Data.testY));
%% (X2, y2)    
fprintf('Started SVM Solver for HLA2 Data\n');
SVMStruct_hla2 = svmtrain(HLA2Data.trainX, HLA2Data.trainY, ...
                 'kernel_function', kernel_func, 'Method','QP', 'quadprog_opts',options);
predY2_svm = svmclassify(SVMStruct_hla2, HLA2Data.testX);

fprintf('Prediction Success of HLA2 = %s with SVM = %.2f\n', ...
        hla2, getPredictionSuccess(predY2_svm, HLA2Data.testY));


%% lassoglm is slow, so do an intermediate save
%% Save to file
saveFile = 1;
savedFileName = strcat('savedWang/', hla1, '_', hla2, '_admm_svm.mat');
if saveFile == 1 && exist(savedFileName, 'file') == 0
    save(savedFileName);
end

%% get lassoglm solution
savedFileName = strcat('savedWang/', hla1, '_', hla2, '.mat');
if exist(savedFileName, 'file') == 0
    %% (X1, y1)
    fprintf('Started LASSO GLM Solver for HLA1 Data\n');
    [B_hla1, FitInfo_hla1] = lassoglm(HLA1Data.trainX, HLA1Data.trainY, 'normal', 'Alpha', 1);
    fprintf('Ended LASSO GLM Solver for HLA1 Data\n');
    index_hla1_glm = find(min(FitInfo_hla1.Deviance));
    beta0_hla1_glm = FitInfo_hla1.Intercept(index_hla1_glm);
    beta_hla1_glm = B_hla1(:, index_hla1_glm);

    predY1_glm = ones(length(HLA1Data.testY), 1);
    for i = 1 : length(HLA1Data.testY)
        predY1_glm(i) = predictY(HLA1Data.testX(i,:), beta0_hla1_glm, beta_hla1_glm);
    end
    fprintf('Prediction Success of HLA1 = %s with GLM = %.2f\n', ...
            hla1, getPredictionSuccess(predY1_glm, HLA1Data.testY));

    %% (X2, y2)
    fprintf('Started LASSO GLM Solver for HLA2 Data\n');
    [B_hla2, FitInfo_hla2] = lassoglm(HLA2Data.trainX, HLA2Data.trainY, 'normal', 'Alpha', 1);
    fprintf('Ended LASSO GLM Solver for HLA2 Data\n');
    index_hla2_glm = find(min(FitInfo_hla2.Deviance));
    beta0_hla2_glm = FitInfo_hla2.Intercept(index_hla2_glm);
    beta_hla2_glm = B_hla2(:, index_hla2_glm);

    predY2_glm = ones(length(HLA2Data.testY), 1);
    for i = 1 : length(HLA2Data.testY)
        predY2_glm(i) = predictY(HLA2Data.testX(i,:), beta0_hla2_glm, beta_hla2_glm);
    end
    fprintf('Prediction Success of HLA2 = %s with GLM = %.2f\n', ...
            hla2, getPredictionSuccess(predY2_glm, HLA2Data.testY));
else
    fprintf('%s  (lasso glm solution) exists, loading it, instead of computing it again\n',...
            savedFileName);
    load(savedFileName);
    %% (X1, y1)   
    index_hla1_glm = find(min(FitInfo_hla1.Deviance));
    beta0_hla1_glm = FitInfo_hla1.Intercept(index_hla1_glm);
    beta_hla1_glm = B_hla1(:, index_hla1_glm);

    predY1_glm = ones(length(HLA1Data.testY), 1);
    for i = 1 : length(HLA1Data.testY)
        predY1_glm(i) = predictY(HLA1Data.testX(i,:), beta0_hla1_glm, beta_hla1_glm);
    end
    fprintf('Prediction Success of HLA1 = %s with GLM = %.2f\n', ...
            hla1, getPredictionSuccess(predY1_glm, HLA1Data.testY));
    %% (X2, y2)            
    index_hla2_glm = find(min(FitInfo_hla2.Deviance));
    beta0_hla2_glm = FitInfo_hla2.Intercept(index_hla2_glm);
    beta_hla2_glm = B_hla2(:, index_hla2_glm);

    predY2_glm = ones(length(HLA2Data.testY), 1);
    for i = 1 : length(HLA2Data.testY)
        predY2_glm(i) = predictY(HLA2Data.testX(i,:), beta0_hla2_glm, beta_hla2_glm);
    end
    fprintf('Prediction Success of HLA2 = %s with GLM = %.2f\n', ...
            hla2, getPredictionSuccess(predY2_glm, HLA2Data.testY));
end
%% Done
fprintf('Done\n');

%% Save to file
saveFile = 1;
savedFileName = strcat('savedWang/', hla1, '_', hla2, '.mat');
if saveFile == 1 && exist(savedFileName, 'file') == 0
    save(savedFileName);
end