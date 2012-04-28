% Start demo
% Read fixed and moving image files
imgFixedFileName = 'data\hand1.png';
imgMovFileName = 'data\hand2.png';

landmarksFileName = 'data\lm.mat';
manualLandmarks = 1;

imgMov = imread(imgMovFileName);
imgFix = imread(imgFixedFileName);

% Set interpolation method
%'nearest'; %'none' % interpolation method
interp.method = 'invdist'; 
% radius or median filter dimension
interp.radius = 5;
%power for inverse weighting interpolation method
interp.power = 2; 

% Get the landmarks manually or through a file
if manualLandmarks == 1
    [Xp Yp Xs Ys] = getLandmarks(imgFixedFileName, imgMovFileName, '');
else
    load(landmarksFileName);    
end

%% Warping
% thin plate spline warping
[imgW, imgWr, map, bendingEnergy]  = tpswarp(imgMov,[size(imgMov,2) size(imgMov,1)],...
                                             [Xp' Yp'],[Xs' Ys'],interp); 
imgW = uint8(imgW);
imgWr = uint8(imgWr);

%% Display Output
figure(1)
Hp = subplot(1,3,1); 
image(imgFix);
title('Fixed Image');
for ix = 1 : length(Xs),
	impoint(gca,Ys(ix),Xs(ix));
end

Hs = subplot(1,3,2); 
image(imgMov);
title('Moving Image');
for ix = 1 : length(Xp),
	impoint(gca,Yp(ix),Xp(ix));
end

Hr = subplot(1,3,3); 
image(imgW);
regStr = sprintf('Reg Image, bEnergy = %.4f', bendingEnergy);
title(regStr);
for ix = 1 : length(Xs),
	impoint(gca,Ys(ix),Xs(ix));
end
%end