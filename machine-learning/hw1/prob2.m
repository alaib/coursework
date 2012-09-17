z = [-5:0.1:5];
fz = 1./(1  +  exp(-z));
plot(z,fz,'LineWidth',3);

%  we  always  label  axes,  yes  we  do!
xlabel('z');
ylabel('f(z)');  
hwplotprep
print  -dpdf  sigmoid.pdf