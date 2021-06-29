/*Felix Tang
G01035724
Project 3
OrderList source file
 */



#include<stdio.h>
#include<stdlib.h>

#include<ctype.h>
#include "orderList.h"
void *dmalloc(size_t size){

    void *p = malloc(size);
    if(!p){
        printf("memory allocation failed\n");
        exit(1);
    }
    return p;
}


int strcmpi(char *s, char *t){
    while (*s && tolower(*s) == tolower(*t)){
        s++;
        t++;
    }
    return tolower(*s) - tolower(*t);
}

orderList *createItem(){
    orderList *order = (orderList*)dmalloc(sizeof(orderList));
    order->head = NULL;

    order->count = 0;

    return order;

}

int insert(char *str, orderList *s){
    foodNode *node = (foodNode*)dmalloc(sizeof(foodNode));
    foodNode *current;

    node->next = NULL;
    node->data = str;
    //Empty
    if(s->head == NULL ){

        s->head = node;
        s->count++;

    }


    else{

        current = s->head;

        //More than two
        while(current->next != NULL && (strcmpi(current->next->data,str) < 0)){

            current = current->next;


        }
        if(current == s->head && (strcmpi(str,current->data) < 0)){

            node->next = s->head;


            s->head = node;


        }





        else{

             node->next = current->next;
             current->next = node;
             //node->next = NULL;


        }



        s->count++;



    }
    return 0;
}








void printItems(orderList *s){
    int i;
    foodNode *current = s->head;

    while(current != NULL){
        printf("%s",current->data);
        current = current->next;

    }

    /*for(i = 0; i < s->count; i++){
        printf("%d\n",s->count);
        printf("%s\n",current->head->data);
        current->head = current->head->next;*/





}
void makeClean(orderList *s){
    foodNode *temp;

      while(s->head != NULL)
    {
        temp = s->head;
        s->head = s->head->next;


        free(temp->data);


        free(temp);
    }

    }



