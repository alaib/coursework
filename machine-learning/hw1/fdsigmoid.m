function  d  =  fdsigmoid(z)
    h = 1e-5;
    %h=10^-5;
    f0  =  1/(1  +  exp(-z));
    f1  =  1/(1  +  exp(-(z  +  h)));
    d  =  (f1  -  f0)/h;
end