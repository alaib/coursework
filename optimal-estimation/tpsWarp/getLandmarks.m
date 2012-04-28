% Inputs: 
% imgFixedFileName - Fixed Image filename
% imgMovFileName - Moving Image filename
% saveFileName - Landmarks save filename (make it '' if you don't want to
% save)
%
% Output: 
% Return Landmark Locations

function [Xp Yp Xs Ys] = getLandmarks(imgFixedFileName, imgMovFilename, saveFileName)
%% Get inputs
imgMov = imread(imgMovFilename);
imgFixed = imread(imgFixedFileName);

%% Get the landmark points

NPs = input('Enter number of landmark points : ');
fprintf('Select %d correspondence / landmark points with mouse on Fig.2.\n',NPs);

figure(2);
title('Choose the landmark on the Moving Image first');
Hp=subplot(1,2,1); % for landmark point selection
image(imgFixed);    
title('Fixed Image [choose second]')
hold on;

Hs=subplot(1,2,2); % for correspondence point selection
image(imgMov);   
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
    text(Yp(ix),Xp(ix),num2str(ix),'FontSize',6);

    axis(Hs);
    [Ys(ix),Xs(ix)]=ginput(1); % get the corresponding point in Fixed Image
    scatter(Ys(ix),Xs(ix),32,'y','*'); % display the point
    text(Ys(ix),Xs(ix),num2str(ix),'FontSize',6);
end
% Save landmarks if filename specified
if strcmp(saveFileName, '') ~= 1
    savefile(saveFileName, 'Xp', 'Yp', 'Xs', 'Ys');
end
return;