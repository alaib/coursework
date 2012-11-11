% Clear and load files
clear
load('hw3.mat');

% Call standard forward pass
standardStart = tic;
m_f_standard  =  fw(seq,x,0.005,0.005,0.99);
standardElapsed = toc(standardStart);

display(standardElapsed);

% Call viterbi forward pass
viterbiStart = tic;
m_f_viterbi  =  vit_fw(seq,x,0.005,0.005,0.99);
viterbiElapsed = toc(viterbiStart);

display(viterbiElapsed);

ratio = standardElapsed / viterbiElapsed;
display(ratio);