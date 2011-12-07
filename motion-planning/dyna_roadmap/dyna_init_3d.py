# Import required libraries
import sys
import random
import pprint
import copy

# Import required classes
import dyna_roadmap_classes_3d 
from dyna_roadmap_classes_3d import *

# Import required functions
import dyna_roadmap_functions_3d
from dyna_roadmap_functions_3d import *

#import djikstra algorithm
import djikstra
from djikstra import *

#import draw graph
import draw_graph_3d
from draw_graph_3d import *

#choice = int(raw_input('1 : Test Case 1 (predefined data)\n2 : Test Case 2 (predefined data)\n3 : Custom Test Case\nChoose an option = '))
choice = 1
if choice < 1 or choice > 3:
    print 'Choice out of range, default choice -> 1 : Test Case 1 (predefined data)'
    choice = 1
if choice == 3:
    # Initalize workspace
    w_xmin = int(raw_input('Enter Workspace xmin = '))
    w_ymin = int(raw_input('Enter Workspace ymin = '))
    w_zmin = int(raw_input('Enter Workspace zmin = '))
    w_xmax = int(raw_input('Enter Workspace xmax = '))
    w_ymax = int(raw_input('Enter Workspace ymax = '))
    w_zmax = int(raw_input('Enter Workspace zmax = '))
    
    w_min = point3D(w_xmin, w_ymin, w_zmin)
    w_max = point3D(w_xmax, w_ymax, w_zmax)
    
    # No of Obstacles
    nObstacles = int(raw_input('Enter the number of Obstacles : '))
    staticOList = []
    i = 1
    while i <= nObstacles:    
        print 'Enter Obstacle %d data' % (i)
        o_xmin = int(raw_input('xmin = '))
        o_ymin = int(raw_input('ymin = '))
        o_zmin = int(raw_input('zmin = '))
        o_xmax = int(raw_input('xmax = '))
        o_ymax = int(raw_input('ymax = '))    
        o_zmax = int(raw_input('zmax = '))    

        if not(validPoint(o_xmin, o_ymin, o_zmin, w_xmin, w_ymin, w_zmin, w_xmax, w_ymax, w_zmax) \
                 and validPoint(o_xmax, o_ymax, o_zmax, w_xmin, w_ymin, w_zmin, w_xmax, w_ymax, w_zmax)):
                print 'Obstacle is outside the workspace (%d,%d,%d,%d,%d,%d), please enter correct values' % (w_xmin, w_ymin, w_zmin, w_xmax, w_ymax, w_zmax)
                continue        
        staticOList.append(staticObstacle3D(o_xmin, o_ymin, o_zmin, o_xmax, o_ymax, o_zmax ));
        i += 1
    #Read start point and end point
    flag = True;
    while flag:
        start_x = int(raw_input('Enter x-coordinate of start point = '))
        start_y = int(raw_input('Enter y-coordinate of start point = '))
        start_z = int(raw_input('Enter z-coordinate of start point = '))
        end_x = int(raw_input('Enter x-coordinate of end point = '))
        end_y = int(raw_input('Enter y-coordinate of end point = '))
        end_z = int(raw_input('Enter z-coordinate of end point = '))
        if not(validPoint(start_x, start_y, start_z, w_xmin, w_ymin, w_zmin, w_xmax, w_ymax, w_zmax) \
               and validPoint(end_x, end_y, end_z, w_xmin, w_ymin, w_zmin, w_xmax, w_ymax, w_zmax)):
            print 'Either start/end point is outside the workspace (%d,%d,%d,%d,%d,%d), please enter correct values' % (w_xmin, w_ymin, w_zmin, w_xmax, w_ymax, w_zmax)
            continue        
        pstart = point3D(start_x,start_y)
        pend = point3D(end_x,end_y)
        if not(clear(pstart, staticOList) and clear(pend, staticOList)):
            print 'Either start/end point is in collision, please choose different set of start and end points'
            continue                    
        flag = False;
elif choice == 2:
    # Initalize workspace
    w_xmin = 0
    w_ymin = 0
    w_zmin = 0
    w_xmax = 10
    w_ymax = 10
    w_zmax = 10
    
    w_min = point3D(w_xmin, w_ymin, w_zmin)
    w_max = point3D(w_xmax, w_ymax, w_zmax)
    
    # No of Obstacles
    nObstacles = 4
    staticOList = []    
    staticOList.append(staticObstacle3D(0,6,1,3,10,2))
    staticOList.append(staticObstacle3D(4,0,1,6,3,2))
    staticOList.append(staticObstacle3D(4,7,2,6,9,4))
    staticOList.append(staticObstacle3D(7,3,2,8,5,4))
    
    dynaOList = []
    #xmin, ymin, zmin, xmax, ymax, zmax, deltax, deltay, deltaz, radius
    dynaOList.append(dynaObstacle3D(2,4,1,6,4,1,0.25,0,0,1))
    
    pstart = point3D(2,3,0)
    pend = point3D(7,8,1)
    
    tstep = 0.5            
