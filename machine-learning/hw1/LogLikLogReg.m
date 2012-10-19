function val = LogLikLogReg(y, X, beta0, beta)
    val = 0;
    for i = 1 : length(y)
        val = val + logProbLogReg(y(i), X(i), beta0, beta);
    end
end