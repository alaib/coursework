clear
load('hw3.mat');
m_f  =  vit_fw(seq,x,0.005,0.005,0.99);
m_b  =  vit_bw(seq,x,0.005,0.005,0.99);
mm  =  m_f  +  m_b;
hMAP = zeros(length(x), 1);
for l = 1 : length(x)  
    [maxVal,maxIndex] = max(m_b(l,:) + m_f(l,:));
    hMAP(l) = maxIndex;
end
