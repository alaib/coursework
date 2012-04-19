import nltk
from nltk.classify import *

import pickle

import classifier_helper
from classifier_helper import *


#start class
class NaiveBayesClassifier:
    """ Naive Bayes Classifier """
    #variables    
    #start __init__
    def __init__(self, data, keyword, trainingDataFile, trainingDumpFile, trainingRequired = 0):
        #Remove duplicates        
        uniq_data = []       
        for element in data:
            if element not in uniq_data:
                uniq_data.append(element)
        self.origTweets = uniq_data        
        self.tweets = []
        for t in self.origTweets:
            self.tweets.append(process_tweet_modified(t))
        #end loop
        self.results = {}
        self.neut_count = 0
        self.pos_count = 0
        self.neg_count = 0
        self.keyword = keyword        
        #call training model
        if(trainingRequired):
            self.classifier = self.getNBTrainedClassifer(trainingDataFile, trainingDumpFile)
        else:
            f = open(trainingDumpFile)
            if f:
                self.classifier = pickle.load(f)
                f.close()
            else:
                self.classifier = self.getNBTrainedClassifer(trainingDataFile, trainingDumpFile)
            
    #end
    
    #start getNBTrainedClassifier
    def getNBTrainedClassifer(self, trainingDataFile, trainingDumpFile):
        inpfile = open(trainingDataFile, "r")
        line = inpfile.readline()
        count = 1
        tweetItems = []                
        while line:    
            count += 1
            l = range(len(line))
            for i in l[::-1]:
                if(line[i] == '|'):
                    processed_tweet = process_tweet_modified(line[0:i-1])
                    opinion = line[i+1:len(line)].strip()
                    break
            #end match loop                        
            tweet_item = processed_tweet, opinion
            if(opinion != 'neutral' and opinion != 'negative' and opinion != 'positive'):
                print('Error with tweet = %s, Line = %s') % (processed_tweet, count)
                line = inpfile.readline()
                continue
            tweetItems.append(tweet_item)    
            line = inpfile.readline()
        #end while loop
        
        tweets = []    
        for (words, sentiment) in tweetItems:
            words_filtered = [e.lower() for e in words.split() if len(e) >= 3]
            tweets.append((words_filtered, sentiment))
        
        word_features = get_word_features(get_words_in_tweets(tweets))
        set_word_features(word_features)
        training_set = nltk.classify.apply_features(extract_features, tweets)
        # Write back classifier to a file
        classifier = nltk.NaiveBayesClassifier.train(training_set)
        f = open(trainingDumpFile, 'wb')
        pickle.dump(classifier, f)
        f.close()
        return classifier
    #end
    
    
    #start classify
    def classify(self):
        count = 0
        for t in self.tweets:
            label = self.classifier.classify(extract_features(t.split()))
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
        html = '''
<html>
<head><title>Twitter Sentiment Analysis</title>
    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssgrids/grids-min.css" />
    <link rel="stylesheet" type="text/css" href="static/styles.css" />
</head>
<body>
    <div class="yui3-g" id="doc">
    <div class="yui3-u" id="hd">
        <h2> Twitter Sentiment Analyzer </h2>
    </div>
    <div class="yui3-u" id="bd">        
        <form name="keyform" id="key-form" method="get" onSubmit="return checkEmpty(this);">
        <p><input type="text" value="" name="keyword" id="keyword"/><input type="submit" value="Submit" id="sub"/></p>
        <div id="choice">
            <input type="radio" name="method" value="baseline">Baseline Method</input>
            <input type="radio" name="method" value="naivebayes" checked="true">Naive Bayes method</input>
        </div>
        </form>       
        <div id="results">            
'''                
        html += '<div id="stats"><p>Keyword = "' + self.keyword + '", '
        html += 'Positive = ' + str(self.pos_count) + ", Negative = " + str(self.neg_count)
        html += ", Neutral = "+ str(self.neut_count) + "</p></div>"
        html += '<div id="result-chart"></div>'
        html += '<div id="content">'
        left = '<div id="left"><h3>Positive</h3><ul>'
        right = '<div id="right"><h3>Negative</h3><ul>'
        middle = '<div id="middle"><h3>Neutral</h3><ul>'
        for i in self.results:
            item = self.results[i]
            if(item['label'] == 'positive'):
                left += '<li>' + item['tweet'] + '</li>'
            elif(item['label'] == 'neutral'):
                middle+= '<li>' + item['tweet'] + '</li>'
            elif(item['label'] == 'negative'):
                right += '<li>' + item['tweet'] + '</li>'
        left += '</ul></div>'
        right += '</ul></div>'
        middle += '</ul></div>'
        html += left + middle + right + '</div>'
        
        html += '''        
        </div>
    </div>
    <script src="http://yui.yahooapis.com/3.5.0/build/yui/yui-min.js"></script>
    <script type="text/javascript">
        YUI().use('charts', function (Y) 
        { 
            var myDataValues = [ 
'''
        html += '{category:"Positive", values:'+ str(self.pos_count) +'},'
        html += '{category:"Negative", values:'+ str(self.neg_count) +'},'
        html += '{category:"Neutral", values:'+ str(self.neut_count) +'}'
        html += '''         
            ];          
            var mychart = new Y.Chart({
                                       dataProvider:myDataValues, 
                                       render:"#result-chart", 
                                       type: "column"}
                                      );
        });
        function checkEmpty(f) {
            if (f.keyword.value === "") {
                alert('Please enter a valid keyword');
                return false;
            }else{
                f.submit();
                return true;
            }
        }    
    </script>
</body>
</html>
'''        

        return html                            
    #end
#end class