# Import required libraries
import copy
import math
import random
# Import required classes
import dyna_roadmap_classes_3d 
from dyna_roadmap_classes_3d import *

# Start validPoint
def validPoint(x, y, z, xmin, ymin, zmin, xmax, ymax, zmax):
    if x < xmin or x > xmax or y < ymin or y > ymax or z < zmin or z > zmax:
        return 0
    else:
        return 1
# End validPoint    
    
# Start clear
def clear(p, obstacleList):
    " Check if a point is in collision or not"
    for o in obstacleList:
        if p.x >= o.xmin and p.x <= o.xmax and p.y >= o.ymin and p.y <= o.ymax and p.z >= o.zmin and p.z <= o.zmax:
            return 0
        else:
            continue
    return 1
# End Clear

# Start link
def link(point1, point2, oList):
    "Generate points in between p1 and p2 using Bresenham's Line Drawing Algorithm"
    "Check if each of these points passes through clear, if yes, return link true, else false"
    p1 = copy.deepcopy(point1)
    p2 = copy.deepcopy(point2)
    for o in oList:
        omin = point3D(o.xmin, o.ymin, o.zmin)
        omax = point3D(o.xmax, o.ymax, o.zmax)
        col = checkLineBox(p1, p2, omin, omax)
        if(col == True):
            return False
    #end loop
    return True
# End link function

#start getIntersection 
def getIntersection(fDst1, fDst2, p1, p2):
    hit = point3D(0, 0, 0)
    if ( (fDst1 * fDst2) >= 0.0):
        return 0, hit
    if (fDst1 == fDst2):
        return 0, hit
    m = -fDst1/(fDst2-fDst1)
    x = p1.x + (p2.x - p1.x) * m
    y = p1.y + (p2.y - p1.y) * m
    z = p1.z + (p2.z - p1.z) * m
    hit = point3D(x ,y, z)
    return 1, hit
#end getIntersection

#start inBox
def inBox( hit, omin, omax, axis):
    if ( axis==1 and hit.z > omin.z and hit.z < omax.z and hit.y > omin.y and hit.y < omax.y):
        return 1
    if ( axis==2 and hit.z > omin.z and hit.z < omax.z and hit.x > omin.x and hit.x < omax.x):
        return 1;
    if ( axis==3 and hit.x > omin.x and hit.x < omax.x and hit.y > omin.y and hit.y < omax.y):
        return 1
    return 0
#end inBox
    
#start checkLineBox
#returns True if line (p1, p2) intersects with the box (omin, omax)
#returns intersection point in hit
def checkLineBox( omin, omax, p1, p2):
    
    if (p2.x < omin.x and p1.x < omin.x):
        return False
    if (p2.x > omax.x and p1.x > omax.x):
        return False
    if (p2.y < omin.y and p1.y < omin.y):
        return False
    if (p2.y > omax.y and p1.y > omax.y):
        return False
    if (p2.z < omin.z and p1.z < omin.z):
        return False
    if (p2.z > omax.z and p1.z > omax.z):
        return False
    if (p1.x > omin.x and p1.x < omax.x and p1.y > omin.y and p1.y < omax.y and p1.z > omin.z and p1.z < omax.z):
        return True
        
    intcol, hit = getIntersection(p1.x-omin.x, p2.x-omin.x, p1, p2)
    if(intcol and inBox(hit, omin, omax, 1)):
        return True

    intcol, hit = getIntersection( p1.y-omin.y, p2.y-omin.y, p1, p2)
    if(intcol and inBox(hit, omin, omax, 2)):
        return True

    intcol, hit = getIntersection(p1.z-omin.z, p2.z-omin.z, p1, p2)
    if(intcol and inBox(hit, omin, omax, 3)):
        return True
    
    intcol, hit = getIntersection(p1.x-omax.x, p2.x-omax.x, p1, p2)
    if(intcol and inBox(hit, omin, omax, 1)):
        return True

    intcol, hit = getIntersection(p1.y-omax.y, p2.y-omax.y, p1, p2)
    if(intcol and inBox(hit, omin, omax, 2)):
        return True

    intcol, hit = getIntersection( p1.z-omax.z, p2.z-omax.z, p1, p2)
    if(intcol and inBox(hit, omin, omax, 3)):
        return True

    return False
#end checkLineBox

#start distance function
def distance(p1, p2):
    return round(math.sqrt(float((p2.x-p1.x)**2+(p2.y-p1.y)**2+(p2.z-p1.z)**2)),1)
#end distance function

#start generate uniformly distributed random number
def gen_uniform_random_point(w_min, w_max):
    xval = random.random()
    yval = random.random()
    zval = random.random()
    xval = int((w_max.x - w_min.x) * xval + w_min.x)
    yval = int((w_max.y - w_min.y) * yval + w_min.y)
    zval = int((w_max.z - w_min.z) * zval + w_min.z)
    return point3D(xval, yval, zval)    
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
        if p.x == cur.x and p.y == cur.y and p.z == cur.z:
            return 0    
    return 1
#end checkIfPointNotAdded

#start computeMilestones
def computeMilestones(w_min, w_max, oList):
    total = (w_max.x - w_min.x) * (w_max.y - w_min.y) * (w_max.z - w_min.z)
    obs = 0
    milestones = 0
    for o in oList:
        obs += (o.xmax - o.xmin) * (o.ymax - o.ymin) * (o.zmax - o.zmin)
    obsRatio = obs/float(total)
    if(obsRatio < 0.5):
        milestones = obsRatio * 2 * total
    else:
        milestones = (1 - obsRatio - 0.1) * total
    return milestones    
#end computeMilestones

#start computeMilestones
def computeNoOfNeighbors(noOfMilestones):    
    return int(math.sqrt(noOfMilestones))
#end computeMilestones

#start printGraph
def printGraph(G, sPoints):
    for i in G:
        print("(%d, %d, %d) : {") % (sPoints[i].x, sPoints[i].y, sPoints[i].z),
        for j in G[i]:
            print('(%d, %d, %d) : %.1f, ') % (sPoints[j].x, sPoints[j].y, sPoints[j].z, G[i][j]),
        print('}')                                
#end printGraph

#start findOptimalVelocity
def findOptimalVelocity(dist, tstep, vmax):
    v = vmax
    while(v <= vmax):
        val = dist / (v * tstep)
        valstr = str(val)
        valsplit = valstr.split('.')       
        if(valsplit[1] == '0'):        
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
    temp.z = alpha * (dest.z - start.z) + start.z
    deltaMoved = distance(curr, temp)
    minPF = 100 
    colFree = True    
    for o in oList:
        dist = math.sqrt((o.currx-temp.x)**2 + (o.curry-temp.y)**2 + (o.currz-temp.z)**2)
        pf = distance(temp, point3D(o.currx, o.curry, o.currz)) - o.r
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
