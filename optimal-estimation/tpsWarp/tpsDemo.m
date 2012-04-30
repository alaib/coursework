% Start demo
% Read fixed and moving image files
imgFixedFileName = 'data\hand1.png';
imgMovFileName = 'data\hand5.png';

% Save the landmarks
landmarksFileName = 'data\lm_hand.mat';
% Set manualLandmarks to 1 if you want to enter landmarks manually
manualLandmarks = 0;
% Set this flag to 0 if no NIFTI 3D data is being read
brainFlag = 0;
% Configure the regularization parameter, 0-> exact, high -> relax exact
lambda = 0.0;
% Set this flag to 1 if you want the overlap output
showOverlap = 1;

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
    [Xp Yp Xs Ys] = getLandmarks(imgFix, imgMov, landmarksFileName, brainFlag);
else
    load(landmarksFileName);    
end

%% Warping
% thin plate spline warping
[imgW, imgWr, map, bendingEnergy]  = tpswarp(imgMov,[size(imgMov,2) size(imgMov,1)],...
                                             [Xp' Yp'],[Xs' Ys'],interp, lambda);                                          
imgW = uint8(imgW);
imgWr = uint8(imgWr);

%% Display Output
figure(1)
Hp = subplot(1,3,1); 
image(imgFix);
title('Fixed Image');
for ix = 1 : length(Xs),
	h = impoint(gca,Ys(ix),Xs(ix));    
    setColor(h, 'r')
end

Hs = subplot(1,3,2); 
image(imgMov);
title('Moving Image');
for ix = 1 : length(Xp),
	h = impoint(gca,Yp(ix),Xp(ix));
    setColor(h, 'r')
end

Hr = subplot(1,3,3); 
image(imgW);
regStr = sprintf('Reg Image, bEnergy = %.4f', bendingEnergy);
title(regStr);
for ix = 1 : length(Xs),
	h = impoint(gca,Ys(ix),Xs(ix));
    setColor(h, 'r')
end

if showOverlap == 1
    figure(3);
    h = imshow(imgFix, gray(256));
    set(h, 'AlphaData', 0.6);
    ylim = get(gca, 'YLim');
    set(gca, 'YLim', [0.5 ylim(2)]);
    axis on;
    hold on;
    h = imshow(imgW, gray(256));
    set(h, 'AlphaData', 0.8);
    axis on;
    title('Overlap of the Registered and the Fixed Images');
end