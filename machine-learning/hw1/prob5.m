zs  =  [-5:0.01:5];
ds = zeros(length(zs),1);

for i = 1:length(zs)
    ds(i)  =  dsigmoid(zs(i));
end

plot(zs,ds,'LineWidth',3);
xlabel('z');
ylabel('df(z)');
hwplotprep
print  -dpdf  dsigmoid.pdf