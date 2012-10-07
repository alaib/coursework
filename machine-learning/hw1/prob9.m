zs = -5:0.001:5;
ds = zeros(length(zs),1);
fs = zeros(length(zs),1);
err = zeros(length(zs),1);

for i = 1:length(zs)
    ds(i)  =  1 ./ (1 + exp(zs(i)));
    fs(i)  =  flog_dsigmoid(zs(i));
    err(i) = abs(ds(i)-fs(i));
end
hist(err,30);
hwplotprep;
display(err(1));