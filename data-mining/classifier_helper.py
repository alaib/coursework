import re
import nltk
from nltk.classify import *


# start get_words_in_tweets
def get_words_in_tweets(tweets):
    all_words = []
    for (words, sentiment) in tweets:
        all_words.extend(words)
    return all_words
#end

#start get_word_features
def get_word_features(wordlist):
    wordlist = nltk.FreqDist(wordlist)
    word_features = wordlist.keys()
    return word_features
#end

# start set_word_features
def set_word_features(word_feat):
    global word_features
    word_features = word_feat
#end
    
#start extract_features
def extract_features(document):
    document_words = set(document)
    features = {}
    for word in word_features:
        features['contains(%s)' % word] = (word in document_words)
    return features
#end

#start process_tweet
def process_tweet(tweet):
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

#start process_tweet_modified
def process_tweet_modified(tweet):
    #Conver to lower case
    tweet = tweet.lower()
    #Convert https?://* to URL
    tweet = re.sub('((www\.[\s]+)|(https?://[^\s]+))','',tweet)    
    #Convert @username to AT_USER
    tweet = re.sub('@[^\s]+','',tweet)    
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
