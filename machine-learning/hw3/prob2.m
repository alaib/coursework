clear
load('hw3.mat');
m_b  =  bw(seq,x,0.005,0.005,0.99);
logProb  =  logsum(m_b(:,end));
display(logProb)