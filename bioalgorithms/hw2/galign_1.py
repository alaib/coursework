#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys

def LCS(v, w): 
    test = 1
    test = test + 1
    s = [[0 for col in range(len(v)+1)] for row in range(len(w)+1)]
    b = [[0 for col in range(len(v)+1)] for row in range(len(w)+1)]
    for i in xrange(1,len(v)+1): 
        for j in xrange(1,len(w)+1): 
            if (v[i-1] == w[j-1]): 
                s[i][j] = max(s[i-1][j], s[i][j-1], s[i-1][j-1] + 1) 
            else: 
                s[i][j] = max(s[i-1][j], s[i][j-1]) 
            if (s[i][j] == s[i][j-1]): 
                #horizontal right
                b[i][j] = 1 
            elif (s[i][j] == s[i-1][j]): 
                #vertical down
                b[i][j] = 2 
            else: 
                #diagonal match
                b[i][j] = 3 
    return s, b
#end

def PrintLCS(b,v,i,j): 
    if ((i == 0) or (j == 0)): 
        return 
    if (b[i][j] == 3): 
        PrintLCS(b,v,i-1,j-1) 
        print v[i-1], 
    else: 
        if (b[i][j] == 2): 
            PrintLCS(b,v,i-1,j) 
        else: 
            PrintLCS(b,v,i,j-1) 
#end            

def PrintTable(v, w, s):
    sys.stdout.write('  w ')
    for i in range(0, len(w)):
        sys.stdout.write(w[i]+' ')
    print    
    for i in range(0,len(v)+1): 
        if(i != 0):
            sys.stdout.write(v[i-1]+ ' ')
        else:
            sys.stdout.write('v ')
        for j in range(0,len(w)+1): 
            sys.stdout.write(str(s[i][j])+' ')
        #end for j
        print
    #end for i
#end

def PrintArrowTable(v, w, s):
    sys.stdout.write('  w ')
    for i in range(0, len(w)):
        sys.stdout.write(w[i]+' ')
    print    
    for i in range(0,len(v)+1): 
        if(i != 0):
            sys.stdout.write(v[i-1]+ ' ')
        else:
            sys.stdout.write('v ')
        for j in range(0,len(w)+1): 
            c = ''
            if(s[i][j] == 1):
                c = '→'
            elif(s[i][j] == 2):
                c = '↓'
            elif(s[i][j] == 3):
                c = '↘'
            else:
                c = '0'
            sys.stdout.write(c+' ')
        #end for j
        print
    #end for i
#end
v = 'AGGACT'
w = 'ATGACT'

s, b = LCS(v, w)
print "==================="
PrintTable(v, w, s)
print "==================="
PrintTable(v, w, b)
print "===================="
PrintArrowTable(v, w, b)
print "===================="
print "LCS = ", PrintLCS(b,v,len(v), len(v))