zs  =  randn(100,1);
err = zeros(length(zs),1);
for  i=1:length(zs)
    err(i)  =  dsigmoid(zs(i))  -  fdsigmoid(zs(i));
end
hist(err,30)
maxError = max(err);
minError = min(err);
%fprintf('The error ranges between %f to %f\n', minError, maxError);
display(strcat('Min Error = ', num2str(minError)));
display(strcat('Max Error = ', num2str(maxError)));
hwplotprep
print  -dpdf  hist.pdf