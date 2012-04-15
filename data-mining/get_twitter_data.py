import urllib
import urllib2
import json

import classifier_helper
from classifier_helper import *

#start getTwitterData
def getData(keyword, pageNo = 1):
    url = 'http://search.twitter.com/search.json'    
    data = {'q': keyword, 'lang': 'en', 'page': pageNo, 'result_type': 'mixed', 'rpp': 50, 'include_entities': 0}
    params = urllib.urlencode(data)
    try:            
        req = urllib2.Request(url, params)
        response = urllib2.urlopen(req)  
        jsonData = json.load(response)
        tweets = []
        for item in jsonData['results']:
            tweet = process_tweet(item['text'])
            tweets.append(tweet)                    
        return tweets
    except urllib2.URLError, e:
        handleError(e)         
#end    

#start handleError
def handleError(e):
    print e.code
    print e.read()
#end

