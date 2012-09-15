% analytical derivatives
syms z
fz = 1./(1+exp(-z)); 
dfz = diff(fz,'z')
dfz2 = diff(dfz, 'z')
