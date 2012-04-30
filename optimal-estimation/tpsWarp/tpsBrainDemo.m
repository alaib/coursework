% Start demo
% Read fixed and moving image files
imgMovFileName = 'data/brain/normal1.nii';
imgFixedFileName = 'data/brain/krabbe2.nii';
[imgMov, imgFix] = getBrainImages(imgMovFileName, imgFixedFileName);
%[imgMov, imgFix] = getBrainImages('data/brain/normal1.nii', 'data/brain/krabbe1.nii');

% Save the landmarks
landmarksFileName = 'data\lm_brain.mat';
% Set manualLandmarks to 1 if you want to enter landmarks manually
manualLandmarks = 0;
% Set this flag to 1 if NIFTI 3D data is being read 
brainFlag = 1;
% Configure the regularization parameter, 0-> exact, high -> relax exact
lambda = 0.0;

% Set interpolation method
%'nearest'; %'none' % interpolation method
interp.method = 'invdist';
% radius or median filter dimension
interp.radius = 5;
%power for inverse weighting interpolation method
interp.power = 2; 

% Get the landmarks manually or through a file
if manualLandmarks == 1
    [Xp Yp Xs Ys] = getLandmarks(imgFix, imgMov, landmarksFileName, brainFlag);
else
    load(landmarksFileName);    
end

%% Warping
% thin plate spline warping
[imgW, imgWr, map, bendingEnergy]  = tpswarp(imgMov,[size(imgMov,2) size(imgMov,1)],...
                                             [Xp' Yp'],[Xs' Ys'],interp, lambda); 
                                         
imgW = imgW / abs(min(imgW(:)));
imgW = imgW + abs(min(imgW(:)));
imgWr = uint8(imgWr);

%% Display Output
figure(1)
Hp = subplot(1,3,1); 
imshow(imgFix);
axis on;
title('Fixed Image');
for ix = 1 : length(Xs),
	h = impoint(gca,Ys(ix),Xs(ix));
    setColor(h, 'r')
end

Hs = subplot(1,3,2); 
imshow(imgMov);
axis on;
title('Moving Image');
for ix = 1 : length(Xp),
	h = impoint(gca,Yp(ix),Xp(ix));
    setColor(h, 'r')
end

Hr = subplot(1,3,3); 
imshow(imgW);
axis on;
regStr = sprintf('Reg Image, bEnergy = %.4f', bendingEnergy);
title(regStr);
for ix = 1 : length(Xs),
	h = impoint(gca,Ys(ix),Xs(ix));
    setColor(h, 'r')
end
%end