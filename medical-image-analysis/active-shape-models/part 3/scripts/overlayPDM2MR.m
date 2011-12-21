function [] = overlayPDM2MR ( PDMfName, MRfName, skipReadPoints)

% function [] = overlayPDM2MR ( PDMfName, MRfName )
% 
% overlayPDM2MR ( '7.pts', '7.2d.mhd' )
%
% draws the PDM on the raw MR slice

I = loadMETA(MRfName);
if skipReadPoints
    pts = PDMfName;
else
    pts = readPoints( PDMfName );
end

figure;
h = imshow(transpose(I),[0 255]);
drawPDM(pts,'g-');

