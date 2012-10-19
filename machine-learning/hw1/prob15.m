load  hw1.mat
X = X';
y = y';
rand('seed',1);
K  =  5;
N  =  length(y);
indices  =  crossvalind('Kfold',N,K);
err = zeros(K, 1);
err1 = zeros(K, 1);

for  k = 1 : K
    testX  =  X(indices  ==  k,:);
    testY  =  y(indices  ==  k);    
    trainX  =  X(indices  ~=  k,:);
    trainY  =  y(indices  ~=  k);    
    %[beta0,beta]  =  fitLogReg(trainY, trainX);    
    [sigma, beta0,beta]  =  coordAscent(trainY, trainX, []);    
    
    predY = zeros(1, size(testY, 1));
    for  i=1:length(testY)
        predY(i) = beta0 + testX(i,:) * beta;        
        err(k) = err(k) + abs(testY(i)-predY(i));
    end   
end

cvErr  =  sum(err)/length(y);
display(cvErr);