from visual import *
import math

#start drawBaseGraph
def drawBaseGraph(w_min, w_max, pstart, pend, oList, dynaOList):
    xc = int((w_max.x - w_min.x) /2)
    yc = int((w_max.y - w_min.y) /2)
    zc = int((w_max.z - w_min.z) /2)
    scene = display(title='State Time Graph', x=0, y=0, width=800, height=800, \
                    center=(xc,yc,zc), autoscale = True, userzoom = True, userspin = True);#background = (1,1,1))  
    for o in oList:
        x = o.xmin + round(float(o.xmax - o.xmin)/2, 1)
        y = o.ymin + round(float(o.ymax - o.ymin)/2, 1)
        z = o.zmin + round(float(o.zmax - o.zmin)/2, 1)        
        l = int(o.xmax - o.xmin)
        h = int(o.ymax - o.ymin)
        w  = int(o.zmax - o.zmin)               
        box(pos=(x,y,z), length=l, height=h, width=w, color = color.red, opacity = 0.5)
    points(pos=(pstart.x, pstart.y, pstart.z), size=10, color=(0,1,1))
    points(pos=(pend.x, pend.y, pend.z), size=10, color=(1,0,1))    
    #end for loop
#end drawBaseGraph
    
#start updateDrawAndScene
def updateDraw(dynaOList, curr, count):  
    if(count % 3 == 0):
        points(pos=(curr.x, curr.y, curr.z), size=7, color=color.green)    
    for o in dynaOList:            
        if(count % 5 == 0):
            s = sphere(pos=(o.currx, o.curry, o.currz), radius = o.r,color = color.blue, opacity = 0.3)             
#end updateDrawAndScene
    
#start updateDraw
def updateDrawAndScene(w_min, w_max, pstart, pend, staticOList, dynaOList, curr, count):  
    scene = drawBaseGraph(w_min, w_max, pstart, pend, staticOList, dynaOList);
    points(pos=(curr.x, curr.y, curr.z), size=7, color=color.green)    
    for o in dynaOList:            
        s = sphere(pos=(o.currx, o.curry, o.currz), radius = o.r,color = color.blue, opacity = 0.3)             
#end updateDraw
