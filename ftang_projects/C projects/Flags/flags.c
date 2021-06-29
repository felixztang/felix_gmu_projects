/*
 * Name: Felix Tang
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "clock.h"
#include "structs.h"
#include "constants.h"
#include "scheduler.h"

/* Initialize the Schedule Struct
 * Follow the specification for this function.
 * Returns a pointer to the new Schedule or NULL on any error.
 */
Schedule *scheduler_init() {
  Schedule* sched=(Schedule*) malloc(sizeof(Schedule));

  if (!sched) return NULL;

  sched->ready_queue= (Queue*) malloc(sizeof(Queue));
  sched->ready_queue->head=NULL;
  sched->ready_queue->count=0;

  if (!sched->ready_queue) return NULL;

  sched->stopped_queue= (Queue*) malloc(sizeof(Queue));
  sched->stopped_queue->head=NULL;
  sched->stopped_queue->count=0;

  if (!sched->stopped_queue) return NULL;

  sched->defunct_queue= (Queue*) malloc(sizeof(Queue));
  sched->defunct_queue->head=NULL;
  sched->defunct_queue->count=0;

  if (!sched->defunct_queue) return NULL;

  return sched;
}

/* Add the process into the appropriate linked list.
 * Follow the specification for this function.
 * Returns a 0 on success or a -1 on any error.
 */
int scheduler_add(Schedule *schedule, Process *process) {

  if (!process || !schedule)  return -1;

  /*UPPER 6*/
  unsigned int proc_state_flags = (process->flags >> EXITCODE_BITS);

	/*CREATED*/
	if (proc_state_flags & CREATED){
    process->flags=0x0;
    process->flags |= (READY << EXITCODE_BITS);
    process->next = schedule->ready_queue->head;
    schedule->ready_queue->head = process;
    schedule->ready_queue->count++;
    return 0;
	}

  /*READY*/
  if (proc_state_flags & READY){

    if (process->time_remaining > 0){
      process->next = schedule->ready_queue->head;
      schedule->ready_queue->head = process;
      schedule->ready_queue->count++;
    }

    else{
      process->flags=0x0;
      process->flags |= (DEFUNCT << EXITCODE_BITS);
      process->next = schedule->defunct_queue->head;
      schedule->defunct_queue->head = process;
      schedule->defunct_queue->count++;
    }
    return 0;
  }

  /*DEFUNCT*/
  if (proc_state_flags & DEFUNCT){
      process->next=schedule->defunct_queue->head;
      schedule->defunct_queue->head=process;
      schedule->defunct_queue->count++;
      return 0;
  }

  return -1;
}

/* HELPER METHOD TO FIND PROCESS WHOSE PID
 * MATCHES WITH PID ARGUMENT
 * TAKES QUEUE* AND PID ARGS TO ITERATE THROUGH
 * RETURNS A 0 ON SUCCESS AND -1 ON ANY FAILURE
 */
int queue_pos(Queue* queue, int pid){
  if (queue==NULL || queue->head==NULL) return -1;
  Process* temp=queue->head;
  int pos=0;
  int ans=-1;
  while (temp!=NULL){
    if (temp->pid==pid){
      ans=pos;
    }
    pos++;
    temp=temp->next;
  }
  return ans;
}

/* HELPER METHOD FOR REMOVING PROCESSES IN QUEUE
 * CONDITIONS:
 * (1) IF POS IS AT THE END OF QUEUE
 * (2) IF POS IS IN THE MIDDLE/HEAD
 */
Process* removeProc(Process** head, int pos){
  if (*head==NULL) return NULL;

  Process* temp = *head;
  Process* ans = NULL;

  if (pos==0){
    *head=temp->next;
    ans=temp;
  }

  else{
    int i=0;
    while (i<pos-1 && temp!=NULL){
      temp=temp->next;
    }

    ans=temp->next;
    Process* nxt=ans->next->next;
    temp->next=nxt;
  }

  if (!ans) return NULL;

  return ans;
}

/* Move the process with matching pid from Ready to Stopped.
 * Change its State to Stopped.
 * Follow the specification for this function.
 * Returns a 0 on success or a -1 on any error.
 */
int scheduler_stop(Schedule *schedule, int pid) {
  /*GET POSITION OF PROCESS WITH MATCHING PID*/
  int pos=-1;
  /*SIGNIFIES IF PROCESS WAS SUCCESSUF*/
  int ans=-1;

  if (!schedule || !schedule->ready_queue->head) return -1;

  pos=queue_pos(schedule->ready_queue,pid);
  if (pos==-1) return -1;

  if (pos!=-1){
    /*REMOVE FROM READY_QUEUE*/
    Process* temp = removeProc(&schedule->ready_queue->head,pos);
    if (temp!=NULL) schedule->ready_queue->count--;

    /*ADD TO STOPPED_QUEUE*/
    if (temp!=NULL){
      temp->next=schedule->stopped_queue->head;
      unsigned int proc_state_flags = (temp->flags >> EXITCODE_BITS);
      if (proc_state_flags | SUDO)  proc_state_flags = STOPPED | SUDO;
      else proc_state_flags = READY;
      temp->flags = (proc_state_flags << EXITCODE_BITS);
      schedule->stopped_queue->head=temp;
      schedule->stopped_queue->count++;

      ans=0;
    }
  }
  return ans;
}

/* Move the process with matching pid from Stopped to Ready.
 * Change its State to Ready.
 * Follow the specification for this function.
 * Returns a 0 on success or a -1 on any error.
 */
