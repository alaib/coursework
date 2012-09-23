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
            while(a[i] > val and j <= m):
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

a = [1, 2, 3]
#ab = [1, 1, 2, 2]
ab = [1, 2, 1, 2]

[c, siteList] = compatible(a, ab)

if(c):
    print "Compatible"
else:
    print "Not Compatible"
print "a = ", a
print "ab = ", ab
print "sites = ", siteList
