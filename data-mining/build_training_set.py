import classifier_helper
from classifier_helper import *
  
inpfile = open("training.1600000.processed.noemoticon.csv", "r")
line = inpfile.readline()
maxCount = 100
count = 0
tweets = []
while line:    
    count += 1
    if count > maxCount:
        break    
    splitArr = line.split(',"')
    unprocessed_tweet = splitArr[5]
    tweet = process_tweet(unprocessed_tweet)
    tweets.append(tweet)    
    line = inpfile.readline()
#end while loop