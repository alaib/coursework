function val = computeAL(y,X,D,mu,rho,w,z0,z2,u0,u2)
   term11 = 0.0;
   for i = 1 : size(y, 1)
       term11 = term11 + ( log(1 + exp(-1 .* y(i) .* z0(i))) );
   end
   term12 = mu * computeL1Norm(z2);
   
   term21 = 0.0;
   for i = 1 : size(z0, 1)
       term21 = term21 + (u0(i) * (z0(i) - X(i,:) * w));
   end
   term22 = sum(u2 .* (z2 - D * w));
   
   term31 = 0.0;
   for i = 1 : size(z0, 1)
       term31 = term31 + ( (rho/2) * ( computeL2Norm(z0(i) - (X(i, :) * w)) ).^ 2) ;
   end
   term32 = (rho/2) * ( computeL2Norm(z2 - D * w).^2 );
   
   val = term11 + term12 + term21 + term22 + term31 + term32;       
end

function value = computeL1Norm(z)
    value = 0;
    for i = 1 : size(z, 2)
        value = value + abs(z(i));
    end
end

function value = computeL2Norm(z)
    value = 0;
    for i = 1 : size(z, 2)
        value = value + abs(z(i)).^2;
    end
    value = sqrt(value);
end