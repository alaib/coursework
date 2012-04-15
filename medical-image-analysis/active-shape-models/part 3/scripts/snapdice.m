% Compute Dice Measure of correct segmentation, snap segmentation and ASM
% segmentation
files=dir('../snap/singleSliceTestImg/*.mhd');
for k = 1: length(files)
    snapFile = char(strcat('../snap/singleSliceTestImg/',files(k).name));  
    tempFile = regexp(files(k).name, '\.', 'split');
    correctFile = char(strcat('../testBin/',tempFile(1),'.seg.',tempFile(2),'.',tempFile(3)));

    correctSeg = loadMETA(correctFile);
    snapSeg = loadMETA(snapFile);

    correctArea = 0;
    snapArea = 0;
    commonArea = 0;

    for i = 1 : size(correctSeg,1)
        for j = 1 : size(correctSeg,2)
            if correctSeg(i,j) ~= 0
                correctArea = correctArea + 1;
            end
            if snapSeg(i,j) ~= 0
                snapArea = snapArea + 1;
            end
            if correctSeg(i,j) ~= 0 && snapSeg(i,j) ~= 0
                commonArea = commonArea + 1;
            end
        end
    end

    avg = (correctArea+snapArea)/2;
    diceVal = commonArea / avg;
    status = char(strcat('File = ',files(k).name,' Dice = ',num2str(diceVal)));
    disp(status);
end