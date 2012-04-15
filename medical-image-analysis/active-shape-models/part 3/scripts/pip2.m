function result = pip2(point, polygon, draw) % Jack Snoeyink
% point inside polygon test with simulation of simplicity and two outcome
% input: point=kx2, polygon=nx2, optional draw=false(default)
% output: logical k-vector with true(1) = inside, false(0) = outside 
%
% Constructs a ray from each point (not including point) horizontally, and 
% counts polygon segments that cross from on or below to above this ray. 
% (This is consistent with the ray being perturbed up and to the right.)
% Thus, points on the left and bottom of a square are inside, and right and
% top are outside.  If you classify a planar subdivision, every point
% should be inside exactly one region. 

%% Test code
% polygon = round(rand(3,2).*100);
% point = round(rand(1000,2).*100);
% r=pip2(point, polygon, true);

%% Clean up input
if nargin < 3
    draw = false;
end

if any(polygon(1,:) ~= polygon(end,:)) % convert polygon to closed polyline
    polygon(end+1,:) = polygon(1,:);
end
segi = 1:(size(polygon, 1)-1); % first index for each segment

%% Loop over each point
n = size(point,1);
result = false(n,1); % default outside
for j = 1:n
    x = point(j,1); % ray origin
    y = point(j,2);
    
    %% find the indices of segments that cross the ray (no horizontal segs do).
    ci = segi((polygon(segi,2)<= y & polygon(segi+1,2) > y) | (polygon(segi,2) > y & polygon(segi+1,2) <= y));

    %% count crossing if input x is strictly < interpolated x on segment.
    X1 = polygon(ci+1,1);
    Y1 = polygon(ci+1,2);
    crossings = sum((x-X1) < (polygon(ci,1)-X1).*(y-Y1)./(polygon(ci,2)-Y1));
    result(j) = mod(crossings,2)==1;
end

%% plot
if draw
plot(polygon(:,1),polygon(:,2),'b-d', ... % polygon
    point(result,1), point(result,2), 'ro', ... % inside
    point(~result,1), point(~result,2), 'y+'); % outside
end
