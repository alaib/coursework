import web

import get_twitter_data
from get_twitter_data import *

import baseline_classifier, naive_bayes_classifier, max_entropy_classifier, libsvm_classifier
import json, logging, html_helper

urls = (
    '/', 'index'
)

class index:
    def GET(self):
        query = web.ctx.get('query')
        html = html_helper.HTMLHelper()
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
                    #trainingDataFile = 'data/training.10000.processed.noemoticon.csv'
                    classifierDumpFile = 'data/naivebayes_full-corpus.pickle'
                    trainingRequired = 0
                    nb = naive_bayes_classifier.NaiveBayesClassifier(tweets, keyword, \
                                                  trainingDataFile, classifierDumpFile, trainingRequired)
                    nb.classify()
                    return nb.getHTML()
                elif(method == 'maxentropy'):
                    trainingDataFile = 'data/full-corpus.csv'                
                    classifierDumpFile = 'data/maxent_full-corpus.pickle'
                    trainingRequired = 0
                    maxent = max_entropy_classifier.MaxEntClassifier(tweets, keyword, \
                                                  trainingDataFile, classifierDumpFile, trainingRequired)
                    maxent.classify()
                    return maxent.getHTML()
                elif(method == 'svm'):
                    trainingDataFile = 'data/full-corpus.csv'                
                    #trainingDataFile = 'data/training.10000.processed.noemoticon.csv'
                    classifierDumpFile = 'data/svm_full-corpus.pickle'
                    trainingRequired = 1
                    sc = libsvm_classifier.SVMClassifier(tweets, keyword, \
                                                  trainingDataFile, classifierDumpFile, trainingRequired)
                    sc.classify()
                    return sc.getHTML()
            else:
                return html.getDefaultHTML(error=1)                        
        else:
            return html.getDefaultHTML()

if __name__ == "__main__":
    app = web.application(urls, globals())
    app.run()
