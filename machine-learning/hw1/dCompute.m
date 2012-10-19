beta0 = 1; beta = [1 1 1 1 0 0 0 0]';
N = 3;
p = length(beta);
X = rand(N,p);
y = beta0 + X*beta;

if 0
sum1 = 0;
a = zeros(length(y), 1);
for i = 1 : length(y)
    term = beta0 + X(i,:) * beta;
    term = y(i) * term;
    term = 1 + exp(term);
    a(i) = y(i) / term;
    sum1 = sum1 + (y(i) / term);
end
dbeta0 = sum(y  ./  (1  +  exp(y  .*  (beta0  +  X  *  beta))));
end

b0 = 1;
b = zeros(size(X, 2),1);
dbeta = zeros(size(X, 2),1);
a = zeros(size(y),1);
t = zeros(size(y),1);
for i = 1 : length(b)
    sum2 = 0;    
        
    for j = 1 : length(y)
        term = beta0 + X(j,:) * beta;
        term = y(j) * term;
        term = 1 + exp(term);
        if(i == 1)
            t(j) = term;    
            test = y(j) .* X(i,j);
            display(test);
            a(j) = sum(y(j) .* X(i,j) ./ term);
        end
        sum2 = sum2 + sum((y(j) .* X(i,j) ./ term));
    end
    b(i) = sum2;
    if(i == 1)
        break;
    end
end

for j = 1 : length(dbeta)
    term1 = (1  +  exp(y  .*  (beta0  +  X  *  beta)));
    term2 = y .* X(:,j);
    term3 = term2 ./ term1;
    dbeta(j) =  sum(  (y  .*  X(:,j))  ./  (1  +  exp(y  .*  (beta0  +  X  *  beta))));        
    if(j == 1)
        break;
    end
end

display(b');
display(dbeta');