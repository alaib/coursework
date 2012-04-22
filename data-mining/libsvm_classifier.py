import svm
from svmutil import *
import re, pickle, csv, os
import classifier_helper, html_helper

#start class
class SVMClassifier:
    """ SVM Classifier """
    #variables    
    #start __init__
    def __init__(self, data, keyword, trainingDataFile, classifierDumpFile, trainingRequired = 0):
        #Instantiate classifier helper
        self.helper = classifier_helper.ClassifierHelper('data/twit_positive.txt', 'data/twit_negative.txt')
        #self.helper = classifier_helper.ClassifierHelper('data/positive_keywords.txt', 'data/negative_keywords.txt')
        
        #Remove duplicates        
        uniq_data = []       
        for element in data:
            if element not in uniq_data:
                uniq_data.append(element)
        self.origTweets = uniq_data        
        self.tweets = []
        for t in self.origTweets:
            self.tweets.append(self.helper.process_tweet(t))
        #end loop
        
        #Variables
        self.results = {}
        self.neut_count = 0
        self.pos_count = 0
        self.neg_count = 0
        self.keyword = keyword
        self.html = html_helper.HTMLHelper()
        
        #call training model
        if(trainingRequired):
            self.classifier = self.getSVMTrainedClassifer(trainingDataFile, classifierDumpFile)
        else:
            fp = open(classifierDumpFile, 'r')
            if(fp):
                self.classifier = svm_load_model(classifierDumpFile)
            else:
                self.classifier = self.getSVMTrainedClassifer(trainingDataFile, classifierDumpFile)
    #end
    
    #start getNBTrainedClassifier
    def getSVMTrainedClassifer(self, trainingDataFile, classifierDumpFile):        
        # read all tweets and labels
        tweetItems = self.getFilteredTrainingData1(trainingDataFile)
        
        tweets = []
        for (words, sentiment) in tweetItems:
            words_filtered = [e.lower() for e in words.split() if(self.helper.is_ascii(e))]
            tweets.append((words_filtered, sentiment))

        results = self.helper.getSVMFeatureVectorAndLabels(tweets)
        self.feature_vectors = results['feature_vector']
        self.labels = results['labels']
        
        #SVM Trainer
        problem = svm_problem(self.labels, self.feature_vectors)
        param = svm_parameter('-q')
        param.C = 10
        param.kernel_type = LINEAR
        param.show()
        classifier = svm_train(problem, param)
        svm_save_model(classifierDumpFile, classifier)
        return classifier
    #end
    
    #start getFilteredTrainingData
    def getFilteredTrainingData(self, trainingDataFile):
        fp = open( trainingDataFile, 'rb' )
        min_count = self.getMinCount(trainingDataFile)        
        neg_count, pos_count, neut_count = 0, 0, 0
        
        reader = csv.reader( fp, delimiter=',', quotechar='"', escapechar='\\' )
        tweetItems = []
        count = 1       
        for row in reader:
            processed_tweet = self.helper.process_tweet(row[4])
            sentiment = row[1]
            
            #Skip first line
            if(sentiment == 'Sentiment'):                
                continue;
            
            if(sentiment == 'irrelevant' or sentiment == 'neutral'):                
                if(neut_count == int(min_count)):
                    continue
                neut_count += 1
            elif(sentiment == 'positive'):
                if(pos_count == min_count):
                    continue
                pos_count += 1
            elif(sentiment == 'negative'):
                if(neg_count == min_count):
                    continue
                neg_count += 1
            
            tweet_item = processed_tweet, sentiment
            tweetItems.append(tweet_item)
            count +=1
        #end loop
        return tweetItems
    #end 
        #start getFilteredTrainingData
    def getFilteredTrainingData1(self, trainingDataFile):
        fp = open( trainingDataFile, 'rb' )
        neg_count, pos_count = 0, 0
        
        reader = csv.reader( fp, delimiter=',', quotechar='"', escapechar='\\' )
        tweetItems = []
        count = 1       
        for row in reader:
            #processed_tweet = self.helper.process_tweet(row[4])
            processed_tweet = self.helper.process_tweet(row[5])
            #sentiment = row[1]
            sentiment = row[0]
            
            if(sentiment == "0"):
                sentiment = "negative"
            elif(sentiment == "2"):
                sentiment = "neutral"
            elif(sentiment == "4"):
                sentiment = "positive"
                                
            if(sentiment == 'positive'):            
                pos_count += 1
            elif(sentiment == 'negative'):            
                neg_count += 1

            tweet_item = processed_tweet, sentiment
            tweetItems.append(tweet_item)
            count +=1
        #end loop
        return tweetItems
    #end 
    
    #start getMinCount
    def getMinCount(self, trainingDataFile):
        fp = open( trainingDataFile, 'rb' )
        reader = csv.reader( fp, delimiter=',', quotechar='"', escapechar='\\' )
        neg_count, pos_count, neut_count = 0, 0, 0
        for row in reader:
            sentiment = row[1]
            if(sentiment == 'irrelevant' or sentiment == 'neutral'):
                neut_count += 1
            elif(sentiment == 'positive'):
                pos_count += 1
            elif(sentiment == 'negative'):
                neg_count += 1
        #end loop
        return min(neg_count, pos_count, neut_count)
    #end
    #start classify
    def classify(self):
        count = 0
        test_tweets = []
        for words in self.tweets:
            words_filtered = [e.lower() for e in words.split() if(self.helper.is_ascii(e))]
            test_tweets.append(words_filtered)
        test_feature_vector = self.helper.getSVMFeatureVector(test_tweets)
        p_labels, p_accs, p_vals = svm_predict([0] * len(test_feature_vector),\
                                            test_feature_vector, self.classifier)
        count = 0
        for t in self.tweets:
            label = p_labels[0]
            if(label == 0):
                label = 'positive'
                self.pos_count += 1
            elif(label == 1):
                label = 'negative'
                self.neg_count += 1
            elif(label == 2):
                label = 'neutral'
                self.neut_count += 1
            res = {'text': t, 'tweet': self.origTweets[count], 'label': label}
            self.results[count] = res
            count += 1
        #end loop
        
    #end
           
    #start writeOutput
    def writeOutput(self, file, writeOption='w'):
        fp = open(file, writeOption)
        for i in self.results:
            item = self.results[i]
            text = item['text'].strip()
            label = item['label']
            if(item['label'] != 'neutral'):
                writeStr = text+" | "+label+"\n"
                fp.write(writeStr)
        #end for loop            
    #end writeOutput
    
    #start printStats
    def getHTML(self):
        return self.html.getResultHTML(self.keyword, self.results, self.pos_count, \
                                       self.neg_count, self.neut_count, 'svm')
    #end
#end class