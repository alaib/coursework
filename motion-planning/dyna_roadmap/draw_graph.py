import numpy as np
from matplotlib.path import Path
from matplotlib.patches import PathPatch
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

# Import required classes
import dyna_roadmap_classes 
from dyna_roadmap_classes import *

#start drawBaseGraph
def drawBaseGraph(w_min, w_max, pstart, pend, oList):
    fig = plt.figure(figsize = (12,10))
    ax = fig.add_subplot(111)
    ax.set_xlim(w_min.x, w_max.x)
    ax.set_ylim(w_min.y, w_max.y)
    ax.set_title('State-Time Graph')
    ax.set_xlabel('X - Axis')
    ax.set_ylabel('Y - Axis')
    vertices = []

    codes = []
    for o in oList:
        codes += [Path.MOVETO] + [Path.LINETO]*3 + [Path.CLOSEPOLY]
        vertices += [(o.xmin, o.ymin), (o.xmin, o.ymax), (o.xmax, o.ymax), (o.xmax, o.ymin), (o.xmin, o.ymin)]
    #end for loop

    path = Path(vertices, codes)
    patch = PathPatch(path, facecolor='red')
    ax.add_patch(patch)
    plt.plot(pstart.x, pstart.y, 'g*', markersize = 10)
    plt.plot(pend.x, pend.y, 'r*', markersize = 10)
    return {'figure': fig, 'plot' : plt, 'axes' : ax}
#end of drawBaseGraph

#start updateDraw
def updateDraw(initDraw, curr, dynaOList, step):    
    #initDraw = drawBaseGraph(w_min, w_max, pstart, pend, oList)    
    fig = initDraw['figure']
    plt = initDraw['plot']
    ax = initDraw['axes']
    for o in dynaOList:
        if(step % 3 == 0):
            c = mpatches.Circle((o.currx, o.curry), o.r, fc="w")                
            ax.add_patch(c)
    #end for loop
    plt.plot(curr.x, curr.y, 'g.', markersize = 5)
    return {'figure': fig, 'plot' : plt, 'axes' : ax}
    #filename = 'results/'+str(step)+'.png'
    #fig.savefig(filename)    
#end updateDraw
