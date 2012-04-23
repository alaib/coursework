import get_twitter_data
from get_twitter_data import *

import baseline_classifier, naive_bayes_classifier, max_entropy_classifier, libsvm_classifier
import json,sys

keyword = 'reebok'
tweets = getData(keyword)

algorithm = 'naivebayes'
#algorithm = 'maxent'
#algorithm = 'svm'
algorithm = sys.argv[1]

if(algorithm == 'naivebayes'):
    trainingDataFile = 'data/training_neatfile.csv'                
    classifierDumpFile = 'data/naivebayes_trained_model.pickle'
    trainingRequired = 1
    nb = naive_bayes_classifier.NaiveBayesClassifier(tweets, keyword, \
                                  trainingDataFile, classifierDumpFile, trainingRequired)
    nb.classify()
    nb.accuracy()
elif(algorithm == 'maxent'):
    trainingDataFile = 'data/training_neatfile.csv'                
    classifierDumpFile = 'data/maxent_trained_model.pickle'
    trainingRequired = 1
    maxent = max_entropy_classifier.MaxEntClassifier(tweets, keyword, \
                                  trainingDataFile, classifierDumpFile, trainingRequired)
    #maxent.analyzeTweets()
    maxent.classify()
    maxent.accuracy()
elif(algorithm == 'svm'):
    trainingDataFile = 'data/training_neatfile.csv'                
    classifierDumpFile = 'data/svm_trained_model.pickle'
    trainingRequired = 1
    sc = libsvm_classifier.SVMClassifier(tweets, keyword, \
                                  trainingDataFile, classifierDumpFile, trainingRequired)
    sc.classify()
    sc.accuracy()

print 'Done'
