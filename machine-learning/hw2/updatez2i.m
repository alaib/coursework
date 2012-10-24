function z2i = updatez2i(w, D, u2i, mu, rho)    
    z2i = shrinkThreshold(D * w - 1/rho*u2i, mu/rho);
end

function  x  =  shrinkThreshold(x,lambda)
    x  =  sign(x).*max(abs(x)  -  lambda,0);
end

