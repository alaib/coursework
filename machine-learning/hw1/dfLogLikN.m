function  d  =  dfLogLikN(y, X, beta0, beta, n)
    h = 1e-5;    
    f0 = 0;    
    for i = 1 : length(y)
        f0 = f0 + logProbLogReg(y(i), X(i,:), beta0, beta);
    end
    f1 = 0;
    for j = 1 : length(beta)
        if(j == n)
            beta(j) = beta(j) + h;
        end            
    end
    for i = 1 : length(y)
        f1 = f1 + logProbLogReg(y(i), X(i,:), beta0, beta);
    end    
    d  =  (f1  -  f0)/h;
end