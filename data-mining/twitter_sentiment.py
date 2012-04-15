import get_twitter_data
from get_twitter_data import *

import pprint

import baseline_classifier

import json

tweets = getData('reebok')
tweets.append('Amazing shoes by reebook')
bc = baseline_classifier.classifier(tweets)
bc.classify()
pp = pprint.PrettyPrinter(indent=4)
pp.pprint(bc.results)
