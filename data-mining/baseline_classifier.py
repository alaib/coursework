import sys
import re
import classifier_helper, html_helper

reload(sys)
sys.setdefaultencoding = 'utf-8'

#start class
class BaselineClassifier:
    """ Classifier using baseline method """
    #variables    
    #start __init__
    def __init__(self, data, keyword):
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
        self.results = {}
        self.neut_count = 0
        self.pos_count = 0
        self.neg_count = 0
        self.keyword = keyword
        self.html = html_helper.HTMLHelper()
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
    
    #start writeOutput
    def writeOutput(self, file, writeOption='w'):
        fp = open(file, writeOption)
        for i in self.results:
            item = self.results[i]
            text = item['text'].strip()
            label = item['label']
            writeStr = text+" | "+label+"\n"
            fp.write(writeStr)
        #end for loop            
    #end writeOutput
    
    #start printStats
    def getHTML(self):
        return self.html.getResultHTML(self.keyword, self.results, self.pos_count, \
                                       self.neg_count, self.neut_count, 'baseline')
    #end
#end class    
