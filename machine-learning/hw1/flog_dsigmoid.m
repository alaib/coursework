function  d  =  flog_dsigmoid(z)
    %h = 1e-5;
    h=10^-5;
    f0  =  log(1/(1  +  exp(-z)));
    f1  =  log(1/(1  +  exp(-(z  +  h))));
    d  =  (f1  -  f0)/h;
end