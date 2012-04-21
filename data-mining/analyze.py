import get_twitter_data
from get_twitter_data import *

import baseline_classifier, naive_bayes_classifier
import json
'''
keyword = 'reebok'
keywords = ['happy', 'sad', 'thank you', 'nike', 'beautiful', 'down', \
            'romney', 'obama', 'sports', 'fashion', 'amazon', 'google', 'yahoo', \
            'microsoft', 'food', 'festival', 'holiday', 'love']
for keyword in keywords:
    tweets = getData(keyword)     
    bc = baseline_classifier.BaselineClassifier(tweets)
    bc.classify()
    bc.writeOutput('data/baseline_output.txt', 'a')
    print 'finished %s' % (keyword)
print('Done')
'''

keyword = 'reebok'
tweets = getData(keyword)
trainingDataFile = 'data/full-corpus.csv'                
classifierDumpFile = 'data/naivebayes_full-corpus.pickle'
trainingRequired = 1
nb = naive_bayes_classifier.NaiveBayesClassifier(tweets, keyword, \
                              trainingDataFile, classifierDumpFile, trainingRequired)
nb.classify()
val = nb.getHTML()
print 'Done'

