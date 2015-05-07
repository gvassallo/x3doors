package x3doors.exporters;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import util.Actionable;
import util.Utils;
import util.XMLFormatter;
import x3doors.Data;
import x3doors.actions.Behaviour;
import x3doors.actions.sensors.AND;
import x3doors.actions.sensors.Click;
import x3doors.actions.sensors.OR;
import x3doors.actions.sensors.Sensor;
import x3doors.nodes.Camera;
import x3doors.nodes.Light;
import x3doors.nodes.Mesh;
import x3doors.nodes.Root;
import x3doors.nodes.SceneObject;
import x3doors.properties.SceneProperties;

class X3DInfo {
	/** The string to insert right before the transform node opening tag while exporting a scene object to X3D */
	public String preTransformX3DString;
	/** The string to insert right after the transform node closing tag while exporting a scene object to X3D */
	public String postTransformX3DString;
	/** true if the related scene object is used as click event trigger in some behaviour */
	public boolean touchSensorDetected;
	
	/** Creates a X3D info for a scene object given these properties. 
	 * 
	 * @param preTransformX3DString The pre-transform string (can be empty too)
	 * @param postTransformX3DString The post-transform string (can be empty too)
	 * @param touchSensorDetected true if the related scene object is used as click event trigger in some behaviour
	 */
	public X3DInfo(String preTransformX3DString, String postTransformX3DString, boolean touchSensorDetected) {
		this.preTransformX3DString = preTransformX3DString;
		this.postTransformX3DString = postTransformX3DString;
		this.touchSensorDetected = touchSensorDetected;
	}
}

public class X3DExporter {
	/* The default path where the scenes will be exported */
	private static String exportingFolderPath = "ExportedScenes/";
	/* If true then the scripts will be embedded in the .x3d file, on the contrary they will be exported in their own .js files */
	private static boolean embeddedScripts = false;
	/* A hash map which maps a scene object handle to its X3D info */
	private static HashMap<Integer, X3DInfo> X3DInfoRegister = new HashMap<Integer, X3DInfo>();
	
	/** Sets the exporting folder path to the given one.
	 * 
	 * @param exportingFolderPath The new exporting folder path
	 */
	public static void setExportingFolderPath(String exportingFolderPath) {
		X3DExporter.exportingFolderPath = exportingFolderPath;
	}
	
	/** @return The exporting folder path */
	public static String getExportingFolderPath() {
		return exportingFolderPath;
	}
	
	/** Sets the embedded scripts mode.
	 * 
	 * @param flag If true forces the exported scripts to be embedded in the .x3d files. If false they will be exported in their own .js files
	 */
	public static void setEmbeddedScripts(boolean flag) {
		embeddedScripts = flag;
		if (flag) {
			Sensor.scriptType = Sensor.ScriptType.EMBEDDED;
			Data.scriptType = Data.ScriptType.EMBEDDED;
		}
		else {
			Sensor.scriptType = Sensor.ScriptType.EXTERNAL;
			Data.scriptType = Data.ScriptType.EXTERNAL;
		}
	}
	
	/** Sets the light sources mode.
	 * 
	 * @param flag If true forces the light sources placeholders to be rendered.
	 */
	public static void setDrawLightSources(boolean flag) {
		Light.drawLightSources = flag;
	}
	
