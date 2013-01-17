% Dwi = Dw(i), Dw = D*w
function z2i = updatez2i(Dwi, u2i, mu, rho)
    z2i = shrinkThreshold(Dwi - ((1/rho) * u2i), mu/rho);
end

function  x = shrinkThreshold(x, lambda)
    x =   sign(x) .* max(abs(x) - lambda, 0);
end