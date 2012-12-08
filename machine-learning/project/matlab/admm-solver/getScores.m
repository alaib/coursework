function st = getScores(trueY, predY)
%%
%                ground truth pos     ground truth neg
%predicted pos         TP                   FP
%predicted neg         FN                   TN
%                    TP+FN                 FP+TN
%                  
%
%%
    tp = 0;
    fp = 0;
    fn = 0;
    tn = 0;
    for i = 1 : length(predY)
        if(trueY(i) == 1)
            if(predY(i) == 1)
                tp = tp + 1;
            else
                fn = fn + 1;
            end
        elseif(trueY(i) == -1)
             if(predY(i) == -1)
                 tn = tn + 1;
             else
                 fp = fp + 1;
             end            
        end
    end
    prec = tp / (tp + fp);
    recall = tp / (tp + fn);
    acc = (tp + tn) / (tp + tn + fp + fn);
    f1 = (2 * prec * recall) / (prec + recall);
    st = struct('prec', prec, 'recall', recall, 'acc', acc, 'f1', f1);
end
