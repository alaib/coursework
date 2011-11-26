import djikstra
from djikstra import *

graph = {0:{1:1,3:5},1:{0:1,2:2,3:4},2:{1:2,3:4},3:{0:5,1:4,2:7}}
cost,path = shortestPath(graph, 0, 2)
print path
print cost
