#include<stdio.h>

int main(){
    char s[8][8];
    int i,j;

    for(i = 0; i < 8; i++){
        for(j = 0; j < 8; j++){
            if(i == j && (i != 7 && j != 7)){
                s[i][j] = 'L';
            }else{
                s[i][j] = 'W';
            }
        }
    }

    for(i = 0; i < 8; i++){
        for(j = 0; j < 8; j++){
            printf("%c ", s[i][j]);
        }
        printf("\n");
    }
}
