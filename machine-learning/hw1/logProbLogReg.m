function logP = logProbLogReg(y, X, beta0, beta)       
    logP = log(1 ./ (1 + exp(-1 .* y .* (beta0 + X * beta))));
end