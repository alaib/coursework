# Import required libraries
import random
# Import required classes
import prm_classes 
from prm_classes import *
# Import required functions
import prm_functions
from prm_functions import *
import djikstra
from djikstra import *

choice = int(raw_input('1 : Test Case 1 (predefined data)\n2 : Test Case 2 (predefined data)\n3 : Custom Test Case\nChoose an option = '))
if choice < 1 or choice > 3:
	print 'Choice out of range, default choice -> 1 : Test Case 1 (predefined data)'
	choice = 1
if choice == 3:
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
		start_x = int(raw_input('Enter x-coordinate of start point = '))
		start_y = int(raw_input('Enter y-coordinate of start point = '))
		end_x = int(raw_input('Enter x-coordinate of end point = '))
		end_y = int(raw_input('Enter y-coordinate of end point = '))
		if not(validPoint(start_x,start_y,w_xmin,w_ymin,w_xmax,w_ymax) \
			   and validPoint(end_x,end_y,w_xmin,w_ymin,w_xmax,w_ymax)):
			print 'Either start/end point is outside the workspace (%d,%d,%d,%d), please enter correct values' % (w_xmin,w_ymin,w_xmax,w_ymax)
			continue		
		pstart = point(start_x,start_y)
		pend = point(end_x,end_y)
		if not(clear(pstart, obstacleList) and clear(pend, obstacleList)):
			print 'Either start/end point is in collision, please choose different set of start and end points'
			continue					
		flag = False;
elif choice == 1 or choice == 2:					
	# Initalize workspace
	w_xmin = 0
	w_ymin = 0
	w_xmax = 22
	w_ymax = 22
	
	w_min = point(w_xmin,w_ymin)
	w_max = point(w_xmax,w_ymax)
	
	# No of Obstacles
	nObstacles = 4
	obstacleList = []
	if choice == 1:
		obstacleList.append(obstacle(7,0,12,8))
		obstacleList.append(obstacle(8,14,13,22))
	else:
		obstacleList.append(obstacle(7,0,12,10))
		obstacleList.append(obstacle(8,12,13,22))
				
	obstacleList.append(obstacle(0,14,5,19))
	obstacleList.append(obstacle(17,6,22,13))
	
	pstart = point(2,2)
	pend = point(14,21)
# end choice

nSample = int(raw_input('Enter the no. of milestones = '))
noOfNeighbors = int(raw_input('Enter the number of nearest neighbors to find for each milestone = '))

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
		if checkIfPointNotAdded(p, samplePoints) and clear(p,obstacleList):
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
			if(p1.x == p2.x and p1.y == p2.y):
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
			if link(p1,p2,obstacleList):
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
if tries > maxTries:
	print 'Unable to find %d milestones with %d neighbors after %d tries, Quitting' % (nSample, noOfNeighbors, maxTries)
	print 'Total Cost = Infinity'	
elif noSampleFound:
	print 'No sample points set found where each point passes clear function , Max Sample Tries = %d' % (maxSampleTries)
	print 'Total Cost = Infinity'	
else:
	# Find the shortest path
	# Start Vertex = 0, End Vertex = 1
	print '%d milestones with %d neighbors found after %d sampling tries' % (nSample, noOfNeighbors, tries)
	cost,path = shortestPath(graph, 0, 1)
	if cost < 0:
		print 'Path may or may not exist between (%d,%d) to (%d,%d)' % (pstart.x,pstart.y,pend.x, pend.y)
		print 'Total Cost = Infinity'
	else:
		print 'Path exists from (%d,%d) to (%d,%d)' % (pstart.x,pstart.y,pend.x, pend.y)
		l = len(path)
		for i in range(l):
			if i != l-1:
				print '(%d,%d) -> ' % (samplePoints[path[i]].x,samplePoints[path[i]].y),
			else:
				print '(%d,%d)' % (samplePoints[path[i]].x,samplePoints[path[i]].y),		
		print '\nTotal Cost = %f' % (cost)					

