import cycling.CyclingPortal;
import cycling.BadMiniCyclingPortal;
import cycling.CyclingPortalInterface;
import cycling.DuplicatedResultException;
import cycling.IDNotRecognisedException;
import cycling.IllegalNameException;
import cycling.InvalidCheckpointsException;
import cycling.InvalidLengthException;
import cycling.InvalidLocationException;
import cycling.InvalidNameException;
import cycling.InvalidStageStateException;
import cycling.InvalidStageTypeException;
import cycling.MiniCyclingPortalInterface;
import cycling.NameNotRecognisedException;
import cycling.SegmentType;
import cycling.StageType;
import cycling.Team;

import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the CyclingPortalInterface interface. 
 * @author Diogo Pacheco, 710010983, 710029379
 * @version 1.1
 */
public class CyclingPortalInterfaceTestApp {

	/**
	 * Test method for CyclingPortal.
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println("The system compiled and started the execution...");

		//MiniCyclingPortalInterface portal = new BadMiniCyclingPortal();
		CyclingPortalInterface portal = new CyclingPortal();

		assert (portal.getRaceIds().length == 0)
				: "Innitial SocialMediaPlatform not empty as required or not returning an empty array.";

	}
}