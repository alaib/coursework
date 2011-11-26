# Import required libraries
import copy
import math
import random
# Import required classes
import markov_classes 
from markov_classes import *

# Start setVariables
def setGlobalVariables(wmin, wmax, goal, g, obstacleList):    
    global oList
    global w_min, w_max
    global gamma
    global pstart, pgoal
    w_min = wmin
    w_max = wmax    
    pgoal = goal
    gamma = g
    oList = obstacleList
# End setVariables

#start sumOfSquareDiff
def isConverged(J, prevJ, threshold):
    maxVal = 0.0
    for i in range(w_min.x, w_max.x+1):
        for j in range(w_min.y, w_max.y+1):            
            diff = math.fabs((J[i][j] - prevJ[i][j]))
            if(maxVal < diff):
                maxVal = diff
    if(maxVal < threshold):
        return 1
    else:
        return 0        
#end sumOfSquareDiff 

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

#start getNearPoint
def getNearPoint(p, d):
    xstep = 0
    ystep = 0
    if(d == 'u'):
        ystep = 1
    elif(d == 'd'):
        ystep = -1
    elif(d == 'r'):
        xstep = 1
    elif(d == 'l'):
        xstep = -1
        
    newPoint = point(p.x+xstep, p.y+ystep)
    if(validPoint(newPoint.x, newPoint.y, w_min.x, w_min.y, w_max.x, w_max.y) and clear(newPoint, oList)):
        return newPoint
    else:
        return p    
#end getNearPoint

#start max3
def max3(a,b,c):
    m = a
    if(b > a and b > c):
        m = b
    elif(c > a and c > b):
        m = c
    return m
#end max3  
      
#start getVal
def getVal(p, J):
    return J[p.x][p.y]
#end getVal      

#start getReward function
def getReward(p, J): 
    upVal    = 0.7 * getVal(getNearPoint(p, 'u'), J) + 0.2 * getVal(getNearPoint(p, 'r'), J)  + 0.1 * getVal(getNearPoint(p, 'l'), J)
    leftVal  = 0.7 * getVal(getNearPoint(p, 'l'), J) + 0.2 * getVal(getNearPoint(p, 'u'), J)  + 0.1 * getVal(getNearPoint(p, 'd'), J)
    rightVal = 0.7 * getVal(getNearPoint(p, 'r'), J) + 0.2 * getVal(getNearPoint(p, 'd'), J)  + 0.1 * getVal(getNearPoint(p, 'u'), J)
    maxVal = max3(upVal, leftVal, rightVal)
    
    val = -1 + gamma * maxVal
    if(maxVal == upVal):
        d = 'up'
    elif(maxVal == leftVal):
        d = 'left'
    elif(maxVal == rightVal):
        d = 'right'
    return {'val':val, 'dir':d}        
#end getReward function