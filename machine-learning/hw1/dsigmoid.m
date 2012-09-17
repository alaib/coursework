function d = dsigmoid(z)    
    % Computes first order deriviative of the sigmoid function
    d = 1 ./ (exp(z) .* (1+exp(-z)).^2);
end