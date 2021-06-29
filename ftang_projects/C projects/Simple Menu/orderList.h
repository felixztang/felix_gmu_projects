/*Felix Tang
G01035724
Project 3
OrderList header file
 */


#ifndef FOOD_ORDER
#define FOOD_ORDER

typedef struct _foodNode
{
    char *data;
    struct _foodNode *next;

}foodNode;

typedef struct _orderList
{

    foodNode *head; // Pointer to first food
    int count;

}orderList;

orderList *createItem();
int insert(char *str, orderList *s);
void printItems(orderList *s);
void makeClean(orderList *s);
#endif
