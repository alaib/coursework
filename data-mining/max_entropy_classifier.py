import nltk.classify
import re, pickle, csv, os
import classifier_helper, html_helper

#start class
class MaxEntClassifier:
    """ Maximum Entropy Classifier """
    #variables    
    #start __init__
    def __init__(self, data, keyword, trainingDataFile, classifierDumpFile, trainingRequired = 0):
        #Instantiate classifier helper
        self.helper = classifier_helper.ClassifierHelper('data/positive_keywords.txt', 'data/negative_keywords.txt')
        
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
            self.classifier = self.getMaxEntTrainedClassifer(trainingDataFile, classifierDumpFile)
        else:
            f1 = open(classifierDumpFile)            
            if(f1):
                self.classifier = pickle.load(f1)                
                f1.close()                
            else:
                self.classifier = self.getMaxEntTrainedClassifer(trainingDataFile, classifierDumpFile)
    #end
    
    #start getMaxEntTrainedClassifier
    def getMaxEntTrainedClassifer(self, trainingDataFile, classifierDumpFile):        
        # read all tweets and labels
        tweetItems = self.getFilteredTrainingData(trainingDataFile)
        
        tweets = []
        for (words, sentiment) in tweetItems:
            words_filtered = [e.lower() for e in words.split() if(self.helper.is_ascii(e))]
            tweets.append((words_filtered, sentiment))
                    
        training_set = nltk.classify.apply_features(self.helper.extract_features, tweets)
        # Write back classifier        
        classifier = nltk.classify.maxent.train_maxent_classifier_with_gis(training_set)
        outfile = open(classifierDumpFile, 'wb')        
        pickle.dump(classifier, outfile)        
        outfile.close()        
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
                if(neut_count == int(min_count*0.98)):
                    continue
                neut_count += 1
            elif(sentiment == 'positive'):
                if(pos_count == min_count):
                    continue
                pos_count += 1
            elif(sentiment == 'negative'):
                if(neg_count == min_count*0.95):
                    continue
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
        for t in self.tweets:
            label = self.classifier.classify(self.helper.extract_features(t.split()))
            if(label == 'positive'):
                self.pos_count += 1
            elif(label == 'negative'):                
                self.neg_count += 1
            elif(label == 'neutral'):                
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
                                       self.neg_count, self.neut_count, 'maxentropy')
    #end
#end class
