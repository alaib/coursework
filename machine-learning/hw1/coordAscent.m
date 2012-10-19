function [sigma,beta0,beta]=coordAscent(y,x,init)
% check input sizes
assert(size(y,1)==size(x,1) && size(y,1)>1);
n = size(y,1); 
k = size(x,2);

% initialize parameters
if ~isempty(init)
    sigmasq = init{1};
    beta0 = init{2};
    beta = init{3};
else
    sigmasq = var(y);
    beta0 = mean(y);
    beta0 = 1;
    beta = 1e-6*ones(k,1);
end

% assume default tolerance and number of iterations
TOL = 1e-5;
MAXIT = 100;

% tracking likelihood
lls = zeros(MAXIT,1);
prevll = -realmax;


ll = loglik(y,x,beta0,beta,sigmasq);
iter = 0;
figure(1);

while ll-prevll > TOL && iter < MAXIT
    iter = iter+1;
    prevll = ll;
    
    % updates
    sigmasq = 1/n*((y - beta0 - x*beta)'*(y - beta0 - x*beta));
    beta0 = 1/n*sum(y - x*beta);
    for j=1:k
        beta(j) = 0;
        beta(j) = ((y - beta0 - x*beta)'*x(:,j))./(sum(x(:,j).^2));        
    end    
    
    % likelihood for new state
    ll = loglik(y,x,beta0,beta,sigmasq);
    
    assert(ll-prevll>0)
    
    lls(iter) = ll; 
    if mod(iter,10) == 0        
        plot(lls(1:iter))
        xlabel('iteration');
        ylabel('log-likelihood');
        drawnow
        save('dbg')
    end
end
sigma = sqrt(sigmasq);

function ll = loglik(y,x,beta0,beta,sigmasq)
n = size(y,1);
ll = -n/2*log(2*pi*sigmasq) ...
     -1/(2*sigmasq)*(y - beta0 - x*beta)'*(y - beta0 - x*beta);


