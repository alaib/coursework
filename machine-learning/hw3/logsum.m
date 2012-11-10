function  s  =  logsum(vec)
    m  =  max(vec);
    s  =  log(sum(exp(vec  -  m)))  +  m;
end