/*Felix Tang
G01035724
Project 3
Lab Section 204 */


#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include "orderList.h"


typedef struct _robotOrder
{
    unsigned char robotNum;
    orderList *data;
    char *deliverTo;
    char *restaurant;
    struct _robotOrder *next;
} robotOrder;



/*void robotInsert(robotOrder *robot, robotOrder **head){
    robotOrder *current = *head;
    robotOrder *bot = dmalloc(sizeof(robotOrder));
    bot = robot;

    if(current == NULL){
        printf("WWWWWWW23213123");
        *head = bot;
    }

    /*else if(current->next == NULL)
			{
			    printf("WWWWWWW2");

				(*head)->next = robot;
			}
    else
        {
            while(current->next != NULL)
				{
				    printf("WWWWWWW1");
					current = current->next;
				}
                    //robot->next = current->next;
                    current->next = bot;
                    printf("WWWWWW3332W");
			}

}*/
int main(){

    char buffer[25];

    char userInput;



    robotOrder *robotHead;
    //robotHead = dmalloc(sizeof(robotOrder));


    robotHead = NULL;
    //robotHead->next = NULL;

    int loop;
    char *foodName;

    int robotNumber;
    char numBuff[10];

    char address[200];
    char foodBuff[200];

    char restBuff[200];

    char foodItem[200];




loop = 0;
while(loop == 0){
    robotOrder *robot = dmalloc(sizeof(robotOrder));
    robot->robotNum = NULL;
    robot->data = NULL;
    robot->deliverTo = NULL;
    robot->restaurant = NULL;
    robot->next = NULL;
    printf("New delivery order? (y/n)  \n");

	fgets(buffer, 25, stdin);
    fflush(stdin);
	//sscanf(buffer," %c",&userInput);
	switch(buffer[0]){
    case 'y':
        {
            printf("\nTask Robot Number: ");
            fgets(numBuff, 10, stdin);
            fflush(stdin);
            //printf("%s\n",numBuff);
            robotNumber = atoi(numBuff);
            robot->robotNum = robotNumber;
            //printf("%d\n",robot->robotNum);
            //strcpy(robot->robotNum,strtok(numBuff,"\n"));
            //head->robotNum = (unsigned char) robotNum;



            printf("Delivery Address for new delivery order: ");
            fgets(address,200,stdin);
            fflush(stdin);
            robot->deliverTo = dmalloc(sizeof(address));
            //head->deliverTo = dmalloc(sizeof(address));
            strcpy(robot->deliverTo,strtok(address,"\n"));
            //printf("%s",robot->deliverTo);
            //strcpy(head->deliverTo,strtok(address,"\n"));

            printf("Restaurant from which to pick up food: ");

            fgets(restBuff,200,stdin);
            fflush(stdin);
            robot->restaurant = dmalloc(sizeof(restBuff));
            //head->restaurant = dmalloc(sizeof(restBuff));
            strcpy(robot->restaurant,strtok(restBuff,"\n"));
            //strcpy(head->restaurant,strtok(restBuff,"\n"));





            int check;
            check = 0;
            orderList* foodList = (orderList*)createItem();

            while(check ==0){

                //foodItem[0] = '\0';
                printf("Food Item:");
                memset(foodItem,'\0',sizeof(foodItem));
                fgets(foodItem,200,stdin);


                foodName = (char*)dmalloc((strlen(foodItem)*sizeof(char))+2);

                strcpy(foodName, foodItem);

                fflush(stdin);




                //printItems(foodList);
                if(foodItem[0] == '\n'){
                        //printf("DDDDDDDDDD\n");
                    check +=1;
                    break;
                }
                else if(foodItem[0] != '\n'){
                    //printf("DDDDDDDDDD\n");
                    insert(foodName,foodList);
                    //free(foodName);



                }
                else{
                    check = 1;
                }

                robot->data = foodList;

            }







            robotOrder *current = robotHead;
            robot->next = NULL;
            if(current == NULL){
                 free(robotHead);
                 robotHead = robot;
               //  printf("First\n");
                 //printItems(robotHead->data);
            }
            else{
                while(current->next != NULL){
                    current = current->next;
                    //printItems(current->data);
                    // printf("Second\n");
                }
                    current->next = robot;
                     //printf("Last\n");
                     //printItems(current->data);

                }



            loop = 0;
            break;

            }


            /*if(current == NULL){
                //printf("WWWWWWW23213123");
                head = node;


            }


            else
            {
                while(current->next != NULL)
				{
				    printf("Third\n");
					current = current->next;
					printItems(current->data);
					//node->next = NULL;

				}
				 if(current == head)
                    {
                    printf("Second2\n");

                    current->next = node;
                    robot->next = NULL;
                //printItems(current->next->data);

                    }
                    else{
                    //robot->next = current->next;
                    node->next = current->next;
                    current->next = node;

                    //node->next = NULL;
                    //robot->next = NULL;
                    //printItems(current->next->data);

                    printf("ElseW\n");
                    }

			}

            //robotInsert(robot,&head);

            loop = 0;
            break;*/





    case 'n':
        {

            printf("List of Deliveries:\n");
            robotOrder* current = (robotOrder*)robotHead;
            printf("\n");


            int converter =0;
            while(current != NULL){
                converter = (int)current->robotNum;
                printf("Robot %d: Delivery order from %s has %d item(s)\n",converter,current->restaurant,current->data->count);

				printItems(current->data);
				printf("Deliver to:%s\n",current->deliverTo);
				current = current->next;

				//robot->next = NULL;


            }
            robotOrder* temp = robotHead;
            orderList *temp2;

            while(temp->next != NULL){
                    robotOrder *hold = temp->next;
                    free(temp->restaurant);
                    free(temp->deliverTo);
                    makeClean(temp->data);
                    free(temp->data);

                    free(temp);
                    temp = hold;

            }


            free(temp->restaurant);
            free(temp->deliverTo);
            free(temp->data);
            free(temp);






            exit(0);


            break;
        }
    default:

        {
            printf("Invalid Input. Try Again\n");
            break;
        }

        }
}

}

