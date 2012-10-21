%load('sample_hw2.mat');
figure(1);
subplot(1,2,1);
example  =  poissrnd(2,300,1);
hist(example,1:50)
xlabel('count');
ylabel('number  of  times  count  was  observed');

load('hw2.mat');
subplot(1,2,2);
hist(counts,1:50);
xlabel('count');
ylabel('number  of  times  count  was  observed');