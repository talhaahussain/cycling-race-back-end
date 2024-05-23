package cycling;

import java.io.Serializable;
import java.util.ArrayList;

/** Represents a team in CyclingPortal.java
 * 
 */

public class Team implements Serializable {

    // Attributes
    private int teamID;
    private String name;
    private String description;

    static ArrayList<Integer> teamIDs = new ArrayList<Integer>();
    static ArrayList<Rider> riders = new ArrayList<Rider>();

    // Team methods
    /** Returns the team's ID.
     * @return The unique ID of the team.
     */
    public int getTeamID() {
        return teamID;
    }
    /** Returns the team's name.
     * @return The name of the team.
     */
    public String getName() {
        return name;
    }

    /** Returns the team's information.
     * @return A string containing the team's ID, the name, and the description.
     */
    public String toString() {
        return "Team ID:" + this.teamID 
        + ", Name:" + this.name 
        + ", Description:" + this.description;
    }

    /** Returns all team IDs in the system.
     * @return An integer array containing all team IDs in the system.
     */
    public static int[] getTeamIDs() {
        int[] arrayTeamIDs = new int[teamIDs.size()];
        for (int i = 0; i < arrayTeamIDs.length; i++) {
            arrayTeamIDs[i] = teamIDs.get(i).intValue();
        }
        return arrayTeamIDs;
    }

    /** Removes the specified team ID from the system.
     * @param teamID An integer of the team ID to be removed.
     */
    public static void removeTeamID(Integer teamID) {
        teamIDs.remove(teamID);
    }

    /**
     * Deletes all of the data related to the team.
     */
    public static void clearAll() {
        teamIDs.clear();
        riders.clear();
    }

    // Rider methods
    /** Adds a rider to the team.
     * @param rider A rider object to be added to the team.
     */
    public void addRider(Rider rider) {
        riders.add(rider);
    }

    /** 
     * Removes a rider from the team.
     * @param rider A rider object to be removed from the team.
     */
    public void removeRider(Rider rider) {
        riders.remove(rider);
    }

    /** 
     * Gets all rider IDs of riders on the team.
     * @return An integer array containing all rider IDs on the team.
     */
    public int[] getRiderIDs() {
        ArrayList<Integer> listRiderIDs = new ArrayList<Integer>(); 
        for (Rider e: riders) {
            listRiderIDs.add(e.getRiderID());            
        }
        int[] arrayRiderIDs = new int[listRiderIDs.size()];
        for (int i = 0; i < arrayRiderIDs.length; i++) {
            arrayRiderIDs[i] = listRiderIDs.get(i).intValue();
        }
        return arrayRiderIDs;
    }

    // constructor
    /**
     * Constructor for Team class.
     * @param name The name of the team.
     * @param description A string description of the team.
     */
    public Team(String name, String description) {
        this.name = name;
        this.description = description;

        if (teamIDs.size() == 0) { // Generating the ID
            this.teamID = 1;
            teamIDs.add(this.teamID);
        } else {
            Integer max = teamIDs.get(0);
            for (int i = 0; i < teamIDs.size(); i++) {
                if (teamIDs.get(i) > max) {
                    max = teamIDs.get(i);                    
                }
            }
            this.teamID = max + 1;
            teamIDs.add(this.teamID);
        }
    }
}
