load('hw1.mat');
lst = find(~isnan(sum(X))); 
y = y(lst);
X = X(:,lst);
clear lst;
save('hw1.mat');