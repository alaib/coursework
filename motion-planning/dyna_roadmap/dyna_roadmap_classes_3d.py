# Define 3DPoint Class
class point3D:
    def __init__(self, xval, yval, zval):
        self.x = xval;
        self.y = yval;
        self.z = zval;
    
    def show(self, optional = ''):
        print "%s(%.2f,%.2f,%.2f)" % (optional, self.x, self.y, self.z)
        
    def __eq__(self, other):
        return ((self.x == other.x) and (self.y == other.y) and (self.z == other.z))
# End Class Definition        

# Define 3DStatic Rectangle Obstacle
class staticObstacle3D:
    def __init__(self, xmin, ymin, zmin, xmax, ymax, zmax):
        self.xmin = xmin;
        self.ymin = ymin;
        self.zmin = zmin;
        self.xmax = xmax;
        self.ymax = ymax;
        self.zmax = zmax;
        
    def show(self):
        print "((%d,%d,%d)-(%d,%d,%d))" % (self.xmin, self.ymin, self.zmin, self.xmax, self.ymax, self.zmax)
# End Class Definition

# Define 3D Dynamic Spherical Obstacle
class dynaObstacle3D:
    def __init__(self, x1, y1, z1, x2, y2, z2, deltax, deltay, deltaz, radius):
        self.xmin = x1
        self.ymin = y1
        self.zmin = z1
        self.xmax = x2
        self.ymax = y2
        self.zmax = z2
        self.currx = x1
        self.curry = y1
        self.currz = z1
        self.xstep = deltax
        self.ystep = deltay
        self.zstep = deltaz
        self.r = radius        
    
    def move(self):
        if( ((self.currx + self.xstep > self.xmax) or (self.curry + self.ystep > self.ymax) or (self.currz + self.zstep > self.zmax)) \
        or  ((self.currx + self.xstep < self.xmin) or (self.curry + self.ystep < self.ymin) or (self.currz + self.zstep < self.zmin)) ):
            self.xstep *= -1
            self.ystep *= -1                    
            self.zstep *= -1                    
        self.currx += self.xstep
        self.curry += self.ystep
        self.currz += self.zstep
        
    def show(self):
        print "Center = (%.1f,%.1f,%.1f) R = %.1f" % (self.currx, self.curry, self.currz, self.r)
# End Class Definition        
