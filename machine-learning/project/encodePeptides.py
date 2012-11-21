#!/usr/bin/python

import pprint
import sys
import glob
import collections

def getProteinCoding(ch):
   #A C D E F G H I K L M N P Q R S T V W Y
   validP = ['A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'Y']
   if ch not in validP:
       print "%s not a valid protein encoding character, Error !!! Exiting !" % (ch)
       sys.exit(-1)

   d = collections.OrderedDict()
   d['A'] = 0
   d['C'] = 0
   d['D'] = 0
   d['E'] = 0
   d['F'] = 0
   d['G'] = 0
   d['H'] = 0
   d['I'] = 0
   d['K'] = 0
   d['L'] = 0
   d['M'] = 0
   d['N'] = 0
   d['P'] = 0
   d['Q'] = 0
   d['R'] = 0
   d['S'] = 0
   d['T'] = 0
   d['V'] = 0
   d['W'] = 0
   d['Y'] = 0

   #change value of given protein char
   d[ch] = 1

   encodeStr = ''
   for k,v in d.items():
       encodeStr += str(v)

   return encodeStr
#end

path = 'data/MHCBN-15mers/encoded/'

for fName in glob.glob('data/MHCBN-15mers/*.txt'):
    fSplit = fName.split('/')
    opFileName = path + fSplit[len(fSplit)-1].split('.')[0]+"_encoded.txt"
    inFH = open(fName)
    opFH = open(opFileName, 'w')
    line = inFH.readline()

    while(line):
        splitArr = line.split(',')
        splitArr = [ elem.strip() for elem in splitArr ]
        seq = splitArr[1]
        encodeStr = ''
        for i in xrange(len(seq)):
            encodeStr += getProteinCoding(seq[i])
        opStr = splitArr[0] + ", " + encodeStr + ", " + splitArr[2] + "\n"
        opFH.write(opStr)
        line = inFH.readline()
    #end while
    opFH.close()
    inFH.close()
#end for
