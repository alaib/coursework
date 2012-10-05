#!/usr/bin/python

T = "AGCTCTAGCGCTAGCGATAGCTAGCTAGCTCGATCGAGA"
s = "AGCGAG"
n = T.__len__()
m = s.__len__()
found = 0
k = 1

for i in range(n-m+1):
    j = 0
    dist = 0
    flag = 1

    while(j <= m-1):
        j = j + 1
        if(T[i+j] != s[j]):
            if(dist > k):
                flag = 0
                break
            else:
                dist = dist + 1
        #end if
    #end while

    if(flag == 1):
        print "Match found with hamming distance <= %d" % (k)
        sPrime = T[i:i+s.__len__()]
        print "sPrime = %s, s = %s" % (sPrime, s)
        found = 1
#end for

if(found == 0):
    print "No Solution found"

