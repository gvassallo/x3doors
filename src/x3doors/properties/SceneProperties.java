package x3doors.properties;

import util.X3DExportable;
import util.Metadata;
import x3doors.nodes.Camera;

/* Implemented using the singleton design pattern, there can only be a single scene properties instance */
public class SceneProperties implements X3DExportable {
	/* This counter is used to assign the scene name automatically when it is not specified */
	private static int counter = 0;
	
	/* The scene properties singleton instance */
	private static SceneProperties instance = null;
	/* The scene title */
	private String title;
	/* The scene description */
	private String description;
	/* The scene creator */
	private String creator;
	/* The scene background color (default value is [0, 0, 0]) */
	private Background background;
	/* The scene skybox (optional) */
	private Skybox skybox;
	/* The scene active camera */
	private Camera activeCamera;
	
	/* Creates the singleton instance with its default values */
	private SceneProperties() {
		title = (title == null || title.equals("")) ? "NewScene" + counter++ : title;
		description = "";
		creator = "Luca Martini";
		background = new Background(0, 0, 0);
		skybox = null;
		try {
			activeCamera = new Camera();
		}
		catch (Exception e) {
			System.out.println("Unable to initialize the default active camera\n");
		}
	}
	
	/** Returns the singleton scene properties instance creating it if it doesn't exist yet.
	 * 
	 * @return The scene properties instance
	 */
	public static SceneProperties getInstance() {
		if (instance == null) {
			instance = new SceneProperties();
		}
		return instance;
	}
	
	/** Sets the new scene title.
	 * 
	 * @param title The new title
	 */
	public static void setTitle(String title) {
		SceneProperties.getInstance().title = title;
	}
	
	/** @return The scene title */
	public static String getTitle() {
		return SceneProperties.getInstance().title;
	}
	
	/** Sets the new scene description.
	 * 
	 * @param description The new description
	 */
	public static void setDescription(String description) {
		SceneProperties.getInstance().description = description;
	}
	
	/** @return The scene description */
	public static String getDescription() {
		return SceneProperties.getInstance().description;
	}
	
	/** Sets the new scene creator.
	 * 
	 * @param creator The new scene creator
	 */
	public static void setCreator(String creator) {
		SceneProperties.getInstance().creator = creator;
	}
	
	/** @return The scene creator */
	public static String getCreator() {
		return SceneProperties.getInstance().creator;
	}

	/** Sets the scene background color.
	 * 
	 * @param r The red component
	 * @param g The green component
	 * @param b The blue component
	 */
	public static void setBackgroundColor(int r, int g, int b) {
		// If skybox == null then the background color must be exported
		SceneProperties.getInstance().background = new Background(r, g, b);
		SceneProperties.getInstance().skybox = null;
	}
	
	/** Sets the scene skybox images.
	 * 
	 * @param frontUrl The url of the front skybox image
	 * @param backUrl The url of the back skybox image
	 * @param leftUrl The url of the left skybox image
	 * @param rightUrl The url of the right skybox image
	 * @param topUrl The url of the top skybox image
	 * @param bottomUrl The url of the bottom skybox image
	 * @throws Exception When trying to specify a skybox image which does not exist or with an unsupported extension
	 */
	public static void setSkybox(String frontUrl, String backUrl, String leftUrl, String rightUrl, String topUrl, String bottomUrl) throws Exception {
		// If background == null then the skybox must be exported
		instance.background = null;
		instance.skybox = new Skybox(frontUrl, backUrl, leftUrl, rightUrl, topUrl, bottomUrl);
	}
	
	/** Sets the given camera to be the active one.
	 * 
	 * @param activeCamera The camera to set as active
	 */
	public static void setActiveCamera(Camera activeCamera) {
		SceneProperties.getInstance().activeCamera = activeCamera;
	}
	
	/** @return The active camera */
	public static Camera getActiveCamera() {
		return SceneProperties.getInstance().activeCamera;
	}
	
	/** @return The scene properties X3D string */
	public String toX3D() {		
		String X3DString =	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
							"<!DOCTYPE X3D PUBLIC \"ISO//Web3D//DTD X3D 3.3//EN\" \"http://www.web3d.org/specifications/x3d-3.3.dtd\">\n" +
							"<X3D profile=\'Immersive\' version=\'3.3\' xmlns:xsd=\'http://www.w3.org/2001/XMLSchema-instance\' xsd:noNamespaceSchemaLocation=\'http://www.web3d.org/specifications/x3d-3.3.xsd\'>\n" +
							"	<head>\n" + (!activeCamera.isPerspective() ?
							"		<component level=\"3\" name=\"Navigation\"/>\n" : "") +
									new Metadata(title + ".x3d", "title").toX3D() +
									new Metadata(description, "description").toX3D() +
									new Metadata(creator, "creator").toX3D() +
									new Metadata("http://someURI/" + title + ".x3d", "identifier").toX3D() +
									new Metadata("X3Doors X3D exporter", "generator").toX3D() +
							"	</head>\n" +
							"	<Scene>\n";		
		System.out.println("X3DExporter:\tfile header created.");
		
		X3DString += (background != null ? background.toX3D() : skybox.toX3D());
		return X3DString;
	}
}
