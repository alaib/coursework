clc; clear;
hla1 = 'HLA-DRB1-0401';
hla2 = 'HLA-DRB1-0901';

savedFileName = strcat('savedWang2/', hla1, '_', hla2, '.mat');
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
fSize = 20;
set(0,'DefaultAxesFontSize',fSize);
f1 = figure(1);
hold on;
title(hla1, 'FontSize', fSize);
xlabel('False Positive Rate (Specificity)');
ylabel('True Positive Rate (Sensitivity)');
plot(fprs_hla1_admm, tprs_hla1_admm, 'LineWidth', lineWidth, 'Color', 'b');
hold on;
plot(fprs_hla1_svm, tprs_hla1_svm, 'LineWidth', lineWidth, 'Color', 'g');
hold on;
plot(fprs_hla1_glm, tprs_hla1_glm, 'LineWidth', lineWidth, 'Color', 'r');
h_legend1 = legend('Fused ADMM', 'SVM', 'GLMNET', 'Location', 'SouthEast');
fname1 = strcat('/home/ravikirn/mlcode/latex/report/images/',hla1,'.png');
print(f1,'-dpng','-r150',fname1)

figure(2);
X_hla1 = {'Precision' 'Recall' 'F1-Score' 'Accuracy'};
Y_hla1 = zeros(4, 3);
Y_hla1(1,:) = [hla1_admm_score.prec, hla1_svm_score.prec, hla1_glm_score.prec];
Y_hla1(2,:) = [hla1_admm_score.recall, hla1_svm_score.recall, hla1_glm_score.recall];
Y_hla1(3,:) = [hla1_admm_score.f1, hla1_svm_score.f1, hla1_glm_score.f1];
Y_hla1(4,:) = [hla1_admm_score.acc, hla1_svm_score.acc, hla1_glm_score.acc];
hold on;
title(hla1);
bar(Y_hla1);
set(gca,'XTick',(1:4));
set(gca,'XTickLabel', X_hla1);
legend('Fused ADMM', 'SVM', 'GLMNET', 'Location', 'NorthEast');


% HLA2
[tprs_hla2_admm, fprs_hla2_admm] = roc(HLA2Data.testY, predY2_admm);
hla2_admm_score = getScores(HLA2Data.testY, predY2_admm);
[tprs_hla2_svm, fprs_hla2_svm] = roc(HLA2Data.testY, predY2_svm);
hla2_svm_score = getScores(HLA2Data.testY, predY2_svm);
[tprs_hla2_glm, fprs_hla2_glm] = roc(HLA2Data.testY, predY2_glm);
hla2_glm_score = getScores(HLA2Data.testY, predY2_glm);

f3 = figure(3);
hold on;
title(hla2);
xlabel('False Positive Rate (Specificity)');
ylabel('True Positive Rate (Sensitivity)');
plot(fprs_hla2_admm, tprs_hla2_admm, 'LineWidth', lineWidth, 'Color', 'b');
hold on;
plot(fprs_hla2_svm, tprs_hla2_svm, 'LineWidth', lineWidth, 'Color', 'g');
hold on;
plot(fprs_hla2_glm, tprs_hla2_glm, 'LineWidth', lineWidth, 'Color', 'r');
h_legend2 = legend('Fused ADMM', 'SVM', 'GLMNET', 'Location', 'SouthEast');
fname3 = strcat('/home/ravikirn/mlcode/latex/report/images/',hla2,'.png');
print(f3,'-dpng','-r150',fname3)

figure(4);
X_hla2 = {'Precision' 'Recall' 'F1-Score' 'Accuracy'};
Y_hla2 = zeros(4, 3);
Y_hla2(1,:) = [hla2_admm_score.prec, hla2_svm_score.prec, hla2_glm_score.prec];
Y_hla2(2,:) = [hla2_admm_score.recall, hla2_svm_score.recall, hla2_glm_score.recall];
Y_hla2(3,:) = [hla2_admm_score.f1, hla2_svm_score.f1, hla2_glm_score.f1];
Y_hla2(4,:) = [hla2_admm_score.acc, hla2_svm_score.acc, hla2_glm_score.acc];
hold on;
title(hla2);
bar(Y_hla2);
set(gca,'XTick',(1:4));
set(gca,'XTickLabel', X_hla2);
legend('Fused ADMM', 'SVM', 'GLMNET', 'Location', 'NorthEast');

% display results to console
fprintf('=================================================================\n');
fprintf('HLA Type\tMethod\t\tPrec\tRecall\tF1\tAcc\n');
fprintf('=================================================================\n');

%HLA1
fprintf('%s\t%s\t\t%.2f\t%.2f\t%.2f\t%.2f\n', hla1, 'ADMM', hla1_admm_score.prec, hla1_admm_score.recall, ...
                                                        hla1_admm_score.f1, hla1_admm_score.acc);
fprintf('=================================================================\n');                                                    
fprintf('%s\t%s\t\t%.2f\t%.2f\t%.2f\t%.2f\n', hla1, 'SVM', hla1_svm_score.prec, hla1_svm_score.recall, ...
                                                        hla1_svm_score.f1, hla1_svm_score.acc);
fprintf('=================================================================\n');                                                    
fprintf('%s\t%s\t\t%.2f\t%.2f\t%.2f\t%.2f\n', hla1, 'GLMNET', hla1_glm_score.prec, hla1_glm_score.recall, ...
                                                        hla1_glm_score.f1, hla1_glm_score.acc);
%HLA2                                                    
fprintf('=================================================================\n');
fprintf('%s\t%s\t\t%.2f\t%.2f\t%.2f\t%.2f\n', hla2, 'ADMM', hla2_admm_score.prec, hla2_admm_score.recall, ...
                                                        hla2_admm_score.f1, hla2_admm_score.acc);
fprintf('=================================================================\n');                                                    
fprintf('%s\t%s\t\t%.2f\t%.2f\t%.2f\t%.2f\n', hla2, 'SVM', hla2_svm_score.prec, hla2_svm_score.recall, ...
                                                        hla2_svm_score.f1, hla2_svm_score.acc);
fprintf('=================================================================\n');                                                    
fprintf('%s\t%s\t\t%.2f\t%.2f\t%.2f\t%.2f\n', hla2, 'GLMNET', hla2_glm_score.prec, hla2_glm_score.recall, ...
                                                        hla2_glm_score.f1, hla2_glm_score.acc);                                                    
fprintf('=================================================================\n');            

%print hla1
fprintf('hla1 = %s & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f\\\\\n', ...
   hla1, hla1_admm_score.prec , hla1_svm_score.prec, hla1_glm_score.prec, ...
   hla1_admm_score.recall , hla1_svm_score.recall, hla1_glm_score.recall, ...
   hla1_admm_score.f1 , hla1_svm_score.f1, hla1_glm_score.f1, ...
   hla1_admm_score.acc , hla1_svm_score.acc, hla1_glm_score.acc);

%print hla2
fprintf('hla2 = %s & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f & %.2f\\\\\n', ...
   hla2, hla2_admm_score.prec , hla2_svm_score.prec, hla2_glm_score.prec, ...
   hla2_admm_score.recall , hla2_svm_score.recall, hla2_glm_score.recall, ...
   hla2_admm_score.f1 , hla2_svm_score.f1, hla2_glm_score.f1, ...
   hla2_admm_score.acc , hla2_svm_score.acc, hla2_glm_score.acc);