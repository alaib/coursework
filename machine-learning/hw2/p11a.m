load('sample_hw2.mat');
figure;
example  =  poissrnd(15,300,1);
hist(example,1:50)
xlabel('count');
ylabel('number  of  times  count  was  observed');
hwplotprep
print  -dpdf  aPoissonExample.pdf