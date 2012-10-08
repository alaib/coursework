#include<iostream>
#include<stdio.h>
#include<stdlib.h>

using namespace std;

void printArr(int *a, int n){
    for(int i = 0; i < n; i++){
        cout<<a[i]<<"\t";
    }
    cout<<endl;
}

int sumArr(int *a, int n){
    int s = 0;
    for(int i = 0; i < n; i++){
        s += a[i];
    }
    return s;
}

int betterChange(int* c, int M, int n, int *res){
    int r = M;
    int minCoins = 0;
    for(int i = 0; i < n; i++){
        res[i] = r / c[i];
        r = r - c[i] * res[i];
        minCoins += res[i];
    }
    return minCoins;
}

int* recursiveChange(int *c, int M, int n, int *best){
    int *res = new int[n];
    if(M == 0){
        return res;
    }
    int minCoins = M+1;
    int thisChange;
    for(int i = 0; i < n; i++){
        if(M >= c[i]){
            res = recursiveChange(c, M - c[i], n, best);
            res[i] += 1;
            if(sumArr(res, n) < minCoins){
                best = res;
                minCoins = sumArr(res, n);
            }

        }
    }
    return best;
}


void resetArr(int *a, int n){
    for(int i =0;i < n; i++){
        a[i] = 0;
    }
}

int main(){
    int c[] = {25, 20, 10, 5, 1};
    int n = 5;
    int *res = new int[5];
    resetArr(res, n);
    int M = 99;
    //Greedy
    int minCoins = betterChange(c, M, n, res);
    cout<<"Greedy"<<endl;
    cout<<"=========="<<endl;
    cout<<"M = "<<M<<endl;
    cout<<"Min Coins = "<<minCoins<<endl;
    printArr(c, n);
    printArr(res, n);

    //Recursive
    res = recursiveChange(c, M, n, res);
    minCoins = sumArr(res, n);
    cout<<"Recursive"<<endl;
    cout<<"=========="<<endl;
    cout<<"M = "<<M<<endl;
    cout<<"Min Coins = "<<minCoins<<endl;
    printArr(c, n);
    printArr(res, n);

    return 0;
}
