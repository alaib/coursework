function  w  =  solveLogRegFusedLasso(y,X,D, lambda, mu)
    rho  =  1;
    N  =  length(y);  assert(N  ==  size(X,1));
    p  =  size(X,2);  assert(p  ==  size(D,2));
    e  =  size(D,1);
    w  =  zeros(p,1);
    z0  =  zeros(N,1);  u0  =  zeros(N,1);
    z1  =  zeros(p,1);  u1  =  zeros(p,1);
    z2  =  zeros(e,1);  u2  =  zeros(e,1);
    MAXIT  =  100;
    n = size(X, 2);
    
    for  it=1:MAXIT
        before  =  computeAL(y,X,D,lambda,mu,rho,w,z0,z1,z2,u0,u1,u2);        
        w = updatew(X, n, D, z0, z1, z2, u0, u1, u2, rho);
        after  =  computeAL(y,X,D,lambda,mu,rho,w,z0,z1,z2,u0,u1,u2);
        assert(after  <  before  +  1e-6);
        
        before  =  after;
        for  i=1:length(y)
            z0(i)  =  updatez0i(y(i), X(i,:), w, u0(i), rho);
        end
        after  =  computeAL(y,X,D,lambda,mu,rho,w,z0,z1,z2,u0,u1,u2);
        assert(after  <  before  +  1e-6);
        
        before  =  after;
        for  i=1:length(w)            
            z1(i) = updatez1i(w(i), u1(i), lambda, rho);
        end
        after  =  computeAL(y,X,D,lambda,mu,rho,w,z0,z1,z2,u0,u1,u2);
        assert(after  <  before  +  1e-6);
        
        before  =  after;
        Dw = D*w;
        for  i=1:size(D,1)            
            %z2(i) = updatez2i(w, D, u2(i), mu, rho);
            z2(i) = updatez2iNew(Dw(i), u2(i), mu, rho);
        end
        after  =  computeAL(y,X,D,lambda,mu,rho,w,z0,z1,z2,u0,u1,u2);
        assert(after  <  before  +  1e-6);
        
        u0  =  u0 + rho * (z0 - X*w);
        u1  =  u1 + rho * (z1 - w);
        u2  =  u2 + rho * (z2 - D*w);
    end        
end