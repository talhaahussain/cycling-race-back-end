package cycling;

import java.util.ArrayList; 
import java.time.LocalTime;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a stage in CyclingPortal.java
 */
public class Stage implements Serializable {
    
    // attributes
    /** The unique ID of the stage. */
    private int stageID;
    /** The name of the stage. */
    private String name;
    /** The unique ID of the stage. */
    private double length;
    /** The length of the stage. */
    private String description;
    /** A string containing the description of the stage. */
    private LocalDateTime startTime;
    /** The type of the stage. */
    private StageType type;
    /** The state of the stage. */
    private StageState stageState;
    /** The ID of the race of which the stage belongs to. */
    private int raceID;
    /** An ArrayList containing all segments of the stage. */
    private ArrayList<Segment> segments = new ArrayList<Segment>();
    
    /** A static ArrayList containing all stageIDs in the system. */
    static ArrayList<Integer> stageIDs = new ArrayList<Integer>(); 


    // Stage methods
    /**
	 * Returns the stage's ID.
	 * @return The unique ID of the stage.
	 */
    public int getStageID() {
        return stageID;
    }

    /**
	 * Returns the stage's name.
	 * @return The name of the stage.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Returns the length of the stage.
	 * @return The stage's length.
	 */
    public double getLength() {
        return length;
    }

    /**
	 * Returns the starting time of the stage.
	 * @return The starting time of the stage.
	 */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
	 * Returns the type of the stage.
	 * @return The type of the stage.
	 */
    public StageType getStageType() {
        return type;
    }

    /**
	 * Returns the state of the stage.
	 * @return The state of the stage.
	 */
    public StageState getStageState() {
        return stageState;
    }

    /**
	 * Removes the specified stageID from the stageIDs arrayList.
	 * @param stageID The ID to be removed.
	 */
    public static void removeStageID(Integer stageID) {
        stageIDs.remove(stageID);
    }

    /**
	 * Returns an array of stageIDs.
	 * @return An int array of IDs for all of the existing stage.
	 */
    public static int[] getStageIDs() {
        int[] arrayStageIDs = new int[stageIDs.size()];
        for (int i = 0; i < arrayStageIDs.length; i++) {
            arrayStageIDs[i] = stageIDs.get(i).intValue();
        }
        return arrayStageIDs;
    }

    /**
	 * Changes the state of the stage.
	 * @param newState The new state of the stage.
	 */
    public void changeState(StageState newState){
        stageState = newState;
    } 

    /**
    * Deletes all stage IDs in the system.
    */
    public static void clearAll() {
        stageIDs.clear();
    }

    // Race methods 
    /**
	 * Returns the ID of the race of which the stage belongs to.
	 * @return The ID of the race.
	 */   
    public int getRaceID() {
        return raceID;
    }

    // Segment methods
    /**
	 * Adds a segment to the stage.
	 * @param segment The segment to be added.
	 */
    public void addSegment(Segment segment) {
        segments.add(segment);
    }
    
    /**
	 * Removes a segment from the stage.
	 * @param segment The segment to be removed.
	 */
    public void removeSegment(Segment segment) {
        segments.remove(segment);
    }

    /**
	 * Removes all segments from the stage.
	 */
    public void removeAllSegments() {
        segments.clear();
    }

    /**
	 * Returns the number of segments in the stage.
	 * @return The number of segments in the stage.
	 */
    public int getNumberOfSegments() {
        return segments.size();
    }

    /**
	 * Returns all segment IDs of segments in the stage.
	 * @return An int array containing all segment IDs in the stage.
	 */
    public int[] getSegmentIDs(){
        ArrayList<Integer> listSegmentIDs = new ArrayList<Integer>(); 
        for (Segment e: segments) {
            listSegmentIDs.add(e.getSegmentID());            
        }
        int[] arraySegmentIDs = new int[listSegmentIDs.size()];
        for (int i = 0; i < arraySegmentIDs.length; i++) {
            arraySegmentIDs[i] = listSegmentIDs.get(i).intValue();
        }
        return arraySegmentIDs;
    }

    /**
    * Returns all segments in the stage.
    * @return An ArrayList of all segments in the stage.
    */
    public ArrayList<Segment> getSegments() {
        return segments;
    }

    // constructor
    /**
	 * This is the constructor method for the Stage class. 
	 * This method is called when a new Stage is to be 
	 * created. It will not return anything, as a constructor 
	 * should, but it will take in necessary parameters
	 * to instantiate the Stage object.
     * @param raceID The raceID that you want to add the stage to
     * @param name The name of the stage
     * @param description The description of the stage
     * @param length The length of the stage
     * @param startTime The starting time of the stage
     * @param type The stage type of the stage
	 */
    public Stage(int raceID, String name, String description, double length, LocalDateTime startTime, StageType type) {
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        this.raceID = raceID;
        this.stageState = StageState.UNDER_DEVELOPMENT;

        if (stageIDs.size() == 0) { // Generating the ID
            this.stageID = 1;
            stageIDs.add(this.stageID);
        } else {
            Integer max = stageIDs.get(0);
            for (int i = 0; i < stageIDs.size(); i++) {
                if (stageIDs.get(i) > max) {
                    max = stageIDs.get(i);                    
                }
            }
            this.stageID = max + 1;
            stageIDs.add(this.stageID);
        }
    }
}