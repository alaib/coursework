function [tprs,fprs] = roc(q,y)
    tprs = zeros(2+length(y),1); % Preallocate space for tps
    fprs = zeros(2+length(y),1); % and fps.
    [~,ii] = sort(q,'descend'); % Descending ordering for q.
    y = y(ii);                   % Reorder y.
    totPos = sum(y>0);           % Count positives.
    totNeg = sum(y<=0);          % Count negatives.
    for i=1:length(y)
        tprs(i+1) = tprs(i) + (y(i)>0); % count another TP
        fprs(i+1) = fprs(i) + (y(i)<=0); % count another FP
    end
    tprs(end) = totPos;          % At the end of curve we say
    fprs(end) = totNeg;          % everything is positive.
    tprs = tprs./totPos;         % Get TPRs out of the counts.
    fprs = fprs./totNeg;         % Get FPRs out of the counts.
end
