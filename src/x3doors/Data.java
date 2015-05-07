package x3doors;

import java.io.File;
import x3doors.nodes.*;

import util.Actionable;
import util.RGBColor;
import util.Utils;

public abstract class Data implements Actionable {
	/** The script exporting mode, editable by the {@link X3DExporter} static method setEmbeddedScripts(boolean embeddedScripts).
	 * @see ScriptType */
	public static ScriptType scriptType = ScriptType.EXTERNAL;
	/* This array is used to check the correct content's extension */
	private final static String[] X3D_AUDIO_SUPPORTED_EXTENSIONS = new String[] {"mid", "wav"};
	
	/* This counter is used to assign the data name automatically when it is not specified */
	private static int counter = 0;
	
	/** The data name. */
	protected String name;
	/** The data type */
	protected String type;
	/** The data handle. */
	protected int handle;
	/** The data user interface type. */
	protected String userInterface;
	/** The data content. */
	protected String content;
	
	/** Defines the data type, can assume the following values<p> 
	 * AUDIO_PLAYER: an audio player which plays the specified content<p>
	 * PDF_VIEWER: a pdf viewer which displays the specified pdf document<p>
	 * VIDEO_PLAYER: a video player which shows the specified content */
	protected enum Type {
		AUDIO_PLAYER,
		PDF_VIEWER,
		VIDEO_PLAYER
	}
	
	/** Defines the data interface type:<p>
	 * NONE: does not display any kind of interface<p>
	 * _2D_MODELESS: should open an external program to best interpret the specified content. When not possible automatically
	 * uses the _3D_MODELESS type<p>
	 * _3D_MODELESS: should open an internal scene interface to best interpret the specified content */
	protected enum UserInterface {
		NONE,
		_2D_MODELESS,
		_3D_MODELESS
	}
	
	/** Defines the script exporting mode, can assume one of the following values<p>
	 * EMBEDDED: when exporting to X3D scripts are directly embedded in the X3D file. This increments the file size and causes
	 * the script code to be redundant, may be necessary for solving some X3D player compatibility issue<p>
	 * EXTERNAL: when exporting to X3D scripts are exported as independent files */
	public enum ScriptType {
		EMBEDDED,
		EXTERNAL
	}
	
	/** Creates a data with the given properties. 
	 * 
	 * @param name The name
	 * @param type The type
	 * @param userInterface The user interface type
	 * @param content The content
	 * @throws Exception When trying to specify a content which does not exist or with an unsupported extension
	 */
	public Data(String name, Type type, UserInterface userInterface, String content) throws Exception {
		Utils.checkContentsExistences(content);
		switch (type) {
			case AUDIO_PLAYER:
				Utils.checkContentsExtensions(X3D_AUDIO_SUPPORTED_EXTENSIONS, content);
				this.type = "AudioPlayer";
				break;
			case PDF_VIEWER:
				Utils.checkContentsExtensions(new String[] {"pdf"}, content);
				this.type = "PDFViewer";
				break;
			case VIDEO_PLAYER:
				break;
		}
		this.name = (name == null || name.equals("")) ? (this.type + "Data_" + counter++) : name;
		this.handle = HandleFactory.getNewHandle();
		switch (userInterface) {
			case NONE:
				this.userInterface = "None";
				break;
			case _2D_MODELESS:
				this.userInterface = "2DModeless";
				break;
			case _3D_MODELESS:
				this.userInterface = "3DModeless";
				break;
		}
		this.content = content;
	}
	
	/** @return The name */
	public String getName() {
		return name;
	}
	
	/** @return The type */
	public String getType() {
		return type;
	}
	
	/** @return The content */
	public String getContent() {
		return content;
	}
}

class AudioPlayer extends Data {
	/* The univocal audio player id */
	private static int id = 0;
	
	/* The default interface background color constant */
	private static final RGBColor INTERFACE_BACKGROUND_COLOR = new RGBColor(0, 0, 255);
	/* The default interface bar color constant */
	private static final RGBColor INTERFACE_BAR_COLOR = new RGBColor(0, 255, 0);
	/* The default interface timer slider color constant */
	private static final RGBColor INTERFACE_TIMER_SLIDER_COLOR = new RGBColor(255, 0, 0);
	/* The default interface volume slider color constant */
	private static final RGBColor INTERFACE_VOLUME_SLIDER_COLOR = new RGBColor(255, 255, 0);
	/* The default interface timer info color constant */
	private static final RGBColor INTERFACE_TIMER_INFO_COLOR = new RGBColor(0, 255, 255);
	/* The default offset between multiple audio player interfaces */
	private static final double INTERFACES_OFFSET = 0.021;

