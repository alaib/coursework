#start class
class classifier:
    """ Classifier using baseline method """
    #variables    
    #start __init__
    def __init__(self, data):
        self.tweets = data        
        self.results = {}
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
            neg_words = [word for word in negative_words if word in t]
            pos_words = [word for word in positive_words if word in t]
            if(len(pos_words) > len(neg_words)):
                label = 'positive'
            elif(len(pos_words) < len(neg_words)):
                label = 'negative'
            else:
                label = 'neutral'
            res = {'text': t, 'label': label, 'neg_words': neg_words, 'pos_words': pos_words}
            self.results[count] = res
            count += 1            
        #end for loop                
    #end
#end class    
        
    
    