% Inputs: 
% imgFixedFileName - Fixed Image filename
% imgMovFileName - Moving Image filename
% saveFileName - Landmarks save filename (make it '' if you don't want to
% save)
%
% Output: 
% Return Landmark Locations

function [Xp Yp Xs Ys] = getLandmarks(imgFixed, imgMov, saveFileName, brainFlag)
%% Get the landmark points

NPs = input('Enter number of landmark points : ');
fprintf('Select %d correspondence / landmark points with mouse on Fig.2.\n',NPs);

figure(2);
title('Choose the landmark on the Moving Image first');
Hp=subplot(1,2,1); % for landmark point selection
if brainFlag
    imshow(imgFixed);  
    axis on;
else
    image(imgFixed);
end
title('Fixed Image [choose second]')
hold on;

Hs=subplot(1,2,2); % for correspondence point selection
if brainFlag
    imshow(imgMov);   
    axis on;
else
    image(imgMov);
end
title('Moving Image [choose first]')
hold on;

Xp = zeros(size(NPs));
Yp = zeros(size(NPs));
Xs = zeros(size(NPs));
Ys = zeros(size(NPs));

for ix = 1:NPs
    axis(Hp);
    [Yp(ix),Xp(ix)]=ginput(1); % get the landmark point in Moving Image
    scatter(Yp(ix),Xp(ix),32,'y','o','filled'); % display the point
    landmarkStr = sprintf('  %d', ix);
    text(Yp(ix),Xp(ix), landmarkStr, 'FontSize', 8, 'Color', 'r');

    axis(Hs);
    [Ys(ix),Xs(ix)]=ginput(1); % get the corresponding point in Fixed Image
    scatter(Ys(ix),Xs(ix),32,'y','*'); % display the point
    landmarkStr = sprintf('  %d', ix);
    text(Ys(ix),Xs(ix), landmarkStr, 'FontSize', 8, 'Color', 'r');
end
% Save landmarks if filename specified
if strcmp(saveFileName, '') ~= 1
    save(saveFileName, 'Xp', 'Yp', 'Xs', 'Ys');
end
return;
