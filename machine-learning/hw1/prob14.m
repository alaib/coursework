load('hw1.mat');
X = X';
y = y';
if 1
[predBeta0, predBeta] = fitLogReg(y, X);
display('===================');
display('predicted values');
display('===================');
display(strcat('predBeta0 = ',num2str(predBeta0)));
display(strcat('predBeta = ', mat2str(predBeta, 4)));
end
%display(predBeta0);
%display(predBeta');


if 0
[fitSigma,fitBeta0,fitBeta] = coordAscent(y,X,[])
display(strcat('predBeta0 = ',num2str(fitBeta0)));
display(strcat('predBeta = ', mat2str(fitBeta, 4)));
end