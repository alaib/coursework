function  w  =  updatew(X, n, D, z0, z1, z2, u0, u1, u2, rho)
    w  =  [X; eye(n); D] \ [z0+((1/rho)*u0); z1+((1/rho)*u1); z2+((1/rho)*u2)];
end
