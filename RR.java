/*    Date         Author                    Project 3         Description                                     */
/* ------------------------------------------------------------------------------------------------------------*/
/*    12/02/2022   Tina Tran(txt200023)      RR                This class will create the round-robin algorithm*/
/*                                                                                                             */
/*                                                                                                             */
import java.util.*;

public class RR extends scheduler {
    private int quantum = 1, serviceTime;
    private Queue<jobs> readyBatch = new LinkedList<>();

    //interrupt when a job comes in
    private void jobInterrupt(ArrayList<jobs> temp, int t) {
        int i, list = 0, go = 0;
        ArrayList<jobs> interruptWaitBatch = new ArrayList<>();
        for(jobs currentJob: temp) {
            if(currentJob.getArrivalTime() <= t) {
                interruptWaitBatch.add(new jobs(currentJob));
            }
        }
        for(jobs j: interruptWaitBatch) {
            i = currentJobPosition(temp,j);
            temp.remove(i);
            list++;
        }
        for(jobs next: interruptWaitBatch) {
            readyBatch.add(new jobs(next));
            go++;
        }
    }

    //start batch to run all the jobs and set them in the ready batch
    private int startBatch(ArrayList<jobs> tempList, int tempTime, String[][] graph) {
        int t = tempTime, i;
        jobs batch;
        ArrayList<jobs> batchList = createWaitingBatchForClock(tempList, tempTime);
        for(jobs j: batchList) {
            readyBatch.add(j);
            i = currentJobPosition(tempList, j);
            tempList.remove(i);
        }
        try {
            if(!readyBatch.isEmpty()) {
                batch = readyBatch.remove();
                t = batch.timeSlicing(quantum, tempTime, graph);
                if (!(checkIfJobFinished(batch))) {
                    if(quantum > 1) {
                        jobInterrupt(tempList,(t - 1));
                    }
                    readyBatch.add(new jobs(batch));
                }
            }
        }
        catch (NoSuchElementException e) {
            System.out.println("Queue is currently empty");
        }
        return t;
    }

    //Round-Robin selected scheduling algorithm
    public void selectedScheduleAlg(ArrayList<jobs> jobList) {
        //variables
        int clockInterrupt = 0;
        //set
        generateRowPosition(jobList);
        ArrayList<jobs> tempList = dupe(jobList);
        String[][] graph = graphMatrixTable(tempList);
        serviceTime = retrieveServiceTime(tempList);
        clockInterrupt = addFirstToArriveList(tempList, clockInterrupt, readyBatch);
        //while the clock interrupt is less than or equal to the total service time start the batch
        while (clockInterrupt <= serviceTime) {
            clockInterrupt = startBatch(tempList, clockInterrupt, graph);
        }
        //print out the graph matrix for the round-robin algorithm
        System.out.println("-> Round-Robin (quantum = " + quantum + "):");
        printGraphMatrix(graph);
    }
}
