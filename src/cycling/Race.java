package cycling;

import java.io.Serializable;
import java.util.ArrayList; 

/**
 * Represents a Race in CyclingPortal.java
 */ 
public class Race implements Serializable {
    
    // attributes

    /** The unique ID of the race. */
    private int raceID;
    /** The name of the race. */
    private String name;
    /** A string containing the description of the race. */
    private String description;
    /** An ArrayList containing all stages of the race. */
    private ArrayList<Stage> stages = new ArrayList<Stage>();
    
    /** A static ArrayList containing all raceIDs in the system. */
    static ArrayList<Integer> raceIDs = new ArrayList<Integer>();

    // Race methods
    /**
	 * Returns the race's ID.
	 * @return The unique ID of the race.
	 */
    public int getRaceID() {
        return raceID;
    }

    /**
	 * Returns the race's name.
	 * @return The name of the race.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Gets the race's information
	 * @return The details of a race in a string.
	 */
    public String toString() {
        double totalLength = 0d;
        for (Stage stage: stages) {
            totalLength += stage.getLength();
        }
        return "Race ID: " + this.raceID 
        + ", Name: " + this.name 
        + ", Description: " + this.description
        + ", Number of stages: " + this.stages.size()
        + ", Total length: " + totalLength;
    }

    /**
	 * Returns an array of raceIDs.
	 * @return An int array of IDs for all of the existing races.
	 */
    public static int[] getRaceIDs() {
        int[] arrayRaceIDs = new int[raceIDs.size()];
        for (int i = 0; i < arrayRaceIDs.length; i++) {
            arrayRaceIDs[i] = raceIDs.get(i).intValue();
        }
        return arrayRaceIDs;
    }    

    /**
	 * Removes the specified raceID from the raceIDs arrayList.
	 * @param raceID The ID to be removed.
	 */
    public static void removeRaceID(Integer raceID) {
        raceIDs.remove(raceID);
    }

    /**
    * Deletes all race IDs in the system.
    */
    public static void clearAll() {
        raceIDs.clear();
    }

    // Stage methods
    /**
	 * Adds a stage to the race.
	 * @param stage The stage to be added to the race.
	 */
    public void addStage(Stage stage) {
        stages.add(stage);
    }
    
    /**
	 * Removes a stage from the race.
	 * @param stage The stage to be removed from the race.
	 */
    public void removeStage(Stage stage) {
        stages.remove(stage);
    }

    /**
     * Removes all stages from the race.
     */
    public void removeAllStages() {
        stages.clear();
    }

    /**
	 * Returns the number of stages in the race.
	 * @return The number of stages in the race.
	 */
    public int getNumberOfStages() {
        return stages.size();
    }

    /**
     * Returns all stage IDs of stages in the race.
     * @return An int array containing all stage IDs in the race.
	 */
    public int[] getStageIDs() {
        ArrayList<Integer> listStageIDs = new ArrayList<Integer>(); 
        for (Stage stage: stages) {
            listStageIDs.add(stage.getStageID()); // Populates list with stage IDs   
        }
        int[] arrayStageIDs = new int[listStageIDs.size()]; // Creates new array with same length as list 
        for (int i = 0; i < arrayStageIDs.length; i++) {
            arrayStageIDs[i] = listStageIDs.get(i).intValue(); // Populates array with list contents
        }
        return arrayStageIDs;
    }

    /**
    * Returns all stages in the race.
    * @return An ArrayList of all stages in the race.
    */
    public ArrayList<Stage> getAllStages() {
        return stages;
    }

    // constructor
    /**
	 * This is the constructor method for the Race class. 
	 * This method is called when a new Race is to be 
	 * created. It will not return anything, as a constructor 
	 * should, but it will take in necessary parameters 
     * to instantiate the Race object.
     * @param name The name of the race
     * @param description The description of the race
	 */
    public Race(String name, String description) {
        this.name = name;
        this.description = description;
        
        if (raceIDs.size() == 0) { // Generating the ID
            this.raceID = 1;
            raceIDs.add(this.raceID);
        } else {
            Integer max = raceIDs.get(0);
            for (int i = 0; i < raceIDs.size(); i++) {
                if (raceIDs.get(i) > max) {
                    max = raceIDs.get(i);                    
                }
            }
            this.raceID = max + 1;
            raceIDs.add(this.raceID);
        }
    }
}