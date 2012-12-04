function x = flasso(y,D,lambda,mu,rho,maxits,reshapeSize)
% flasso solves a simplified Fused Lasso problem (Fused Lasso Signal Approximator).
%
% Solve
%   minimize_x     1/2||y - x||_2^2 + lambda*||x||_1 + mu*||D*x||_1
%
% via dual decomposition
%   minimize_x,z   1/2||y - z1||_2^2 + lambda*||z2||_1 + mu*||z3||_1
%   subject to     z1 = x
%                  z2 = x
%                  z3 = Dx
%
% The augmented lagrangian for this problem
%   AL(x,z,u) =    1/2||y - z1||_2^2 + lambda*||z2||_1 + mu*||z3||_1
%                + sum_i (x_i - z1_i)*u1_i + rho/2 ||x - z1||_2^2
%                + sum_i (x_i - z2_i)*u2_i + rho/2 ||x - z2||_2^2
%                + sum_j ((Dx)_j - z3_j)*u3_j + rho/2 ||D*x - z3||_2^2
%
% [x,val] = flasso(y,D,lambda,mu,rho) returns the optimizer x and optimal
%   value val, for input vector y, difference matrix D, penalty weights
%   lambda, mu and ADMM parameter rho.
%
% Vladimir Jojic, 2012
close all
if size(y,2) ~= 1
    error('Input y should be a vector.');
end

if size(y,1) ~= size(D,2)
    error('Number of rows of y does not match number of columns of D.')
end

if lambda<0 || mu<0 || rho<0
    error('Penalty weights lambda, mu and ADMM parameter rho should all be positive.');
end

I = speye(length(y));
x = zeros(length(y),1);

z1 = zeros(length(y),1); z2 = zeros(length(y),1);
u1 = zeros(length(y),1); u2 = zeros(length(y),1);

z3 = zeros(size(D,1),1);
u3 = zeros(size(D,1),1);
for it=1:maxits
    x = [I;I;D]\[z1 + u1/rho; z2 + u2/rho; z3 + u3/rho];
    z1 = [I;sqrt(rho)*I]\[y;sqrt(rho)*x-1/sqrt(rho)*u1];
    z2 = shrinkThreshold(x - 1/rho*u2,lambda/rho);
    z3 = shrinkThreshold(D*x - 1/rho*u3,mu/rho);
    u1 = u1 + rho*(z1 - x);
    u2 = u2 + rho*(z2 - x);
    u3 = u3 + rho*(z3 - D*x);
    if exist('reshapeSize')
        imy = reshape(y,reshapeSize);
        subplot(1,3,1);
        image(imy);
        colormap(gray(255));
        axis image
        title('Noisy');

        
        imx = reshape(x,reshapeSize);
        subplot(1,3,2);
        image(imx);
        colormap(gray(255));
        axis image
        title('Denoised');
        
        subplot(1,3,3);
        surf(imx,'EdgeColor','none','LineStyle','none','FaceLighting','phong'); 
        view(-150,30);
        ylim([1 size(imx,1)])
        xlim([1 size(imx,2)])
        pbaspect([1 1 0.5*size(imx,2)/max(1,max(x))]);
        title('3D view of Denoised')
        drawnow
    else
        plot(y,'b.');
        hold on
        plot(x,'r.');
        hold off
        legend('Noisy','Denoised','Location','SouthEast')

        drawnow
    end
end
function x = shrinkThreshold(x,lambda)
x = sign(x).*max(abs(x) - lambda,0);
