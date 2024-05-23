package cycling;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a Segment in CyclingPortal.java
 *
 */ 
public class Segment implements Serializable {
    
    // attributes
    /** The ID of the stage of which the segment belongs to. */
    private int stageID;
    /** The unique ID of the segment. */
    private int segmentID;
    /** The type of the segment. */
    private SegmentType type;
    /** The location of the segment. */
    private double location;
    /** The average gradient of the segment. */
    private Double averageGradient;
    /** The length of the segment. */
    private Double length;
    
    /** A static ArrayList containing all segmentIDs in the system. */
    static ArrayList<Integer> segmentIDs = new ArrayList<Integer>();

    // Segment methods
    /**
	 * Returns the segment's ID.
	 * @return The unique ID of the segment.
	 */
    public int getSegmentID() {
        return segmentID;
    }
    
    /**
	 * Returns the average gradient of the segment.
	 * @return The average gradient
	 */
    public Double getAverageGradient() {
        if (this.type == SegmentType.SPRINT) {
            return null;
        } else {
            return averageGradient;
        }
    }

    /**
	 * Returns the length of the segment.
	 * @return The segment's length.
	 */
    public Double getLength() {
        if (this.type == SegmentType.SPRINT) {
            return null;
        } else {
            return length;
        }
    }

    /**
	 * Returns the location of the segment.
	 * @return The segment's location.
	 */
    public Double getLocation() {
        return location;
    }

    /**
	* Removes the specified segmentID from the segmentIDs arrayList.
	 * @param segmentID The ID to be removed.
	 */
    public static void removeSegmentID(Integer segmentID) {
        segmentIDs.remove(segmentID);
    }

    /**
    * Returns the type of the segment.
	* @return The segment type
    */
    public SegmentType getSegmentType() {
        return this.type;
    }

    /**
    * Deletes all segment IDs in the system.
    */
    public static void clearAll() {
        segmentIDs.clear();
    }
    
    // Stage methods
    /**
	 * Returns the ID of the stage of which the segment belongs to.
	 * @return The ID of the stage.
	 */
    public int getStageID() {
        return stageID;
    }

    
    // 2 overloaded constructors
    /**
	 * This is the constructor method for the Segment class.
	 * This method is called when a new Segment is to be 
	 * created. It will not return anything, as a constructor 
	 * should, but it will take in necessary parameters 
	 * to instantiate the Segment object.
     * @param stageID The stageID of the stage that you want 
     *                to add the segment to
     * @param location The location of the segment
	 */
    public Segment(int stageID, double location) {
        this.stageID = stageID;
        this.location = location;
        this.type = SegmentType.SPRINT;
        if (segmentIDs.size() == 0){
            this.segmentID = 1;
            segmentIDs.add(this.segmentID);
        } else {
            Integer max = segmentIDs.get(0);
            for (int i = 0; i < segmentIDs.size(); i++){
                if (segmentIDs.get(i) > max) {
                    max = segmentIDs.get(i);                    
                }
            }
            this.segmentID = max + 1;
            segmentIDs.add(this.segmentID);
        }
    }

    /**
	* This method is also a constructor, but overloads 
	* the original constructor by allowing it to take in 
	* more parameters (stageID, location, type, averageGradient,
	* length). This constructor takes in stageID and location just
	* like the original constructor but has been overloaded so that
	* it can also take in the segment type, the averageGradient and
	* the length of the segment.
    * @param stageID The stageID of the stage that you want 
    *                to add the segment to
    * @param location The location of the segment
    * @param type The segmentType of the segment
    * @param averageGradient The average gradient of the segment
    * @param length The length of the segment
	*/
    public Segment(int stageID, Double location, SegmentType type, Double averageGradient, Double length) {
        this.stageID = stageID;
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
        this.length = length;
        if (segmentIDs.size() == 0) { // Generating the ID
            this.segmentID = 1;
            segmentIDs.add(this.segmentID);
        } else {
            Integer max = segmentIDs.get(0);
            for (int i = 0; i < segmentIDs.size(); i++) {
                if (segmentIDs.get(i) > max) {
                    max = segmentIDs.get(i);                    
                }
            }
            this.segmentID = max + 1;
            segmentIDs.add(this.segmentID);
        }
    }
}
