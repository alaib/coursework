Pre-requisites
==============
1) Python 2.6+
2) matplotlib library for Python
3) Visual Python (VPython)

How to Install Pre-requisites
=============================
For Ubuntu, run the below command to install all the pre-requisites,

$ sudo apt-get install python python-matplotlib python-visual

If you're using a Linux flavor other than Ubuntu you may need to 
use the appropriate package installer and install the pre-requisites.

Note
====
If you don't have the appropriate graphic drivers, the 3D output display 
may not work and you may get Segmentation Fault.

Programs to Run
===============
There are two main programs in the codebase namely:-

1) dyna_init.py - 2D implementation of Jur P. van den Berg et al two search algorithm
                  to compute a trajectory for a point robot from start to goal avoiding
                  the static and dynamic obstacles. Uses matplotlib for visualization.
                  
2) dyna_init_3d.py - 3D implementation of Jur P. van den Berg et al two search algorithm
                     to compute a trajectory for a point robot from start to goal avoiding
                     the static and dynamic obstacles. Uses VPython for visualization.

Steps to run the program
========================
a) Each of the above program can be run by typing 'python dyna_init.py' and 'python dyna_init_3d.py'
respectively on the command line. 

b)On doing so, the user is asked to choose between three choices, i.e, the user can either choose two 
pre-defined test cases with options 1 and 2 or enter his own custom data.

c) If custom data option is chosen, the user needs to enter the workspace, static obstacles, dynamic 
obstacles, start and goal coordinates. 

d) Once the PRM has been constructed for the static scene, the program asks the user whether he/she wants 
to enter values for the velocity of the robot and the potential field threshold 'alpha' or use the default 
values. 

e) Once this step is done, the program computes the trajectory to the goal. If 'debug' flag is disabled, 
at the end of computation, only the success or failure of the program is displayed along with time taken. 

f) If 'debug' flag is enabled, one can see the exact coordinates chosen by the point robot at each
time step. The debug flag is disabled by default.
