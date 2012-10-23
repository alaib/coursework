y  =  1;  a  =  0.1;  gamma  =  0.5;
f  =  @(z)  logRegGaussPrior(z,y,a,gamma);
z  =  newtonWolfeBacktrack(f,0);