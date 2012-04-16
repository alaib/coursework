import re
import classifier_helper
from classifier_helper import *

#start class
class classifier:
    """ Classifier using baseline method """
    #variables    
    #start __init__
    def __init__(self, data):
        #Remove duplicates 
        uniq_data = []       
        for element in data:
            if element not in uniq_data:
                uniq_data.append(element)
        self.origTweets = uniq_data        
        self.tweets = []
        for t in self.origTweets:
            self.tweets.append(process_tweet(t))
        #end loop
        self.results = {}
        self.neut_count = 0
        self.pos_count = 0
        self.neg_count = 0
    #end
    
    #start classify
    def classify(self):
        #load positive keywords file        
        inpfile = open("data/positive_keywords.txt", "r")            
        line = inpfile.readline()
        positive_words = []
        while line:
            positive_words.append(line.strip())
            line = inpfile.readline()
            
        #load negative keywords file    
        inpfile = open("data/negative_keywords.txt", "r")            
        line = inpfile.readline()
        negative_words = []
        while line:
            negative_words.append(line.strip())
            line = inpfile.readline()
        #start processing each tweet
        count = 0
        for t in self.tweets:
            neg_words = [word for word in negative_words if(self.string_found(word, t))]
            pos_words = [word for word in positive_words if(self.string_found(word, t))]
            if(len(pos_words) > len(neg_words)):
                label = 'positive'
                self.pos_count += 1
            elif(len(pos_words) < len(neg_words)):
                label = 'negative'
                self.neg_count += 1
            else:
                if(len(pos_words) > 0 and len(neg_words) > 0):
                    label = 'positive'
                    self.pos_count += 1
                else:
                    label = 'neutral'
                    self.neut_count += 1
            res = {'text': t, 'tweet': self.origTweets[count], 'label': label, 'neg_words': neg_words, 'pos_words': pos_words}
            self.results[count] = res
            count += 1            
        #end for loop                   
    #end
    
    #start substring whole word match
    def string_found(self, string1, string2):
        if re.search(r"\b" + re.escape(string1) + r"\b", string2):
            return True
        return False
    #end
    
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
        <form id="key-form" method="get">
        <p><input type="text" value="" name="keyword" id="keyword"/><input type="submit" value="Submit" id="sub"/></p>
        </form>       
        <div id="results">            
'''
        html += '<div id="stats"><p>Positive = ' + str(self.pos_count) + ", Negative = " + str(self.neg_count)
        html += ", Neutral = "+ str(self.neut_count) + "</p></div>"
        
        html += '''
        <div id="result-chart"></div>
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
    </script>
</body>
</html>
'''        
        html += '<div id="content">'
        left = '<div id="left"><h3>Positive</h3><ul>'
        right = '<div id="right"><h3>Negative</h3><ul>'
        for i in self.results:
            item = self.results[i]
            if(item['label'] == 'positive'):
                left += '<li>' + item['tweet'] + '</li>'
            elif(item['label'] == 'negative'):
                right += '<li>' + item['tweet'] + '</li>'
        left += '</ul></div>'
        right += '</ul></div>'
        html += left + right + '</div>'
        return html                            
    #end
#end class    
