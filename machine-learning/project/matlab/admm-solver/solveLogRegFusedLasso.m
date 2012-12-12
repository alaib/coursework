% Logistic Regression Fused Lasso
function w = solveLogRegFusedLasso(y1, x1, y2, x2)
    DEBUG = 1;
    
    %Construct Y, X and w
    % y = [y1 y2]'
    y = vertcat(y1, y2);
    
    % X = [x1 0; 0 x2];
    X_1 = horzcat(x1, zeros(size(x1)));
    X_2 = horzcat(zeros(size(x2)), x2);
    X = sparse(vertcat(X_1, X_2));
    clear X_1 X_2
            
    % Construct w = [w1 w2]'    
    x1FeatLen = size(x1, 2);
    x2FeatLen = size(x2, 2);
    assert(x1FeatLen == x2FeatLen);
    
    clear y1 x1 y2 x2
    
    w1 = zeros(x1FeatLen, 1);
    w2 = zeros(x2FeatLen, 1);
    
    w = vertcat(w1, w2);
    clear w1 w2
    
    % Initialize constants
    rho = 1.00;        
    %best params, 0.9, 0.9, 0.9, 30 (l1, l2, mu, iter)
    lambda1 = 0.9;
    lambda2 = 0.9;
    mu = 0.9;
    
    %Compute D
    w1size = x1FeatLen;
    w2size = x2FeatLen;
    
    % D = [ mu*I;          -mu* I      ] 
    %     [ lambda1 * I ;  lambda2 * I ] 
    %d1 = horzcat(mu*eye(w1size), -mu * eye(w2size));
    %d2 = horzcat((lambda1) * eye(w1size), (lambda2 / mu) * eye(w2size));
    d1 = horzcat(mu*speye(w1size), -mu * speye(w2size));
    d2 = horzcat((lambda1) * speye(w1size), (lambda2 / mu) * speye(w2size));
    D = vertcat(d1, d2);
    
    clear d1 d2
           
    N = length(y); assert(N == size(X,1));        
    e = size(D,1); % z2 - D*w (D = 40x40, w = 40x1, size(D*w) = 40x1, hence z2 = 40x1)    
    
    z0 = zeros(N,1); % z0 - X*w, same logic as above
    u0 = zeros(N,1);
    
    z2 = zeros(e,1);
    u2 = zeros(e,1);
        
    MAXIT = 20;
    for it=1:MAXIT
        %% print iteration
        if DEBUG
            fprintf('\n================ Start Iteration %d ================\n', it);
        end
        %% update w
        w_prev = w;
        before = computeAL(y, X, D, mu, rho, w, z0, z2, u0, u2);
        if DEBUG
            fprintf('Before w update = %.10f\n', before);
        end
        
        w = updatew(X, D, z0, z2, u0, u2, rho);
        after = computeAL(y, X, D, mu, rho, w, z0, z2, u0, u2);
        if DEBUG
            fprintf('After w update = %.10f\n', after);
            fprintf('Reduction = %.10f\n\n', before-after);
        end
        
        %assert
        %assert(after < before + 1e-6);        
        % restore w and return
        if(after >= before + 1e-6)
            w = w_prev;
            fprintf('Failed assertion, iteration no. = %d\n', it);
            return;
        end
            
        
        %% z0 update
        before = after;
        if DEBUG
            fprintf('Before z0 update = %.10f\n', before);
        end
        for i=1:length(y)
            z0(i) = updatez0i(y(i), X(i,:), w, u0(i), rho);
        end
        
        after = computeAL(y, X, D, mu, rho, w, z0, z2, u0, u2);        
        if DEBUG
            fprintf('After z0 update = %.10f\n', after);
            fprintf('Reduction = %.10f\n\n', before-after);
        end
        
        %assert        
        %assert(after < before + 1e-6);        
        % restore w and return
        if(after >= before + 1e-6)
            w = w_prev;
            fprintf('Failed assertion, iteration no. = %d\n', it);
            return;
        end
        
        
        %% z2 update                       
        before = after;
        Dw = D * w;
        if DEBUG
            fprintf('Before z2 update = %.10f\n', before);        
        end
        for i=1:size(D,1)
            z2(i) = updatez2i(Dw(i), u2(i), mu, rho);
        end
        
        after = computeAL(y, X, D, mu, rho, w, z0, z2, u0, u2);        
        if DEBUG
            fprintf('After z2 update = %.10f\n', after);
            fprintf('Reduction = %.10f\n\n', before-after);
        end
        %assert        
        %assert(after < before + 1e-6);        
        % restore w and return
        if(after >= before + 1e-6)
            w = w_prev;
            fprintf('Failed assertion, iteration no. = %d\n', it);
            return;
        end
        
        %% update dual variables        
        u0 = u0 + rho * (z0 - X * w);
        u2 = u2 + rho * (z2 - D * w);
        
        %% end iteration
        if DEBUG
            fprintf('================ End Iteration %d ================\n', it);
        end                
    end
end
