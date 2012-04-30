README
=======
I Pre-requisites
-----------------
1) Install the latest Matlab

II Running the code
--------------------
1) There are two main executables in the folder namely:-
    a) tpsDemo.m - for 2D Data
    b) tpsBrainDemo.m - for slices of 3D medical data

2) Each of the above files take the below paramters in order to run:-
    a) imgFixedFileName - path of the fixed image
    b) imgMovFileName - path of the moving image 
    c) landmarksFileName - path to save the landmarks
    d) manualLandmarks - 1 = set landmarks manually
                         0 = use the landmarks in the landmarksFileName
    e) brainFlag - 1 = use 3D Brain Data
                   0 = no 3D Data
    f) lambda - Regularization parameter, 0 - exact matching, high - relax matching
    g) showOverlap - 1 = show the overlap of registered and fixed image (optional)
