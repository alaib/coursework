y1 = [ones(50,1);2*ones(50,1);3*ones(50,1)];
y1 = y1+randn(length(y1),1)*0.1;
D1 = diag(ones(length(y1),1)) - diag(ones(length(y1)-1,1),1);
D1 = D1(1:length(y1)-1,:);

%%

y2 = imread('einstein.bmp');
colormap(gray(255));
subplot(1,2,1);
y2 = double(y2);
image(y2);
title('Original')
axis image
subplot(1,2,2);
y2 = double(y2) + randn(size(y2))*15;
image(y2);
title('Noisy')
axis image
is = zeros(4*length(y2(:)),1);
js = zeros(4*length(y2(:)),1);
vs = zeros(4*length(y2(:)),1);

ct = 0; ect=0;
for i=1:size(y2,1)
    for j=1:size(y2,2)
        ind = sub2ind(size(y2),i,j);
        if i>1            
            indLeft = sub2ind(size(y2),i-1,j);
            ect = ect+1; 
            ct = ct+1; is(ct) = ect; js(ct) = ind; vs(ct) = 1;
            ct = ct+1; is(ct) = ect; js(ct) = indLeft; vs(ct) = -1;
        end
        if j>1
            indUp = sub2ind(size(y2),i,j-1); 
            ect = ect+1; 
            ct = ct+1; is(ct) = ect; js(ct) = ind; vs(ct) = 1;
            ct = ct+1; is(ct) = ect; js(ct) = indUp; vs(ct) = -1;
        end
    end
end
D2 = sparse(is(1:ct),js(1:ct),vs(1:ct));


%%


%%
load hapmap
y3 = dt.vals(find(dt.chr==8),212);
y3 = y3(find(isfinite(y3)));
D3 = diag(ones(length(y3),1)) - diag(ones(length(y3)-1,1),1);
D3 = D3(1:length(y3)-1,:);

%%
flasso(y1(:),D1,0.1,4,10,500)
%%
flasso(y2(:),D2,0.1,10,2,100,size(y2))
%%
flasso(y2(:),D2,0.1,100,2,100,size(y2))
%%
flasso(y3,D3,0.05,2.5,20,1000)