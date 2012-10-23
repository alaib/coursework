function z0i = updatez0i(yi, xi, w, u0i, rho)
    y = yi;    
    a = (xi*w) - ((1/rho) * u0i);
    gamma = rho/2;
    f = @(z) logRegGaussPrior(z,y,a,gamma);
    z0i = newtonWolfeBacktrack(f,0);   
end