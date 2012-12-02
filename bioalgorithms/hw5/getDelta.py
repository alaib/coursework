import sys
import itertools
import math

def display(result, S1, S2):
    print "=================================================="
    first= 1
    for elem in S2:
        if(first):
            sys.stdout.write("\t")
            first = 0
        sys.stdout.write(str(elem)+"\t")
    sys.stdout.write("\n")
    
    for i in xrange(len(result)):
        sys.stdout.write(str(S1[i])+"\t")
        for j in xrange(len(result[i])):
            elem = result[i][j]
            if(i <= j):
                sys.stdout.write(str(int(elem))+"\t")
            else:
                sys.stdout.write("-\t")
        sys.stdout.write("\n")
    print "=================================================="

def nonZeroMin(D):
    minimum = 9999
    for l in D:
        for elem in l:
            if(elem != 0 and minimum > elem):
                minimum = elem
    return minimum
#end

D = [[0, 18, 15, 21, 6, 16],
     [18, 0, 23, 19, 20, 24],
     [15, 23, 0, 26, 17, 19],
     [21, 19, 26, 0, 23, 27],
     [6, 20, 17, 23, 0, 18],
     [16, 24, 19, 27, 18, 0]]

#delta strategy = floor(min(D)/2) to 1 in steps of 1
delta = 1
alphabet = ['A', 'B', 'C', 'D', 'E', 'F']
count = 0
foundCount = 0
iterCount = 0
display(D, alphabet, alphabet)
while len(D) > 2 :
    count += 1
    print "Iteration = %d, Delta = %d, Subtracted Value = %d" % (count, delta,  2* delta)
    D_len = len(D)
    for i in xrange(D_len):
        for j in xrange(len(D[i])):
            if(D[i][j] > 0 and (D[i][j] - 2 * delta) >= 0):
                D[i][j] = D[i][j] - 2 * delta
        #end inner
    #end outer
    display(D, alphabet, alphabet)
    
    l = [i for i in xrange(D_len)]
    r = list(itertools.combinations(l, 3))
    
    found = 0
    for comb in r:
        for p in list(itertools.permutations(comb)):
            i = p[0]
            j = p[1]
            k = p[2]
            
            D_ij = D[i][j]
            D_jk = D[j][k]
            D_ik = D[i][k]
            
            if(D_ij + D_jk == D_ik):
                print "i = %d (%s), j = %d (%s), k = %d (%s)" % \
                                    (i, alphabet[i], j, alphabet[j], k, alphabet[k])
                print "D_ij = %d, D_jk = %d, D_ik = %d" % (D_ij, D_jk, D_ik)
                print "count = %d, delta = %d, tDelta = %d" % (count, delta, 2*count*delta)
                found = 1
                foundCount += 1
                break
            
        if(found):
            break
    #end
    
    if(found):
        #Delete jth column in each row
        for i in xrange(D_len):
            del D[i][j]
                
        #Delete jth row
        del D[j]
        del alphabet[j]
        display(D, alphabet, alphabet)
        
    if(foundCount == 2):
        break
    iterCount += 1
    
    if(iterCount > 1000):
        print "No Solution Found"
        break
 #end   