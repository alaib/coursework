x = [1 4 5 10 11 3];

Tx = x;
s = 0.0;
n = 0;
for i =  1 : size(Tx,2)
    s = s + Tx(i);
    n = n + 1;
end

val = s / n;

display(val);