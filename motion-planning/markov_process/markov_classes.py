# Define Point Class
# Begin Class Definition
class point:
    def __init__(self, xval, yval):
        self.x = int(xval);
        self.y = int(yval);
    
    def show(self):
        print "(%d,%d)\n" % (self.x, self.y)
# End Class Definition        

# Define Rectangle Obstacle
class obstacle:
    def __init__(self,xmin,ymin,xmax,ymax):
        self.xmin = xmin;
        self.ymin = ymin;
        self.xmax = xmax;
        self.ymax = ymax;
        
    def show(self):
        print "((%d,%d)-(%d,%d))" % (self.xmin, self.ymin, self.xmax, self.ymax)
# End Class Definition        