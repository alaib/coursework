#!/bin/python
import sys;
import pprint;

#Declare the DNA codon table
codonMap = {
                'TTT' : 'Phenylalanine', 'TTC' : 'Phenylalanine',
                'TTA' : 'Leucine', 'TTG' : 'Leucine', 'CTT' : 'Leucine', 'CTC' : 'Leucine', 'CTA' : 'Leucine', 'CTG' : 'Leucine',
                'ATT' : 'Isoleucine', 'ATC' : 'Isoleucine', 'ATA' : 'Isoleucine',
                'ATG' : 'Methionine',
                'GTT' : 'Valine', 'GTC' : 'Valine', 'GTA' : 'Valine', 'GTG' : 'Valine',
                'TCT' : 'Serine', 'TCC' : 'Serine', 'TCA' : 'Serine', 'TCG' : 'Serine',
                'CCT' : 'Proline', 'CCC' : 'Proline', 'CCA' : 'Proline', 'CCG' : 'Proline',
                'ACT' : 'Threonine', 'ACC' : 'Threonine', 'ACA' : 'Threonine', 'ACG' : 'Threonine',
                'GCT' : 'Alanine', 'GCC' : 'Alanine', 'GCA' : 'Alanine', 'GCG' : 'Alanine',
                'TAT' : 'Tyrosine', 'TAC' : 'Tyrosine',
                'TAA' : 'Stop', 'TAG' : 'Stop',
                'CAT' : 'Histidine', 'CAC' : 'Histidine',
                'CAA' : 'Glutamine', 'CAG' : 'Glutamine',
                'AAT' : 'Asparagine', 'AAC' : 'Asparagine',
                'AAA' : 'Lysine', 'AAG' : 'Lysine',
                'GAT' : 'Aspartic acid', 'GAC' : 'Aspartic acid',
                'GAA' : 'Glutamic acid', 'GAG' : 'Glutamic acid',
                'TGT' : 'Cysteine', 'TGC' : 'Cysteine',
                'TGA' : 'Stop',
                'TGG' : 'Tryptophan',
                'CGT' : 'Arginine', 'CGC' : 'Arginine', 'CGA' : 'Arginine', 'CGG' : 'Arginine',
                'AGT' : 'Serine', 'AGC' : 'Serine',
                'AGA' : 'Arginine', 'AGG' : 'Arginine',
                'GGT' : 'Glycine', 'GGC' : 'Glycine', 'GGA' : 'Glycine', 'GGG' : 'Glycine'
            }

#Extract codon names
aminoAcids = sorted(set(codonMap.values()))
codons = sorted(set(codonMap.keys()))

aminoCount = {}
for c in aminoAcids:
    aminoCount[c] = 0

aminoCount['Total'] = 0

codonCount = {}
for c in codons:
    codonCount[c] = 0

codonCount['Total'] = 0

#Check the args
lenArgs = len(sys.argv)

if(lenArgs < 2):
    print "Not enough arguments, Usage: python analyze.py file1.fa offset1 [file2.fa offset 2 ...]"
    sys.exit()
elif(lenArgs % 2 != 1):
    print "Filename/Offset argument missing, Usage: python analyze.py file1.fa offset1 [file2.fa offset 2 ...]"
    sys.exit()

fileList = []
i = 1
#outfile = open('test', 'w')
#Iterate over all files
while(i < lenArgs):
    fName = str(sys.argv[i])
    offset = int(sys.argv[i+1])
    #print fName, offset
    f = open(fName)

    #skip header
    c = f.read(1)
    while(c != '\n'):
        c = f.read(1)

    #skip until offset
    j = 1
    while(j <= offset-1):
        c = f.read(1)
        if(c != '\n'):
            j += 1

    #start codon starts (read 3 consecutive chars and check the codonMap to update codon)
    while(True):
        j = 1
        amino = ''
        while(j <= 3):
            c = f.read(1)
            if(c != '\n'):
                amino += c
                j += 1
        #end
        #print amino
        #outfile.write(amino)
        cName = codonMap[amino]

        aminoCount[cName] += 1
        codonCount[amino] += 1

        aminoCount['Total'] += 1
        codonCount['Total'] += 1

        if(cName == 'Stop'):
            #print fName, amino, cName
            break
        
    i += 2
#end while loop    
#outfile.close()

codonFinal = {} 
for c in aminoCount:
    if(c != 'Total'):
        codonFinal[c] = {}
        codonFinal[c]['cTotal'] = aminoCount[c]

for c in codonCount:
    if(c != 'Total'):
        amino = codonMap[c]
        codonFinal[amino][c] = codonCount[c]

codonOut = open('codonHisto.txt', 'w')
codonOut.write('Amino Acid\t\tCodon\tCount\tPercentage\n')
codonOut.write('==============================================\n')
for c in sorted(codonFinal.keys()):
    val = codonFinal[c]
    total = val['cTotal']
    for key, value in val.items():
         if(key != 'cTotal'):
            percent = round((float(value)/total)*100, 2)
            if(len(c) > 10):
                codonOut.write(c+'\t'+key+'\t\t'+str(value)+'\t\t'+str(percent)+'%\n')
            elif(len(c) > 7):
                codonOut.write(c+'\t\t'+key+'\t\t'+str(value)+'\t\t'+str(percent)+'%\n')
            else:
                codonOut.write(c+'\t\t\t'+key+'\t\t'+str(value)+'\t\t'+str(percent)+'%\n')
    codonOut.write('\n') 
#end loop
codonOut.close()

aminoOut = open('aminoHisto.txt', 'w')
aminoOut.write('Amino Acid\t\tCount\tPercentage\n')
aminoOut.write('=================================\n')
total = aminoCount['Total']
for c in sorted(aminoCount.keys()):
    if(c != 'Total'):
        value = aminoCount[c]
        percent = round((float(value)/total)*100, 2)
        if(len(c) > 10):
            aminoOut.write(c+'\t'+str(value)+'\t\t'+str(percent)+'%\n')
        elif(len(c) > 7):
            aminoOut.write(c+'\t\t'+str(value)+'\t\t'+str(percent)+'%\n')
        else:
            aminoOut.write(c+'\t\t\t'+str(value)+'\t\t'+str(percent)+'%\n')
#end loop
aminoOut.close()

#print aminoCount
#print codonCount
#pprint.pprint(codonFinal)
