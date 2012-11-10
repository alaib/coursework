function  m_f  =  fw(s,x,pins,pdel,pcopy)
    N  =  length(s);
    L  =  length(x)/2;
    m_f  =  -realmax*ones(N,2*L);
    logpins  =  log(pins);
    logpdel  =  log(pdel);
    logpcopy  =  log(pcopy);

    for  a=1:4
        for  b=1:4
            logmut(a,b)  =  log(0.99)*(a==b)  +  log(0.01)*(a~=b);
        end
    end

    m_f(1:N,1)  =  -log(N)  +  logmut(s,x(1));
    for  i=2:2*L
        test = 1;
        for  prev=1:N
            if  i~=L+1
                %  insert
                vala  =  m_f(prev,i);
                valb  =  m_f(prev,i-1) +  logmut(s(prev),x(i))  +  logpins;
                m_f(prev,i)  =  logsum([vala  valb]);

                if  prev<=N-2
                    %  delete
                    vala  =  m_f(prev+2,i);
                    valb  =  m_f(prev+2,i-1) + logmut(s(prev+2), x(i)) + logpdel;
                    m_f(prev+2,i)  =  logsum([vala  valb]);
                end

                if  prev<=N-1
                    %  copy
                    vala  =  m_f(prev+1,i);
                    valb  =  m_f(prev+1,i-1) + logmut(s(prev+1), x(i)) + logpcopy;
                    m_f(prev+1,i)  =  logsum([vala  valb]);
                end


            else
                if  prev+90<=N-1
                    for  next = prev+90 : min(prev+110,N-1)
                        vala  =  m_f(next,i);   
                        %display(prev);
                        %display(next);
                        %display(i);
                        diff = m_f(next, i) - m_f(next-1, i);
                        valb  =  logProbTruncPoiss(diff, 100);
                        
                        %valb = -0.5;
                        valCompute = logsum([vala  valb]);
                        m_f(next,i)  =  logsum([vala  valb]);
                    end
                end
            end
        end
    end
end

function  logProb  =  logProbTruncPoiss(i,lambda)       
    %display(i);
    logProb  =  log(lambda)*i  +  (-lambda)  -  sum(log(1:i));   
    %display(logProb);
end