elif choice == 1:                    
    # Initalize workspace
    w_xmin = 0
    w_ymin = 0
    w_zmin = 0
    w_xmax = 22
    w_ymax = 22
    w_zmax = 22
    
    w_min = point3D(w_xmin, w_ymin, w_zmin)
    w_max = point3D(w_xmax, w_ymax, w_zmax)
    
    # No of Obstacles
    nObstacles = 4
    staticOList = []    
    staticOList.append(staticObstacle3D(7,0,1,12,8,2))
    staticOList.append(staticObstacle3D(8,14,1,13,22,2))
    staticOList.append(staticObstacle3D(0,14,2,5,19,4))
    staticOList.append(staticObstacle3D(17,6,2,22,13,4))
    
    dynaOList = []
    #xmin, ymin, xmax, ymax, deltax, deltay, radius, velocity
    dynaOList.append(dynaObstacle3D(6,11,1, 14,11,1, 0.25,0,0, 1))
    dynaOList.append(dynaObstacle3D(4,6,1, 4,11,1,   0,0.25,0, 1))
    
    pstart = point3D(2,2,0)
    #pend = point3D(14,21,1)
    pend = point3D(14,22,2)
    
    tstep = 0.5    
# end choice

#sys.exit(0)
#nSample = int(raw_input('Enter the no. of milestones = '))
#noOfNeighbors = int(raw_input('Enter the number of nearest neighbors to find for each milestone = '))
nSample = computeMilestones(w_min, w_max, staticOList)
noOfNeighbors = computeNoOfNeighbors(nSample)

print('Milestones = %d, No. of Neighbors = %d') % (nSample, noOfNeighbors)

samplePoints = []
#Append Start and end points
samplePoints.append(pstart)
samplePoints.append(pend)
i = 0;
notFound = 0
notConnected = True
tries = 1
maxTries = 1000
maxSampleTries = 100000
noSampleFound = False
# Compute 'nSample' points which pass 'clear'
while notConnected and tries <= maxTries:
    sampleTries = 0
    while i < nSample-2:
        sampleTries += 1
        if sampleTries > maxSampleTries:                
            noSampleFound = True
            break;             
        p = gen_uniform_random_point(w_min,w_max)
        # Check if point is not already generated and is collision free
        if checkIfPointNotAdded(p, samplePoints) and clear(p, staticOList):
            samplePoints.append(p)
            i +=1
            
    # Break out of outer while loop if no suitable sample was found        
    if noSampleFound:
        break;    
    # Initialize the graph
    graph = {}    
            
    # For each point, find its nearest neighbors and draw the graph
    for i in range(len(samplePoints)):
        p1 = samplePoints[i]    
        # Initialize the distance array
        d = []
        for j in range(len(samplePoints)):
            p2 = samplePoints[j];        
            # Set very high value for the same point, so that we avoid it        
            if(p1.x == p2.x and p1.y == p2.y and p1.z == p2.z):
                d.insert(j,99999)
            else :
                d.insert(j,distance(p1,p2))    
        #Find the minimum distance vertex in d
        n = 0
        flag = True
        v = {}
        while n < noOfNeighbors and flag:
            pos, minval = min_list_value(d)
            if minval == 99999:
                flag = False
                continue        
            p2 = samplePoints[pos];
            if link(p1,p2,staticOList):
                v[pos] = minval                
                d[pos] = 99999
                n +=1
            else:
                d[pos] = 99999
        #end while loop
        graph[i] = v
        if (n < noOfNeighbors):                
            notFound = 1
            break                                    
    # end for loop
    if notFound == 0:
        notConnected = False
    else :        
        #print 'Unable to find %d nearest neighbors retrying sample points, tries = %d' % (noOfNeighbors, tries)
        #Append Start and end points
        samplePoints = []        
        samplePoints.append(pstart)
        samplePoints.append(pend)
        i = 0;
        notFound = 0        
        tries += 1
#end connectivity loop

#Failure to build a connected Graph, hence no solution found    
if tries > maxTries:
    print 'Unable to find %d milestones with %d neighbors after %d tries, Quitting' % (nSample, noOfNeighbors, maxTries)
    print 'Total Cost = Infinity'    
    sys.exit(1)
elif noSampleFound:
    print 'No sample points set found where each point passes clear function , Max Sample Tries = %d' % (maxSampleTries)
    print 'Total Cost = Infinity'
    sys.exit(1)
else: 
    #print '%d milestones with %d neighbors found after %d sampling tries' % (nSample, noOfNeighbors, tries)
    cost,path = shortestPath(graph, 0, 1)
    if cost < 0:
        print 'Path may or may not exist between (%d,%d,%d) to (%d,%d,%d)' % (pstart.x, pstart.y, pstart.z, pend.x, pend.y, pend.z)
        print 'Total Cost = Infinity'
        sys.exit(1)
#end failure case handling        
    
#Found a successful connected graph, continue with Dynamic PRM Algorithm
# Find the shortest path
# Start Vertex = 0, End Vertex = 1
print '%d milestones with %d neighbors found after %d sampling tries' % (nSample, noOfNeighbors, tries)

