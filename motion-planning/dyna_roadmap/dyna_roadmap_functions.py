# Import required libraries
import copy
import math
import random
# Import required classes
import dyna_roadmap_classes 
from dyna_roadmap_classes import *

# Start validPoint
def validPoint(x,y,xmin,ymin,xmax,ymax):
    if x < xmin or x > xmax or y < ymin or y > ymax:
        return 0
    else:
        return 1
# End validPoint    
    
# Start clear
def clear(p, obstacleList):
    " Check if a point is in collision or not"
    for o in obstacleList:
        if p.x >= o.xmin and p.x <= o.xmax and p.y >= o.ymin and p.y <= o.ymax :
            return 0
        else:
            continue
    return 1
# End Clear

# Start link
def link(point1, point2, obstacleList):
    "Generate points in between p1 and p2 using Bresenham's Line Drawing Algorithm"
    "Check if each of these points passes through clear, if yes, return link true, else false"
    p1 = copy.deepcopy(point1);
    p2 = copy.deepcopy(point2);
    
    # Compute if line is steep (slope > 1)    
    issteep = abs(p2.y-p1.y) > abs(p2.x-p1.x)
    # if steep, swap x and y value of each point
    if issteep:
        p1.x, p1.y = p1.y, p1.x
        p2.x, p2.y = p2.y, p2.x

    # if p1.x > p2.x , swap the two points
    if p1.x > p2.x:
        p1.x, p2.x = p2.x, p1.x
        p1.y, p2.y = p2.y, p1.y

    # Compute deltax, deltay, error, ystep
    deltax = p2.x - p1.x
    deltay = abs(p2.y-p1.y)
    error = int(deltax / 2)
    y = p1.y
    ystep = 1
    if p1.y > p2.y:
        ystep = -1
    """print '================================='
    print 'Interim Point    Clear'
    print '================================='"""
    for x in range(p1.x, p2.x + 1):
        if issteep:
            p = point(y,x)            
        else:
            p = point(x,y)        
        clearVal = clear(p,obstacleList)
        #print "(%d,%d)            %d" % (p.x,p.y,clearVal)
        # Check if the intermediate point is clear, if not return 0, else continue
        if clearVal == 0:
            return 0                    
        error -= deltay
        if error < 0:
            y += ystep
            error += deltax
    # All the intermediate points are collision free, return 1    
    return 1;                    
# End link function

#start distance function
def distance(p1, p2):
    return round(math.sqrt(float((p2.x-p1.x)**2+(p2.y-p1.y)**2)),1)
#end distance function

#start generate uniformly distributed random number
def gen_uniform_random_point(w_min, w_max):
    xval = random.random()
    yval = random.random()
    xval = int((w_max.x - w_min.x) * xval + w_min.x)
    yval = int((w_max.y - w_min.y) * yval + w_min.y)
    return point(xval,yval)    
#end gen_random function    

#start min_value 
def min_list_value(A):
    min = A[0]
    pos = 0
    for i in range(1, len(A)):
        if min > A[i]:
            min = A[i]
            pos = i
    return pos, min        
#end min_value

#start checkIfPointNotAdded
def checkIfPointNotAdded(p, samplePoints):
    for i in range(len(samplePoints)):
        cur = samplePoints[i]
        if p.x == cur.x and p.y == cur.y:
            return 0    
    return 1
#end checkIfPointNotAdded

#start computeMilestones
def computeMilestones(w_min, w_max, oList):
    total = (w_max.x - w_min.x) * (w_max.y - w_min.y)
    for o in oList:
        total -= (o.xmax - o.xmin) * (o.ymax - o.ymin)
    return int(total * 0.6)
#end computeMilestones

#start computeMilestones
def computeNoOfNeighbors(w_min, w_max, oList):
    total = (w_max.x - w_min.x) * (w_max.y - w_min.y)
    for o in oList:
        total -= (o.xmax - o.xmin) * (o.ymax - o.ymin)
    return int(math.sqrt(total)*0.6)
#end computeMilestones

#start printGraph
def printGraph(G, sPoints):
    for i in G:
        print("(%d, %d) : {") % (sPoints[i].x, sPoints[i].y),
        for j in G[i]:
            print('(%d, %d) : %.1f, ') % (sPoints[j].x, sPoints[j].y, G[i][j]),
        print('}')                                
#end printGraph

#start findOptimalVelocity
def findOptimalVelocity(dist, tstep, vmax):
    v = vmax
    while(v <= vmax):
        val = dist / (v * tstep)
        diff = val - math.ceil(val)
        if( diff >= -1e-10 and diff <= 1e-10):
            return v
        else:
            v -= 0.1
    #no proper velocity found
    return -1
#end findOptimalVelocity

#start updateObstacles
def updateObstacles(oList):
    for o in oList:
        o.move()    
#start updateObstacles

#start collisionFree
def collisionFree(alpha, curr, start, dest, v, tstep, oList, action):
    temp = copy.deepcopy(curr)
    d = distance(start, dest)
    alphastep = action * (v*tstep)/d
    alpha += alphastep
    temp.x = alpha * (dest.x - start.x) + start.x
    temp.y = alpha * (dest.y - start.y) + start.y
    deltaMoved = distance(curr, temp)
    minPF = 100 
    colFree = True    
    for o in oList:
        dist = math.sqrt((o.currx-temp.x)**2 + (o.curry-temp.y)**2)
        pf = distance(temp, point(o.currx, o.curry)) - o.r
        if(pf < minPF):
            minPF = pf
        if(dist <= o.r):
            colFree = False
            break            
    return {'colFree': colFree, 'newPos' : temp, 'newAlpha' : alpha, 'pf' : pf, 'deltaMoved' : deltaMoved}
#start collisionFree    

#start debugDisplay
def debugDisplay(col, count, totalTime, action, alpha, vp, curr, dest, dynaOList, prevPF):
    print('\n=============================')                
    print('Step = %d') % (count)
    print('time = %.1f') % (totalTime)
    print('action = %d, alpha = %.2f, vel = %.1f') % (action, alpha, vp)                        
    curr.show('Current Pos = ')
    dest.show('Dest Pos = ')
    dynaOList[0].show()
    print('Current Potential Field = %f , Previous Potential Field = %f') % (col['pf'], prevPF)
    print('Delta Move = %f') % (col['deltaMoved'])
    print('===============================\n')
#end debugDisplay     

#start printCoords
def printCoords(col, count, totalTime, action, alpha, vp, curr, dest, dynaOList, prevPF):
    print('\n=============================')                
    print('Step = %d') % (count)
    print('time = %.1f') % (totalTime)
    print('action = %d, alpha = %.2f, vel = %.1f') % (action, alpha, vp)                        
    curr.show('Current Pos = ')
    dest.show('Dest Pos = ')
    dynaOList[0].show()
    print('Current Potential Field = %f , Previous Potential Field = %f') % (col['pf'], prevPF)
    print('Delta Move = %f') % (col['deltaMoved'])
    print('===============================\n')
#end printCoords     