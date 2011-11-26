# Import required libraries
# Import required classes
import prm_classes 
from prm_classes import *
# Import required functions
import prm_functions
from prm_functions import *

choice = int(raw_input('1 : Test Case 1 (predefined data)\n2 : Test Case 2 (predefined data)\n3 : Custom Test Case\nChoose an option = '))
if choice < 1 or choice > 3:
    print 'Choice out of range, default choice -> 1 : Test Case 1 (predefined data)'
    choice = 1
if choice == 3:
    # Initalize workspace
    w_xmin = int(raw_input('Enter Workspace xmin = '))
    w_ymin = int(raw_input('Enter Workspace ymin = '))
    w_xmax = int(raw_input('Enter Workspace xmax = '))
    w_ymax = int(raw_input('Enter Workspace ymax = '))
    
    w_min = point(w_xmin,w_ymin)
    w_max = point(w_xmax,w_ymax)
    
    # No of Obstacles
    nObstacles = int(raw_input('Enter the number of Obstacles : '))
    obstacleList = []
    i = 1
    while i <= nObstacles:    
        print 'Enter Obstacle %d data' % (i)
        o_xmin = int(raw_input('xmin = '))
        o_ymin = int(raw_input('ymin = '))
        o_xmax = int(raw_input('xmax = '))
        o_ymax = int(raw_input('ymax = '))    
        if not(validPoint(o_xmin,o_ymin,w_xmin,w_ymin,w_xmax,w_ymax) \
                 and validPoint(o_xmax,o_ymax,w_xmin,w_ymin,w_xmax,w_ymax)):
                print 'Obstacle is outside the workspace (%d,%d,%d,%d), please enter correct values' % (w_xmin,w_ymin,w_xmax,w_ymax)
                continue        
        obstacleList.append(obstacle(o_xmin,o_ymin,o_xmax,o_ymax));
        i += 1   
elif choice == 1 or choice == 2:                    
    # Initalize workspace
    w_xmin = 0
    w_ymin = 0
    w_xmax = 22
    w_ymax = 22
    
    w_min = point(w_xmin,w_ymin)
    w_max = point(w_xmax,w_ymax)
    
    # No of Obstacles
    nObstacles = 4
    obstacleList = []
    if choice == 1:
        obstacleList.append(obstacle(7,0,12,8))
        obstacleList.append(obstacle(8,14,13,22))
    else:
        obstacleList.append(obstacle(7,0,12,10))
        obstacleList.append(obstacle(8,12,13,22))
                
    obstacleList.append(obstacle(0,14,5,19))
    obstacleList.append(obstacle(17,6,22,13))    
# end choice
 
while(1):
    inp = int(raw_input('\nEnter your choice of operation (0: Quit, 1: Clear, 2: Link) = '))
    #Default case
    if inp < 0 or inp > 2:
        print 'Choice out of range , default choice -> 2: Link'
        inp = 2
        
    if inp == 0:
        break
    elif inp == 1:
        p_x = int(raw_input('Enter the x-coordinate of the test point = '))
        p_y = int(raw_input('Enter the y-coordinate of the test point = '))
        if not(validPoint(p_x,p_y,w_xmin,w_ymin,w_xmax,w_ymax)):
            print 'Point is outside the workspace (%d,%d,%d,%d), please enter correct values' % (w_xmin,w_ymin,w_xmax,w_ymax)
            continue
        p = point(p_x,p_y)
        clearVal = clear(p, obstacleList)
        print "Clear function returned %d (0 = collision-exists, 1 = collision-free)\n" % (clearVal),
    elif inp == 2:
        p1_x = int(raw_input('Enter the x-coordinate of the start point = '))
        p1_y = int(raw_input('Enter the y-coordinate of the start point = '))
        if not(validPoint(p1_x,p1_y,w_xmin,w_ymin,w_xmax,w_ymax)):
            print 'Point is outside the workspace (%d,%d,%d,%d), please enter correct values' % (w_xmin,w_ymin,w_xmax,w_ymax)
            continue                
        p2_x = int(raw_input('Enter the x-coordinate of the end point = '))
        p2_y = int(raw_input('Enter the y-coordinate of the end point = '))
        if not(validPoint(p2_x,p2_y,w_xmin,w_ymin,w_xmax,w_ymax)):
            print 'Point is outside the workspace (%d,%d,%d,%d), please enter correct values' % (w_xmin,w_ymin,w_xmax,w_ymax)
            continue
        p1 = point(p1_x,p1_y)
        p2 = point(p2_x,p2_y)
        linkVal = link(p1, p2, obstacleList)
        print "Link function returned %d (0 = collision-exists, 1 = collision-free)\n" % (linkVal),        
        
                 
