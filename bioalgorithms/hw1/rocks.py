#!/usr/bin/python
import re
import sys

def computeAns():
    while 1:
        line = raw_input("Enter your rock number (W, B) = ");
        if re.match('^0,0', line):
            break
        splitArr = line.split(',')
        w = int(splitArr[0])
        b = int(splitArr[1])
        #Case 1 (Remove 2 Whites, return 1 white)
        w1,b1 = w-1, b
        #Case 2 (Remove 1 White and 1 Black, return 1 black)
        w2,b2 = w-1, b
        #Case 3 (Remove 2 Blacks, return 1 white)
        w3,b3 = w+1, b-2

        if(w1 < 0):
            print "WW = "+str(w1)+","+str(b1)+" --> not possible, not enough Whites"
        else:
            if(w1 == 0 and b1 == 1):
                print "WW = "+str(w1)+","+str(b1)+" --> win"
            else:
                print "WW = "+str(w1)+","+str(b1)

        if(w2<0):
            print "WB = "+str(w2)+","+str(b2)+" --> not possible, not enough Whites"
        else:
            if(w2 == 0 and b2 == 1):
                print "WB = "+str(w1)+","+str(b1)+" --> win"
            else:
                print "WB = "+str(w2)+","+str(b2)
        
        if(b3<0):
            print "BB = "+str(w3)+","+str(b3)+" --> not possible, not enough Blacks"
        else:
            print "BB = "+str(w3)+","+str(b3)

    print "End"
#end    
    
def checkCall(w, b, a):
    if(w < 0 or b < 0):
        #bad choice
        return

    if(w == 0 and b == 1):
        print "===Solution start==="
        for index in range(len(a)):
            print "index = ", index, " value = "+a[index]
        return
        print "===Solution end==="

    a.append(str(w)+','+str(b));

    checkCall(w-1, b, a)
    checkCall(w+1, b-2, a)
#end

def recursiveAns():
    while 1:
        line = raw_input("Enter your rock number (W, B) = ");
        if re.match('^0,0', line):
            break
        splitArr = line.split(',')
        w = int(splitArr[0])
        b = int(splitArr[1])
        a = [];
        checkCall(w,b, a)
#end

recursiveAns()
#computeAns()
