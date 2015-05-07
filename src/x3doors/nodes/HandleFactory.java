package x3doors.nodes;

//TODO: This class is a "fake" one, should be substituted by a true handle managing class
public class HandleFactory {
	/* The handle to increment to each scene object creation */
	private static int handle = 0;
	
	/** @return A new handle */
	public static int getNewHandle() {
		return handle++;
	}
}