zs  =  randn(100,1);
%zs = [0];
err = zeros(length(zs),1);
for  i=1:length(zs)
    display(dsigmoid(zs(i)));
    display(fdsigmoid(zs(i)));
    err(i)  =  dsigmoid(zs(i))  -  fdsigmoid(zs(i));
end
hist(err,30)
maxError = max(err);
minError = min(err);
display(strcat('Min Error = ', num2str(minError)));
display(strcat('Max Error = ', num2str(maxError)));
hwplotprep
print  -dpdf  hist.pdf