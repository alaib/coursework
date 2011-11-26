# Import required libraries
# Import required classes
import copy
import markov_classes
from markov_classes import *
# Import required functions
import markov_functions
from markov_functions import *

choice = int(raw_input('1 : Test Case 1 (predefined data)\n2 : Custom Test Case\nChoose an option = '))
#choice = 1
if choice < 1 or choice > 2:
    print 'Choice out of range, default choice -> 1 : Test Case 1 (predefined data)'
    choice = 1
if choice == 2:
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
    #Read start point and end point
    flag = True;
    while flag:        
        goal_x = int(raw_input('Enter x-coordinate of goal state = '))
        goal_y = int(raw_input('Enter y-coordinate of goal state = '))
        if not(validPoint(goal_x,goal_y,w_xmin,w_ymin,w_xmax,w_ymax)):
            print 'Goal state is outside the workspace (%d,%d,%d,%d), please enter correct values' % (w_xmin,w_ymin,w_xmax,w_ymax)
            continue                
        pgoal = point(goal_x,goal_y)
        if not(clear(pgoal, obstacleList)):
            print 'Goal state is in collision, please choose different goal state'
            continue                    
        flag = False;
    flag = True
    while flag:
        gamma = float(raw_input('Enter the discount factor value = '))
        if(gamma >= 0.0 and gamma <= 1.0):
            flag = False                
elif choice == 1:                    
    # Initalize workspace
    w_xmin = 1
    w_ymin = 1
    w_xmax = 7
    w_ymax = 7
    
    w_min = point(w_xmin,w_ymin)
    w_max = point(w_xmax,w_ymax)
    
    # No of Obstacles
    nObstacles = 4
    obstacleList = []
    
    obstacleList.append(obstacle(1,6,2,7))
    obstacleList.append(obstacle(4,4,4,5))
    obstacleList.append(obstacle(4,1,7,1))
    obstacleList.append(obstacle(7,3,7,4)) 
    
    #Initialize start and end point    
    pgoal = point(6,6)   
    gamma = 0.8            
# end choice

#Set Variables in markov_functions
setGlobalVariables(w_min, w_max, pgoal, gamma, obstacleList);

#Declare and initialize prevJ, J and D to 0
prevJ = [[0]*(w_max.x+1) for x in xrange(w_max.x+1)]
J = [[0]*(w_max.x+1) for x in xrange(w_max.x+1)]
D = [[0]*(w_max.x+1) for x in xrange(w_max.x+1)]

#initialize J with randome values 
for i in range(w_min.x, w_max.x+1):
    for j in range(w_min.y, w_max.y+1):
        p = point(i,j)
        if(p.x == pgoal.x and p.y == pgoal.y):
            J[i][j] = 100            
        elif(clear(p, obstacleList)):
            J[i][j] = random.randint(1, 1000)            
        else:
            J[i][j] = -100
#end initialization

#Loop until the (max(J - prevJ) > threshold)
threshold = 1.0e-15
first = True
count = 1
#compute J and D values for each state
while(True):
    for i in range(w_min.x, w_max.x+1):
        for j in range(w_min.y, w_max.y+1):
            p = point(i,j)
            if(p.x == pgoal.x and p.y == pgoal.y):
                J[i][j] = 100            
            elif(clear(p, obstacleList)):
                reward = getReward(p, J)
                J[i][j] = reward['val']
                D[i][j] = reward['dir']                       
            else:
                J[i][j] = -100                   
    if(first):
        prevJ = copy.deepcopy(J)
        first = False
    else:
        if(isConverged(J, prevJ, threshold)):
            break
        prevJ = copy.deepcopy(J)
    count += 1                    
#end of computation

#start print of J
print('\nExpected Rewards at different states')
print('=======================================')
for i in range(w_min.x, w_max.x+1):
    for j in range(w_min.y, w_max.y+1):
        p = point(i,j)
        if(clear(p, obstacleList)):
            print('J[%d,%d] = %f') % (i, j, J[i][j])
    print('')        
#end print of J                          

#start print of D
print('\nOptimal Action at different states')
print('=====================================')
for i in range(w_min.x, w_max.x+1):
    for j in range(w_min.y, w_max.y+1):
        p = point(i,j)
        if(clear(p, obstacleList)):
            print('J[%d,%d] = %s') % (i, j, D[i][j])
    print('')        
#end print of D
print('Converged after %d iterations') % (count)                        
