function  d  =  fdsigmoid(z)
    f0  =  1/(1  +  exp(-z));
    f1  =  1/(1  +  exp(-(z  +  1e-5)));
    d  =  (f1  -  f0)/1e-5;
end