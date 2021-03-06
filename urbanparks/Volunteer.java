package urbanparks;

import java.util.ArrayList;
import java.util.Calendar;

public class Volunteer extends User {
	
	// todo: move to constants class
	private final static int MIN_DAYS_BEFORE_SIGNUP = 2;
	private final static int MILLISECONDS_IN_DAY = 86400000;
	
	public class volunteerJobOverlapException extends Exception {
		public volunteerJobOverlapException() {}
	    public volunteerJobOverlapException(String message) {
	        super(message);
	    }
	}
	public class jobSignupTooLateException extends Exception {
		public jobSignupTooLateException() {}
	    public jobSignupTooLateException(String message) {
	        super(message);
	    }
	}
	
	private ArrayList<Job> acceptedJobs;
	
	/**
	 * Constructor for Volunteer class
	 */
	public Volunteer(String firstName, String lastName, String email, String phoneNum) {
		super(firstName, lastName, email, phoneNum);
		acceptedJobs = new ArrayList<Job>();
	}
	
	/**
	 * Signs up this volunteer for a job
	 * 
	 * @param candidateJob the job to be signed up for
	 * @throws volunteerJobOverlapException
	 * @throws jobSignupTooLateException
	 */
	public void signUpForJob(Job candidateJob) throws volunteerJobOverlapException, jobSignupTooLateException {

		/**
		 * Checks business rule "A volunteer cannot sign up for more than one job 
		 * that extends across any particular calendar day"
		 */
		if (doesNewJobOverlap(candidateJob)) {
			throw new volunteerJobOverlapException();
		}
		
		/**
		 * Checks business rule "A volunteer may sign up only if the job begins
		 *  at least a minimum number of calendar days after the current date"
		 */
		if (isSignupEarlyEnough(candidateJob)) {
			throw new jobSignupTooLateException();
		}
		acceptedJobs.add(candidateJob);
	}
	
	/**
	 * Checks if a candidate job's start/end days equal those of any other 
	 * job this Volunteer is signed up for
	 * 
	 * @param candidateJob
	 * @return
	 */
	public boolean doesNewJobOverlap(Job candidateJob) {
		for (Job j : acceptedJobs) {
			if (are2DatesOnSameDay(candidateJob.getStartDateTime(), j.getStartDateTime())) {
				return false;
			}
			if (are2DatesOnSameDay(candidateJob.getStartDateTime(), j.getEndDateTime())) {
				return false;
			}
			if (are2DatesOnSameDay(candidateJob.getEndDateTime(), j.getEndDateTime())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if 2 Calendar objects are on the same calendar day
	 */
	// todo: put in a utility class if others need to use.
	private static boolean are2DatesOnSameDay(Calendar cal1, Calendar cal2) {
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && 
				cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * Checks whether the time between now and the job start time is at least the minimum value,
	 *  for job sign up.
	 * 
	 * @param theCandidateJob
	 * @return true if there is enough time between now and when the job starts, false otherwise.
	 */
	public static boolean isSignupEarlyEnough(Job theCandidateJob) {
		Calendar now = Calendar.getInstance();
		long diff = theCandidateJob.getStartDateTime().getTimeInMillis() - now.getTimeInMillis();
		long minDaysInMillis = MIN_DAYS_BEFORE_SIGNUP * MILLISECONDS_IN_DAY;
		return (diff >= minDaysInMillis);
	}
}