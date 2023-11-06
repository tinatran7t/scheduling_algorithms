/*    Date         Author                    Project 3         Description                                     */
/* ------------------------------------------------------------------------------------------------------------*/
/*    12/02/2022   Tina Tran(txt200023)      FB                This class will create the feedback algorithm   */
/*                                                                                                             */
/*                                                                                                             */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FB extends scheduler {
    private int quantum = 1, queues = 3;
    private Queue<jobs> queue1 = new LinkedList<>(), queue2 = new LinkedList<>(),  queue3 = new LinkedList<>();

    //check all queues to see if they are empty
    private boolean checkIfQueuesAreEmpty() {
        return (queue1.isEmpty() && queue2.isEmpty() && queue3.isEmpty());
    }

    //next job in feedback scheduling algorithm, add to queues if the current ready batch is empty
    private void nextJob(ArrayList<jobs> temp, int t, jobs prevJob) {
        ArrayList<jobs> batchReady = new ArrayList<>();
        for(jobs j: temp) {
            if(j.getArrivalTime() == t-1) {
                batchReady.add(new jobs(j));
            }
        }
        if(batchReady.isEmpty()) {
            if(t > 2) {
                queue2.add(new jobs(prevJob));
            }
            else {
                queue1.add(new jobs(prevJob));
            }
        }
        else {
            queue2.add(new jobs(prevJob));
        }
    }

    //adjusted current job batch
    private void currentBatch(ArrayList<jobs> temp, int t) {
        int i, track = 0;
        ArrayList<jobs> currentReadyBatch = new ArrayList<>();
        for(jobs j: temp) {
            if(j.getArrivalTime() <= t) {
                currentReadyBatch.add(new jobs(j));
                track++;
            }
        }
        for (jobs j: currentReadyBatch) {
            queue1.add(new jobs(j));
            i = currentJobPosition(temp,j);
            track++;
            temp.remove(i);
        }
    }

    //adjust the ready batch to run
    private int adjustReadyBatch(ArrayList<jobs> temp, Queue<jobs> batchReady, int clockInterrupt) {
        int t = clockInterrupt, cnt = 0, i;
        for(jobs currentJob: temp) {
            if(currentJob.getArrivalTime() == t) {
                batchReady.add(new jobs(currentJob));
            }
        }
        for(jobs j: batchReady) {
            if(checkIfCurrentJobInList(temp, j)) {
                i = currentJobPosition(temp, j);
                temp.remove(i);
                cnt++;
            }
        }
        t++;
        return t;
    }

    //start the batch to run all the jobs and set them in the right queues
    private int startBatch(ArrayList<jobs> tempList, int tempTime, String[][] graph) {
        int t = tempTime, i = 0, j = 0, k = 0;
        jobs batch;
        currentBatch(tempList,tempTime);
        if(!checkIfQueuesAreEmpty()) {
            if(!queue1.isEmpty()) {
                i++;
                batch = new jobs(queue1.remove());
                t = batch.timeSlicing(quantum, tempTime, graph);
                if(!checkIfJobFinished(batch)) {
                    nextJob(tempList,t,batch);
                }
            }
            else if(!queue2.isEmpty()) {
                j++;
                batch = new jobs(queue2.remove());
                t = batch.timeSlicing(quantum, tempTime, graph);
                if(!checkIfJobFinished(batch)) {
                    queue3.add(new jobs(batch));
                }
            }
            else if(!queue3.isEmpty()) {
                k++;
                batch = new jobs(queue3.remove());
                t = batch.timeSlicing(quantum, tempTime, graph);
                if(!checkIfJobFinished(batch))
                {
                    queue3.add(new jobs(batch));
                }
            }
        }
        currentBatch(tempList,t);
        return t;
    }

    //Feedback selected scheduling algorithm
    public void selectedScheduleAlg(ArrayList<jobs> jobList) {
        //variables
        int serviceTime, clockInterrupt = 0;
        //set
        generateRowPosition(jobList);
        ArrayList<jobs> tempList = dupe(jobList);
        String[][] graph = graphMatrixTable(tempList);
        serviceTime = retrieveServiceTime(tempList);
        clockInterrupt = adjustReadyBatch(tempList, queue1, clockInterrupt);
        //for each job send to a temp list and adjust the arrival time
        for(jobs j: tempList) {
            j.adjustArrivalTime(1);
        }
        while (clockInterrupt <= serviceTime) {
            clockInterrupt = startBatch(tempList, clockInterrupt, graph);
        }
        //print out the graph matrix for the feedback algorithm
        System.out.println("-> Feedback (quantum = " + quantum + ", queue(s) = " + queues + "):");
        printGraphMatrix(graph);
    }
}