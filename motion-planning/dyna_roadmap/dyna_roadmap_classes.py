# Define Point Class
class point:
    def __init__(self, xval, yval):
        self.x = int(xval);
        self.y = int(yval);
    
    def show(self, optional = ''):
        print "%s(%.2f,%.2f)" % (optional, self.x, self.y)
        
    def __eq__(self, other):
        return ((self.x == other.x) and (self.y == other.y))
# End Class Definition        

# Define Static Rectangle Obstacle
class staticObstacle:
    def __init__(self,xmin,ymin,xmax,ymax):
        self.xmin = xmin;
        self.ymin = ymin;
        self.xmax = xmax;
        self.ymax = ymax;
        
    def show(self):
        print "((%d,%d)-(%d,%d))" % (self.xmin, self.ymin, self.xmax, self.ymax)
# End Class Definition

# Define Dynamic Circle Obstacle
class dynaObstacle:
    def __init__(self, x1, y1, x2, y2, deltax, deltay, radius):
        self.xmin = x1
        self.ymin = y2
        self.xmax = x2
        self.ymax = y2
        self.currx = x1
        self.curry = y1
        self.xstep = deltax
        self.ystep = deltay
        self.r = radius        
    
    def move(self):
        if( ((self.currx + self.xstep > self.xmax) and (self.curry + self.ystep > self.ymax)) \
        or  ((self.currx + self.xstep < self.xmin) and (self.curry + self.ystep < self.ymin)) ):
            self.xstep *= -1
            self.ystep *= -1                    
        self.currx += self.xstep
        self.curry += self.ystep
        
    def show(self):
        print "Center = (%.1f,%.1f) R = %.1f" % (self.currx, self.curry, self.r)
# End Class Definition        