#!/usr/bin/python

#start compatible
def compatible2(a, b, ab):
    n = len(a)
    m = len(ab)
    sites1 = [];
    i = 0
    j = 0
    sitesVal = 0
    aComp = 1

    while(i < n and j < m):
        val = ab[j]
        if(a[i] == val):
            sitesVal += val
            sites1.append(sitesVal)
            i += 1
            j += 1
        elif(a[i] > val):
            while(a[i] > val and j < m):
                j += 1
                val += ab[j]
            #end while

            if(a[i] != val):
                #Failure case
                aComp = 0
                break
            else:
                sitesVal += val
                sites1.append(sitesVal)
                i+= 1
                j += 1
        elif(a[i] < val):
            #Failure Case
            aComp = 0
            break
    #end while

    sites2 = [];
    i = 0
    j = 0
    sitesVal = 0
    l = len(b)
    bComp = 1

    while(i < l and j < m):
        val = ab[j]
        if(b[i] == val):
            sitesVal += val
            sites2.append(sitesVal)
            i += 1
            j += 1
        elif(b[i] > val):
            while(b[i] > val and j < m):
                j += 1
                val += ab[j]
            #end while

            if(b[i] != val):
                #Failure case
                bComp = 0
                break
            else:
                sitesVal += val
                sites2.append(sitesVal)
                i+= 1
                j += 1
        elif(b[i] < val):
            #Failure Case
            bComp = 0
            break
    #end while
    
    #Check if cuts are made at right places
    cutSites = {}
    val = 0
    for i in range(m):
        val += ab[i]
        cutSites[val] = 0
    for i in range(len(sites1)):
        cutSites[sites1[i]] = 1
    for i in range(len(sites2)):
        cutSites[sites2[i]] = 1
        
    fullComp = 1
    if(0 in cutSites.values()):
        fullComp = 0


    return aComp, bComp, fullComp, sites1, sites2
#end compatible

a = [3, 2, 1]
#a = [2, 3, 1]
b = [2, 4]
#ab = [1, 1, 2, 2]
#ab = [1, 2, 1, 2]
ab = [2, 1, 2, 1]

[aComp, bComp, fullComp, sites1, sites2] = compatible2(a, b, ab)

if(aComp):
    print "Compatible with A"
else:
    print "Not Compatible with A"
print "a = ", a
print "ab = ", ab
print "sites = ", sites1

if(bComp):
    print "Compatible with B"
else:
    print "Not Compatible with B"
print "b = ", b
print "ab = ", ab
print "sites = ", sites2

print "Full Comp = ", fullComp
