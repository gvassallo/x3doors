package x3doors.actions;

import java.util.ArrayList;

import util.Actionable;
import util.Printable;
import x3doors.actions.controllers.Controller;
import x3doors.actions.sensors.Sensor;

public class Behaviour implements Printable {
	/* An array list containing the current scene declared behaviours */
	private static ArrayList<Behaviour> register = new ArrayList<Behaviour>();
	
	/* This counter is used to assign the data name automatically when it is not specified */
	private static int counter = 0;
	
	/* The behaviour name */
	private String name;
	/* The behaviour sensor */
	private Sensor sensor;
	/* The behaviour action, can be either a controller or a data */
	private Actionable action;
	
	/** Stores a behaviour in the behaviour register.
	 * 
	 * @param The behaviour to be stored
	 */
	public static void store(Behaviour behaviour) {
		register.add(behaviour);
	}
	
	/** Returns the behaviour with the given register index.
	 * 
	 * @param index The behaviour register index
	 * @return The behaviour
	 */
	public static Behaviour get(int index) {
		return register.get(index);
	}
	
	/** @return The behaviour register size */
	public static int registerSize() {
		return register.size();
	}
	
	/** Creates a behaviour with the given properties.
	 * 
	 * @param name The name
	 * @param sensor The sensor
	 * @param action The action
	 */
	public Behaviour(String name, Sensor sensor, Actionable action) {
		this.name = (name == null || name.equals("")) ? ("Behaviour_" + counter++) : name;
		this.sensor = sensor;
		this.action = action;
		Behaviour.store(this);
	}
	
	/** @return The sensor */
	public Sensor getSensor() {
		return sensor;
	}
	
	/** @return The action, either a controller or data */
	public Actionable getAction() {
		return action;
	}
	
	/** Print the properties to screen. */
	public void print() {
		String actionName = action.getName();
		String actionType = action.getType();
		System.out.println(	name + "\n\t" +
							sensor.getType() + " Sensor --> " + sensor.getName() + "\n\t" +
							actionType + "--> " + actionName + (action instanceof Controller ? " on object \'" + ((Controller) action).getAttachedTo() + "\'" : "")
		);
	}
	
	/** @return This behaviour X3D string */
	public String toX3D() {
		String X3DString = "";
		String sensorName = sensor.getName();
		String actionName = "";
		actionName = action.getName();
		String sensorToActionX3DString = "					<ROUTE fromNode=\"" + sensorName + "_Filter\" fromField=\"inputTrue\" toNode=\"" + actionName + "_Trigger\" toField=\"set_boolean\"/>\n";
		/* switch (sensor.getType()) {
			case "AND":
				sensorToActionX3DString +=	"					<ROUTE fromNode=\"" + sensorName + "_Filter\" fromField=\"inputTrue\" toNode=\"" + actionName + "_Trigger\" toField=\"set_boolean\"/>\n";
				break;
			case "Delay":
				sensorToActionX3DString +=	"					<ROUTE fromNode=\"" + sensorName + "\" fromField=\"delayComplete\" toNode=\"" + actionName + "_Trigger\" toField=\"set_boolean\"/>\n";
				break;
			case "Click":
				sensorToActionX3DString +=	"					<ROUTE fromNode=\"" + sensorName + "_Filter\" fromField=\"inputTrue\" toNode=\"" + actionName + "_Trigger\" toField=\"set_boolean\"/>\n";
				break;
			case "Distance":
				sensorToActionX3DString +=	"					<ROUTE fromNode=\"" + sensorName + "_Filter\" fromField=\"inputTrue\" toNode=\"" + actionName + "_Trigger\" toField=\"set_boolean\"/>\n";
				break;
			case "OR":
				sensorToActionX3DString += "					<ROUTE fromNode=\"" + sensorName + "_Filter\" fromField=\"inputTrue\" toNode=\"" + actionName + "_Trigger\" toField=\"set_boolean\"/>\n";
		} */
		X3DString =	"			<Group DEF=\"" + this.name + "\">\n" +
											action.toX3D() +
							"				<Group DEF=\"" + sensorName + "_Group\">\n" +
												sensor.toX3D() +
												sensorToActionX3DString +
							"				</Group>\n" +
							"			</Group>\n";
		return X3DString;
	}
}
