function val = computeAL(y, X, D, lambda, mu, rho, w, z0, z1, z2, u0, u1, u2)
    term11 = 0.0;
    for i = 1 : size(y, 2)
        term11 = term11 + log(1 + exp(-1 .* y(i) .* z0(i)));
    end
    term12 = lambda * computeL1Norm(z1);
    term13 = mu * computeL1Norm(z2);
    
    term21 = 0.0;
    for i = 1 : size(z0, 2)
        term21 = term21 + (u0(i) * (z0(i) - w * X(:,i)));
    end
    term22 = u1 * (z1 - w);
    term23 = u2 * (z2 - w * D);
    
    term31 = 0.0;
    for i = 1 : size(z, 2)
        term31 = term21 + ((rho/2) * computeL2Norm(z0(i) - (w * X(:, i))));        
    end
    term32 = (rho/2) * computeL2Norm(z1 - w);
    term33 = (rho/2) * computeL2Norm(z2 - w * D);
    
    val = term11 + term12 + term13 + ...
          term21 + term22 + term23 + ...
          term31 + term32 + term33;
end

function value = computeL1Norm(z)
    value = 0;
    for i = 1 : size(z, 2)
        display(z(i));
        value = value + abs(z(i));
    end
end

function value = computeL2Norm(z)
    value = 0;
    for i = 1 : size(z, 2)
        value = value + abs(z(i)).^2;
    end
end