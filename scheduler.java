/*    Date         Author                    Project 3         Description                                     */
/* ------------------------------------------------------------------------------------------------------------*/
/*    12/02/2022   Tina Tran(txt200023)      scheduler class   This class provides method to help with         */
/*                                                             scheduling each algorithm                       */
/*                                                                                                             */
import java.util.*;
import java.util.ArrayList;

public abstract class scheduler {

    //the selected scheduling algorithm (abstract class for each to list)
    public abstract void selectedScheduleAlg(ArrayList<jobs> jobList);

    //add all arrival times that start at 0 to a list
    public ArrayList<jobs> firstToArriveList(ArrayList<jobs> tempList, ArrayList<jobs> waitingBatch) {
        //for each job in the list
        for(jobs j: tempList) {
            if(j.getArrivalTime() == 0) {
                waitingBatch.add(new jobs(j));
            }
        }
        return waitingBatch;
    }

    //add all jobs in arrival list to the ready queue and return the current runtime
    public int addFirstToArriveList(ArrayList<jobs> temp, int runtime, Queue<jobs> readyQueue) {
        ArrayList<jobs> aList = createWaitingBatchForClock(temp,runtime);
        runtime++;
        for(jobs j: aList) {
            readyQueue.add(j);
            int hold = currentJobPosition(temp,j);
            temp.remove(hold);
        }
        return runtime;
    }

    //get the current job position and return that position
    public int currentJobPosition(ArrayList<jobs> jobsList , jobs prevJob) {
        int position = 0, count = 0;
        for(int i = 0; i < jobsList.size(); i++) {
            if(jobsList.get(i).getJobLetter().equals(prevJob.getJobLetter())) {
                position = i;
                count++;
                //System.out.println(count);
            }
        }
        return position;
    }

    //duplicate previous list to next list
    public ArrayList<jobs> dupe(ArrayList<jobs> prevList) {
        ArrayList<jobs> nextList = new ArrayList<>();
        for(jobs j: prevList) {
            nextList.add(new jobs(j));
        }
        return nextList;
    }

    //check if the current job is finished
    public boolean checkIfJobFinished(jobs currentJob) {
        return (currentJob.getProcessedTime() == currentJob.getServiceTime()) ? true : false;
    }

    //check if the current job is in the job list
    public boolean checkIfCurrentJobInList(ArrayList<jobs> jobList, jobs currentJob) {
        boolean inList = false;
        for(jobs j: jobList) {
            if(j.getJobLetter().equals(currentJob.getJobLetter())) {
                inList = true;
            }
        }
        return inList;
    }

    //check if the jobs have the same value
    public static boolean checkTheSame(jobs[] job)  {
        for (int i = 0; i < job.length - 1; i++)  {
            if (job[i] != job[i + 1])  {
                return false;
            }
        }
        return true;
    }

    //find the latest arrival time and generate the row position for the graph
    public void generateRowPosition(ArrayList<jobs> jobList) {
        int latestArrivalTime = 0, row = 0, count = 0;
        Collections.sort(jobList, new compareJobs());
        for(jobs j: jobList) {
            if(j.getArrivalTime() >= latestArrivalTime) {
                latestArrivalTime = j.getArrivalTime();
                j.setRow(row);
                count++;
                row++;
            }
        }
    }

    //for each current job in the list that is less than or equal to clock interrupt add to waiting batch 
    public ArrayList<jobs> createWaitingBatchForClock(ArrayList<jobs> tempList, int clockInterrupt) {
        ArrayList<jobs> waitingBatch = new ArrayList<>();
        for(jobs currentJob : tempList) {
            if(currentJob.getArrivalTime() <= clockInterrupt) {
                waitingBatch.add(new jobs(currentJob));
            }
        }
        return waitingBatch;
    }

    //retrieve the service time of jobs in the list
    public int retrieveServiceTime(ArrayList<jobs> jobList) {
        int serviceTime = 0;
        for(jobs j: jobList) {
            serviceTime += j.getServiceTime();
        }
        return serviceTime;
    }

    //matrix table for job positions
    public String[][] graphMatrixTable(ArrayList<jobs> jobList) {
        int row = jobList.size(), column = retrieveServiceTime(jobList) + 1, countRow = 0, countColumn = 0;
        String[][] matrixPositions = new String[row][column];
        for(int r = 0; r < matrixPositions.length; r++) {
            countRow++;
            for(int c = 0; c < matrixPositions[r].length; c++) {
                matrixPositions[r][c] = " ";
                countColumn++;
            }
        }
        return matrixPositions;
    }

    //print out the graph matrix to show algorithm
    public void printGraphMatrix(String[][] graph) {
        for(int r = 0; r < graph.length; r++) {
            switch (r){
                case 0:
                    System.out.print("A: ");
                    break;
                case 1:
                    System.out.print("B: ");
                    break;
                case 2:
                    System.out.print("C: ");
                    break;
                case 3:
                    System.out.print("D: ");
                    break;
                case 4:
                    System.out.print("E: ");
                    break;
                default:
                    System.out.println("incorrect text file (incorrect amount of rows)");
                    break;
            }
            for(int c = 0; c < graph[r].length; c++) {
                System.out.print(graph[r][c]);
            }
            System.out.println();
        }
    }
}