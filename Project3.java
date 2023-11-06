/*    Date         Author                    Project 3         Description                                     */
/* ------------------------------------------------------------------------------------------------------------*/
/*    12/02/2022   Tina Tran(txt200023)      Project3          This class acts as a driver to run the          */
/*                                                             simulation for user to choose which scheduling  */
/*                                                             algorithm and takes in text file for the jobs   */
import java.io.*;
import java.util.*;

public class Project3 {
    public static void main(String args[]) {
        //variables
        String userInput = args[0];
        ArrayList<jobs> jobList = new ArrayList<>();

        //method to read jobs.txt file and method to run the algorithm that the user chooses
        readFile(jobList);
        userChooses(jobList, userInput);
    }

    //method to read in the values provided by the text file
    public static void readFile(ArrayList<jobs> listOfJobs) {
        String jobsFile;
        try {
            FileReader readInFile = new FileReader("jobs.txt");
            BufferedReader readInBuffer = new BufferedReader(readInFile);

            while((jobsFile = readInBuffer.readLine()) != null) {
                String[] parseLine = jobsFile.split("\t",3);
                listOfJobs.add(new jobs(parseLine[0],
                        Integer.parseInt(parseLine[1]),
                        Integer.parseInt(parseLine[2])));
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open the provided text file");
        }
        catch (IOException e) {
            System.out.println("Error with reading the provided text file");
        }
    }

    //method to provide a menu for the user to pick which algorithm they would like to use
    public static void userChooses(ArrayList<jobs> listOfJobs, String input) {
        scheduler[] schedulers = {new RR(), new SRT(), new FB()};
            switch (input) {
                case "RR":
                    schedulers[0].selectedScheduleAlg(listOfJobs);
                    break;
                case "SRT":
                    schedulers[1].selectedScheduleAlg(listOfJobs);
                    break;
                case "FB":
                    schedulers[2].selectedScheduleAlg(listOfJobs);
                    break;
                case "ALL":
                    schedulers[0].selectedScheduleAlg(listOfJobs);
                    schedulers[1].selectedScheduleAlg(listOfJobs);
                    schedulers[2].selectedScheduleAlg(listOfJobs);
                    break;
                default:
                    System.out.println("*** ERROR: Wrong input please use either 'RR', 'SRT', 'FB',or 'ALL' ***");
                    break;
        }
    }
}

