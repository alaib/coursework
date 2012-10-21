function z1i = updatez1i(w, u1i, lambda, rho)
    z1i = shrinkThreshold(w - 1/rho*u1i, lambda/rho);        
end

function  x  =  shrinkThreshold(x,lambda)
    x  =  sign(x).*max(abs(x)  -  lambda,0);
end