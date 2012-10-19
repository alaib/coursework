function  d  =  dfLogLik(y, X, beta0, beta)
    h = 1e-5;    
    f0 = 0;
    for i = 1 : length(y)
        f0 = f0 + logProbLogReg(y(i), X(i,:), beta0, beta);
    end
    f1 = 0;
    for i = 1 : length(y)
        f1 = f1 + logProbLogReg(y(i), X(i,:), beta0+h, beta);
    end    
    d  =  (f1  -  f0)/h;
end