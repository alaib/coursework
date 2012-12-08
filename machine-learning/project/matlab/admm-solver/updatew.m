function w = updatew(X, D, z0, z2, u0, u2, rho)        
    w =   [X; D] \ [z0+((1/rho)*u0); z2+((1/rho)*u2)];
end
