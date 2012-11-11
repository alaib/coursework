% Clear and load files
clear
load('hw3.mat');

% Call standard forward pass
%profile clear;
%profile on;
m_f_standard  =  fw(seq,x,0.005,0.005,0.99);
%profile viewer;


% Call viterbi forward pass
profile clear;
profile on;
m_f_viterbi  =  vit_fw(seq,x,0.005,0.005,0.99);
profile viewer;