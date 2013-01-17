clc;clear;
y1 = [ones(50,1);4*ones(50,1);10*ones(50,1)];
y1 = y1-randn(length(y1),1)*0.1;
D1 = diag(ones(length(y1),1)) - diag(ones(length(y1)-1,1),1);
D1 = D1(1:length(y1)-1,:);

x = flasso(y1(:),D1,0.1,4,10,100);