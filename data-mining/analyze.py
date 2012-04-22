import get_twitter_data
from get_twitter_data import *

import baseline_classifier, naive_bayes_classifier, max_entropy_classifier, libsvm_classifier
import json

keyword = 'reebok'
tweets = getData(keyword)

'''
trainingDataFile = 'data/full-corpus.csv'                
classifierDumpFile = 'data/naivebayes_full-corpus.pickle'
trainingRequired = 1
nb = naive_bayes_classifier.NaiveBayesClassifier(tweets, keyword, \
                              trainingDataFile, classifierDumpFile, trainingRequired)
nb.classify()
nb.accuracy()
'''

'''trainingDataFile = 'data/full-corpus.csv'                
classifierDumpFile = 'data/maxent_full-corpus1.pickle'
trainingRequired = 1
maxent = max_entropy_classifier.MaxEntClassifier(tweets, keyword, \
                              trainingDataFile, classifierDumpFile, trainingRequired)
maxent.classify()
val = maxent.getHTML()
'''

trainingDataFile = 'data/full-corpus.csv'                
#trainingDataFile = 'data/training.10000.processed.noemoticon.csv'
classifierDumpFile = 'data/svm_full-corpus1.pickle'
trainingRequired = 1
sc = libsvm_classifier.SVMClassifier(tweets, keyword, \
                              trainingDataFile, classifierDumpFile, trainingRequired)
sc.classify()
sc.accuracy()
print 'Done'
