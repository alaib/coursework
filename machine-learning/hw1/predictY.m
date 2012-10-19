function predY = predictY(X, beta0, beta)
    logProbY = logProbLogReg(1, X, beta0, beta);
    if logProbY > log(0.5)
        predY = 1;
    else
        predY = 0;
    end
end