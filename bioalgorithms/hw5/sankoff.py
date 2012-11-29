## Sankoff's algorithm for parsimony-based phylogenetic modeling. Uses
## parameters tuned for my particular homework, so it's not a general
## solution.  -Peter

# A substitution cost matrix
"""
cost = [[0, 3, 4, 9],
        [3, 0, 2, 4],
        [4, 2, 0, 4],
        [9, 4, 4, 0]]
"""
cost = [[0, 2, 3, 8],
        [2, 0, 1, 3],
        [3, 1, 0, 3],
        [8, 3, 3, 0]]


inf = float('Inf')                      # Infinity

# Individual nucleotides
a = [0, inf, inf, inf]
t = [inf, 0, inf, inf]
g = [inf, inf, 0, inf]
c = [inf, inf, inf, 0]

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

# Do an example problem
n1 = join(g, t)
n2 = join(c, a)
n3 = join(n1, n2)
print n1
print n2
print n3
"""
n1 = join(a, c)
n2 = join(t, g)
n3 = join(n1, n2)
print n1
print n2
print n3
"""