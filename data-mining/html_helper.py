class HTMLHelper:
    #start __init__
    def __init__(self):
        self.html = ''
    #end
        
    #start getDefaultHTML
    def getDefaultHTML(self, error = 0):
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
            <input type="radio" name="method" id="baseline" value="baseline" checked="true">Baseline</input>
            <input type="radio" name="method" id="naivebayes" value="naivebayes">Naive Bayes</input>
            <input type="radio" name="method" id="maxentropy" value="maxentropy">Maximum Entropy</input>
            <input type="radio" name="method" id="svm" value="svm">Support Vector Machine</input>
        </div>
        </form>
'''
        if(error == 1):
            html += '<div id="error">Unable to fetch TWitter API data. Please try again later.</div>'
        elif(error == 2):
            html += '<div id="error">Unrecognized Method of Classfication, please choose one from above.</div>'
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
    
    #start getResultHTML
    def getResultHTML(self, keyword, results, pos_count, neg_count, neut_count, checked):
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
            <input type="radio" name="method" id="baseline" value="baseline">Baseline</input>
            <input type="radio" name="method" id ="naivebayes" value="naivebayes">Naive Bayes</input>
            <input type="radio" name="method" id="maxentropy" value="maxentropy">Maximum Entropy</input>
            <input type="radio" name="method" id="svm" value="svm">Support Vector Machine</input>
        </div>
        </form>       
        <div id="results">            
'''                
        html += '<div id="stats"><p>Keyword = "' + keyword + '", '
        html += 'Positive = ' + str(pos_count) + ", Negative = " + str(neg_count)
        html += ", Neutral = "+ str(neut_count) + "</p></div>"
        html += '<div id="result-chart"></div>'
        html += '<div id="content">'
        left = '<div id="left"><h3>Positive</h3><ul>'
        right = '<div id="right"><h3>Negative</h3><ul>'
        middle = '<div id="middle"><h3>Neutral</h3><ul>'
        for i in results:
            item = results[i]
            if(item['label'] == 'positive'):
                left += '<li>' + item['tweet'] + '</li>'
            elif(item['label'] == 'neutral'):
                middle+= '<li>' + item['tweet'] + '</li>'
            elif(item['label'] == 'negative'):
                right += '<li>' + item['tweet'] + '</li>'
        left += '</ul></div>'
        right += '</ul></div>'
        middle += '</ul></div>'
        html += left + middle + right + '</div>'
        
        html += '''        
        </div>
    </div>
    <script src="http://yui.yahooapis.com/3.5.0/build/yui/yui-min.js"></script>
    <script type="text/javascript">
        YUI().use('charts', function (Y) 
        { 
            var myDataValues = [ 
'''
        html += '{category:"Positive", values:'+ str(pos_count) +'},'
        html += '{category:"Negative", values:'+ str(neg_count) +'},'
        html += '{category:"Neutral", values:'+ str(neut_count) +'}'
        html += '''         
            ];          
            var mychart = new Y.Chart({
                                       dataProvider:myDataValues, 
                                       render:"#result-chart", 
                                       type: "column"}
                                      );
        });
        '''
        checked = 'document.getElementById("'+checked+'").checked=true;'
        html += checked
        html += '''        
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
#end class    
