function [] = drawPDM( pts, spec )

% function [] = drawPDM( pts, spec )
% 
% drawPDM( pts, 'k-' )
%
% draws the PDM with black lines


xCoors = pts(:,1);
yCoors = pts(:,2);

hold on
set(gca,'YDir','reverse');
plot([xCoors; xCoors(1)], [yCoors; yCoors(1)], spec);

