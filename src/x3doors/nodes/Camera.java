package x3doors.nodes; 

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import math.Vec3;
import math.Vec4;

import util.MyNodeList;

import x3doors.DocInstance;

public class Camera extends SceneObject {
	private static final double AVATAR_HEIGHT = 1.6;
	/** Defines the scene explore mode types, can assume the following values<p>
	 * MANIPULATOR: examine<p>
	 * HUMAN: explore as a human avatar<p>
	 * HELICOPTER: fly like an helicopter<p>
	 * DISABLED: disables any interactivity with the scene */
	public enum exploreModeType {
		MANIPULATOR,
		HUMAN,
		HELICOPTER,
		DISABLED
	}
	
	/* A description of the camera */
	public String description;
	/* The camera near clipping plane */
	// 3Doors range: [0.0001, 9999.9999]
	private double nearPlane;
	/* The camera far clipping plane */
	// 3Doors range: [0.1, 100000000.0]
	private double farPlane;
	/* The field of view */
	// 3Doors range: [10.0, 180.0]
	public double verticalAngle;
	// The aspect ratio is useless since it depends from the X3D players window's size
	// 3Doors options: 16:9 / 4:3
	// private double aspectRatio;
	/* The kind of projection of the camera, if true then perspective projection, else orthogonal projection */
	public boolean perspective;
	/* The speed of the camera's avatar when moving */
	// 3Doors range: [0.0, 100.0];
	private double speed;
	// TODO: Find a way to capture these parameters in the X3D exporting process
	// private double speedMin;
	// private double speedMax;
	// private double rotationSpeed;
	// private double walkDistance;
	// private double gravity;
	/* The tallest height the avatar can walk over */
	// 3Doors range: [0.001, 999999.0]
	private double walkHeight;
	/* The scene explore mode type */
	private exploreModeType startingExploreMode;
	
	/** Creates a camera using root as parent and the 3Doors default values.
	 * 
	 * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
	 */
	public Camera() throws Exception {
		this(Root.getInstance());
	}
	
	/** Creates a camera using the given parameters and the 3Doors default values for the unspecified ones.
	 * 
	 * @param parent The parent
	 * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
	 */
	public Camera(SceneObject parent) throws Exception {
		// The default X3D camera orientation is [0, 0, -1] (inside the screen)
		// The default 3Doors rotation can be obtained by rotating the default X3D camera orientation this way:
		// - counterclockwise rotation of 45° around the y-axis, [1, 0, 0, 0.785]
		// - counterclockwise rotation of 45° around the z-axis first rotated counterclocwise of 45° around the y-axis, that is [[0, 0, 1] * [0, 1, 0, 0.785], 0.785] = [-0.6784, 0.6786, 0.2815, 1.0955]
		this("", "", new Vec3(943.799, 1334.734, 943.799), new Vec4(-0.6784, 0.6786, 0.2815, 1.0955), 0.1, 10000.0, 44.0, true, 50.0, 1.0, exploreModeType.MANIPULATOR, parent);
	}
	
	/** Creates a camera using the given parameters.
	 * 
	 * @param name The name
	 * @param description A description
	 * @param worldTranslationCoordinates The initial world translation coordinates
	 * @param worldRotationCoordinates The initial world rotation coordinates
	 * @param nearPlane The initial near clipping plane
	 * @param farPlane The initial far clipping plane
	 * @param verticalAngle The initial field of view
	 * @param perspective If true then perspective projection, else orthogonal projection
	 * @param speed The maximum moving speed
	 * @param walkHeight The tallest height the avatar can walk over
	 * @param startingExploreMode The initial explore mode type
	 * @param parent The parent
	 * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
	 */
	public Camera(String name, String description, Vec3 worldTranslationCoordinates, Vec4 worldRotationCoordinates, double nearPlane, double farPlane, double verticalAngle, boolean perspective, double speed, double walkHeight, exploreModeType startingExploreMode, SceneObject parent) throws Exception {
		super(worldTranslationCoordinates, worldRotationCoordinates, new Vec3(1.0, 1.0, 1.0), true, parent);
		this.name = (name == null || name.equals("")) ? ("Camera_" + handle) : name;
		this.description = (description == null || description.equals("")) ? this.name : description;
		
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.verticalAngle = verticalAngle;
		this.perspective = perspective;
		this.speed = speed;
		this.walkHeight = walkHeight;
		this.startingExploreMode = startingExploreMode;
	}
	
	/** Creates a camera using root as parent and the given parameters.
	 * 
	 * @param name The name
	 * @param description A description
	 * @param worldTranslationCoordinates The initial world translation coordinates
	 * @param worldRotationCoordinates The initial world rotation coordinates
	 * @param nearPlane The initial near clipping plane
	 * @param farPlane The initial far clipping plane
	 * @param verticalAngle The initial field of view
	 * @param perspective If true then perspective projection, else orthogonal projection
	 * @param speed The maximum moving speed
	 * @param walkHeight The tallest height the avatar can walk over
	 * @param startingExploreMode The initial explore mode type
	 * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
	 */
	public Camera(String name, String description, Vec3 worldTranslationCoordinates, Vec4 worldRotationCoordinates, double nearPlane, double farPlane, double verticalAngle, boolean perspective, double speed, double walkHeight, exploreModeType startingExploreMode) throws Exception {
		this(name, description, worldTranslationCoordinates, worldRotationCoordinates, nearPlane, farPlane, verticalAngle, perspective, speed, walkHeight, startingExploreMode, Root.getInstance());
	}
	
	
	 /** @return true if the camera is perspective, false otherwise */
	public boolean isPerspective() {
		return perspective;
	}
	
	/** @return This camera X3D string */
	public String toX3D() {
		String X3DString =	"		<NavigationInfo type=\'\"" + getType() + "\"\' visibilityLimit=\"" + farPlane + "\" speed=\"" + speed + "\" avatarSize=\"" + nearPlane + " " + getAvatarHeight() + " " + walkHeight +  "\" headlight=\"false\"/>\n";
		return X3DString;
	}
    public String getType(){ 
        String navigationTypeX3DString = new String(); 
		switch (startingExploreMode) {
		case MANIPULATOR:
			navigationTypeX3DString += "EXAMINE\" \"ANY";
			break;
		case HUMAN:
			navigationTypeX3DString += "WALK\" \"ANY";
			break;
		case HELICOPTER:
			navigationTypeX3DString += "FLY\" \"ANY";
			break;
		case DISABLED:
			navigationTypeX3DString += "NONE";
			break;
		}
        return navigationTypeX3DString; 
    }
	public static double getAvatarHeight() {
		return AVATAR_HEIGHT;
	}

	@Override
	public MyNodeList toX3Dom() {
        MyNodeList wrapper = new MyNodeList(); 
        Document doc = DocInstance.getInstance(); 
        Element navigationInfo = doc.createElement("NavigationInfo"); 
        navigationInfo.setAttribute("type", getType()); 
        navigationInfo.setAttribute("visibilityLimit", ((Double)farPlane).toString()); 
        navigationInfo.setAttribute("speed", ((Double)speed).toString()); 
        navigationInfo.setAttribute("avatarSize", nearPlane+" "+getAvatarHeight()+" "+walkHeight); 
        navigationInfo.setAttribute("headlight", "false"); 
        wrapper.appendChild(navigationInfo);
		return wrapper;
	}
}

