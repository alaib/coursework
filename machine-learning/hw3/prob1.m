clear
load('hw3.mat');
m_f  =  fw(seq,x,0.005,0.005,0.99);
logProb  =  logsum(m_f(:,end));
display(logProb)