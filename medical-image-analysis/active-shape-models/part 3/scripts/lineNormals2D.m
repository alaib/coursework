function N=lineNormals2D(V,L)
% This function calculates the normals, of the line points
% using the neighbouring points of each contour point, 
% forward and backward differences on the end points
%
% N=lineNormals2D(V,L)
%
% inputs,
%   V : List of points/vertices M x 2
% (optional)
%   Lines : A N x 2 list of line pieces, by indices of the vertices
%         (if not set assume Lines=[1 2; 3 4 ; ... ; M-1 M])
%
% outputs,
%   N : The normals of the Vertices M x 2
%
%  N=LineNormals2D(Vertices,Lines);
%  figure,
%  plot([Vertices(:,1) Vertices(:,1)+10*N(:,1)]',[Vertices(:,2) Vertices(:,2)+10*N(:,2)]');
%
% Concept Explanation 
% ====================
% if we define dx=x2-x1 and dy=y2-y1, then the normals are (-dy, dx) and (dy, -dx).

% If no line-indices, assume a x(1) connected with x(2), x(3) with x(4) ...
if(nargin<2)
    L=[(1:(size(V,1)-1))' (2:size(V,1))'];
end

% Calculate tangent vectors
DT=V(L(:,1),:)-V(L(:,2),:);

% Make influence of tangent vector 1/Distance
% (Weighted Central Differences. Points which are closer give a 
% more accurate estimate of the normal)
LL=sqrt(DT(:,1).^2+DT(:,2).^2);
DT(:,1)=DT(:,1)./max(LL.^2,eps);
DT(:,2)=DT(:,2)./max(LL.^2,eps);

D1=zeros(size(V)); D1(L(:,1),:)=DT;
D2=zeros(size(V)); D2(L(:,2),:)=DT;
D=D1+D2;

% Normalize the normal
LL=sqrt(D(:,1).^2+D(:,2).^2);
N(:,1)=-D(:,2)./LL;
N(:,2)= D(:,1)./LL;