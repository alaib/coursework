function [val,d,dd]   = logRegGaussPrior(z,y,a,gamma)
    val = log(1 +     exp(-y*z)) + gamma*(z-a)^2;
    if nargout > 1     % caller asked for derivatives
        d = (-y ./ (1 + exp(y*z))) + (2 * gamma * (z - a));
        dd = ((y.^2 * exp(y*z)) ./ ((1 + exp(y*z)).^2)) + (2 * gamma);
    end
end