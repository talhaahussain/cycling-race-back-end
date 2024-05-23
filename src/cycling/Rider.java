package cycling;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

/** 
 * Represents a Rider in CyclingPortal.java
 */
public class Rider implements Serializable {

    // attributes
    private int riderID;
    private String name;
    private int yearOfBirth;
    private int teamID;
    private ArrayList<StageResult> stageResults = new ArrayList<StageResult>();
    
    static ArrayList<Integer> riderIDs = new ArrayList<Integer>();
    
    // Rider methods
    /** 
     * Gets the rider's ID.
     * @return The unique ID of the rider.
     */
    public int getRiderID() {
        return riderID;
    }

    /** 
     * Removes a riderID of a rider from the riderIDs
     * arrayList
     * @param riderID The riderID of a rider
     */
    public static void removeRiderID (Integer riderID) {
        riderIDs.remove(riderID);
    }


    /**
     * Deletes all of the data related to a rider
     */
    public static void clearAll() {
        riderIDs.clear();   
    }

    // Team methods
    /** 
     * Gets the team's ID.
     * @return The unique ID of the team.
     */
    public int getTeamID() {
        return teamID;
    }

    // StageResult methods
    /** 
     * Gets the rider's stage results.
     * @return An arrayList of StageResult objects.
     */
    public ArrayList<StageResult> getStageResults() {
        return stageResults;
    }

    /** 
     * Gets the points in a race for a given rider
     * @param raceID The unique ID of the race
     * @return The number of points
     */
    public int getPointsInRace(int raceID) {
        int points = 0;
        for (StageResult result: stageResults) {
            if (result.getRaceID() == raceID) {
                points += result.getPoints();
            }
        }
        return points;
    }

    /** 
     * Gets the mountain points in a race
     * for a given rider
     * @param raceID The unique ID of the race
     * @return The number of mountain points
     */
    public int getMountainPointsInRace(int raceID) {
        int mountainPoints = 0;
        for (StageResult result: stageResults) {
            if (result.getRaceID() == raceID) {
                mountainPoints += result.getMountainPoints();
            }
        }
        return mountainPoints;
    }

    /** 
     * Gets the adjusted elapsed time of a rider in a race
     * @param raceID The unique ID of the race
     * @return The adjusted elapsed time
     */
    public int getIntAdjustedElapsedTimeInRace(int raceID) {
        int intAdjustedElapsedTimeinRace = 0;
        for (StageResult result: stageResults) {
            if (result.getRaceID() == raceID && result.getRiderID() == this.riderID) {
                intAdjustedElapsedTimeinRace += result.getIntAdjustedElapsedTime();
            }
        }
        return intAdjustedElapsedTimeinRace;
    }

    /** 
     * Adds a stage result for a rider
     * @param stageResult The stage result of a rider
     */
    public void addStageResult(StageResult stageResult) {
        if (stageResult.getRiderID() != this.riderID) {
            return;
        } else {
            stageResults.add(stageResult);
        }
    }

    /** 
     * Removes a stage result for a rider
     * @param stageResult The stage result of a rider
     */
    public void removeStageResult(StageResult stageResult) {
        stageResults.remove(stageResult);
    }   
    
    // constructor
    /**
     * Constructor method for Rider.
     * @param teamID The ID of the rider's team.
     * @param name The name of the rider.
     * @param yearOfBirth The rider's year of birth.
     */
    public Rider(int teamID, String name, int yearOfBirth) {
        this.teamID = teamID;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        
        if (riderIDs.size() == 0) { // Generating the ID
            this.riderID = 1;
            riderIDs.add(this.riderID);
        } else {
            Integer max = riderIDs.get(0);
            for (int i = 0; i < riderIDs.size(); i++) {
                if (riderIDs.get(i) > max) {
                    max = riderIDs.get(i);                    
                }
            }
            this.riderID = max + 1;
            riderIDs.add(this.riderID);
        }
        
    }

}
