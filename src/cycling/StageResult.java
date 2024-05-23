package cycling;
import java.io.Serializable;
// the terms 'results' and 'checkpoints' are interchangeable.
import java.time.LocalTime;
import java.time.temporal.*;

/** Represents a StageResult in CyclingPortal.java
* 
*/
public class StageResult implements Serializable {

    // attributes
    private int stageID;
    private int riderID;
    private int raceID;
    private int points;
    private int mountainPoints;
    private LocalTime elapsedTime;
    private LocalTime adjustedElapsedTime;
    private LocalTime[] checkpoints;
    
    /** Gets the team's information.
     * @return A string containing the rider's ID, 
     * the stage's ID, and the raceID.
     */
    public String toString() {
        return "Rider ID: " + this.riderID 
        + ", Stage ID: " + this.stageID
        + ", Race ID: " + this.raceID;
    }
    // getters and setters
    /** Gets the stage's ID.
     * @return The unique ID of the team.
     */
    public int getStageID() {
        return stageID;
    }

    /** Gets the riderID for the stage result.
     * @return The unique ID of the rider.
     */
    public int getRiderID() {
        return riderID;
    }
    
    /** Gets the raceID for a stage result.
     * @return The unique ID of the rider.
     */
    public int getRaceID() {
        return raceID;
    }

    /** Gets the elapsed time for a stage result.
     * @return The elapsed time of a stage result.
     */
    public LocalTime getElapsedTime() {
        if (elapsedTime == null) {
            this.elapsedTime = computeElapsedTime();
        }
        return this.elapsedTime;
    }
    
    /** Gets the checkpoints array for a stage result.
     * @return An array of checkpoints.
     */
    public LocalTime[] getCheckpoints() {
        return checkpoints;
    }

    /** Gets the elapsed time array 
     * (the checkpoints and the elapsed
     * time at the end of the array) for a stage result.
     * @return An array of checkpoints and the elapsed
     * time of a stage result.
     */
    public LocalTime[] getElapsedTimeArray() {
        LocalTime elapsedTime = this.computeElapsedTime();
        LocalTime[] elapsedTimeArray = new LocalTime[checkpoints.length - 1];
        for (int i = 0; i < checkpoints.length - 2; i++) {
            elapsedTimeArray[i] = checkpoints[i+1];
        }
        elapsedTimeArray[elapsedTimeArray.length - 1] = elapsedTime;
        return elapsedTimeArray;
        
    }

    /** Gets the adjusted elapsed time for a
     * stage result.
     * @return The adjusted elapsed time.
     */
    public LocalTime getAdjustedElapsedTime() {
        return adjustedElapsedTime;
    }

    /** Sets the adjusted elapsed time for a
     * stage result.
     * @param adjustedElapsedTime The adjusted
     * elapsed time of a stage result.
     */
    public void setAdjustedElapsedTime(LocalTime adjustedElapsedTime) {
        this.adjustedElapsedTime = adjustedElapsedTime;
    }

    /** Sets the number of points awarded
     * for a stage result.
     * @param points The number of points.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /** Gets the number of points for
     * a stage result.
     * @return The number of points.
     */
    public int getPoints() {
        return points;
    }

    /** Sets the number of mountain points
     * for a stage result.
     * @param mountainPoints The number of
     * mountain points obtained.
     */
    public void setMountainPoints(int mountainPoints) {
        this.mountainPoints = mountainPoints;
    } 
    
    /** Gets the number of mountain points
     * for a stage result.
     * @return The number of
     * mountain points obtained.
     */
    public int getMountainPoints() {
        return mountainPoints;
    }

    /** Computes the elapsed time.
     * @return The elapsed time.
     */
    public LocalTime computeElapsedTime() {
        LocalTime startTime = checkpoints[0];
        LocalTime endTime = checkpoints[checkpoints.length - 1];
        long elapsedSeconds = startTime.until(endTime, ChronoUnit.SECONDS); // will return elapsed time in seconds (long data type)
        long hours = elapsedSeconds / 3600;
        long minutes = (elapsedSeconds % 3600) / 60;
        long seconds = elapsedSeconds % 60;
        String elapsedTimeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        LocalTime elapsedTime = LocalTime.parse(elapsedTimeString);
        return elapsedTime;
    }

    /** Gets the elapsed time as
     *  an int data type.
     * @return Returns the elapsed time
     * in int form.
     */
    public int getIntElapsedTime() {
        int intElapsedTime = 0;
        intElapsedTime += (this.getElapsedTime().getHour() * 3600);
        intElapsedTime += (this.getElapsedTime().getMinute() * 60);
        intElapsedTime += (this.getElapsedTime().getSecond());
        return intElapsedTime;        
    }

    /** Gets the adjusted elapsed time as an int data type
     * @return Returns the adjusted elapsed time in int form
     */
    public int getIntAdjustedElapsedTime() {
        if (this.adjustedElapsedTime == null) {
            this.computeElapsedTime();
        }
        int intAdjustedElapsedTime = 0;
        intAdjustedElapsedTime += (this.adjustedElapsedTime.getHour() * 3600);
        intAdjustedElapsedTime += (this.adjustedElapsedTime.getMinute() * 60);
        intAdjustedElapsedTime += this.adjustedElapsedTime.getSecond();
        return intAdjustedElapsedTime;
    }


    /** Converts the intTime to a LocalTime object
     * @param intTime The integer time (in seconds) to be converted
     * @return A LocalTime object that is equivalent to the intTime
     */
    public static LocalTime convertIntTimeToLocalTime(int intTime) {
        long hours = intTime / 3600;
        long minutes = (intTime % 3600) / 60;
        long seconds = intTime % 60;
        String localTimeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        LocalTime localTime = LocalTime.parse(localTimeString);
        return localTime;
    }

    public StageResult(int stageID, int riderID, int raceID, LocalTime... checkpoints) {
        this.stageID = stageID;
        this.riderID = riderID;
        this.raceID = raceID;
        this.checkpoints = checkpoints;
    }
}      