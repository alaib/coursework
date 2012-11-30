import sys
from math import log, exp

def dispTransitionMatrix(mat, states):
    print "========================================================="
    for j in xrange(3):
        for i in xrange(len(mat)):
            if(i == 0):
                sys.stdout.write(states[j] + '\t')
                
            if(mat[i][j] < -2000000):
                sys.stdout.write('-inf' + '\t')
            else:
                sys.stdout.write(str(mat[i][j]) + '\t')
        print
    print "========================================================="
#end

def logsum(*args):
    lSum = 0.0
    #Check if any of the args has 0, if yes, return -intMax
    for a in args:
        if(a == 0):
            return -1 * sys.maxint
        
    #compute logsum    
    for a in args:
        lSum += log(a)
    return lSum

seq = '112122'
states = ['D1', 'D2', 'E']

#Transition Proability
T = {}
T['D1'] = {'D1':  0.50, 'D2' : 0.25, 'E': 0.25}
T['D2'] = {'D1':  0.25, 'D2' : 0.50, 'E': 0.25}
T['E']  = {'D1' :  0.00, 'D2' : 0.00, 'E': 1.00}

E = {}
E['D1'] = {'1' : 0.50, '2' : 0.25, '3': 0.25}
E['D2'] = {'1' : 0.25, '2' : 0.50, '3': 0.25}
E['E']  = {'1' : 0.00, '2' : 0.00, '3': 0.00}

start = {'D1': 0.5, 'D2': 0.5, 'E': 0.0}

mat = []
for i in xrange(len(seq)):
    mat.append([])
    for j in xrange(len(T)):
        mat[i].append(0)
        
#Set precision
prec = 4

#Initialize first index, 0 -> D1, 1 -> D2, 2 -> E
mat[0][0] = round(logsum(start['D1'], E['D1'][seq[0]]), prec)
mat[0][1] = round(logsum(start['D2'], E['D2'][seq[0]]), prec)
mat[0][2] = round(logsum(start['E'] , E['E'][seq[0]]) , prec)

for i in xrange(1, len(seq)):
    #D1
    mat[i][0] = round(max(mat[i-1][0] + logsum(T['D1']['D1'], E['D1'][seq[i]]), \
                          mat[i-1][1] + logsum(T['D2']['D1'], E['D1'][seq[i]]), \
                          mat[i-1][2] + logsum(T['E']['D1'], E['D1'][seq[i]])), prec)
    
    #D2
    mat[i][1] = round(max(mat[i-1][0] + logsum(T['D1']['D2'], E['D2'][seq[i]]), \
                          mat[i-1][1] + logsum(T['D2']['D2'], E['D2'][seq[i]]), \
                          mat[i-1][2] + logsum(T['E']['D2'], E['D2'][seq[i]])), prec)
    
    #E
    mat[i][2] = round(max(mat[i-1][0] + logsum(T['D1']['E'], E['E'][seq[i]]), \
                          mat[i-1][1] + logsum(T['D2']['E'], E['E'][seq[i]]), \
                          mat[i-1][2] + logsum(T['E']['E'], E['E'][seq[i]])), prec)
    
hidden = ''
for li in mat:
    index = li.index(max(li)) 
    hidden += states[index] + ' '
        
print "HMM Matrix"
dispTransitionMatrix(mat, states)
print "Sequence = ", seq
print 'Hidden Sequence = ', hidden
logProb = max(mat[len(mat)-1])
print 'Log Prob(Sequence) = %.4f, Probability = %.10f' % (logProb, exp(logProb))