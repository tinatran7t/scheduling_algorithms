/*    Date         Author                    Project 3         Description                                     */
/* ------------------------------------------------------------------------------------------------------------*/
/*    12/02/2022   Tina Tran(txt200023)      SRT               This class will create the shortest remaining   */
/*                                                             time algorithm                                  */
/*                                                                                                             */
import java.util.ArrayList;
import java.util.Collections;

public class SRT extends scheduler
{
    private int quantum = 1;

    //check if there is a job present
    private boolean checkIfJobIsPresent(ArrayList<jobs> readyJobs, jobs currentJob) {
        for(jobs j: readyJobs) {
            if(j.getJobLetter().equals(currentJob.getJobLetter())) {
                return true;
            }
        }
        return false;
    }

    //find the job with the shortest remaining time
    private jobs findShortestJob(ArrayList<jobs> tempList, ArrayList<jobs> readyJobs, int t) {
        int holdTime = t - 1, shortestTime = 10000, remainingTime, i;
        jobs selected = null;
        for(jobs currentJob: tempList) {
            if(currentJob.getArrivalTime() == holdTime) {
                if(!checkIfJobIsPresent(readyJobs, currentJob)) {
                    readyJobs.add(new jobs(currentJob));
                }
            }
        }
        for(jobs j: readyJobs) {
            remainingTime = j.getServiceTime() - j.getProcessedTime();
            if(j.getArrivalTime() == 2){
                remainingTime--;
            }
            if(remainingTime <= shortestTime) {
                shortestTime = remainingTime;
                selected = new jobs(j);
            }
        }
        i = currentJobPosition(readyJobs, selected);
        return readyJobs.get(i);
    }

    //start batch to run all the jobs
    private int startBatch(ArrayList<jobs> tempList, ArrayList<jobs> readyBatch, int tempTime, String[][] graph) {
        int t = tempTime, i, hold, num = 0;
        jobs batch;
        if(!readyBatch.isEmpty()) {
            batch = findShortestJob(tempList, readyBatch, tempTime);
            t = batch.timeSlicing(quantum, tempTime, graph);
            if(checkIfJobFinished(batch)) {
                hold = currentJobPosition(readyBatch, batch);
                readyBatch.remove(hold);
                i = currentJobPosition(tempList, batch);
                tempList.remove(i);
                num++;

            }
        }
        return t;

    }

    //Shortest Remaining Time selected scheduling algorithm
    public void selectedScheduleAlg(ArrayList<jobs> jobList) {
        //variables
        int serviceTime, clockInterrupt = 1;
        //set
        generateRowPosition(jobList);
        ArrayList<jobs> tempList = dupe(jobList);
        String[][] graph = graphMatrixTable(tempList);
        serviceTime = retrieveServiceTime(tempList);
        ArrayList<jobs> readyBatch = new ArrayList<>();
        readyBatch = firstToArriveList(tempList, readyBatch);
        //while the clock interrupt is less than or equal to the total service time start the batch
        while (clockInterrupt <= serviceTime) {
            clockInterrupt = startBatch(tempList, readyBatch, clockInterrupt, graph);
        }
        //print out the graph matrix for the shortest remaining time algorithm
        System.out.println("-> Shortest Remaining Time (quantum = " + quantum + "):");
        printGraphMatrix(graph);
    }
}