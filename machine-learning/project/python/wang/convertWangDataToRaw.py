#!/usr/bin/python

import pprint
import glob
import sys
import re

path = '/home/ravikirn/mlcode/data/wang-paper-data/raw/'
for fName in glob.glob('/home/ravikirn/mlcode/data/wang-paper-data/original/HLA-DRB1*.txt'):
    print "FileName = %s" % (fName)
    fSplit = fName.split('/')
    type = fSplit[len(fSplit)-1].split('.')[0]
    opFileName = path + type +".txt"

    fh = open(fName)
    op = open(opFileName, 'w')
    #skip first line
    line = fh.readline()

    line = fh.readline()
    totalAffinity = 0.0
    count = 0
    allRows = []
    while(line):
        line = line.rstrip('\n\r')
        splitArr = line.split('\t')
        splitArr[0] = re.sub(' ', '-', splitArr[0])
        splitArr[1] = int(splitArr[1])
        splitArr[3] = float(splitArr[3])
        if(splitArr[1] == 15):
            allRows.append(splitArr)
            totalAffinity += splitArr[3]
            count += 1
        line = fh.readline()
    #end while

    mean_affinity = totalAffinity / count
    neg, pos = 0, 0
    negLabel, posLabel = -1, 1
    for row in allRows:
        opStr = ''
        #If affinity greater than mean_affinity, it is a negative label
        if(row[3] > mean_affinity):
            neg += 1
            opStr = row[2] + ', ' + str(negLabel) + '\n'
        else:
            pos += 1
            opStr = row[2] + ', ' + str(posLabel) + '\n'
        op.write(opStr)
    #end loop
    print "Neg = %d, Pos = %d, Total = %d" % ( neg, pos, neg+pos)
    print "Output written to file = %s\n" % (opFileName)
    op.close()
    fh.close()
    #write output to a file
#end outer for loop
