function  [dbeta0, dbeta]  =  dLogLikLogReg(y,X,beta0,beta)
    % compute gradients
    dbeta0 = sum(y ./ (1 + exp(y .* (beta0 + X * beta))));
    dbeta = zeros(size(X,2),1);
    for j = 1 : length(beta)                            
        dbeta(j) = sum( (y .* X(:,j)) ./ (1 + exp(y .* (beta0 + X * beta))));
    end      
end