cost,path = shortestPath(graph, 0, 1)
l = len(path)
for i in range(l):
    if i != l-1:
        print '(%d,%d,%d) -> ' % (samplePoints[path[i]].x, samplePoints[path[i]].y, samplePoints[path[i]].z),
    else:
        print '(%d,%d,%d)' % (samplePoints[path[i]].x, samplePoints[path[i]].y, samplePoints[path[i]].z),
print '\nTotal Cost = %f' % (cost)                    
#sys.exit(0);
#printGraph(graph, samplePoints)
drawBaseGraph(w_min, w_max, pstart, pend, staticOList, dynaOList)
start = point3D(samplePoints[path[0]].x, samplePoints[path[0]].y, samplePoints[path[0]].z)
curr = point3D(samplePoints[path[0]].x, samplePoints[path[0]].y, samplePoints[path[0]].z)
dest = point3D(samplePoints[path[1]].x, samplePoints[path[1]].y, samplePoints[path[1]].z)
i = 2
tmax = 100.0
totalTime = 0.0
tstep = 0.5
vrange = 2.5 
vmax = vrange
actions = [1, 0, -1]
prevPF = 100.0
currPF = 100.0
first = True
count = 0
drawGraph = True
pfThreshold = 0.3 
#Update Obstacles
updateObstacles(dynaOList)
colFree = False
#start dyna while loop
while(totalTime < tmax):        
    #Traverse from curr to dest vertex in timesteps
    d = distance(curr, dest)
    if(d < vrange):
        vmax = d / tstep
    else:
        vmax = vrange
                
    vp = findOptimalVelocity(d, tstep, vmax) 
    if(vp == -1):
        print('Negative vp velocity')
        sys.exit(0)
    #end failure case
    
    alpha = 0
    tedgeMax = (d / vp) * 100 * tstep
    tedge = 0.0
           
    while(alpha < 1 and tedge < tedgeMax):
        #start action check
        colFree = False
        for action in actions:            
            #Try to move ahead if possible                                    
            col = collisionFree(alpha, curr, start, dest, vp, tstep, dynaOList, action)
            
            if(col['pf'] <= pfThreshold * col['deltaMoved']):
                print('Dangerous Potential Field')
                action1 = 0
                action2 = -1
                col1 = collisionFree(alpha, curr, start, dest, vp, tstep, dynaOList, action1)
                col2 = collisionFree(alpha, curr, start, dest, vp, tstep, dynaOList, action2)
                debugDisplay(col, count, totalTime, action, alpha, vp, curr, dest, dynaOList, prevPF)
                print('0 PF = %f') % (col1['pf'])
                print('-1 PF = %f') % (col2['pf'])
                print('Alternate action is to be taken')
                if(col2['pf'] >= col1['pf'] and col2['colFree'] == True):
                    col = col2
                    action = action2
                    colFree = True
                    print('Possible new action to take = %d') % (action)
                elif(col1['pf'] > col2['pf'] and col1['colFree'] == True):
                    col = col1
                    action = action1
                    colFree = True
                    print('Possible new action to take = %d') % (action)
                else:
                    print('No possible action can be undertaken')                
                if(colFree == True):
                    curr = col['newPos']
                    alpha = col['newAlpha']
                    if(drawGraph):
                        updateDraw(dynaOList, curr, count)                                            
                    debugDisplay(col, count, totalTime, action, alpha, vp, curr, dest, dynaOList, prevPF)
                    #Print action taken
                    count += 1
                    if(first):
                        prevPF = col['pf']
                        first = False
                    else:
                        #hack for assigning value to prevPF
                        prevPF = currPF - 1.0 + 1.0
                        currPF = col['pf']                                
                break                                                                                                           
                
            if(col['colFree'] == True):
                colFree = True
                curr = col['newPos']
                alpha = col['newAlpha']
                if(drawGraph):
                    updateDraw(dynaOList, curr, count)                                            
                debugDisplay(col, count, totalTime, action, alpha, vp, curr, dest, dynaOList, prevPF)
                #Print action taken
                count += 1                
                if(first):
                    prevPF = col['pf']
                    first = False
                else:
                    prevPF = currPF
                    currPF = col['pf']
                break
        #end actions
        
        #end failure case
        if(colFree == False):
            print('Robot hit the obstacle, no viable action possible')
            break
        #end failure case
        
        tedge += tstep
        updateObstacles(dynaOList)
        totalTime += tstep                    
    #end while loop    
    
    if(colFree == False):
        break
    if(tedge >= tedgeMax):
        print('No solution exists, timed out on local edge traversal')
        start.show('Current Pos = ')
        dest.show(' Dest Pos = ')    
        break                
    #end failure case    
    start = dest
    curr = point3D(start.x, start.y, start.z)
    if(curr.x == pend.x and curr.y == pend.y and curr.z == pend.z):
        break
    dest = point3D(samplePoints[path[i]].x, samplePoints[path[i]].y, samplePoints[path[i]].z)
    i += 1    
#
