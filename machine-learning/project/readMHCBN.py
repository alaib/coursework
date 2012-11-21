#!/usr/bin/python

import pprint
import glob

count = 0
total = 0
seqLen = 15
zeroLen = 0
oneLen = 0
lenDict = {}
trainFile = 'data/MHCBN-15mers/train.txt'
testFile = 'data/MHCBN-15mers/test.txt'

zTrain = 0
zTest = 0
oTrain = 0
oTest = 0
tTrain = 0
tTest = 0

train = open(trainFile, 'w')
test = open(testFile, 'w')

trainRatio = 0.80

for fName in glob.glob('data/MHCBN-Full/UPDS/*.arff'):
    fh = open(fName)

    fSplit = fName.split('/')
    type = fSplit[len(fSplit)-1].split('.')[0]

    #Skip four lines
    for i in xrange(4):
        line = fh.readline()

    #Read from 5th line to EOF
    line = fh.readline()

    oneSet = []
    zeroSet = []

    while(line):
        total += 1
        splitArr = line.split(',')
        splitArr = [ elem.strip() for elem in splitArr ]
        sLen = len(splitArr[0])
        c = splitArr[1]
        if(sLen == seqLen):
            count += 1
            if(c == '0'):
                zeroLen += 1
                zeroSet.append(splitArr[0])
            else:
                oneLen += 1
                oneSet.append(splitArr[0])

        if(sLen not in lenDict):
            lenDict[sLen] = 1
        else:
            lenDict[sLen] += 1
        line = fh.readline()
    #end while

    #Split and write out to training and testing datasets
    oneTrain = int(trainRatio * len(oneSet))
    zeroTrain = int(trainRatio * len(zeroSet))
    for i in xrange(len(oneSet)):
        s = type + ", " + oneSet[i] + ", 1\n"
        if(i < oneTrain):
            train.write(s)
        else:
            test.write(s)

    for i in xrange(len(zeroSet)):
        s = type + ", " + zeroSet[i] + ", 0\n"
        if(i < zeroTrain):
            train.write(s)
        else:
            test.write(s)
    zTrain += zeroTrain
    oTrain += oneTrain
    zTest += len(zeroSet) - zeroTrain
    oTest += len(oneSet) - oneTrain

    tTest += zTest + oTest
    tTrain += zTrain + oTrain
#end for
train.close()
test.close()

print "Train Data ====> Total = %d, ZeroLabel = %d, OneLabel = %d" % (tTrain, zTrain, oTrain)
print "Test Data  ====> Total = %d, ZeroLabel = %d, OneLabel = %d" % (tTest, zTest, oTest)
