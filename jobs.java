/*    Date         Author                    Project 3         Description                                     */
/* ------------------------------------------------------------------------------------------------------------*/
/*    12/02/2022   Tina Tran(txt200023)      jobs class        This class provides method to help with         */
/*                                                             getting arrival times, service times, processed */
/*                                                             time, and job letters for each job. Etc.        */
public class jobs {
    //variables
    private String jobLetter;
    private int arrivalTime, serviceTime, processedTime = 0, row = -1;

    //constructor for jobs
    public jobs(jobs prevJob) {
        jobLetter = prevJob.jobLetter;
        arrivalTime = prevJob.arrivalTime;
        serviceTime = prevJob.serviceTime;
        processedTime = prevJob.processedTime;
        row = prevJob.row;
    }
    public jobs(String jobLetter, int arrivalTime, int executionTime) {
        this.jobLetter = jobLetter;
        this.arrivalTime = arrivalTime;
        this.serviceTime = executionTime;
    }

    //setters and getters
    public void setRow(int r) {
        row = r;
    }
    public String getJobLetter() {
        return jobLetter;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }
    public int getProcessedTime() {
        return processedTime;
    }

    //adjust the arrival time by adding feedback value
    public void adjustArrivalTime(int value) {
        arrivalTime += value;
    }

    //given a quantum, place at the back of the queue
    public int timeSlicing(int quantum, int x, String[][] graphMatrix) {
        int temp = x - 1, count = 0;
        for(int i = 0; i < quantum; i++) {
            if(i < serviceTime) {
                if(processedTime < serviceTime) {
                    processedTime++;
                    if (x < graphMatrix[row].length) {
                        x++;
                        graphMatrix[row][temp] = "X";
                        temp++;
                        count++;
                    }
                }
            }
        }
        return x;
    }
}