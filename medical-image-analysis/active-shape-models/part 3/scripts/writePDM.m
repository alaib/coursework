function writePDM( pdm, pdmFname, MRFname )
    I = loadMETA(MRFname);
    temp = zeros(size(I,1), size(I,2));
    %temp = transpose(temp);   
    maxx = size(temp,1);
    maxy = size(temp,2);
    for i = 1 : maxx
        for j = 1 : maxy
            points((i-1)*maxy+j,1) = i;
            points((i-1)*maxy+j,2) = j;
        end
    end    
    result = pip2(points,round(pdm));
    for i = 1 : size(result,1)
        if(result(i) ~= 0)
            temp(points(i,1),points(i,2)) = 255;
        end
    end
    writeMETA(temp,pdmFname);
    imshow(transpose(temp));
end

