import re
import nltk
from nltk.classify import *

class ClassifierHelper:
    #start __init__
    def __init__(self, posFeatureFile, negFeatureFile):
        self.wordFeatures = []
        # Read positive feature words
        inpfile = open(posFeatureFile, 'r')
        line = inpfile.readline()        
        while line:
            self.wordFeatures.append(line.strip())
            line = inpfile.readline()
        # Read negative feature words
        inpfile = open(negFeatureFile, 'r')
        line = inpfile.readline()        
        while line:
            self.wordFeatures.append(line.strip())
            line = inpfile.readline()
    #end    
    
    #start extract_features
    def extract_features(self, document):
        document_words = set(document)
        features = {}
        for word in self.wordFeatures:
            features['contains(%s)' % word] = (word in document_words)
        return features
    #end
    
    #start process_tweet
    def process_tweet(self, tweet):
        #Conver to lower case
        tweet = tweet.lower()
        #Convert https?://* to URL
        tweet = re.sub('((www\.[\s]+)|(https?://[^\s]+))','URL',tweet)
        #Convert @username to AT_USER
        tweet = re.sub('@[^\s]+','AT_USER',tweet)    
        #Remove additional white spaces
        tweet = re.sub('[\s]+', ' ', tweet)
        #Replace #word with word
        tweet = re.sub(r'#([^\s]+)', r'\1', tweet)
        #trim
        tweet = tweet.strip()
        #remove last " at string end
        tweet = tweet.rstrip('"')
        return tweet
    #end 
    
    #start is_ascii
    def is_ascii(self, word):
        return all(ord(c) < 128 for c in word)
    #end
#end class