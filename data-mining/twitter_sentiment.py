import web

import get_twitter_data
from get_twitter_data import *

import baseline_classifier
import json

#start defaultHTML
def defaultHTML():
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
    </div>
    <div id='ft'>by Ravikiran Janardhana</div>
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
            for item in arr:
                if 'keyword' in item:
                    keyword = item.split('=')[1]
                    break
            #end loop                    
            tweets = getData(keyword)        
            bc = baseline_classifier.classifier(tweets)
            bc.classify()
            return bc.getHTML()
        else:               
            return defaultHTML()

if __name__ == "__main__":
    app = web.application(urls, globals())
    app.run()