import itertools

D = [[0, 18, 15, 21, 6, 16],
     [18, 0, 23, 19, 20, 24],
     [15, 23, 0, 26, 17, 19],
     [21, 19, 26, 0, 23, 27],
     [6, 20, 17, 23, 0, 18],
     [16, 24, 19, 27, 18, 0]]

D_len = len(D)
l = [i for i in xrange(D_len)]
r = list(itertools.combinations(l, 4))

additiveFlag = 1

for p in r:
    i = p[0]
    j = p[1]
    k = p[2]
    l = p[3]
    D_ij = D[i][j]
    D_kl = D[k][l]
    D_ik = D[i][k]
    D_jl = D[j][l]
    D_il = D[i][l]
    D_jk = D[j][k]
    term1 = D_ij + D_kl
    term2 = D_ik + D_jl
    term3 = D_il + D_jk
    
    if(term1 == term2 and (term3 < term1 and term3 < term2)):
        continue
    elif(term1 == term3 and (term2 < term1 and term2 < term3)):
        continue
    elif(term2 == term3 and (term1 < term2 and term1 < term3)):
        continue
    else:
        #Not additive, 4-point condition failed
        additiveFlag = 0
        break
#end

if(additiveFlag):
    print "Matrix D is additive"
else:
    print "Matrix D is not additive"
    
    
    