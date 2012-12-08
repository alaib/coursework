function w = updatew(X, D, z0, z2, u0, u2, rho)        
    w =   [X; D] \ [z0+((1/rho)*u0); z2+((1/rho)*u2)];    
        
    if 0
    % block coordinate descent solver if 'OUT OF MEMORY'
    x = [X; D];
    y = [z0+((1/rho)*u0); z2+((1/rho)*u2)];        
    featSize = size(X, 2);

    %clear variables
    clear X D;
    prevw = ones(featSize, 1); %so that on first update, we get back something
    w = ones(featSize, 1); %so that on first update, we get back something
    blk = featSize / 10;
    p = featSize;
    maxIter = 50;

    for it=1:maxIter;
        yres = y - x*w;
        for i=1:blk:p
            inds = i:min(i+blk-1,p);
            yres = yres + x(:,inds)*w(inds);
            w(inds) = x(:,inds) \ yres;
            yres = yres - x(:,inds)*w(inds);
        end

        if norm(prevw - w) < 1e-6
            break;
        end
        fprintf('Within update w, Iteration no. = %d, Diff (prevw-w) = %.4f\n', it, norm(prevw-w));
        prevw = w;        
    end   
    end
end
