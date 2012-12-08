clc;
clear;

dirPath = '/home/ravikirn/mlcode/data/MHCBN-15mers/encoded/';
files = dir(fullfile('/home/ravikirn/mlcode/data/MHCBN-15mers/encoded/*_encoded.txt'));

for i = 1 : size(files, 1)
    % construct file name
    fName = strcat(dirPath, files(i).name);
    [pathstr, opName, ext] = fileparts(fName);    
    pathstr = regexprep(pathstr, 'encoded', 'matfiles');    
    opFileName = strcat(pathstr, '/', opName, '.mat');
          
    % open file handle
    fid = fopen(fName);
    tline = fgets(fid);     
    
    % Get no. of lines
    cmd = sprintf('wc -l %s', fName);        
    [status, result] = system(cmd);
    splitResult = splitString(result);
    lines = splitResult(1);
    lines = str2double(lines);
            
    X = zeros(lines, 80100);
    y = zeros(lines, 1);
    j = 1;    
    while ischar(tline)
        % trim line
        tline = strtrim(tline);
        % split on comma
        splitStr = splitString(tline, ',');        
        
        % extract code and classification (0 or 1)
        pCode = strtrim(splitStr(2));
        pCode = pCode{1, 1};
        pVal = str2double(strtrim(splitStr(3)));
        
        % convert to a matrix
        for k = 1 : size(pCode, 2)
            if(pCode(k) == '0')
                X(j,k) = 0;
            else
                X(j,k) = 1;
            end            
        end        
        % write out y
        y(j) = pVal; 
        
        % increment j and go to next line
        j = j + 1;
        tline = fgets(fid);        
    end
    % close file handle
    fclose(fid);
    
    % save variables to file
    k = strfind(opFileName, 'test_encoded');
    if(isempty(k))
        trainX = X;
        trainY = y;
        save(opFileName, 'trainX', 'trainY');
    else
        testX = X;
        testY = y;
        save(opFileName, 'testX', 'testY');
    end    
    disp(strcat('Finished processing and output written to ', opFileName));
end
clear;