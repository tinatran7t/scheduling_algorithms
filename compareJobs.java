/*    Date         Author                    Project 3         Description                                     */
/* ------------------------------------------------------------------------------------------------------------*/
/*    12/02/2022   Tina Tran(txt200023)      compareJobs       This will compare 2 jobs to check for the latest*/
/*                                                             arrival time                                    */
/*                                                                                                             */
import java.util.Comparator;
public class compareJobs implements Comparator<jobs> {
    @Override
    //compare the arrival times of 2 jobs
    public int compare(jobs firstJob, jobs secondJob) {
        int firstJobArrives = firstJob.getArrivalTime(), secondJobArrives = secondJob.getArrivalTime();
        return (firstJobArrives > secondJobArrives) ? 1 : -1;
    }
}