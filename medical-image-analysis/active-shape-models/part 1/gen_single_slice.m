segmented_image = loadMETA('segmented_ellipse.mhd');
single_ellipse = segmented_image(:,:,101);
writeMETA(single_ellipse, 'result_single_ellipse.mhd');
imshow(single_ellipse);