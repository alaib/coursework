function z2i = updatez2iNew(Dw, u2i, mu, rho)    
    z2i = shrinkThreshold(Dw - 1/rho*u2i, mu/rho);
end

function  x  =  shrinkThreshold(x,lambda)
    x  =  sign(x).*max(abs(x)  -  lambda,0);
end