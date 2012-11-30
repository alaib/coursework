## Sankoff's algorithm for parsimony-based phylogenetic modeling. Uses
## parameters tuned for my particular homework, so it's not a general
## solution.  -Peter

# A substitution cost matrix

cost = [[0, 1, 1, 1],
        [1, 0, 1, 1],
        [1, 1, 0, 1],
        [1, 1, 1, 0]]
"""
cost = [[0, 3, 4, 9],
        [3, 0, 2, 4],
        [4, 2, 0, 4],
        [9, 4, 4, 0]]
"""
"""
cost = [[0, 2, 3, 8],
        [2, 0, 1, 3],
        [3, 1, 0, 3],
        [8, 3, 3, 0]]
"""


inf = float('Inf')                      # Infinity

# Individual nucleotides
init = {}
init['a'] = [0, inf, inf, inf]
init['t'] = [inf, 0, inf, inf]
init['g'] = [inf, inf, 0, inf]
init['c'] = [inf, inf, inf, 0]

def join(node1, node2):
    """Join two nodes together using the global cost matrix."""
    this = [0, 0, 0, 0]
    
    for i in range(0, 4):
        # Find min-cost change from node on the left
        min_left = inf
        for j in range(0, 4):
            this_cost = cost[i][j] + node1[j]
            min_left = min(min_left, this_cost)
        # Find min-cost change from node on the right
        min_right = inf
        for k in range(0, 4):
            this_cost = cost[i][k] + node2[k]
            min_right = min(min_right, this_cost)
        this[i] = min_left + min_right
        
    return this

def printRes(n1, s):
    print s, '=> arr = ', n1, ', minValue = ', min(n1), ', val = ', arr[n1.index(min(n1))]
#end

# Do an example problem
arr = ['a', 't', 'g', 'c']
leaves = 'aaag'

"""
n1 = join(init[leaves[0]], init[leaves[1]])
n2 = join(n1, init[leaves[2]])
n3 = join(n2, init[leaves[3]])

printRes(n3, 'Root')
printRes(n2, 'Left1')
printRes(n1, 'Left2')
"""

"""
n1 = join(init[leaves[0]], init[leaves[1]])
n2 = join(init[leaves[2]], init[leaves[3]])
n3 = join(n1, n2)

printRes(n3, 'Root')
printRes(n1, 'Left')
printRes(n2, 'Right')
"""

lStr = ["gttg", "gtca", "acca", "atta"]
ps = 0
for i in xrange(4):
    leaves = lStr[0][i] + lStr[1][i] + lStr[2][i] + lStr[3][i] 
    print leaves
    n1 = join(init[leaves[0]], init[leaves[1]])
    n2 = join(n1, init[leaves[2]])
    n3 = join(n2, init[leaves[3]])
    """
    n1 = join(init[leaves[0]], init[leaves[1]])
    n2 = join(init[leaves[2]], init[leaves[3]])
    n3 = join(n1, n2)
    """
    ps += min(n3)
    printRes(n3, 'Root')
    
print "Parsimony score = %d" % (ps)