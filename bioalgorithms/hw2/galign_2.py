#!/usr/bin/python
# -*- coding: utf-8 -*-
import pprint
import sys

def LCS(v, w, sigma, mu, match_premium): 
    test = 1
    test = test + 1
    s = [[0 for col in range(len(v)+1)] for row in range(len(w)+1)]
    b = [[0 for col in range(len(v)+1)] for row in range(len(w)+1)]
    for i in xrange(1,len(v)+1): 
        for j in xrange(1,len(w)+1): 
            if (v[i-1] == w[j-1]): 
                s[i][j] = max(s[i-1][j]-sigma, s[i][j-1]-sigma, s[i-1][j-1]+match_premium) 
            else: 
                s[i][j] = max(s[i-1][j]-sigma, s[i][j-1]-sigma, s[i-1][j-1]-mu) 
                
            if (s[i][j] == s[i][j-1]-sigma): 
                #horizontal right
                b[i][j] = 1 
            elif (s[i][j] == s[i-1][j]-sigma): 
                #vertical down
                b[i][j] = 2 
            elif (s[i][j] == s[i-1][j-1]+match_premium): 
                #diagonal match
                b[i][j] = 3 
            else:
                #diagonal mismatch
                b[i][j] = 4
    return s, b
#end
def PrintLCS(b,v,i,j): 
    if ((i == 0) or (j == 0)): 
        return 
    if (b[i][j] == 3): 
        PrintLCS(b,v,i-1,j-1) 
        print v[i-1], 
    elif(b[i][j] == 4):
        PrintLCS(b,v,i-1,j-1) 
    elif (b[i][j] == 2): 
        PrintLCS(b,v,i-1,j) 
    else: 
        PrintLCS(b,v,i,j-1) 
#end            

def PrintTable(v, w, s):
    print "=" * (4 * len(w))
    print "Score Table"
    print "=" * (4 * len(w))
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
    print "=" * (4 * len(w))
#end
def PrintSignTable(v, w, s):
    print "=" * (4 * len(w))
    print "Sign Table"
    print "=" * (4 * len(w))
    sys.stdout.write('    w ')
    for i in range(0, len(w)):
        sys.stdout.write(' '+w[i]+' ')
    print    
    for i in range(0,len(v)+1): 
        if(i != 0):
            sys.stdout.write(' '+v[i-1]+ ' ')
        else:
            sys.stdout.write(' v ')
        for j in range(0,len(w)+1): 
            if(s[i][j] < 0):
                c = str(s[i][j])
            else:
                c = '+'+str(s[i][j])
            sys.stdout.write(c+' ')
        #end for j
        print
    #end for i
    print "=" * (4 * len(w))
#end

def PrintArrowTable(v, w, s):
    print "=" * (4 * len(w))
    print "Arrow Table"
    print "=" * (4 * len(w))
    sys.stdout.write('  w  ')
    for i in range(0, len(w)):
        sys.stdout.write(w[i]+'  ')
    print    
    for i in range(0,len(v)+1): 
        if(i != 0):
            sys.stdout.write(v[i-1]+ ' ')
        else:
            sys.stdout.write('v ')
        for j in range(0,len(w)+1): 
            c = ''
            if(s[i][j] == 1):
                c = '→ '
            elif(s[i][j] == 2):
                c = '↓ '
            elif(s[i][j] == 3):
                c = '↘ '
            elif(s[i][j] == 4):
                c = '↘.'
            else:
                c = '0 '
            sys.stdout.write(c+' ')
        #end for j
        print
    #end for i
    print "=" * (4 * len(w))
#end
v = 'TACGGGTAT'
w = 'GGACGTACG'
#indel penalty (don't pass negative penalty)
sigma = 1
#mismatch penalty (don't pass negative penalty)
mu = 1
#match score
match_premium = 1

s, b = LCS(v, w, sigma, mu, match_premium)
PrintSignTable(v, w, s)
PrintArrowTable(v, w, b)
PrintTable(v, w, b)
print "========================"
print "LCS = ", PrintLCS(b, v, len(v), len(v))
print "========================"