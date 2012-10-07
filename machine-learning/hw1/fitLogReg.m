function [beta0, beta] = fitLogReg(y, X)    
    %initialize beta0 and beta
    beta0 = 0;
    beta = zeros(size(X, 2),1);

    %initialize gradients
    dbeta0 = 1;
    dbeta = zeros(size(X,2), 1);
    
    % initialize step size (or learning rate) and max iterations
    eta = 1e-5; % learning rate or step size    
    it = 0; 
    itmax = 2000; % maximum number of iterations    
        
    while (sum(abs(dbeta)) + abs(dbeta0)) > 1e-10 % continue whilst gradient is large        
        % increment the number of updates carried out
        it = it + 1; 
        
        % reset gradients
        dbeta0 = 0; 
        dbeta = 0 * dbeta;
        
        % compute gradients
        dbeta0 = sum(y ./ (1 + exp(y .* (beta0 + X * beta))));
                
        for j = 1 : length(beta)                            
            dbeta(j) = sum( (y .* X(:,j)) ./ (1 + exp(y .* (beta0 + X * beta))));
        end
        
        % update beta0 and beta
        beta0 = beta0 + eta .* dbeta0;
        beta = beta + eta .* dbeta;        

        % break out if max iterations has been reached
        if it > itmax
            break; 
        end
    end   
end