% Logistic Regression Fused Lasso
function w = solveLogRegFusedLasso(y1, x1, y2, x2)
    %Construct Y, X and w
    % y = [y1 y2]'
    y = vertcat(y1, y2);
    
    % X = [x1 0; 0 x2];
    X_1 = horzcat(x1, zeros(size(x2)));
    X_2 = horzcat(zeros(size(x1)), x2);
    X = vertcat(X_1, X_2);
    
    % Construct w = [w1 w2]'    
    x1FeatLen = size(x1, 2);
    x2FeatLen = size(x2, 2);
    assert(x1FeatLen == x2FeatLen);
    
    w1 = zeros(x1FeatLen, 1);
    w2 = zeros(x2FeatLen, 1);
    
    w = vertcat(w1, w2);
    
    % Initialize constants
    rho = 1.00;        
    lambda1 = 1.00;
    lambda2 = 1.00;
    mu = 1.00;
    
    %Compute D
    w1size = size(w1, 1);
    w2size = size(w2, 1);
    
    % D = [ mu*I;          -mu* I      ] 
    %     [ lambda1 * I ;  lambda2 * I ] 
    d1 = horzcat(mu*eye(w1size), -mu * eye(w2size));
    d2 = horzcat((lambda1) * eye(w1size), (lambda2 / mu) * eye(w2size));
    D = vertcat(d1, d2);
           
    N = length(y); assert(N == size(X,1));        
    e = size(D,1); % z2 - D*w (D = 40x40, w = 40x1, size(D*w) = 40x1, hence z2 = 40x1)    
    
    z0 = zeros(N,1); % z0 - X*w, same logic as above
    u0 = zeros(N,1);
    
    z2 = zeros(e,1);
    u2 = zeros(e,1);
        
    MAXIT = 1000;
    prevIterAL = -1;
    currIterAL = 0;
    for it=1:MAXIT
        %% print iteration
        fprintf('\n================ Start Iteration %d ================\n', it);
        %% update w
        before = computeAL(y, X, D, mu, rho, w, z0, z2, u0, u2);
        fprintf('Before w update = %.10f\n', before);
        
        w = updatew(X, D, z0, z2, u0, u2, rho);
        after = computeAL(y, X, D, mu, rho, w, z0, z2, u0, u2);
        fprintf('After w update = %.10f\n\n', after);
        
        %assert
        assert(after < before + 1e-6);        
        
        %% z0 update
        before = after;
        fprintf('Before z0 update = %.10f\n', before);
        for i=1:length(y)
            z0(i) = updatez0i(y(i), X(i,:), w, u0(i), rho);
        end
        
        after = computeAL(y, X, D, mu, rho, w, z0, z2, u0, u2);        
        fprintf('After z0 update = %.10f\n\n', after);
        
        %assert
        assert(after < before + 1e-6);        
        
        
        %% z2 update                       
        before = after;
        Dw = D * w;
        fprintf('Before z2 update = %.10f\n', before);        
        for i=1:size(D,1)
            z2(i) = updatez2i(Dw(i), u2(i), mu, rho);
        end
        
        after = computeAL(y, X, D, mu, rho, w, z0, z2, u0, u2);        
        fprintf('After z2 update = %.10f\n\n', after);
        %assert
        assert(after < before + 1e-6);
        
        %% update dual variables        
        u0 = u0 + rho * (z0 - X * w);
        u2 = u2 + rho * (z2 - D * w);
        
        %% end iteration
        fprintf('================ End Iteration %d ================\n', it);
        
        %% check if value has changed
        currIterAL = after;
        if(abs(currIterAL - prevIterAL) < 1e-12)
            break
        end
        prevIterAL = currIterAL;
    end
end
