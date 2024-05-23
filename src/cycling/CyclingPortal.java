package cycling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * CyclingPortal is an implementation of the CyclingPortalInterface interface.
 * @author Diogo Pacheco, 710010983, 710029379
 * @version 1.1
 *
 */
public class CyclingPortal implements CyclingPortalInterface {

	ArrayList<Race> allRaces = new ArrayList<Race>(); // A list of all Race objects 
	ArrayList<Stage> allStages = new ArrayList<Stage>(); // A list of all Stage objects
	ArrayList<Segment> allSegments = new ArrayList<Segment>(); // A list of all Segment objects
	ArrayList<Team> allTeams = new ArrayList<Team>(); // A list of all Team objects
	ArrayList<Rider> allRiders = new ArrayList<Rider>(); // A list of all Rider objects
	ArrayList<StageResult> allStageResults = new ArrayList<StageResult>(); // A list of all StageResult objects
	// When we delete a Race, it must be removed from the above list.
	// When we delete a Stage, it must be removed from the above list AND the list within the race object
	// When we delete a Segment, it must be removed from the above list AND the list within the stage object


	@Override
	public int[] getRaceIds() {
		return Race.getRaceIDs();  // returns race ids
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		for (Race e:allRaces) {
			if (e.getName() == name) {
				throw new IllegalNameException("This race name already exists in the system."); // check if name exists
			}
		}
		if (name.length() > 30 || name.isEmpty() || name == null || name.contains(" ")) { // Check if name violates rules
			throw new InvalidNameException("Invalid race name entered.");
		} else {
			allRaces.add(new Race(name, description));
			return allRaces.get(allRaces.size() - 1).getRaceID();
		}
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		for (Race race: allRaces) {
			if (race.getRaceID() == raceId) {
				return race.toString();
			}
		}
		throw new IDNotRecognisedException("ID does not match any race in the system."); // thrown if ID is invalid
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		for (Race race: allRaces) {
			if (race.getRaceID() == raceId) {
				ArrayList<StageResult> resultsToBeRemoved = new ArrayList<>();
				Race.removeRaceID(raceId);
				allStages.removeAll(race.getAllStages()); // 
				race.removeAllStages();
				for (StageResult result: allStageResults) {
					if (result.getRaceID() == raceId) {
						resultsToBeRemoved.add(result);
					}
				}
				allRaces.remove(race);
				allStageResults.removeAll(resultsToBeRemoved);  // removes results of race from allStageResults
				return;
			}			
		}
		throw new IDNotRecognisedException("ID does not match any race in the system."); // thrown if ID is invalid
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		for (Race race: allRaces) {
			if (race.getRaceID() == raceId) {
				return race.getNumberOfStages(); // if raceID matches, return number of stages for the race
			}
		}
		throw new IDNotRecognisedException("ID does not match any race in the system."); // thrown if ID is invalid
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		Boolean IDRecognised = false;
		for (Race race: allRaces) {
			if (race.getRaceID() == raceId) {
				IDRecognised = true;
			}
		}
		if (IDRecognised == false) {
			throw new IDNotRecognisedException("ID does not match any race in the system."); // thrown if ID is invalid
		}
		for (Stage e:allStages) {
			if (e.getName() == stageName) {
				throw new IllegalNameException("This stage name already exists in the system."); // thrown if stage name already exists
			}
		}
		if (stageName.length() > 30 || stageName.isEmpty() || stageName == null) { // check if name violates rules
			throw new InvalidNameException(("Invalid stage name entered."));
		}
		if (length < 5.0d) { // check if length is too short
			throw new InvalidLengthException("The length entered is less than 5km.");
		}
		allStages.add(new Stage(raceId, stageName, description, length, startTime, type));
		for (Race race:allRaces) {
			if (race.getRaceID() == raceId) {
				race.addStage(allStages.get(allStages.size() - 1)); // adds stage to race
			}
		}
		return allStages.get(allStages.size() - 1).getStageID(); // return stageID 
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		for (Race race:allRaces) {
			if (race.getRaceID() == raceId) {
				return race.getStageIDs(); // return stageIDs within a race
			}
		}
		throw new IDNotRecognisedException("ID does not match any race in the system."); // thrown if ID is invalid
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				return stage.getLength(); // return stage length
			}
		}
		throw new IDNotRecognisedException("ID does not match any stage in the system."); // thrown if ID is invalid
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				for (Race race: allRaces) {
					if (stage.getRaceID() == race.getRaceID()) {
						Stage.removeStageID(stageId);
						race.removeStage(stage);
					}
				}
				allStages.remove(stage);
				return; // removes stage then breaks out to make sure exception isn't unncessarily thrown
			}
		}
		throw new IDNotRecognisedException("ID does not match any stage in the system."); // thrown if ID is invalid
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		Boolean IDRecognised = false;
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				IDRecognised = true;
				if (0 >= location || location > stage.getLength()) {
					throw new InvalidLocationException("Location falls outside of bounds of stage."); // check if location is invalid
				}
				if (stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
					throw new InvalidStageStateException("Stage is waiting for results."); // check stage state
				}
				if (stage.getStageType() == StageType.TT) { // check stage type
					throw new InvalidStageTypeException("You have attempted to add segment to a time trial stage.");
				}
			}
		}
		if (IDRecognised == false) {
			throw new IDNotRecognisedException("ID does not match any stage in the system."); // thrown if ID is invalid
		}

		allSegments.add(new Segment(stageId, location, type, averageGradient, length));
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				stage.addSegment(allSegments.get(allSegments.size() - 1)); // add categorized climb segment to stage
			}
		}
		return allSegments.get(allSegments.size() - 1).getSegmentID(); // return segmentID

	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		Boolean IDRecognised = false;
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				IDRecognised = true;
				if (0 >= location || location > stage.getLength()) {
					throw new InvalidLocationException("Location falls outside of bounds of stage."); // check if location is invalid
				}
				if (stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
					throw new InvalidStageStateException("Stage is waiting for results."); // check stage state
				}
				if (stage.getStageType() == StageType.TT) { // check stage type
					throw new InvalidStageTypeException("You have attempted to add segment to a time trial stage.");
				}
			}
		}
		if (IDRecognised == false) {
			throw new IDNotRecognisedException("ID does not match any stage in the system."); // thrown if ID is invalid
		}
		
		allSegments.add(new Segment(stageId, location));
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				stage.addSegment(allSegments.get(allSegments.size() - 1)); // add intermediate sprint segment to stage
			}
		}
		return allSegments.get(allSegments.size() - 1).getSegmentID(); // return segmentID
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
		for (Segment segment: allSegments) {
			if (segment.getSegmentID() == segmentId) {
				for (Stage stage: allStages) {
					if (segment.getStageID() == stage.getStageID()) {
						Segment.removeSegmentID(segmentId);
						stage.removeSegment(segment);
					}
				}
				allSegments.remove(segment);
				return; // remove segment from everywhere then break out of method to avoid
				        // unnecessarily triggering exception
			}
		}
		throw new IDNotRecognisedException("ID does not match any segment in the system."); // thrown if ID is invalid
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				if (stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
					throw new InvalidStageStateException("Stage is already concluded."); // check if stage has already concluded
				} else {
					stage.changeState(StageState.WAITING_FOR_RESULTS);
					assert stage.getStageState() != StageState.WAITING_FOR_RESULTS; // Assertion to ensure state is correct
					return; // change state then break out to avoid triggering exception
				}
			}
		}
		throw new IDNotRecognisedException("ID does not match any stage in the system.");
	}

	@Override
	public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				return stage.getSegmentIDs(); // return array of segmentIDs for stage
			}
		}
		throw new IDNotRecognisedException("ID does not match any stage in the system."); // thrown if ID is invalid
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		for (Team team: allTeams) {
			if (team.getName() == name) { // check if team exists
				throw new IllegalNameException("This team name already exists in the system.");
			}
		}
		if (name.length() > 30 || name.isEmpty() || name == null) {
			throw new InvalidNameException("Invalid team name entered."); // check if name breaks rules
		} else {
			allTeams.add(new Team(name, description)); // add team
			return allTeams.get(allTeams.size() - 1).getTeamID(); // return teamID
		}
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		for (Team team: allTeams) {
			if (team.getTeamID() == teamId) {
				Team.removeTeamID(teamId);
				allTeams.remove(team);
				return; // remove team from everywhere then break out of method to avoid
				        // unnecessarily triggering exception
			}
		}
		throw new IDNotRecognisedException("ID does not match any team in the system."); // thrown if ID is invalid
	}

	@Override
	public int[] getTeams() {
		return Team.getTeamIDs(); // return teamIDs
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		for (Team team: allTeams) {
			if (team.getTeamID() == teamId) {
				return team.getRiderIDs(); // return riderIDs
			}
		}
		throw new IDNotRecognisedException("ID does not match any team in the system."); // thrown if ID is invalid
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		if (name == null) { // check if invalid name
			throw new IllegalArgumentException("Invalid rider name entered.");
		}
		if (yearOfBirth < 1900) { // check if invalid year of birth
			throw new IllegalArgumentException("Invalid year of birth entered.");
		}
		for (Team team: allTeams) {
			if (team.getTeamID() == teamID) {
				allRiders.add(new Rider(teamID, name, yearOfBirth));
				team.addRider(allRiders.get(allRiders.size() - 1));
				return allRiders.get(allRiders.size() - 1).getRiderID(); // create rider then return their ID
			}
		}
		throw new IDNotRecognisedException("ID does not match any team in the system."); // thrown if ID is invalid
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		ArrayList<StageResult> resultsToBeRemoved = new ArrayList<>();
		for (Rider rider: allRiders) {
			if (rider.getRiderID() == riderId) {
				for (Team team: allTeams) {
					if (rider.getTeamID() == team.getTeamID()) {
						for (StageResult result: allStageResults) {
							if (result.getRiderID() == rider.getRiderID()) {
								resultsToBeRemoved.add(result); // remove results of rider
							}
						}
						Rider.removeRiderID(riderId);
						team.removeRider(rider);
					} 
				}
				allRiders.remove(rider);
				allStageResults.removeAll(resultsToBeRemoved);
				assert allRiders.contains(rider) == false;
				return; // remove all of the details, results and associations of a rider
				        // then break out
			}
		}
		throw new IDNotRecognisedException("ID does not match any rider in the system."); // thrown if ID is invalid
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
			InvalidStageStateException {
		boolean stageIDrecognised = false;
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				if (stage.getStageState() != StageState.WAITING_FOR_RESULTS) { // check stage state
					throw new InvalidStageStateException("Stage is not accepting results at this time.");
				}
				if (stage.getNumberOfSegments() + 2 != checkpoints.length) { // check length of checkpoints array
					throw new InvalidCheckpointsException("Invalid number of checkpoints.");
				}
				stageIDrecognised = true;
			}
		}
		if (stageIDrecognised == false) {
			throw new IDNotRecognisedException("ID does not match any stage in the system."); //thrown if ID is invalid
		}
		for (Rider rider: allRiders) {
			if (rider.getRiderID() == riderId) {
				for (StageResult result: rider.getStageResults()) {
					if (result.getStageID() == stageId) {
						throw new DuplicatedResultException("Rider already has results registered for this stage."); 
						// thrown if rider already has results registered for a stage
					}
				}
				for (Stage stage: allStages) {
					if (stage.getStageID() == stageId) {
						int raceID = stage.getRaceID();
						allStageResults.add(new StageResult(stageId, riderId, raceID, checkpoints));
						if (rider.getRiderID() == riderId) {
							rider.addStageResult(allStageResults.get(allStageResults.size() - 1));
							// add stage result for the rider
						}
						return; // break out
					}
				}
			}
		}
		throw new IDNotRecognisedException("ID does not match any rider in the system."); //thrown if ID is invalid
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		boolean stageIDrecognised = false;
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				stageIDrecognised = true;
			}
		}
		if (stageIDrecognised == false) {
			throw new IDNotRecognisedException("ID does not match any stage in the system.");
			// thrown if ID is invalid
		}
		for (StageResult result: allStageResults) {
			if (result.getRiderID() == riderId && result.getStageID() == stageId) {
				if (result.getElapsedTimeArray().length == 0) {
					return new LocalTime[0]; // if no results then return empty array
				} else {
					return result.getElapsedTimeArray();
				}
			}
		}
		throw new IDNotRecognisedException("ID does not match any rider in the system.");
		// thrown if ID is invalid
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		boolean stageIDrecognised = false;
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				stageIDrecognised = true;
			}
		}
		if (stageIDrecognised == false) {
			throw new IDNotRecognisedException("ID does not match any stage in the system.");
			// thrown if ID is invalid
		}
		for (Rider rider: allRiders) {
			if (rider.getRiderID() == riderId) {
				int[] ridersRankInStage = getRidersRankInStage(stageId);
				for (int i = 0; i < ridersRankInStage.length; i++) {
					if (ridersRankInStage[i] == riderId) {
						if (getRiderResultsInStage(stageId, riderId).length == 0) {
							return null;
						}
						assert getRiderResultsInStage(stageId, riderId).length != 0;
						if (ridersRankInStage[0] == riderId) {
							LocalTime currentRiderResult = getRiderResultsInStage(stageId, riderId)[getRiderResultsInStage(stageId, riderId).length - 1];
							for (StageResult result: allStageResults) {
								if (result.getRiderID() == riderId && result.getStageID() == stageId) {
									result.setAdjustedElapsedTime(currentRiderResult);
								}
							}
							return currentRiderResult;
						}
						if (ridersRankInStage.length == 0) {
							return null;
						}
						LocalTime currentRiderResult = getRiderResultsInStage(stageId, riderId)[getRiderResultsInStage(stageId, riderId).length - 1];
						LocalTime nextRiderResult = getRiderResultsInStage(stageId, ridersRankInStage[i - 1])[getRiderResultsInStage(stageId, ridersRankInStage[i - 1]).length - 1];
						if (nextRiderResult.until(currentRiderResult, ChronoUnit.SECONDS) <= 1) {
							currentRiderResult = nextRiderResult;
							for (StageResult result: allStageResults) {
								if (result.getRiderID() == riderId && result.getStageID() == stageId) {
									result.setAdjustedElapsedTime(currentRiderResult);
								}
							}
							return currentRiderResult; // returns adjusted elapsed time
						} else {
							for (StageResult result: allStageResults) {
								if (result.getRiderID() == riderId && result.getStageID() == stageId) {
									result.setAdjustedElapsedTime(currentRiderResult);
								}
							}
							return currentRiderResult; // returns adjusted elapsed time
						}
					}
				}
			}
		}
		throw new IDNotRecognisedException("ID does not match any rider in the system."); // thrown if ID is invalid
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		for (Rider rider: allRiders) {
			if (rider.getRiderID() == riderId) {
				for (StageResult result: rider.getStageResults()) {
					if (result.getStageID() == stageId) {
						rider.removeStageResult(result);
						allStageResults.remove(result);
						return; // remove all results then break out
					}
				}
				throw new IDNotRecognisedException("ID does not match any stage in the system."); // thrown if ID is invalid
			}
		}
		throw new IDNotRecognisedException("ID does not match any rider in the system."); // thrown if ID is invalid
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		boolean stageIDrecognised = false;
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				stageIDrecognised = true;
			}
		}
		if (stageIDrecognised == false) {
			throw new IDNotRecognisedException("ID does not match any stage in the system."); // thrown if ID is invalid
		}
		
		ArrayList<StageResult> stageResultsforStageID = new ArrayList<StageResult>();
		for (StageResult result: allStageResults) {
			if (result.getStageID() == stageId) {
				stageResultsforStageID.add(result);
			}
		}
		if (stageResultsforStageID.size() == 0) {
			return new int[0];
		}

		int[] elapsedTimes = new int[stageResultsforStageID.size()];
		for (int i = 0; i < stageResultsforStageID.size(); i++) {
			elapsedTimes[i] = stageResultsforStageID.get(i).getIntElapsedTime();						
		}
		Arrays.sort(elapsedTimes);
		int[] sortedIDs = new int[elapsedTimes.length];
		for (int i = 0; i < elapsedTimes.length; i++) {
			for (StageResult result: stageResultsforStageID) {
				if (result.getIntElapsedTime() == elapsedTimes[i]) {
					if (i == 0) {
						sortedIDs[i] = result.getRiderID();
					} else if (sortedIDs[i - 1] == result.getRiderID()) {
						break;
					}
					sortedIDs[i] = result.getRiderID();
				} 
			}
		}
		return sortedIDs; // returns an array of sorted IDs in a stage
	}



	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				int[] riderRank = getRidersRankInStage(stageId);
				LocalTime[] riderAdjustedElapsedTimes = new LocalTime[riderRank.length];
				if (riderRank.length == 0) {
					return new LocalTime[0]; // return empty array if there are no riders
				}
				for (int i = 0; i < riderRank.length; i++) {
					riderAdjustedElapsedTimes[i] = getRiderAdjustedElapsedTimeInStage(stageId, riderRank[i]);
				}
				return riderAdjustedElapsedTimes; // returns ranked adjusted elapsed times
			}
		}
		throw new IDNotRecognisedException("ID does not match any stage in the system."); // thrown if ID is invalid
	}


	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				if (getRidersRankInStage(stageId).length == 0) {
					return new int[0]; // return empty array if there are no riders in stage
				}
				if (stage.getStageType() == StageType.FLAT) {
					int[] ridersPoints = {50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2};
					int[] riderIDs = getRidersRankInStage(stageId);
					for (int i = 0; i < riderIDs.length; i++) {
						for (Rider rider: allRiders) {
							if (rider.getRiderID() == riderIDs[i]) {
								for (StageResult result: allStageResults) {
									if (result.getStageID() == stageId && result.getRiderID() == rider.getRiderID()) {
										result.setPoints(ridersPoints[i]);
									}
								}
							}
						}
					}
					return ridersPoints;  // return rider's points
				}
				if (stage.getStageType() == StageType.MEDIUM_MOUNTAIN) {
					int[] ridersPoints = {30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
					int[] riderIDs = getRidersRankInStage(stageId);
					for (int i = 0; i < riderIDs.length; i++) {
						for (Rider rider: allRiders) {
							if (rider.getRiderID() == riderIDs[i]) {
								for (StageResult result: allStageResults) {
									if (result.getStageID() == stageId && result.getRiderID() == rider.getRiderID()) {
										result.setPoints(ridersPoints[i]);
									}
								}
							}
						}
					}
					return ridersPoints; // return rider's points
				}
				if (stage.getStageType() == StageType.HIGH_MOUNTAIN) {
					int[] ridersPoints = {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
					int[] riderIDs = getRidersRankInStage(stageId);
					for (int i = 0; i < riderIDs.length; i++) {
						for (Rider rider: allRiders) {
							if (rider.getRiderID() == riderIDs[i]) {
								for (StageResult result: allStageResults) {
									if (result.getStageID() == stageId && result.getRiderID() == rider.getRiderID()) {
										result.setPoints(ridersPoints[i]);
									}
								}
							}
						}
					}
					return ridersPoints;	// return rider's points
				}
				if (stage.getStageType() == StageType.TT) {
					int[] ridersPoints = {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
					int[] riderIDs = getRidersRankInStage(stageId);
					for (int i = 0; i < riderIDs.length; i++) {
						for (Rider rider: allRiders) {
							if (rider.getRiderID() == riderIDs[i]) {
								for (StageResult result: allStageResults) {
									if (result.getStageID() == stageId && result.getRiderID() == rider.getRiderID()) {
										result.setPoints(ridersPoints[i]);
									}
								}
							}
						}
					}
					return ridersPoints; // return rider's points
				}
			}
		}
		throw new IDNotRecognisedException("ID does not match any stage in the system.");	
		//thrown if ID is invalid	
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		for (Stage stage: allStages) {
			if (stage.getStageID() == stageId) {
				if (getRidersRankInStage(stageId).length == 0) { // ADD CONDITIONS TO ENSURE THAT STAGE IS MOUNTAIN TYPE!
					return new int[0];
				}
				for (Segment segment: stage.getSegments()) {
					if (segment.getSegmentType() == SegmentType.C4) {
						int[] ridersPoints = {1, 0, 0 ,0 ,0 ,0 ,0, 0, 0, 0, 0, 0, 0, 0, 0};
						int[] riderIDs = getRidersRankInStage(stageId);
						for (int i = 0; i < riderIDs.length; i++) {
							for (Rider rider: allRiders) {
								if (rider.getRiderID() == riderIDs[i]) {
									for (StageResult result: allStageResults) {
										if (result.getStageID() == stageId && result.getRiderID() == rider.getRiderID()) {
											result.setMountainPoints(ridersPoints[i]);
										}
									}
								}
							}
						}
						return ridersPoints;  // return rider's points
					}
					if (segment.getSegmentType() == SegmentType.C3) {
						int[] ridersPoints = {2, 1, 0 ,0 ,0 ,0 ,0, 0, 0, 0, 0, 0, 0, 0, 0};
						int[] riderIDs = getRidersRankInStage(stageId);
						for (int i = 0; i < riderIDs.length; i++) {
							for (Rider rider: allRiders) {
								if (rider.getRiderID() == riderIDs[i]) {
									for (StageResult result: allStageResults) {
										if (result.getStageID() == stageId && result.getRiderID() == rider.getRiderID()) {
											result.setMountainPoints(ridersPoints[i]);
										}
									}
								}
							}
						}
						return ridersPoints; // return rider's points
					}
					if (segment.getSegmentType() == SegmentType.C2) {
						int[] ridersPoints = {5, 3, 2, 1, 0 ,0 ,0, 0, 0, 0, 0, 0, 0, 0, 0};
						int[] riderIDs = getRidersRankInStage(stageId);
						for (int i = 0; i < riderIDs.length; i++) {
							for (Rider rider: allRiders) {
								if (rider.getRiderID() == riderIDs[i]) {
									for (StageResult result: allStageResults) {
										if (result.getStageID() == stageId && result.getRiderID() == rider.getRiderID()) {
											result.setMountainPoints(ridersPoints[i]);
										}
									}
								}
							}
						}
						return ridersPoints; // return rider's points
					}
					if (segment.getSegmentType() == SegmentType.C1) {
						int[] ridersPoints = {10, 8, 6, 4, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
						int[] riderIDs = getRidersRankInStage(stageId);
						for (int i = 0; i < riderIDs.length; i++) {
							for (Rider rider: allRiders) {
								if (rider.getRiderID() == riderIDs[i]) {
									for (StageResult result: allStageResults) {
										if (result.getStageID() == stageId && result.getRiderID() == rider.getRiderID()) {
											result.setMountainPoints(ridersPoints[i]);
										}
									}
								}
							}
						}
						return ridersPoints; // return rider's points
					}
					if (segment.getSegmentType() == SegmentType.HC) {
						int[] ridersPoints = {20, 15, 12, 10, 8, 6, 4, 2, 0, 0, 0, 0, 0, 0, 0};
						int[] riderIDs = getRidersRankInStage(stageId);
						for (int i = 0; i < riderIDs.length; i++) {
							for (Rider rider: allRiders) {
								if (rider.getRiderID() == riderIDs[i]) {
									for (StageResult result: allStageResults) {
										if (result.getStageID() == stageId && result.getRiderID() == rider.getRiderID()) {
											result.setMountainPoints(ridersPoints[i]);
										}
									}
								}
							}
						}
						return ridersPoints; // return rider's points
					}
				}
			}
		}
		throw new IDNotRecognisedException("ID does not match any stage in the system.");
		//thrown if ID is invalid
	}

	@Override
	public void eraseCyclingPortal() {
		allRaces.clear();
		Race.clearAll();
		allStages.clear();
		Stage.clearAll();
		allSegments.clear();
		Segment.clearAll();
		allTeams.clear();
		Team.clearAll();
		allRiders.clear();
		Rider.clearAll();
		allStageResults.clear();
		// delete everything
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		try {
			ArrayList<Object> allObjects = new ArrayList<>();
			for (Race race: allRaces) {
				allObjects.add(race);
			}
			for (Stage stage: allStages) {
				allObjects.add(stage);
			}
			for (Segment segment: allSegments) {
				allObjects.add(segment);
			}
			for (Team team: allTeams) {
				allObjects.add(team);
			}
			for (Rider rider: allRiders) {
				allObjects.add(rider);
			}
			for (StageResult result: allStageResults) {
				allObjects.add(result);
			}
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream output = new ObjectOutputStream(file);
			output.writeObject(allObjects);
			output.close();
			file.close();
			// serializing state of portal into file

		} catch (IOException i) {
			throw new IOException("There was a problem when trying to store portal contents to file.");
		}
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		try {
			ArrayList <Object> allObjects = new ArrayList<>();
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream input = new ObjectInputStream(file);
			allObjects = (ArrayList) input.readObject();
			for (Object a: allObjects) {
				if (a instanceof Race) {
					allRaces.add((Race) a);
				}
				if (a instanceof Stage) {
					allStages.add((Stage) a);
				}
				if (a instanceof Segment) {
					allSegments.add((Segment) a);
				}
				if (a instanceof Team) {
					allTeams.add((Team) a);
				}
				if (a instanceof Rider) {
					allRiders.add((Rider) a);
				}
				if (a instanceof StageResult) {
					allStageResults.add((StageResult) a);
				}
			}
			input.close();
			file.close();
			//deserializing file back to portal
		} catch (IOException i) {
			throw new IOException("There was a problem when trying to read portal contents from file.");
		} catch (ClassNotFoundException c) {
			throw new ClassNotFoundException("Required class files could not be found when loading.");
		}
	}
	

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		for (Race race: allRaces) {
			if (race.getName() == name) {
				try {
				removeRaceById(race.getRaceID());
					return; // removes race then breaks out
				} catch (IDNotRecognisedException i) {
					i.printStackTrace();
				}
			}
		}
		throw new NameNotRecognisedException("Name does not match any race in the system.");
		// thrown if name is invalid
	}
	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		boolean raceIDrecognised = false;
		ArrayList<StageResult> stageResultsInRace = new ArrayList<>();
		ArrayList<Rider> ridersInRace = new ArrayList<>();
		for (Race race: allRaces) {
			if (race.getRaceID() == raceId) {
				raceIDrecognised = true;
				for (int stageID: race.getStageIDs()) {
					getRankedAdjustedElapsedTimesInStage(stageID);
				}
				for (StageResult result: allStageResults) {
					if (result.getRaceID() == raceId) {
						stageResultsInRace.add(result);
					}
				}
			}
		}
		if (stageResultsInRace.isEmpty()) {
			return new LocalTime[0];
		}
		if (raceIDrecognised == false) {
			throw new IDNotRecognisedException("Race ID does not match any race in the system");
			// thrown if ID is invalid
		}
		for (Rider rider: allRiders) {
			for (StageResult result: stageResultsInRace) {
				if (rider.getRiderID() == result.getRiderID()) {
					if (ridersInRace.contains(rider)) {
						continue;
					} else {
						ridersInRace.add(rider);
					}
				}
			}
		}
		int[] intAdjustedElapsedTimes = new int[ridersInRace.size()];
		
		for (int i = 0; i < intAdjustedElapsedTimes.length; i++) {
			intAdjustedElapsedTimes[i] = ridersInRace.get(i).getIntAdjustedElapsedTimeInRace(raceId);
		}
		Arrays.sort(intAdjustedElapsedTimes);
		LocalTime[] adjustedElapsedTimes = new LocalTime[intAdjustedElapsedTimes.length];
		for (int i = 0; i < adjustedElapsedTimes.length; i++) {
			adjustedElapsedTimes[i] = StageResult.convertIntTimeToLocalTime(intAdjustedElapsedTimes[i]);
		}
		return adjustedElapsedTimes; // returns general classification times of all riders in race
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		boolean raceIDrecognised = false;
		ArrayList<StageResult> stageResultsInRace = new ArrayList<>();
		ArrayList<Rider> ridersInRace = new ArrayList<>();
		for (Race race: allRaces) {
			if (race.getRaceID() == raceId) {
				raceIDrecognised = true;
				for (int stageID: race.getStageIDs()) {
					getRidersPointsInStage(stageID);
				}
				for (StageResult result: allStageResults) {
					if (result.getRaceID() == raceId) {
						stageResultsInRace.add(result);
					}
				}
			}
		}
		if (stageResultsInRace.isEmpty()) {
			return new int[0];
		}
		if (raceIDrecognised == false) {
			throw new IDNotRecognisedException("Race ID does not match any race in the system");
			// thrown if ID is invalid
		}
		for (Rider rider: allRiders) {
			for (StageResult result: stageResultsInRace) {
				if (rider.getRiderID() == result.getRiderID()) {
					if (ridersInRace.contains(rider)) {
						continue;
					} else {
						ridersInRace.add(rider);
					}
				}
			}
		}		
		int[] riderPoints = new int[ridersInRace.size()];
		for (int i = 0; i < riderPoints.length; i++) {
			riderPoints[i] = ridersInRace.get(i).getPointsInRace(raceId);
		}
		Integer[] integerRiderPoints = Arrays.stream(riderPoints).boxed().toArray(Integer[]::new);
		Arrays.sort(integerRiderPoints, Comparator.reverseOrder());
		for (int i = 0; i < integerRiderPoints.length; i++) {
			riderPoints[i] = integerRiderPoints[i].intValue();
		}
		return riderPoints; // returns rider points of all riders in race
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		boolean raceIDrecognised = false;
		ArrayList<StageResult> stageResultsInRace = new ArrayList<>();
		ArrayList<Rider> ridersInRace = new ArrayList<>();
		for (Race race: allRaces) {
			if (race.getRaceID() == raceId) {
				raceIDrecognised = true;
				for (int stageID: race.getStageIDs()) {
					getRidersMountainPointsInStage(stageID);
				}
				for (StageResult result: allStageResults) {
					if (result.getRaceID() == raceId) {
						stageResultsInRace.add(result);
					}
				}
			}
		}
		if (stageResultsInRace.isEmpty()) {
			return new int[0];
		}
		if (raceIDrecognised == false) {
			throw new IDNotRecognisedException("Race ID does not match any race in the system");
			// thrown if ID is invalid
		}
		for (Rider rider: allRiders) {
			for (StageResult result: stageResultsInRace) {
				if (rider.getRiderID() == result.getRiderID()) {
					if (ridersInRace.contains(rider)) {
						continue;
					} else {
						ridersInRace.add(rider);
					}
				}
			}
		}		
		int[] riderMountainPoints = new int[ridersInRace.size()];
		for (int i = 0; i < riderMountainPoints.length; i++) {
			riderMountainPoints[i] = ridersInRace.get(i).getMountainPointsInRace(raceId);
		}
		Integer[] integerRiderPoints = Arrays.stream(riderMountainPoints).boxed().toArray(Integer[]::new);
		Arrays.sort(integerRiderPoints, Comparator.reverseOrder());
		for (int i = 0; i < integerRiderPoints.length; i++) {
			riderMountainPoints[i] = integerRiderPoints[i].intValue();
		}
		return riderMountainPoints;
		// returns rider mountain points of all riders in race
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		boolean raceIDrecognised = false;
		ArrayList<StageResult> stageResultsInRace = new ArrayList<>();
		ArrayList<Rider> ridersInRace = new ArrayList<>();
		for (Race race: allRaces) {
			if (race.getRaceID() == raceId) {
				raceIDrecognised = true;
				for (int stageID: race.getStageIDs()) {
					getRankedAdjustedElapsedTimesInStage(stageID);
				}
				for (StageResult result: allStageResults) {
					if (result.getRaceID() == raceId) {
						stageResultsInRace.add(result);
					}
				}
			}
		}
		if (stageResultsInRace.isEmpty()) {
			return new int[0];
		}
		if (raceIDrecognised == false) {
			throw new IDNotRecognisedException("Race ID does not match any race in the system");
			// thrown if ID is invalid
		}
		for (Rider rider: allRiders) {
			for (StageResult result: stageResultsInRace) {
				if (rider.getRiderID() == result.getRiderID()) {
					if (ridersInRace.contains(rider)) {
						continue;
					} else {
						ridersInRace.add(rider);
					}
				}
			}
		}
		int[] intAdjustedElapsedTimes = new int[ridersInRace.size()];
		for (int i = 0; i < intAdjustedElapsedTimes.length; i++) {
			intAdjustedElapsedTimes[i] = ridersInRace.get(i).getIntAdjustedElapsedTimeInRace(raceId);
		}
		Arrays.sort(intAdjustedElapsedTimes);
		int[] ridersRankInRace = new int[intAdjustedElapsedTimes.length];
		for (int i = 0; i < ridersRankInRace.length; i++) {
			for (Rider rider: ridersInRace) {
				if (rider.getIntAdjustedElapsedTimeInRace(raceId) == intAdjustedElapsedTimes[i]) {
					if (i == 0) {
						ridersRankInRace[i] = rider.getRiderID();
					} else if (ridersRankInRace[i - 1] == rider.getRiderID()) {
						break;
					}
					ridersRankInRace[i] = rider.getRiderID();
				}
			}
		}
		return ridersRankInRace;
		// returns ranks of riders by general classification in a race
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		boolean raceIDrecognised = false;
		ArrayList<StageResult> stageResultsInRace = new ArrayList<>();
		ArrayList<Rider> ridersInRace = new ArrayList<>();
		for (Race race: allRaces) {
			if (race.getRaceID() == raceId) {
				raceIDrecognised = true;
				for (int stageID: race.getStageIDs()) {
					getRidersPointsInStage(stageID);
				}
				for (StageResult f: allStageResults) {
					if (f.getRaceID() == raceId) {
						stageResultsInRace.add(f);
					}
				}
			}
		}
		if (stageResultsInRace.isEmpty()) {
			return new int[0];
		}
		if (raceIDrecognised == false) {
			throw new IDNotRecognisedException("Race ID does not match any race in the system");
			// thrown if ID is invalid
		}
		for (Rider rider: allRiders) {
			for (StageResult result: stageResultsInRace) {
				if (rider.getRiderID() == result.getRiderID()) {
					if (ridersInRace.contains(rider)) {
						continue;
					} else {
						ridersInRace.add(rider);
					}
				}
			}
		}
		
		int[] riderPoints = new int[ridersInRace.size()];
		for (int i = 0; i < riderPoints.length; i++) {
				riderPoints[i] = ridersInRace.get(i).getPointsInRace(raceId);
			}
		
		Arrays.sort(riderPoints);
		Integer[] integerRiderPoints = Arrays.stream(riderPoints).boxed().toArray(Integer[]::new);
		Arrays.sort(integerRiderPoints, Comparator.reverseOrder());
		for (int i = 0; i < integerRiderPoints.length; i++) {
			riderPoints[i] = integerRiderPoints[i].intValue();
		}
		int[] ridersRankInRace = new int[riderPoints.length];
		for (int i = 0; i < ridersRankInRace.length; i++) {
			for (Rider rider: ridersInRace) {
				if (rider.getPointsInRace(raceId) == riderPoints[i]) {
					if (i == 0) {
						ridersRankInRace[i] = rider.getRiderID();
					} else if (ridersRankInRace[i - 1] == rider.getRiderID()) {
						break;
					}
					ridersRankInRace[i] = rider.getRiderID();					
				}
			}
		}
		return ridersRankInRace;
		// returns ranks of riders by points classification in a race
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		boolean raceIDrecognised = false;
		ArrayList<StageResult> stageResultsInRace = new ArrayList<>();
		ArrayList<Rider> ridersInRace = new ArrayList<>();
		for (Race race: allRaces) {
			if (race.getRaceID() == raceId) {
				raceIDrecognised = true;
				for (int stageID: race.getStageIDs()) {
					getRidersMountainPointsInStage(stageID);
				}
				for (StageResult result: allStageResults) {
					if (result.getRaceID() == raceId) {
						stageResultsInRace.add(result);
					}
				}
			}
		}
		if (stageResultsInRace.isEmpty()) {
			return new int[0];
		}
		if (raceIDrecognised == false) {
			throw new IDNotRecognisedException("Race ID does not match any race in the system");
			// thrown if ID is invalid
		}
		for (Rider rider: allRiders) {
			for (StageResult result: stageResultsInRace) {
				if (rider.getRiderID() == result.getRiderID()) {
					if (ridersInRace.contains(rider)) {
						continue;
					} else {
						ridersInRace.add(rider);
					}
				}
			}
		}		
		int[] riderMountainPoints = new int[ridersInRace.size()];
		for (int i = 0; i < riderMountainPoints.length; i++) {
			riderMountainPoints[i] = ridersInRace.get(i).getMountainPointsInRace(raceId);
		}
		Integer[] integerRiderPoints = Arrays.stream(riderMountainPoints).boxed().toArray(Integer[]::new);
		Arrays.sort(integerRiderPoints, Comparator.reverseOrder());
		for (int i = 0; i < integerRiderPoints.length; i++) {
			riderMountainPoints[i] = integerRiderPoints[i].intValue();
		}
		int[] ridersRankInRace = new int[riderMountainPoints.length];
		for (int i = 0; i < ridersRankInRace.length; i++) {
			for (Rider rider: ridersInRace) {
				if (rider.getMountainPointsInRace(raceId) == riderMountainPoints[i]) {
					if (i == 0) {
						ridersRankInRace[i] = rider.getRiderID();
					} else if (ridersRankInRace[i - 1] == rider.getRiderID()) {
						break;
					}
					ridersRankInRace[i] = rider.getRiderID();
				}
			}
		}
		return ridersRankInRace;
		// returns ranks of riders by mountain points classification in a race
	}
}