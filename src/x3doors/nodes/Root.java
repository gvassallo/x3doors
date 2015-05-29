package x3doors.nodes; 

import math.Vec3;
import math.Vec4;

import org.w3c.dom.Element;

import util.MyNodeList;

public class Root extends SceneObject {
	/* The root singleton instance */
	private static Root instance = null;
	
	/** Creates a root object with parent = null and the given properties.
	 * 
	 * @param worldTranslationCoordinates The initial world translation coordinates
	 * @param worldRotationCoordinates The initial world rotation coordinates
	 * @param worldScaleFactor The initial world scale factor
	 * @param visible The initial rendering state, if true then the scene object and its children are rendered
	 * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
	 */
	private Root(Vec3 worldTranslationCoordinates, Vec4 worldRotationCoordinates, Vec3 worldScaleFactor, boolean visible) throws Exception {
		super(worldTranslationCoordinates, worldRotationCoordinates, worldScaleFactor, visible, null);
	}
	
	/** Returns the singleton root instance creating it if it doesn't exist yet.
	 * 
	 * @return The root object
	 * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
	 */
	public static Root getInstance() throws Exception {
		if (instance == null) {
			instance = new Root(new Vec3(0, 0, 0), new Vec4(1, 0, 0, 0), new Vec3(1, 1, 1), true);
			instance.name = "Root";
		}
		return instance;
	}
	
    public MyNodeList toX3Dom(){
    	return (new MyNodeList()); 
    }
}
