clc; clear;
hla1 = 'HLA-DRB1-0401';
hla2 = 'HLA-DRB1-0404';

savedFileName = strcat('savedWang/', hla1, '_', hla2, '.mat');
%savedFileName = strcat('savedWang2/', hla1, '_', hla2, '_admm_svm.mat');
load(savedFileName);

% HLA1
[tprs_hla1_admm, fprs_hla1_admm] = roc(HLA1Data.testY, predY1_admm);
hla1_admm_score = getScores(HLA1Data.testY, predY1_admm);
[tprs_hla1_svm, fprs_hla1_svm] = roc(HLA1Data.testY, predY1_svm);
hla1_svm_score = getScores(HLA1Data.testY, predY1_svm);
[tprs_hla1_glm, fprs_hla1_glm] = roc(HLA1Data.testY, predY1_glm);
hla1_glm_score = getScores(HLA1Data.testY, predY1_glm);

lineWidth = 2;
figure;
hold on;
title(hla1);
xlabel('False Positive Rate (Specificity)');
ylabel('True Positive Rate (Sensitivity)');
plot(fprs_hla1_admm, tprs_hla1_admm, 'LineWidth', lineWidth, 'Color', 'g');
hold on;
plot(fprs_hla1_svm, tprs_hla1_svm, 'LineWidth', lineWidth, 'Color', 'b');
hold on;
plot(fprs_hla1_glm, tprs_hla1_glm, 'LineWidth', lineWidth, 'Color', 'r');
legend('Fused ADMM', 'SVM', 'GLMNET', 'Location', 'SouthEast');

% HLA2
[tprs_hla2_admm, fprs_hla2_admm] = roc(HLA2Data.testY, predY2_admm);
hla2_admm_score = getScores(HLA2Data.testY, predY2_admm);
[tprs_hla2_svm, fprs_hla2_svm] = roc(HLA2Data.testY, predY2_svm);
hla2_svm_score = getScores(HLA2Data.testY, predY2_svm);
[tprs_hla2_glm, fprs_hla2_glm] = roc(HLA2Data.testY, predY2_glm);
hla2_glm_score = getScores(HLA2Data.testY, predY2_glm);

figure;
hold on;
title(hla2);
xlabel('False Positive Rate (Specificity)');
ylabel('True Positive Rate (Sensitivity)');
plot(fprs_hla2_admm, tprs_hla2_admm, 'LineWidth', lineWidth, 'Color', 'g');
hold on;
plot(fprs_hla2_svm, tprs_hla2_svm, 'LineWidth', lineWidth, 'Color', 'b');
hold on;
plot(fprs_hla2_glm, tprs_hla2_glm, 'LineWidth', lineWidth, 'Color', 'r');
legend('Fused ADMM', 'SVM', 'GLMNET', 'Location', 'SouthEast');