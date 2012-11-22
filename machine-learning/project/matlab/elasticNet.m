function [beta0, beta, logLikeArray] = elasticNet(y, X, lambda, alpha)
    % assert X and y sizes
    assert(size(X,1) == size(y, 1));

    % data size
    n = size(y, 1);

    % feature vector size
    k = size(X, 2);

    % initialize features
    beta0 = mean(y);
    beta = ones(k, 1);

    % max Iterations
    maxIter = 1000;

    % min update required
    epsilon = 1e-5;

    % track all likelihood values in array
    logLikeArray = zeros(1, maxIter);

    % initialize previous log likelihood
    prevLL = -realmax;

    % compute current log likelihood
    currLL = computeLogLikelihood(y, X, beta0, beta, lambda, alpha);

    % initialize iteration
    iter = 0;

    while( (currLL - prevLL) > epsilon && iter <= maxIter)
        iter = iter + 1;
        prevLL = currLL;

        % updates
        % Note: see Lecture 2 and Lecture 3 slides for update explanation
        beta0 = (1/n) * sum(y - X * beta);
        for j = 1 : k
            beta(j) = 0;
            denom = sum( X(:,j).^2 ) + (lambda * alpha);
            term1 = (y - beta0 - X * beta)' * X(:, j);
            term2 = (1 - alpha) * lambda;
            num = shrinkThreshold(term1, term2);
            beta(j) = num / denom;
        end

        % compute log likelihood for new updates obtained
        currLL = computeLogLikelihood(y, X, beta0, beta, lambda, alpha);

        % Assert the new likelihood is atleast same as the prevLL
        assert(currLL-prevLL >= 0);

        logLikeArray(iter) = currLL;
    end
end

% Compute Log Likelihood - see Lecture 3 notes (Slide 14) for details
function logLike = computeLogLikelihood(y, X, beta0, beta, lambda, alpha)    
    term1 = -1/2 * (y - beta0 - X * beta)' * (y - beta0 - X * beta);
    term2 = -alpha * (lambda/2) * sum(beta .^ 2);
    term3 = -(1 - alpha) * lambda * sum(abs(beta));
    logLike = term1 + term2 + term3;
end

% Shrink Threshold function
function val = shrinkThreshold(x, lambda)
    val = sign(x) .* max(abs(x)-lambda, 0);
end