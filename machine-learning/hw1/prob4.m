z = [-5:0.1:5];
dfz = 1 ./ (exp(z) .* (1+exp(-z)).^2);
%dfz1 = 1 ./((exp(z).*(1./exp(z) + 1).^2));
plot(z,dfz1,'LineWidth',3);
%  we  always  label  axes,  yes  we  do!
xlabel('z');
ylabel('df(z)');  
hwplotprep
print  -dpdf  sigmoid_first_derivative.pdf