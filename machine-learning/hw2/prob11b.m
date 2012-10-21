load('hw2.mat');
figure;
hist(counts,1:50);
xlabel('count');
ylabel('number  of  times  count  was  observed');
hwplotprep
print  -dpdf  HW2Counts.pdf