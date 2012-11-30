import sys

#'1' = Head, '0' => Tail
seq = '011011'

#Transition Proability
T = {}
T['F'] = {'F':  0.9, 'B' : 0.1}
T['B'] = {'F':  0.1, 'B' : 0.9}

E = {}
E['F'] = {'1' : 0.5, '0' : 0.5}
E['B'] = {'1' : 0.75, '0' : 0.25}

start = {'F': 0.5, 'B': 0.5}

mat = []
for i in xrange(len(seq)):
    mat.append([])
    for j in xrange(len(T)):
        mat[i].append(0)
        
#Initialize first index, 0 -> Fair Coin, 1 -> Biased coin
mat[0][0] = round(start['F'] * E['F'][seq[0]], 4)
mat[0][1] = round(start['B'] * E['B'][seq[0]], 4)

for i in xrange(1, len(seq)):
    #Fair coin
    mat[i][0] = round(max(mat[i-1][0] * T['F']['F'] * E['F'][seq[i]], \
                          mat[i-1][1] * T['B']['F'] * E['F'][seq[i]]), 4)
    
    #Biased coin
    mat[i][1] = round(max(mat[i-1][1] * T['B']['B'] * E['B'][seq[i]], \
                          mat[i-1][0] * T['F']['B'] * E['B'][seq[i]]), 4)

print mat
hidden = ''
for li in mat:
    index = li.index(max(li)) 
    if(index == 1):
        hidden += 'B'
    else:
        hidden += 'F'
        
print 'Hidden Sequence = ', hidden