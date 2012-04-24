import get_twitter_data
import baseline_classifier, naive_bayes_classifier, max_entropy_classifier, libsvm_classifier
import json,sys

keyword = 'iphone'
twitterData = get_twitter_data.TwitterData()
#tweets = twitterData.getData(keyword, {'since': '2012-04-22', 'until' : '2012-04-23'})
tweets = []
weekTweets = twitterData.getWeeksData(keyword)

#algorithm = 'baseline'
#algorithm = 'naivebayes'
algorithm = 'maxent'
#algorithm = 'svm'

if(algorithm == 'baseline'):
    bc = baseline_classifier.BaselineClassifier(weekTweets, keyword, 'today')
    bc.classify()
    val = bc.getHTML()
elif(algorithm == 'naivebayes'):
    trainingDataFile = 'data/training_neatfile.csv'                
    classifierDumpFile = 'data/naivebayes_trained_model.pickle'
    trainingRequired = 0
    nb = naive_bayes_classifier.NaiveBayesClassifier(tweets, keyword, 'today',\
                                  trainingDataFile, classifierDumpFile, trainingRequired)
    nb.classify()
    nb.accuracy()
elif(algorithm == 'maxent'):
    trainingDataFile = 'data/training_neatfile.csv'                
    classifierDumpFile = 'data/maxent_trained_model.pickle'
    trainingRequired = 0
    maxent = max_entropy_classifier.MaxEntClassifier(tweets, keyword, 'today',\
                                  trainingDataFile, classifierDumpFile, trainingRequired)
    #maxent.analyzeTweets()
    maxent.classify()
    maxent.accuracy()
elif(algorithm == 'svm'):
    trainingDataFile = 'data/training_neatfile.csv'                
    classifierDumpFile = 'data/svm_trained_model.pickle'
    trainingRequired = 1
    sc = libsvm_classifier.SVMClassifier(tweets, keyword, 'today',\
                                  trainingDataFile, classifierDumpFile, trainingRequired)
    sc.classify()
    sc.accuracy()

print 'Done'
