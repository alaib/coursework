import get_twitter_data
from get_twitter_data import *

import baseline_classifier
import json

keyword = 'nike'
tweets = getData(keyword)     
bc = baseline_classifier.classifier(tweets)
bc.classify()
val = bc.getHTML()
