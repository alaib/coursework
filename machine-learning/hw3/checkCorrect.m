m_f  =  fw(seq,x,0.005,0.005,0.99);
m_b  =  bw(seq,x,0.005,0.005,0.99);
mm  =  m_f  +  m_b;
logsum(mm(:,1))
logsum(mm(:,end))