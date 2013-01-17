function success = getPredictionSuccess(realY, pY)       
    match = 0;
    for i = 1 : size(pY, 1)
        if(pY(i) == realY(i))        
            match = match + 1;
        end    
    end
    
    success = (match/length(realY))*100;
end