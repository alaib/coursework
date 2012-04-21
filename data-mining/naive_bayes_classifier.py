import nltk.classify
import re, pickle, csv, os
import classifier_helper

#start class
class NaiveBayesClassifier:
    """ Naive Bayes Classifier """
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
        
        #call training model
        if(trainingRequired):
            self.classifier = self.getNBTrainedClassifer(trainingDataFile, classifierDumpFile)
        else:
            f1 = open(classifierDumpFile)            
            if(f1):
                self.classifier = pickle.load(f1)                
                f1.close()                
            else:
                self.classifier = self.getNBTrainedClassifer(trainingDataFile, classifierDumpFile)
    #end
    
    #start getNBTrainedClassifier
    def getNBTrainedClassifer(self, trainingDataFile, classifierDumpFile):        
        # read all tweets and labels
        tweetItems = self.getFilteredTrainingData(trainingDataFile)
        
        tweets = []
        for (words, sentiment) in tweetItems:
            words_filtered = [e.lower() for e in words.split() if(self.helper.is_ascii(e))]
            tweets.append((words_filtered, sentiment))
                    
        training_set = nltk.classify.apply_features(self.helper.extract_features, tweets)
        # Write back classifier and word features to a file
        classifier = nltk.NaiveBayesClassifier.train(training_set)
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
                if(neut_count == min_count):
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
