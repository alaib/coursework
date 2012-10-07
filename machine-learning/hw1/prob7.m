zs = 0:0.1:1;
ds = zeros(length(zs),1);
fs = zeros(length(zs),1);
err = zeros(length(zs),1);

for i = 1:length(zs)
    ds(i)  =  dsigmoid(zs(i));
    fs(i)  =  fdsigmoid(zs(i));
    err(i) = abs(ds(i)-fs(i));
end

display(err(1));