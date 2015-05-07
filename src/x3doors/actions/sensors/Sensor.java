package x3doors.actions.sensors;

import util.Printable;
import x3doors.exporters.*;
import util.*;

public abstract class Sensor implements Printable, X3DExportable {
	/** The script exporting mode, editable by the {@link X3DExporter} static method setEmbeddedScripts(boolean embeddedScripts).
	 * @see ScriptType */
	public static ScriptType scriptType = ScriptType.EXTERNAL;
	/* This counter is used to assign the sensor name automatically when it is not specified */
	private static int counter = 0;

	/** The sensor name. */
	protected String name;
	// TODO: Additional delay seems not affecting anything in 3Doors, check the source code to be sure about it
	// protected double additionalDelay;
	/** If true then the related sensor event is triggered on the complementary of its original logical condition. It affects only some kind of sensor. */
	protected boolean negated;
	/** If true then this sensor can trigger its related event more than once. */
	protected boolean repeatable;
	/** The string transcription of the sensor type.
	 * @see Type */
	protected String type;
	
	/** Defines the script exporting mode, can assume one of the following values<p>
	 * EMBEDDED: when exporting to X3D scripts are directly embedded in the X3D file. This increments the file size and causes
	 * the script code to be redundant, may be necessary for solving some X3D player compatibility issue.<p>
	 * EXTERNAL: when exporting to X3D scripts are exported as independent files */
	public enum ScriptType {
		EMBEDDED,
		EXTERNAL
	}
	
	/** Defines the sensor type. */
	protected enum Type {
		CLICK,
		DELAY,
		DISTANCE,
		AND,
		OR
	}
	
	/** Creates a sensor with the given properties.
	 * 
	 * @param name The name
	 * @param negated If true then the related sensor event is triggered on the complementary of its original logical condition
	 * @param repeatable If true then this sensor can trigger its related event more than once
	 * @param type The type
	 */
	public Sensor(String name, boolean negated, boolean repeatable, Type type) {
		switch (type) {
			case CLICK:
				this.type = "Click";
				break;
			case DELAY:
				this.type = "Delay";
				break;
			case DISTANCE:
				this.type = "Distance";
				break;
			case AND:
				this.type = "AND";
				break;
			case OR:
				this.type = "OR";
				break;
		}
		this.name = (name == null || name.equals("")) ? (this.type + "Sensor_" + counter++) : name;
		// this.additionalDelay = additionalDelay;
		this.negated = negated;
		this.repeatable = repeatable;
	}
	
	/** @return The name */
	public String getName() {
		return name;
	}
	
	/** @return The type */
	public String getType() {
		return type;
	}
	
	/** Print the properties to screen. */
	public void print() {
		System.out.print(	"Name:\t\t\t" + name + "\n" +
							// "AdditionalDelay:\t" + additionalDelay + "\n" +
							"Negated:\t\t" + (negated ? "True" : "False") + "\n" +
							"Repeatable:\t\t" + (repeatable ? "True" : "False") + "\n" +
							"Type:\t\t\t" + type + " Sensor\n"
		);
	}
}