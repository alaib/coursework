import pprint;

#start DPChange
def DPChange(M, c): 
    change = [[0 for i in xrange(len(c))]] 
    for m in xrange(1, M + 1): 
        bestNumCoins = m + 1 
        for i in xrange(len(c)): 
            if(m >= c[i]): 
                thisChange = [x for x in change[m - c[i]]] 
                thisChange[i] += 1 
                if (sum(thisChange) < bestNumCoins): 
                    change[m:m] = [thisChange] 
                    bestNumCoins = sum(thisChange) 
    return change[M], sum(change[M]) 
#end

def DPChangeAll(M, c): 
    change = [[0 for i in xrange(len(c))]] 
    for m in xrange(1, M + 1): 
        bestNumCoins = m + 1 
        for i in xrange(len(c)): 
            if(m >= c[i]): 
                thisChange = [x for x in change[m - c[i]]] 
                thisChange[i] += 1 
                if (sum(thisChange) < bestNumCoins): 
                    change[m:m] = [thisChange] 
                    bestNumCoins = sum(thisChange) 
    s = []
    for l in change:
        s.append(sum(l))
    return change, s
#end

def BetterChange(M, c):
    r = M
    n = 0
    i = [0] * len(c) 
    
    for k in xrange(len(c)):
        i[k] = r / c[k]
        n = n + i[k]
        r = r - i[k] * c[k]
        
    return i, n
#end    

def computeApproxRatio(num, c):
    aRatio = [0]*num
    dpList, dpCoins = DPChangeAll(num, c)
    for i in range(num):
        bcList, bcCoins = BetterChange(i, c)
        if(dpCoins[i] != 0):
            aRatio[i] = float(bcCoins) / float(dpCoins[i])
        else:
            aRatio[i] = 0
        
    max_value = max(aRatio)
    max_index = 0
    for i, v in enumerate(aRatio):
        if(max_value == v):
            max_index = i
            break
    print "Max Approx Ratio = %.2f for %d" % (max_value, max_index)
    for i in xrange(len(aRatio)):
        print "%d = %f" % (i, aRatio[i])
    
#end

def singleRatio(M, c):
    dpList, dpCoins = DPChange(M, c)
    bcList, bcCoins = BetterChange(M, c)
    #BetterChange
    print "Better Change"
    print c
    print bcList
    print "BcCoins = ", bcCoins
    print "===================="
    #DPChange
    print "DP Change"
    print c
    print dpList
    print "DPCoins = ", dpCoins
#end    

c = [25, 20, 10, 5, 1]
s = 0.0;
l = [0] * len(c)
M = 100 
for i in range(len(c)):
    l[i] += float(M)/c[i] 
    s += 1/float(c[i])

print l 
print s/(1/float(max(c)))
val = 1 / float(max(c))
print val
t = s / val
print t

#singleRatio(M, c)
#computeApproxRatio(100, c)
