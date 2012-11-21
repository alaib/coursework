#!/usr/bin/python

import pickle
import pprint

inpFileName = 'data/MHCPEP.txt'
opFileName = 'data/MHCPEPList.pickle'
textOPFileName = 'data/MHCPEPList.txt'

fh = open(inpFileName)
textOP = open(textOPFileName, 'w')
op = open(opFileName, 'wb')
line = fh.readline()

mhcList = []
id = 1

#Write first line to file as header
lineOP = "id\tdbName\tclass\ttype\tsequence\tbindingFlag\tbindingLevel\n"
textOP.write(lineOP)

while(line):
    mhc = {}
    #First line has DB name
    mhc['dbName'] = line.strip("\n>#")

    #Read second line 'MHC MOLECULE'
    line = fh.readline()

    #MHC MOLECULE: Name, Class, Human/Mouse
    if(line.find("MHC MOLECULE") != -1):
        molArr = line.split(' ')
        lenSA = len(molArr)
        molArr = [elem.rstrip("\n,#") for elem in molArr]
        molArr[lenSA-1] = molArr[lenSA-1].strip("()")
        #['MHC', 'MOLECULE:', 'HLA-A2(A*0201)', 'CLASS-1', 'HUMAN']
        mhc['id'] = id
        mhc['type'] = molArr[2]
        mhc['class'] = molArr[3]
        mhc['species'] = molArr[4]

        #Skip lines until BINDING line
        while(1):
            if(line.find("BINDING") == -1):
                line = fh.readline()
            else:
                break

        #Binding Line found
        #BINDING: yes, moderate#
        bindArr = line.split(' ')
        bindArr = [elem.rstrip("\n,#") for elem in bindArr]
        #['BINDING:', 'yes', 'moderate']
        mhc['bindingFlag'] = bindArr[1]
        mhc['bindingLevel'] = bindArr[2]

        #Skip lines until SEQUENCE line
        while(1):
            if(line.find("SEQUENCE") == -1):
                line = fh.readline()
            else:
                break

        #Sequence line found
        #SEQUENCE: ILKEPVHGV*#
        seqArr = line.split(' ')
        seqArr = [elem.rstrip("\n,#*") for elem in seqArr]
        #['SEQUENCE:', 'EILKEPVHGV']
        mhc['sequence'] = seqArr[1]
 
        #skip until end of MHC '...#' 
        while(1):
            if(line.find("...#") == -1):
                line = fh.readline()
            else:
                #Read first line of next molecule
                line = fh.readline()
                break

        if(mhc['species'] == 'HUMAN'):
            #lineOP = "id    dbName  class  type    sequence    bindingFlag bindingLevel\n"
            lineOP = str(mhc['id']) + "\t" + mhc['dbName'] + "\t" + mhc['class'] + "\t" + mhc['type'] + "\t" + mhc['sequence'] + "\t" + mhc['bindingFlag'] + "\t" + mhc['bindingLevel'] + "\n"
            textOP.write(lineOP)
        mhcList.append(mhc)
        id += 1
#end while loop

#Dump sequences to a file
pickle.dump(mhcList, op)

#Close files
fh.close()
textOP.close()
op.close()

#Optional dump to console
#pprint.pprint(mhcList)
