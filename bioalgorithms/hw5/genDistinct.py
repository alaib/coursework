def gendistinct(n):
    leafnode = '(.)'
    dp = []
    newset = set()
    newset.add(leafnode)
    dp.append(newset)
    for i in range(1,n):
        newset = set()
        for j in range(i):
            for leftchild in dp[j]:
                for rightchild in dp[i-j-1]:
                    newset.add('(' + '.' + leftchild + rightchild + ')')
        dp.append(newset)
    return dp[-1]

alltrees = gendistinct(3)
for tree in alltrees:
    print tree