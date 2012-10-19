function  [dbeta0, dbeta]  =  dLogLikLogReg1(y,X,beta0,beta)
    %dbeta0 = exp(-1 * y' * (beta0 + X * beta));
    term = exp(-1 * y .* (beta0 + X * beta))';
    dbeta0 = term * y / (1 + sum(term)); 
    dbeta01 = exp(-1 * y .* (beta0 + X * beta))' * y ./ ...
                    (1 + sum(exp(-1 * y .* (beta0 + X * beta))')); 
    %dbeta02 = sum(y) ./ (1 + sum(exp(-1 * y .* (beta0 + X * beta))')); 
    dbeta02 =  sum(y ./ (1 + exp(y .* (beta0 + X * beta))));
       
    k = size(X, 2);        
    dbeta =  1e-6*ones(k,1);
    dbeta2 =  1e-6*ones(k,1);
    dbeta3 =  1e-6*ones(k,1);
    for j=1:k
       % dbeta(j) = 0;
        t1 = exp(-1 * y .* (beta0 + X * beta))';
        t2 = t1 * y * X(:,j);
        t3 = sum(t2) / ( 1 + sum(t1));        
        dbeta(j) = t3;        
        dbeta2(j) = sum(exp(-1 * y .* (beta0 + X * beta))' * y * X(:,j)) ./ ...
                        ( 1 + sum(exp(-1 * y .* (beta0 + X * beta))'));        
                    
        dbeta3(j) = sum( (y' * X(:,j)) ./ (1 + exp(y .* (beta0 + X * beta))));
    end        
    
    d2 = 1e-6 * ones(k,1);
    for j = 1: k
        for i = 1 : length(y)
            upp = y(i) * X(i,j);
            upp2 = y(i);
            val = 0;
            for l = 1 : k
                val = val + X(i,l) * beta(l);
            end
            den = 1 + exp(y(i) * (beta0 + val));
            d2(j) = upp / den;
            dem = upp2 / den;
        end
    end
    
    display(dbeta0)
    display(dbeta01)
    display(dbeta02)
    display(dbeta2')
    display(dbeta')
    display(dbeta3')
    display(d2')
end