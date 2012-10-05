#!/usr/bin/python

#start compatible
def compatible(a, ab):
    n = len(a)
    m = len(ab)
    sites = [];
    i = 0
    j = 0
    sitesVal = 0
    while(i < n and j < m):
        val = ab[j]
        if(a[i] == val):
            sitesVal += val
            sites.append(sitesVal)
            i += 1
            j += 1
        elif(a[i] > val):
            while(a[i] > val and j < m):
                j += 1
                val += ab[j]
            #end while

            if(a[i] != val):
                return 0, sites
            else:
                sitesVal += val
                sites.append(sitesVal)
                i+= 1
                j += 1
        elif(a[i] < val):
            return 0, sites
    #end while

    return 1, sites
#end compatible

def compatibleBranchAndBound(a, b, ab):
    aComp, sites1 = compatible(a, ab)
    if(aComp == 0):
        return 0

    bComp, sites2 = compatible(b, ab)
    if(bComp == 0):
        return 0
    
    #Check if cuts are made at right places
    cutSites = {}
    val = 0
    for i in range(len(ab)):
        val += ab[i]
        cutSites[val] = 0
    for i in range(len(sites1)):
        cutSites[sites1[i]] = 1
    for i in range(len(sites2)):
        cutSites[sites2[i]] = 1
        
    fullComp = 1
    if(0 in cutSites.values()):
        fullComp = 0

    return fullComp
#end

#a = [3, 2, 1]
a = [1, 3, 2]
a = [2, 3, 1]
b = [2, 4]
#ab = [1, 1, 2, 2]
#ab = [1, 2, 1, 2]
ab = [2, 1, 2, 1]

comp = compatibleBranchAndBound(a, b, ab)

if(comp):
    print "Compatible"
else:
    print "Not Compatible"
print "a = ", a
print "b = ", b
print "ab = ", ab
