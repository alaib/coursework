function [krabbe, normal] = getBrainImages(krabbeFileName, normalFileName)
info1 = nii_read_header(krabbeFileName);
data1 = nii_read_volume(info1);
krabbe = imrotate(squeeze(data1(:,:,35)), -90);

info2 = nii_read_header(normalFileName);
data2 = nii_read_volume(info2);
normal = imrotate(squeeze(data2(:,:,35)), -90);
return;