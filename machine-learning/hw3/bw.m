function  m_b  =  bw(s,x,pins,pdel,pcopy)
N  =  length(s);
L  =  length(x)/2;
m_b  =  -realmax*ones(N,2*L);
logpins  =  log(pins);
logpdel  =  log(pdel);
logpcopy  =  log(pcopy);
for  a=1:4
    for  b=1:4
        logmut(a,b)  =  log(0.99)*(a==b)  +  log(0.01)*(a~=b);
    end
end
m_b(:,2*L)  =  0;
for  i=2*L-1:-1:1    
    for  next=1:N
        if  i~=L
            %  insert
            vala  =  m_b(next,i);
            valb  =  m_b(next,i+1)+  logmut(s(next),x(i+1))  +  logpins;
            m_b(next,i)  =  logsum([vala  valb]);
            if  next-2>=1
                %  delete
                vala  =  m_b(next-2,i);
                valb  =  m_b(next-2,i+1) + logmut(s(next-2),x(i+1)) + logpdel;
                m_b(next-2,i)  =  logsum([vala  valb]);
            end
            if  next-1>=1
                %  copy
                vala  =  m_b(next-1,i);
                valb  =  m_b(next-1,i+1) + logmut(s(next-1),x(i+1)) + logpcopy;
                m_b(next-1,i)  =  logsum([vala  valb]);
            end
        else
            if  next-90>=1
                for  prev=max(1,next-110):next-90
                    vala  =  m_b(prev,i);
                    diff = m_b(prev,i) - m_b(prev, i);
                    valb  =  logProbTruncPoiss(diff, 100);
                    m_b(prev,i)  =  logsum([vala  valb]);
                end
            end
        end
    end
end
end

function  s  =  logsum(vec)
    m  =  max(vec);
    s  =  log(sum(exp(vec  -  m)))  +  m;
end

function  logProb  =  logProbTruncPoiss(i,lambda)
    logProb  =  log(lambda)*i  +  (-lambda)  -  sum(log(1:i));
end