int scheduler_continue(Schedule *schedule, int pid) {
  int pos=-1;
  int ans=-1;
  if (!schedule) return -1;

  pos=queue_pos(schedule->stopped_queue,pid);
  if (pos==-1) return -1;

  if (pos!=-1){
    /*REMOVE FROM STOPPED_QUEUE*/
    Process* temp = removeProc(&schedule->stopped_queue->head,pos);
    if (temp!=NULL) schedule->stopped_queue->count--;
    /*ADD TO READY_QUEUE*/
    if (temp!=NULL){
      temp->next=schedule->ready_queue->head;
      unsigned int proc_state_flags = (temp->flags >> EXITCODE_BITS);

      if (proc_state_flags | SUDO)  proc_state_flags = SUDO | READY;
      else proc_state_flags = READY;

      temp->flags = proc_state_flags << EXITCODE_BITS;
      schedule->ready_queue->head=temp;
      schedule->ready_queue->count++;
      ans=0;
    }
  }
  return ans;
}

/* Remove the process with matching pid from Defunct.
 * Follow the specification for this function.
 * Returns its exit code (from flags) on success or a -1 on any error.
 */
int scheduler_reap(Schedule *schedule, int pid) {
  if (!schedule || !schedule->defunct_queue || !schedule->defunct_queue->head) return -1;

  int ans=-1;
  int pos=queue_pos(schedule->defunct_queue,pid);
  if (pos==-1) return -1;

  if (pos!=-1){
      Process* prc = removeProc(&schedule->defunct_queue->head,pos);
      ans=(prc->flags << 6);
  }

  return ans;
}

/* Create a new Process with the given information.
 * - Malloc and copy the command string, don't just assign it!
 * Set the CREATED state flag.
 * If is_sudo, also set the SUDO flag.
 * Follow the specification for this function.
 * Returns the Process on success or a NULL on any error.
 */
Process *scheduler_generate(char *command, int pid, int base_priority,
                            int time_remaining, int is_sudo) {
  Process* proc=(Process*) malloc(sizeof(Process));
  proc->command = (char*) malloc(sizeof(char)*20);
  if (proc==NULL || proc->command==NULL){
	return NULL;
  }
  strcpy(proc->command,command);
  proc->pid=pid;
  proc->base_priority=base_priority;
  proc->cur_priority=base_priority;
  proc->time_remaining=time_remaining;
  if (is_sudo==1){
	  proc->flags=(CREATED<<EXITCODE_BITS) | (SUDO<<EXITCODE_BITS);
  }
  else{
	  proc->flags=CREATED<<EXITCODE_BITS;
  }
  proc->next=NULL;
  return proc;
}

/*HELPER FUNCTION THAT RETURNS POSITION OF PROCESS W/ LOWEST CUR_PRIORITY IN QUEUE*/
int cur_priority_find(Queue *queue){
  if (queue==NULL || queue->head==NULL) return -1;
  Process* temp=queue->head;
  int pos=0;
  int ans=0;
  int curs=temp->cur_priority;
  while (temp!=NULL){
    if (temp->cur_priority<curs){
      curs=temp->cur_priority;
      ans=pos;
    }
    temp=temp->next;
    pos++;
  }
  return ans;
}

/* HELPER FUNCTION TO SUBTRACT ONE FROM EACH PROCESS' CUR_PRIORITY
 * AS LONG AS IT IS >0
 */
void subtract_one(Queue* queue){
  if (!queue) exit(1);
  Process* temp=queue->head;
  while (temp!=NULL){
    if (temp->cur_priority>0){
      temp->cur_priority--;
    }
    temp=temp->next;
  }
}

/* Select the next process to run from Ready Queue.
 * Follow the specification for this function.
 * Returns the process selected or NULL if none available or on any errors.
 */
Process *scheduler_select(Schedule *schedule) {

  if (!schedule) return NULL;
  int pos = cur_priority_find(schedule->ready_queue);
  Process* temp = (Process*) malloc(sizeof(Process));
  temp=NULL;
  if (pos==-1) return NULL;
  if (pos==0 && pos>schedule->ready_queue->count){
    temp=schedule->ready_queue->head;
    Process* temp2=schedule->ready_queue->head->next;
    free(schedule->ready_queue->head);
    schedule->ready_queue->head = temp2;
    schedule->ready_queue->count--;
  }
  else if(pos==schedule->ready_queue->count-1){
    temp=schedule->ready_queue->head;
    schedule->ready_queue->head=NULL;
    schedule->ready_queue->count--;
  }
  else{
    temp = removeProc(&schedule->ready_queue->head,pos);
    if (temp!=NULL) schedule->ready_queue->count--;
  }
  temp->cur_priority = temp->base_priority;
  if (schedule->ready_queue->head!=NULL){
    subtract_one(schedule->ready_queue);
  }
  return temp;
}

/* Returns the number of items in a given Linked List (Queue) (Queue)
 * Follow the specification for this function.
 * Returns the count of the Linked List, or -1 on any errors.
 */
int scheduler_count(Queue *ll) {
  if (ll->count<0){
	  return -1;
  }
  else{
  	return ll->count;
  }
}

void list_free(Process* head){
  Process* temp;
  while (head!=NULL){
    temp=head;
    head=head->next;
    free(temp->command);
    free(temp);
   }
  return;
}

/* Completely frees all allocated memory in the scheduler
 * Follow the specification for this function.
 */
void scheduler_free(Schedule *scheduler) {
  //(1) free Processes in each queue
  //(2) free Schedule
  list_free(scheduler->ready_queue->head);
  free(scheduler->ready_queue);
  scheduler->ready_queue=NULL;

  list_free(scheduler->stopped_queue->head);
  free(scheduler->stopped_queue);
  scheduler->stopped_queue=NULL;

  list_free(scheduler->defunct_queue->head);
  free(scheduler->defunct_queue);
  scheduler->defunct_queue=NULL;

  free(scheduler);
  return;
}
