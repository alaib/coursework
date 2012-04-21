import web

import get_twitter_data
from get_twitter_data import *

import baseline_classifier, naive_bayes_classifier
import json, logging

#start defaultHTML
def defaultHTML(error = 0):
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
            <input type="radio" name="method" value="baseline" checked="true">Baseline Method</input>
            <input type="radio" name="method" value="naivebayes">Naive Bayes method</input>
        </div>
        </form>
'''
    if(error):
        html += '<div id="error"> Unable to fetch TWitter API data. Please try again later.</div>'
    html += '''
    </div>
    <div id='ft'>by Ravikiran Janardhana</div>
    <script type="text/javascript">
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

urls = (
    '/', 'index'
)

class index:
    def GET(self):
        query = web.ctx.get('query')
        if query:
            if(query[0] == '?'):
                query = query[1:]
            arr = query.split('&')
            logging.warning(arr)
            for item in arr:
                if 'keyword' in item:
                    keyword = item.split('=')[1]
                elif 'method' in item:
                    method = item.split('=')[1]
            #end loop                    
            tweets = getData(keyword)            
            if(tweets):
                if(method == 'baseline'):
                    bc = baseline_classifier.BaselineClassifier(tweets, keyword)
                    bc.classify()
                    return bc.getHTML()
                elif(method == 'naivebayes'):
                    trainingDataFile = 'data/full-corpus.csv'
                    classifierDumpFile = 'data/naivebayes_full-corpus.pickle'
                    trainingRequired = 0
                    nb = naive_bayes_classifier.NaiveBayesClassifier(tweets, keyword, \
                                                  trainingDataFile, classifierDumpFile, trainingRequired)
                    nb.classify()
                    return nb.getHTML()
            else:
                return defaultHTML(error=1)                        
        else:
            return defaultHTML()

if __name__ == "__main__":
    app = web.application(urls, globals())
    app.run()