	/* Checks the given sensor type and according to it and to the embedded script mode exports/embeds the scripts (creating
	 * the missing directories hierarchy if necessary).
	 * 
	 * @param sensor The sensor
	 * @throws IOException If the embedded scripts mode is true and has some problem in copying a required script file
	 */
	private static void checkSensor(Sensor sensor) throws IOException {
		String path = X3DExporter.exportingFolderPath + SceneProperties.getTitle() + "/Scripts/"; 
		File dir = new File(path);
		switch (sensor.getType()) {
			case "AND":
				if (!embeddedScripts && !new File(path + "ANDSensor.js").exists()) {
					dir.mkdirs();
					System.out.println("X3DExporter:\tANDSensor.js required.");
					Files.copy(new File("JavascriptSensors/ANDSensor.js").toPath(), new File(path + "ANDSensor.js").toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
				checkSensor(((AND) sensor).getSensor1());
				checkSensor(((AND) sensor).getSensor2());
				break;
			case "Delay":
				if (!embeddedScripts && !new File(path + "DelaySensor.js").exists()) {
					dir.mkdirs();
					System.out.println("X3DExporter:\tDelaySensor.js required.");
					Files.copy(new File("JavascriptSensors/DelaySensor.js").toPath(), new File(path + "DelaySensor.js").toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
				break;
			case "Distance":
				if (!embeddedScripts && !new File(path + "DistanceSensor.js").exists()) {
					dir.mkdirs();
					System.out.println("X3DExporter:\tDistanceSensor.js required.");
					Files.copy(new File("JavascriptSensors/DistanceSensor.js").toPath(), new File(path + "DistanceSensor.js").toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
				break;
			case "Click":
				SceneObject sensorObject = SceneObject.get(((Click) sensor).getObjectToClick());
				X3DInfo sensorObjectX3DInfo = X3DInfoRegister.get(sensorObject.getHandle());
				// If there's already an entry corresponding to the sensor scene object in the X3D info register
				if (sensorObjectX3DInfo != null) {
					if (!sensorObjectX3DInfo.touchSensorDetected) {
						sensorObjectX3DInfo.preTransformX3DString += 	"		<Group DEF=\"" + sensorObject.getName() + "_ClickSensors\">\n" +
																		"		<TouchSensor DEF=\"" + sensor.getName() + "\" description=\"Touch to activate " + sensor.getName() + "\" enabled=\"true\"/>\n";
						sensorObjectX3DInfo.touchSensorDetected = true;
					}
				}
				else {
					X3DInfoRegister.put(sensorObject.getHandle(),
							sensorObjectX3DInfo = new X3DInfo(
									"		<Group DEF=\"" + sensorObject.getName() + "_ClickSensors\">\n" +
									"		<TouchSensor DEF=\"" + sensor.getName() + "\" description=\"Touch to activate " + sensor.getName() + "\" enabled=\"true\"/>\n",
									"		</Group>\n",
									true
									
							)
					);
				}
				break;
			case "OR":
				if (!embeddedScripts && !new File(path + "ORSensor.js").exists()) {
					dir.mkdirs();
					System.out.println("X3DExporter:\tORSensor.js required.");
					Files.copy(new File("JavascriptSensors/ORSensor.js").toPath(), new File(path + "ORSensor.js").toPath());
				}
				checkSensor(((OR) sensor).getSensor1());
				checkSensor(((OR) sensor).getSensor2());
				break;
		}
	}
	
	/* Checks the given data copying its content in the default directory and the required scripts according to the embedded scripts
	 * mode (creating the missing directory hierarchy if necessary)
	 * 
	 * @param data The data
	 * @throws IOException If the embedded scripts mode is true and has some problem in copying a required script file
	 */
	private static void checkData(Data data) throws IOException {
		switch (data.getType()) {
			case "AudioPlayer":
				//String content = ((AudioPlayer) data).getContent();
		//		File contentFile = new File(content);
				String title = SceneProperties.getTitle();
				String path = exportingFolderPath + title + "/Audio/";
				File dir = new File(path);
				dir.mkdirs();
//				if (!new File(path + contentFile.getName()).exists()) {
//					Files.copy(contentFile.toPath(), new File(X3DExporter.getExportingFolderPath() + title + "/Audio/" + contentFile.getName()).toPath());
//					System.out.println("X3DExporter\t" + content + " copied.");
//				}
				path = X3DExporter.getExportingFolderPath() + title + "/Scripts/";
				dir = new File(path);
				if (!embeddedScripts && !new File(path + "AudioPlayer.js").exists()) {
					dir.mkdirs();
					System.out.println("X3DExporter:\tAudioPlayer.js required.");
					Files.copy(new File("JavascriptSensors/AudioPlayer.js").toPath(), new File(path + "AudioPlayer.js").toPath());
				}
				path = X3DExporter.getExportingFolderPath() + title + "/Textures/";
				dir = new File(path);
				if (!new File(path + "play.png").exists()) {
					dir.mkdirs();
					Files.copy(new File("Textures/play.png").toPath(), new File(path + "play.png").toPath());
					System.out.println("X3DExporter\tplay.png copied.");
				}
				if (!new File(path + "pause.png").exists()) {
					dir.mkdirs();
					Files.copy(new File("Textures/pause.png").toPath(), new File(path + "pause.png").toPath());
					System.out.println("X3DExporter\tpause.png copied.");
				}
				if (!new File(path + "stop.png").exists()) {
					dir.mkdirs();
					Files.copy(new File("Textures/stop.png").toPath(), new File(path + "stop.png").toPath());
					System.out.println("X3DExporter\tstop.png copied.");
				}
				break;
		}
	}
	
	/* Returns the X3D string of the scene graph having the given scene object as root.<p>
	 * When necessary, it adds the X3D info strings to the scene object X3D string obtained its toX3D method invocation. 
	 * 
	 * @param sceneObject
	 * @return The X3D string of the scene graph having the given scene object as root
	 */
	private static String getSceneObjectX3DString(SceneObject sceneObject) {
		String X3DString = "";
		X3DInfo sceneObjectX3DInfo = X3DInfoRegister.get(sceneObject.getHandle());
		boolean sceneObjectIsCamera = sceneObject instanceof Camera;
		boolean sceneObjectIsLight = sceneObject instanceof Light;
		boolean sceneObjectIsMesh = sceneObject instanceof Mesh;
		if (sceneObjectX3DInfo != null) {
			X3DString += 	sceneObjectX3DInfo.preTransformX3DString;
		}
		if (!sceneObjectIsLight && (!sceneObjectIsCamera || (sceneObjectIsCamera && (sceneObject == SceneProperties.getActiveCamera())))) {
				X3DString += 	"		<Transform DEF=\"" + sceneObject.getName() + (sceneObjectIsLight ? "_Placeholder" : "") + "_Translation\" translation=\"" + sceneObject.transform.localTranslationCoordinates.toX3D() + "\">\n" +
								"		<Transform DEF=\"" + sceneObject.getName() + (sceneObjectIsLight ? "_Placeholder" : "") + "_Rotation\" rotation=\"" + sceneObject.transform.localRotationCoordinates.toX3D() + "\">\n" +
								"		<Transform scale=\"" + Utils.double2StringFormat(sceneObject.transform.localScaleFactor.x) + " " + Utils.double2StringFormat(sceneObject.transform.localScaleFactor.y) + " " + Utils.double2StringFormat(sceneObject.transform.localScaleFactor.z) + "\">\n" + (sceneObjectIsMesh ?
								"		<Switch DEF=\"" + sceneObject.getName() + "_Switch\" whichChoice=\"" + (sceneObject.visible ? "0" : "-1") + "\">\n" +
								"		<Group DEF=\"" + sceneObject.getName() +"_SwitchGroup\">\n" : "");	
		}
		if (!sceneObjectIsCamera || (sceneObjectIsCamera && (sceneObject == SceneProperties.getActiveCamera()))) {
			X3DString += 						sceneObject.toX3D();
		}
		for (SceneObject child : sceneObject.getChildren()) {
			X3DString += getSceneObjectX3DString(child);
		}
		
		if (!sceneObjectIsLight && (!sceneObjectIsCamera || (sceneObjectIsCamera && (sceneObject == SceneProperties.getActiveCamera())))) {
			X3DString+=		(sceneObjectIsMesh?
							"		</Group>\n" +
							"		</Switch>\n" : "") + 
							"		</Transform>\n" +
							"		</Transform>\n" +
							"		</Transform>\n";
		}
		if (sceneObjectX3DInfo != null) {
			X3DString += sceneObjectX3DInfo.postTransformX3DString;
		}
		return X3DString;
	}
	
	/** Exports the scene graph with the root scene object as root to X3D.
	 * 
	 * @throws Exception
	 */
	public static void export() throws Exception {
		if (!embeddedScripts) {
			System.out.println("X3DExporter:\trequired scripts will be copied into the ExportedScenes folder.");
		}
		else {
			System.out.println("X3DExporter:\trequired scripts will be embedded.");
		}
		String title = SceneProperties.getTitle();
		File dir = new File(exportingFolderPath + title);
		// Create a new folder for the exported scene
		dir.mkdir();
		// Create the exported scene file
		PrintWriter out = new PrintWriter(exportingFolderPath + title + "/" + title + ".x3d", "UTF-8");
		
		// Get the scene properties X3D strings (header, metadata, background or skybox)
		String X3DString = SceneProperties.getInstance().toX3D();
		
		Behaviour behaviour = null;
		Sensor sensor = null;
		Actionable action = null;
		String behavioursX3DString = "";
		// Get the behaviours X3D strings
		for (int i = 0; i < Behaviour.registerSize(); i++) {
			behaviour = Behaviour.get(i);
			sensor = behaviour.getSensor();
			checkSensor(sensor);
			action = behaviour.getAction();
			if (action instanceof Data) {
				checkData((Data) action);
			}
			behavioursX3DString += behaviour.toX3D();
		}
		
		System.out.println("X3DExporter:\tbehaviours successfully exported.");
		
		Camera activeCamera = SceneProperties.getActiveCamera();
		X3DInfoRegister.put(activeCamera.getHandle(),
							new X3DInfo(
										"		<ProximitySensor DEF=\"HereIAm_World\" size=\"100000 100000 100000\"/>\n",
										"		<ROUTE fromNode=\"HereIAm_World\" fromField=\"position_changed\" toNode=\"" + activeCamera.getName() + "_Translation\" toField=\"set_translation\"/>\n" +
										"		<ROUTE fromNode=\"HereIAm_World\" fromField=\"orientation_changed\" toNode=\"" + activeCamera.getName() + "_Rotation\" toField=\"set_rotation\"/>\n",
										true
							)
		);
		
		// Get the scene object X3D strings
		String sceneObjectsX3DString = getSceneObjectX3DString(Root.getInstance());
		
		System.out.println("X3DExporter:\tscene objects successfully exported.");
		
		X3DString += 	"		<" + (activeCamera.perspective ? "" : "Ortho") + "Viewpoint DEF=\"" + activeCamera.getName() + "_View\" description=\"" + activeCamera.description + "\" position=\"" + activeCamera.transform.worldTranslationCoordinates.toX3D() + "\" orientation=\"" + activeCamera.transform.worldRotationCoordinates.toX3D() + "\" fieldOfView=\"" + Utils.double2StringFormat(activeCamera.verticalAngle * 3.14 / 180) + "\"/>\n" +
								sceneObjectsX3DString +
								(behavioursX3DString.equals("") ? "" :
						"		<Group DEF=\"Interactive\">\n") +
									behavioursX3DString +
									(behavioursX3DString.equals("") ? "" :
						"		</Group>\n") +
						"	</Scene>\n" +
						"</X3D>\n";
		try {
			out.print(new XMLFormatter(X3DString).getXML());
		}
		catch (java.net.SocketTimeoutException exception) {
			System.out.println("X3DExporter:\tconnection to the pretty-print server timed out, unable to pretty-print the output X3D file.");
			out.print(X3DString);
		}
		catch (java.net.UnknownHostException exception) {
			System.out.println("X3DExporter:\tconnection to the pretty-print server failed, unable to pretty-print the output X3D file.");
			out.print(X3DString);
		}
		finally {
			out.close();
		}
		/* out.print(X3DString);
		out.close(); */
	}
}
