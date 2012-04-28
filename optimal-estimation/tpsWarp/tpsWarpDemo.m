% Demonstration of Thin-Plate-Spline Warping 
%
% Inputs: 
% imgInFilename - input image filename
% mapFilename - colormap data file for display
% lmFilename - landmark data file for display
%
% Output: 
% Display of warped image
%
% Example:
% tpsWarpDemo('..\data\0505_02.jpg','map.mat','tpsDemoLandmark.mat')
%
% Author: Fitzgerald J Archibald
% Date: 07-Apr-09

function tpsWarpDemo(imgMovFilename, imgFixedFileName, lmFilename, manual)
%% Get inputs
%imgInFilename = '..\data\0505_02.jpg'; %'..\data\HermannGrid.1.gif'; %

interp.method = 'invdist'; %'nearest'; %'none' % interpolation method
interp.radius = 5; % radius or median filter dimension
interp.power = 2; %power for inverse wwighting interpolation method

imgMov = imread(imgMovFilename);
imgFix = imread(imgFixedFileName);


%% Get the landmark points
if manual % Get points interactively
    NPs = input('Enter number of landmark points : ');
    fprintf('Select %d correspondence / landmark points with mouse on Fig.2.\n',NPs);

    figure(2);
    Hp=subplot(1,2,1); % for landmark point selection
    title('Moving Image')
    image(imgMov);    
    hold on;
    
    Hs=subplot(1,2,2); % for correspondence point selection
    imagesc(imgFixed);   
    title('Fixed Image')
    hold on;
    
    Xp=[]; Yp=[]; Xs=[]; Ys=[];
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
else % load stored landmark positions
	load(lmFilename);
    Xp = lm(1,:);
    Yp = lm(2,:);
    Xs = lm(3,:);
    Ys = lm(4,:);
end

%% Warping
% thin plate spline warping
[imgW, imgWr]  = tpswarp(imgMov,[size(imgMov,2) size(imgMov,1)],[Xp' Yp'],[Xs' Ys'],interp); 
imgW = uint8(imgW);
imgWr = uint8(imgWr);

%% Display
figure(1)
subplot(1,3,1); imshow(imgFix,[]);
title('Fixed Image');
for ix = 1 : length(Xs),
	impoint(gca,Ys(ix),Xs(ix));
end

subplot(1,3,2); imshow(imgMov,[]);
title('Moving Image');
for ix = 1 : length(Xp),
	impoint(gca,Yp(ix),Xp(ix));
end

subplot(1,3,3); imshow(imgW,[]);
title('Registered Image');
for ix = 1 : length(Xs),
	impoint(gca,Ys(ix),Xs(ix));
end

return;