	/* The default interface play button texture path */
	public static final String INTERFACE_PLAY_BUTTON_TEXTURE = "Textures/play.png";
	/* The default interface pause button texture path */
	public static final String INTERFACE_PAUSE_BUTTON_TEXTURE = "Textures/pause.png";
	/* The default interface stop button texture path */
	public static final String INTERFACE_STOP_BUTTON_TEXTURE = "Textures/stop.png";
	
	// private boolean useContentSize;
	private boolean close;
	/* Handled by userInterface variable defined in super class */
	// private boolean showWindow;
	// private boolean showMaximized;
	
	/** Create an audio player with the given properties.
	 * 
	 * @param name The name
	 * @param userInterface The user interface type
	 * @param content The content
	 * @param close If true then the audio player interface will be close after its content playing
	 * @throws Exception When trying to specify a content which does not exist or with an unsupported extension
	 */
	public AudioPlayer(String name, UserInterface userInterface, String content, boolean close) throws Exception {
		super(name, Type.AUDIO_PLAYER, userInterface, content);
		this.close = close;
		// id = ++counter;
	}
	
	/** @return This data X3D string */
	public String toX3D() {
		/* id must be incremented here and not during the audio player creation, elsewhere some X3D "USEd" node won't work correctly */
		id++;
		String contentName = new File(content).getName();
		boolean showAudioPlayer = !userInterface.equals("None");
		String X3DString =	(id == 1 && showAudioPlayer ?
							"				<ProximitySensor DEF=\"HereIAm_Local\" size=\"100000 100000 100000\"/>\n" 
							: "") +
							"				<TimeTrigger DEF=\"" + name + "_Trigger\"/>\n" +
							"				<Sound DEF=\"" + name + "_Sound\" location=\"0 " + Camera.getAvatarHeight() + " 0\" minBack=\"9999\" minFront=\"9999\" maxBack=\"10000\" maxFront=\"10000\" spatialize=\"false\">\n" +
							"					<AudioClip DEF=\"" + name + "_Clip\" loop=\"false\" pitch=\"1\" description=\"" + name + " audio clip\" url=\'\"Audio/" + contentName + "\" \"http://someURL/Audio/" + contentName + "\"\'/>\n" +
							"				</Sound>\n" +
							"				<ROUTE fromNode=\"" + name + "_Clip\" fromField=\"isActive\" toNode=\"" + name + "_Clip\" toField=\"loop\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_Trigger\" fromField=\"triggerTime\" toNode=\"" + name + "_Clip\" toField=\"set_startTime\"/>\n";
		if (showAudioPlayer) {
			X3DString += 
							"				<Transform DEF=\"PlayerInterface" + id + "_Transform\">\n" +
							"				<Group>\n" +
							"				<PlaneSensor DEF=\"PlayerInterface" + id + "Dragger\" description=\"Click and drag player\" maxPosition=\"10000 10000\" minPosition=\"-10000 -10000\"/>\n" +
							"				<Transform DEF=\"PlayerInterface" + id + "Offset_Transform\">\n" +
							"					<Switch DEF=\"PlayerInterface" + id + "_Switch\" whichChoice=\"-1\">\n" +
							"						<Group>\n" +
							"							<Transform translation=\"0 0 " + Utils.double2StringFormat(-1 - INTERFACES_OFFSET * (id - 1)) + "\" scale=\"" + Utils.double2StringFormat(10 + (10 * INTERFACES_OFFSET * (id - 1))) + " " + Utils.double2StringFormat(6 + (6 * INTERFACES_OFFSET  * (id - 1))) + " 1\">\n" +
							"								<Shape>\n" +
							"									<Appearance>\n" +
							"										<Material emissiveColor=\"" + INTERFACE_BACKGROUND_COLOR.toX3D() + "\"/>\n" +
							"									</Appearance>\n" + (id == 1 ?
							"									<IndexedFaceSet DEF=\"IndexedFaceSet_Square\" colorPerVertex=\"false\" coordIndex=\"0 1 3 -1 0 3 2\" solid=\"false\">\n" +
							"										<Coordinate point=\"-0.025 -0.025 -0 0.025 -0.025 0 -0.025 0.025 0 0.025 0.025 0\"/>\n" +
							"										<Normal vector=\"0 0 1 0 0 1 0 0 1 0 0 1\"/>\n" +
							"										<TextureCoordinate point=\"0 0 1 0 0 1 1 1\"/>\n" +
							"									</IndexedFaceSet>\n" :
							"									<IndexedFaceSet USE=\"IndexedFaceSet_Square\"/>\n") +
							"								</Shape>\n" +
							"							</Transform>\n" +
							"							<Transform scale=\"" + Utils.double2StringFormat(8 + (8 * INTERFACES_OFFSET * (id - 1))) + " " + Utils.double2StringFormat(0.1 + (0.1 * INTERFACES_OFFSET  * (id - 1))) + " 1\">\n" +	
							"								<Transform translation=\"0 1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + "\">\n" + (id == 1 ?
							"									<Shape DEF=\"Bar\">\n" +
							"										<Appearance>\n" +
							"											<Material emissiveColor=\"" + INTERFACE_BAR_COLOR.toX3D() + "\"/>\n" +
							"										</Appearance>\n" +
							"										<IndexedFaceSet USE=\"IndexedFaceSet_Square\"/>\n" +
							"									</Shape>\n" :
							"									<Shape USE=\"Bar\"/>\n") +
							"								</Transform>\n" +
							"							</Transform>" +
							"							<Transform scale=\"" + Utils.double2StringFormat(2 + (2 * INTERFACES_OFFSET * (id - 1))) + " " + Utils.double2StringFormat(0.1 + (0.1 * INTERFACES_OFFSET  * (id - 1))) + " 1\">\n" +		
							"								<Transform translation=\"0.075 -1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + "\">\n"  +
							"									<Shape USE=\"Bar\"/>\n" +
							"								</Transform>\n" +
							"							</Transform>\n" +
							"							<Transform scale=\"" + Utils.double2StringFormat(1 + (INTERFACES_OFFSET * (id - 1))) + " " + Utils.double2StringFormat(1 + (INTERFACES_OFFSET  * (id - 1))) + " 1\">\n" +
							"								<Transform DEF=\"PlayButton" + id + "_Translation\" translation=\"-0.2 -0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + "\">\n" +
							"									<Group>\n" +
							"										<TouchSensor DEF=\"PlayButton" + id + "Click\" description=\"Play\"/>\n" +
							"										<Shape>\n" +
							"											<Appearance>\n" +
                            "												<ImageTexture url='\"" + INTERFACE_PLAY_BUTTON_TEXTURE + "\" \"http://someURL/" + INTERFACE_PLAY_BUTTON_TEXTURE + "\"'/>\n" +
                            "											</Appearance>\n" +
                            "											<IndexedFaceSet USE=\"IndexedFaceSet_Square\"/>\n" +
                            "										</Shape>\n" +
                            "									</Group>\n" +
                            "								</Transform>\n" +
                            "							</Transform>\n" +
                            "							<BooleanFilter DEF=\"PlayButton" + id + "_Filter\"/>\n" +
                            "							<TimeTrigger DEF=\"PlayButton" + id + "ClickAnimation_Trigger\"/>\n" +
                            "							<TimeTrigger DEF=\"PlayButton" + id + "ReleaseAnimation_Trigger\"/>\n" +
                            "							<TimeSensor DEF=\"PlayButton" + id + "ClickAnimation_Timer\" cycleInterval=\"0.1\" enabled=\"true\" loop=\"false\"/>\n" +
                            "							<PositionInterpolator DEF=\"PlayButton" + id + "ClickAnimation\" key=\"0 1\" keyValue=\"-0.2 -0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + " -0.2 -0.1 " + Utils.double2StringFormat(-0.9995 - INTERFACES_OFFSET * (id - 1)) + "\"/>\n" +
                            "							<TimeSensor DEF=\"PlayButton" + id + "ReleaseAnimation_Timer\" cycleInterval=\"0.1\" enabled=\"true\" loop=\"false\"/>\n" +
                            "							<PositionInterpolator DEF=\"PlayButton" + id + "ReleaseAnimation\" key=\"0 1\" keyValue=\"-0.2 -0.1 " + Utils.double2StringFormat(-0.9995 - INTERFACES_OFFSET * (id - 1)) + " -0.2 -0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + "\"/>\n" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "Click\" fromField=\"isActive\" toNode=\"PlayButton" + id + "_Filter\" toField=\"set_boolean\"/>\n" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "_Filter\" fromField=\"inputTrue\" toNode=\"PlayButton" + id + "ClickAnimation_Trigger\" toField=\"set_boolean\"/>\n" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "ClickAnimation_Trigger\" fromField=\"triggerTime\" toNode=\"PlayButton" + id + "ClickAnimation_Timer\" toField=\"set_startTime\"/>\n" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "ClickAnimation_Timer\" fromField=\"fraction_changed\" toNode=\"PlayButton" + id + "ClickAnimation\" toField=\"set_fraction\"/>\n" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "ClickAnimation\" fromField=\"value_changed\" toNode=\"PlayButton" + id+ "_Translation\" toField=\"translation\"/>\n" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "_Filter\" fromField=\"inputFalse\" toNode=\"PlayButton" + id + "ReleaseAnimation_Trigger\" toField=\"set_boolean\"/>\n" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "ReleaseAnimation_Trigger\" fromField=\"triggerTime\" toNode=\"PlayButton" + id + "ReleaseAnimation_Timer\" toField=\"set_startTime\"/>\n" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "ReleaseAnimation_Trigger\" fromField=\"triggerTime\" toNode=\"" + name + "_Clip\" toField=\"set_startTime\"/>\n" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "ReleaseAnimation_Trigger\" fromField=\"triggerTime\" toNode=\"" + name + "_Clip\" toField=\"set_resumeTime\"/>\n" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "ReleaseAnimation_Timer\" fromField=\"fraction_changed\" toNode=\"PlayButton" + id + "ReleaseAnimation\" toField=\"set_fraction\"/>" +
                            "							<ROUTE fromNode=\"PlayButton" + id + "ReleaseAnimation\" fromField=\"value_changed\" toNode=\"PlayButton" + id+ "_Translation\" toField=\"translation\"/>\n" +
							"							<Transform scale=\"" + Utils.double2StringFormat(1 + (INTERFACES_OFFSET * (id - 1))) + " " + Utils.double2StringFormat(1 + (INTERFACES_OFFSET  * (id - 1))) + " 1\">\n" +
                            "								<Transform DEF=\"PauseButton" + id + "_Translation\" translation=\"-0.1 -0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + "\">\n" +
                            "									<Group>\n" +
                            "									<TouchSensor DEF=\"PauseButton" + id + "Click\" description=\"Pause\"/>\n" +
                            "										<Shape>\n" +
                            "											<Appearance>\n" +
                            "												<ImageTexture url=\'\"" + INTERFACE_PAUSE_BUTTON_TEXTURE + "\" \"http://someURL/" + INTERFACE_PAUSE_BUTTON_TEXTURE + "\"\'/>\n" +
                            "											</Appearance>\n" +
                            "											<IndexedFaceSet USE=\"IndexedFaceSet_Square\"/>\n" +
                            "										</Shape>\n" +
                            "									</Group>\n" +
                            "								</Transform>\n" +
                            "							</Transform>\n" +
                            "							<BooleanFilter DEF=\"PauseButton" + id + "_Filter\"/>\n" +
                            "							<TimeTrigger DEF=\"PauseButton" + id + "ClickAnimation_Trigger\"/>\n" +
                            "							<TimeTrigger DEF=\"PauseButton" + id + "ReleaseAnimation_Trigger\"/>\n" +
                            "							<TimeSensor DEF=\"PauseButton" + id + "ClickAnimation_Timer\" cycleInterval=\"0.1\" enabled=\"true\" loop=\"false\"/>\n" +
                            "							<PositionInterpolator DEF=\"PauseButton" + id + "ClickAnimation\" key=\"0 1\" keyValue=\"-0.1 -0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + " -0.1 -0.1 " + Utils.double2StringFormat(-0.9995 - INTERFACES_OFFSET * (id - 1)) + "\"/>\n" +
                            "							<TimeSensor DEF=\"PauseButton" + id + "ReleaseAnimation_Timer\" cycleInterval=\"0.1\" enabled=\"true\" loop=\"false\"/>\n" +
                            "							<PositionInterpolator DEF=\"PauseButton" + id + "ReleaseAnimation\" key=\"0 1\" keyValue=\"-0.1 -0.1 " + Utils.double2StringFormat(-0.9995 - INTERFACES_OFFSET * (id - 1)) + " -0.1 -0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + "\"/>\n" +
                            "							<ROUTE fromNode=\"PauseButton" + id + "Click\" fromField=\"isActive\" toNode=\"PauseButton" + id + "_Filter\" toField=\"set_boolean\"/>\n" +
                            "							<ROUTE fromNode=\"PauseButton" + id + "_Filter\" fromField=\"inputTrue\" toNode=\"PauseButton" + id + "ClickAnimation_Trigger\" toField=\"set_boolean\"/>\n" +
                            "							<ROUTE fromNode=\"PauseButton" + id + "ClickAnimation_Trigger\" fromField=\"triggerTime\" toNode=\"PauseButton" + id + "ClickAnimation_Timer\" toField=\"set_startTime\"/>\n" +
                            "							<ROUTE fromNode=\"PauseButton" + id + "ClickAnimation_Timer\" fromField=\"fraction_changed\" toNode=\"PauseButton" + id + "ClickAnimation\" toField=\"set_fraction\"/>\n" +
                            "							<ROUTE fromNode=\"PauseButton" + id + "ClickAnimation\" fromField=\"value_changed\" toNode=\"PauseButton" + id + "_Translation\" toField=\"translation\"/>\n" +
                            "							<ROUTE fromNode=\"PauseButton" + id + "_Filter\" fromField=\"inputFalse\" toNode=\"PauseButton" + id + "ReleaseAnimation_Trigger\" toField=\"set_boolean\"/>\n" +
                            "							<ROUTE fromNode=\"PauseButton" + id + "ReleaseAnimation_Trigger\" fromField=\"triggerTime\" toNode=\"PauseButton" + id + "ReleaseAnimation_Timer\" toField=\"set_startTime\"/>\n" +
                            "							<ROUTE fromNode=\"PauseButton" + id + "ReleaseAnimation_Trigger\" fromField=\"triggerTime\" toNode=\"" + name + "_Clip\" toField=\"set_pauseTime\"/>" +
                            "							<ROUTE fromNode=\"PauseButton" + id + "ReleaseAnimation_Timer\" fromField=\"fraction_changed\" toNode=\"PauseButton" + id + "ReleaseAnimation\" toField=\"set_fraction\"/>\n" +
                            "							<ROUTE fromNode=\"PauseButton" + id + "ReleaseAnimation\" fromField=\"value_changed\" toNode=\"PauseButton" + id + "_Translation\" toField=\"translation\"/>\n" +
                            "							<Transform scale=\"" + Utils.double2StringFormat(1 + (INTERFACES_OFFSET * (id - 1))) + " " + Utils.double2StringFormat(1 + (INTERFACES_OFFSET  * (id - 1))) + " 1\">\n" +
                            "								<Transform DEF=\"StopButton" + id + "_Translation\" translation=\"0 -0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + "\">\n" +
                            "									<Group>\n" +
                            "										<TouchSensor DEF=\"StopButton" + id + "Click\" description=\"Stop\"/>\n" +
                            "										<Shape>\n" +
                            "											<Appearance>\n" +
                            "												<ImageTexture url=\'\"" + INTERFACE_STOP_BUTTON_TEXTURE + "\" \"http://someURL/" + INTERFACE_STOP_BUTTON_TEXTURE + "\"\'/>\n" +
                            "											</Appearance>\n" +
                            "											<IndexedFaceSet USE=\"IndexedFaceSet_Square\"/>\n" +
                            "										</Shape>\n" +
                            "									</Group>\n" +
                            "								</Transform>\n"+
                            "							</Transform>\n" +
                            "							<BooleanFilter DEF=\"StopButton" + id + "_Filter\"/>\n" +
                            "							<TimeTrigger DEF=\"StopButton" + id + "ClickAnimation_Trigger\"/>\n" +
                            "							<TimeTrigger DEF=\"StopButton" + id + "ReleaseAnimation_Trigger\"/>\n" +
                            "							<TimeSensor DEF=\"StopButton" + id + "ClickAnimation_Timer\" cycleInterval=\"0.1\" enabled=\"true\" loop=\"false\"/>\n" +
                            "							<PositionInterpolator DEF=\"StopButton" + id + "ClickAnimation\" key=\"0 1\" keyValue=\"0 -0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + " 0 -0.1 " + Utils.double2StringFormat(-0.9995 - INTERFACES_OFFSET * (id - 1)) + "\"/>\n" +
                            "							<TimeSensor DEF=\"StopButton" + id + "ReleaseAnimation_Timer\" cycleInterval=\"0.1\" enabled=\"true\" loop=\"false\"/>\n" +
                            "							<PositionInterpolator DEF=\"StopButton" + id + "ReleaseAnimation\" key=\"0 1\" keyValue=\"0 -0.1 " + Utils.double2StringFormat(-0.9995 - INTERFACES_OFFSET * (id - 1)) + " 0 -0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + "\"/>\n" +
                            "							<ROUTE fromNode=\"StopButton" + id + "Click\" fromField=\"isActive\" toNode=\"StopButton" + id + "_Filter\" toField=\"set_boolean\"/>\n" +
                            "							<ROUTE fromNode=\"StopButton" + id + "_Filter\" fromField=\"inputTrue\" toNode=\"StopButton" + id + "ClickAnimation_Trigger\" toField=\"set_boolean\"/>\n" +
                            "							<ROUTE fromNode=\"StopButton" + id + "ClickAnimation_Trigger\" fromField=\"triggerTime\" toNode=\"StopButton" + id + "ClickAnimation_Timer\" toField=\"set_startTime\"/>\n" +
                            "							<ROUTE fromNode=\"StopButton" + id + "ClickAnimation_Timer\" fromField=\"fraction_changed\" toNode=\"StopButton" + id + "ClickAnimation\" toField=\"set_fraction\"/>\n" +
                            "							<ROUTE fromNode=\"StopButton" + id + "ClickAnimation\" fromField=\"value_changed\" toNode=\"StopButton" + id + "_Translation\" toField=\"translation\"/>\n" +
                            "							<ROUTE fromNode=\"StopButton" + id + "_Filter\" fromField=\"inputFalse\" toNode=\"StopButton" + id + "ReleaseAnimation_Trigger\" toField=\"set_boolean\"/>\n" +
                            "							<ROUTE fromNode=\"StopButton" + id + "ReleaseAnimation_Trigger\" fromField=\"triggerTime\" toNode=\"StopButton" + id + "ReleaseAnimation_Timer\" toField=\"set_startTime\"/>\n" +
                            "							<ROUTE fromNode=\"StopButton" + id + "ReleaseAnimation_Trigger\" fromField=\"triggerTime\" toNode=\"" + name + "_Clip\" toField=\"set_stopTime\"/>\n" +
                            "							<ROUTE fromNode=\"StopButton" + id + "ReleaseAnimation_Timer\" fromField=\"fraction_changed\" toNode=\"StopButton" + id + "ReleaseAnimation\" toField=\"set_fraction\"/>\n" +
                            "							<ROUTE fromNode=\"StopButton" + id + "ReleaseAnimation\" fromField=\"value_changed\" toNode=\"StopButton" + id + "_Translation\" toField=\"translation\"/>\n" +
                            "							<Group>\n" +
                            "								<PlaneSensor DEF=\"Timer" + id + "Slider\" description=\"Slider\" minPosition=\"-0.2 0.1\" maxPosition=\"0.2 0.1\" offset=\"-0.2 0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + "\"/>\n" +
                            "								<Transform scale=\"" + Utils.double2StringFormat(1 + (INTERFACES_OFFSET * (id - 1))) + " " + Utils.double2StringFormat(1 + (INTERFACES_OFFSET  * (id - 1))) + " 1\">\n" +
                            "									<Transform DEF=\"Timer" + id + "Slider_Translation\" translation=\"-0.2 0.1 " + Utils.double2StringFormat(-0.9895 - INTERFACES_OFFSET * (id - 1)) + "\">\n" +
                            "										<Shape>\n" +
                            "											<Appearance>\n" +
                            "												<Material emissiveColor=\"" + INTERFACE_TIMER_SLIDER_COLOR.toX3D() + "\"/>\n" +
                            "											</Appearance>\n" +
                             "											<Box size=\"0.01 0.05 0.01\"/>\n" +
                            "										</Shape>\n" +
                            "									</Transform>\n" +
                            "								</Transform>\n" +
                            "							</Group>\n" +
                            "							<ROUTE fromField=\"translation_changed\" fromNode=\"Timer" + id + "Slider\" toField=\"set_translation\" toNode=\"Timer" + id + "Slider_Translation\"/>\n" +
                            "							<Group>\n" +
                            "								<PlaneSensor DEF=\"Volume" + id + "Slider\" description=\"Volume slider\" minPosition=\"0.1 -0.1\" maxPosition=\"0.2 -0.1\" offset=\"0.2 -0.1 " + Utils.double2StringFormat(-0.9895 - (INTERFACES_OFFSET * (id - 1))) + "\"/>\n" +
                            "								<Transform scale=\"" + Utils.double2StringFormat(1 + (INTERFACES_OFFSET * (id - 1))) + " " + Utils.double2StringFormat(1 + (INTERFACES_OFFSET  * (id - 1))) + " 1\">\n" +
                            "									<Transform DEF=\"Volume" + id + "Slider_Translation\" translation=\"0.2 -0.1 " + Utils.double2StringFormat(-0.9895 - (INTERFACES_OFFSET * (id - 1))) + "\">\n" +
                            "										<Shape>\n" +
                            "											<Appearance>\n" +
                            "												<Material emissiveColor=\"" + INTERFACE_VOLUME_SLIDER_COLOR.toX3D() + "\"/>\n" +
                            "											</Appearance>\n" +
                            "											<Box size=\"0.01 0.025 0.01\"/>\n" +
                            "										</Shape>\n" +
                            "									</Transform>\n" +
                            "								</Transform>\n" +
                            "							</Group>\n" +
                            "							<ROUTE fromField=\"translation_changed\" fromNode=\"Volume" + id + "Slider\" toField=\"set_translation\" toNode=\"Volume" + id + "Slider_Translation\"/>\n" +
                            "							<Transform scale=\"" + Utils.double2StringFormat(0.05 + (0.05 * INTERFACES_OFFSET * (id - 1))) + " " + Utils.double2StringFormat(0.05 + (0.05 * INTERFACES_OFFSET * (id - 1))) + " 1\">\n" +
                            "								<Transform DEF=\"Timer" + id + "Info_Translation\" translation=\"-3.4 -0.018 " + Utils.double2StringFormat(-0.9895 - (INTERFACES_OFFSET * (id - 1))) + "\">\n" +
                            "									<Shape>\n" +
                            "										<Text DEF=\"ElapsedTime" + id + "\" string=\'\" \"\'/>\n" + (id == 1 ?
                            "										<Appearance DEF=\"TimerInfo_Appearance\">\n" +
                            "											<Material emissiveColor=\"" + INTERFACE_TIMER_INFO_COLOR.toX3D() + "\"/>\n" +
                            "										</Appearance>\n" :
                            "										<Appearance USE=\"TimerInfo_Appearance\"/>\n") +
                            "									</Shape>\n" +
                            "									<Transform translation=\"3.1 0 0\">\n" +
                            "										<Shape>\n" +
                            "											<Text DEF=\"Duration" + id + "\" string=\'\" \"\'/>\n" +
                            "											<Appearance USE=\"TimerInfo_Appearance\"/>\n" +
                            "										</Shape>\n" +
                            "									</Transform>\n" +
                            "							</Transform>\n" +
                            "							</Transform>\n" +
                            "							<Script DEF=\"" + name + "_Player\"" + (scriptType == ScriptType.EXTERNAL ? " url=\'\"Scripts/AudioPlayer.js\" \"http://someURL/AudioPlayer.js\"\'" : "") + ">\n" +
                            "								<field name=\"Clip\" accessType=\"initializeOnly\" type=\"SFNode\">\n" +
                            "									<AudioClip USE=\"" + name + "_Clip\"/>\n" +
                            "								</field>\n" +
                            "								<field name=\"Interface\" accessType=\"initializeOnly\" type=\"SFNode\">\n" +
                            "									<Switch USE=\"PlayerInterface" + id + "_Switch\"/>\n" +
                            "								</field>\n" +
                            "								<field name=\"TimerSlider\" accessType=\"initializeOnly\" type=\"SFNode\">\n" +
                            "									<Transform USE=\"Timer" + id + "Slider_Translation\"/>\n" +
                            "								</field>\n" +
                            "								<field name=\"ElapsedTimeInfo\" accessType=\"initializeOnly\" type=\"SFNode\">\n" +
                            "									<Text USE=\"ElapsedTime" + id + "\"/>\n" +
                            "								</field>\n" +
                            "								<field name=\"DurationInfo\" accessType=\"initializeOnly\" type=\"SFNode\">\n" +
                            "									<Text USE=\"Duration" + id + "\"/>\n" +
                            "								</field>\n" +
                            "								<field name=\"close\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"" + close + "\"/>\n" +
                            "								<field name=\"updateVolume\" accessType=\"inputOnly\" type=\"SFVec3f\"/>\n" +
                            "								<field name=\"volume\" accessType=\"outputOnly\" type=\"SFFloat\"/>\n" +
                            "								<field name=\"updateElapsedTime\" accessType=\"inputOnly\" type=\"SFTime\"/>\n" + (scriptType == ScriptType.EMBEDDED ?
                            "								<![CDATA[\n" +
                            "									ecmascript:" +
                            "\n" +
                            "									function updateVolume(volumeSliderTranslation) {\n" +
                            "										volume = (volumeSliderTranslation[0] - 0.1) / 0.1;\n" +
                            "									}\n" +
                            "\n" +
                            "									function timeToString(timeInSeconds) {\n" +
                            "										seconds = timeInSeconds % 60;\n" +
                            "										minute = ((timeInSeconds - seconds) / 60) % 60;\n" +
                            "										hour = (timeInSeconds - minute * 60 - seconds) / 3600;\n" +
                            "										hStr = (hour    < 10 ? \'0\' + hour    : \'\' + hour);\n" +
                            "										mStr = (minute  < 10 ? \'0\' + minute  : \'\' + minute);\n" +
                            "										sStr = (seconds < 10 ? \'0\' + seconds : \'\' + seconds);\n" +
                            "										timeStr = hStr + \':\' + mStr + \':\' + sStr;\n" +
                            "										return timeStr;\n" +
                       		"									}\n" +
                       		"\n" +
                       		"									function updateElapsedTime(elapsedTime, timestamp) {\n" +
                       		"										if (Interface.whichChoice == -1) {\n" +
                            "											Interface.whichChoice = 0;\n" +
                            "											DurationInfo.string = new MFString(\' / \' + timeToString(Math.floor(Clip.duration_changed)));\n" +
                            "										}\n" +
                            "										TimerSlider.translation = new SFVec3f(-0.2 + 0.4 * elapsedTime / Clip.duration_changed * Clip.pitch, TimerSlider.translation[1], TimerSlider.translation[2]);\n" + 
                            "										ElapsedTimeInfo.string = new MFString(timeToString(Math.floor(elapsedTime)));\n" +
                            "										if (elapsedTime >= Clip.duration_changed / Clip.pitch) {\n" +
                            "											Clip.stopTime = timestamp;\n" +
                            "											if (close) {\n" +
                            "												Interface.whichChoice = -1;\n" +
                            "											}\n" +
                            "										}\n" +
                            "									}\n" +
                            "								]]>\n" : "") +
                            "							</Script>\n" +
                            "							<ROUTE fromField=\"translation_changed\" fromNode=\"Volume" + id + "Slider\" toField=\"updateVolume\" toNode=\"" + name + "_Player\"/>\n" +
                            "							<ROUTE fromNode=\"" + name + "_Clip\" fromField=\"elapsedTime\" toNode=\"" + name + "_Player\" toField=\"updateElapsedTime\"/>\n" +
                            "							<ROUTE fromNode=\"" + name + "_Player\" fromField=\"volume\" toNode=\"" + name + "_Sound\" toField=\"intensity\"/>\n" +
                            "						</Group>\n" +
                            "					</Switch>\n" +
                            "					</Transform>\n" +
                            "					<ROUTE fromNode=\"PlayerInterface" + id + "Dragger\" fromField=\"translation_changed\" toNode=\"PlayerInterface" + id + "Offset_Transform\" toField=\"set_translation\"/>\n" +
                            "					</Group>\n" +
                            "				</Transform>\n";
                            if (showAudioPlayer) {
                            	X3DString +=	"				<ROUTE fromNode=\"HereIAm_Local\" fromField=\"position_changed\" toNode=\"PlayerInterface" + id + "_Transform\" toField=\"set_translation\"/>\n" +
                            					"				<ROUTE fromNode=\"HereIAm_Local\" fromField=\"orientation_changed\" toNode=\"PlayerInterface" + id + "_Transform\" toField=\"set_rotation\"/>\n";
                            }
		}
		return X3DString;
	}
}

// TODO: Find a way to trigger data actions viewer by not click events, at the moment the only solution i found was using the Anchor X3D node.
/* class PDFViewer extends Data {
	public PDFViewer(String name, UserInterface userInterface, String content) throws Exception {
		super(name, Type.PDF_VIEWER, userInterface, content);
	}
	
	public String toX3D() {
		String contentName = new File(content).getName();
		String X3DString =	"			<Anchor url=\"\'" + contentName + "\'\">" +
							"			</Anchor>";
		return X3DString;
	}
} */
