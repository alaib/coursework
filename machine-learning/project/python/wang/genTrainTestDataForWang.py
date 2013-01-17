#!/usr/bin/python

import pprint
import glob

baseTrainPath = '/home/ravikirn/mlcode/data/wang-paper-data/trainTest/'
baseTestPath = '/home/ravikirn/mlcode/data/wang-paper-data/trainTest/'

for fName in glob.glob('/home/ravikirn/mlcode/data/wang-paper-data/raw/*.txt'):
    fh = open(fName)

    fSplit = fName.split('/')
    type = fSplit[len(fSplit)-1].split('.')[0]

    trainFile = baseTrainPath + type + "_train.txt"
    testFile = baseTestPath + type + "_test.txt"

    train = open(trainFile, 'w')
    test = open(testFile, 'w')

    #Read line
    line = fh.readline()

    oneSet = []
    zeroSet = []
    lenDict = {}

    count = 0
    total = 0
    seqLen = 15
    zeroLen = 0
    oneLen = 0

    zTrain = 0
    zTest = 0
    oTrain = 0
    oTest = 0
    tTrain = 0
    tTest = 0

    trainRatio = 0.70

    while(line):
        total += 1
        splitArr = line.split(',')
        splitArr = [ elem.strip() for elem in splitArr ]
        sLen = len(splitArr[0])
        c = splitArr[1]
        if(sLen == seqLen):
            count += 1
            #zeroSet corresponds to -1
            if(c == '-1'):
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

    #maxLen
    maxTrainLen = 223
    maxTestLen = 200

    #For negative cases, send 60% to training and 40% to testing
    zeroTrain = int(0.6 * len(zeroSet))
    zeroTest = int(0.4 * len(zeroSet))
    for i in xrange(len(zeroSet)):
        s = type + ", " + zeroSet[i] + ", -1\n"
        if(i < zeroTrain):
            train.write(s)
        else:
            test.write(s)

    #For positive cases, add until maxTrainLen is met for training and 100 to testing (or the remaining)
    oneTrain = 0
    oneTest = 0
    if(len(oneSet) > 200):
        oneTrain = maxTrainLen - zeroTrain
        #add (maxTrainLen - zeroTrain) to test
        for i in xrange(maxTrainLen - zeroTrain):
            s = type + ", " + oneSet[i] + ", 1\n"
            train.write(s)
        #add next 100 or remaining to test set
        j = maxTrainLen - zeroTrain
        count = 0
        while(j < len(oneSet) and count < 100):
            s = type + ", " + oneSet[j] + ", 1\n"
            test.write(s)
            count += 1
            j += 1
            oneTest += 1
    else:
        #add 70% to training and 30% to testing
        oneTrain = int(0.7 * len(oneSet))
        oneTest = int(0.3 * len(oneSet))
        for i in xrange(len(oneSet)):
            s = type + ", " + oneSet[i] + ", 1\n"
            if(i < oneTrain):
                train.write(s)
            else:
                test.write(s)
    #end 

    zTrain += zeroTrain
    oTrain += oneTrain
    zTest += zeroTest
    oTest += oneTest

    tTest += zTest + oTest
    tTrain += zTrain + oTrain

    train.close()
    test.close()

    print "Class Type = ", type
    print "Train Data ====> Total = %d, ZeroLabel = %d, OneLabel = %d" % (tTrain, zTrain, oTrain)
    print "Test Data  ====> Total = %d, ZeroLabel = %d, OneLabel = %d" % (tTest, zTest, oTest)
    print "Output written to ", trainFile, " and ", testFile, "\n\n"
#end for
