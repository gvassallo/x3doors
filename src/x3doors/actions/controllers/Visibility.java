package x3doors.actions.controllers;

import java.util.ArrayList;

import util.Utils;

import x3doors.nodes.SceneObject;

public class Visibility extends Controller {
	/* A list containing the key frames times */
	private ArrayList<Double> times;
	/* A list containing the key frames visibility values */
	private ArrayList<Boolean> visibility;
	
	/** Creates a controller with the given properties.
	 * 
	 * @param name The name
	 * @param repeat The repeat mode
	 * @param attachedTo The handle of the scene object which this controller is attached to
	 */
	public Visibility(String name, Repeat repeat, SceneObject attachedTo) {
		super(name, Type.VISIBILITY, repeat, attachedTo);
		times = new ArrayList<Double>();
		visibility = new ArrayList<Boolean>();
	}
	
	/** Adds a key frame with the given properties.
	 * 
	 * @param time The key frame time
	 * @param isVisible true if the scene object which this controller is attached to is visible
	 */
	public void addKeyFrame(double time, boolean isVisible) {
		times.add(time);
		visibility.add(isVisible);
	}
	
	/** @return This controller X3D string */
	public String toX3D() {
		int timesSize = times.size();
		double cycleInterval = times.get(timesSize - 1);
		double offset = times.get(0);
		double range = times.get(timesSize - 1) - offset;
		
		String keyX3DString = " key=\"";
		String visibilityIntegerInterpolatorKeyValueX3DString = " keyValue=\"";
	
		switch (repeat) {
			case "Clamp":
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2DoubleArray(times), cycleInterval), ' ', false);
				visibilityIntegerInterpolatorKeyValueX3DString += Utils.booleanList2X3DString(visibility, "0", "-1");
				break;
			case "Wrap":
				cycleInterval -= offset;

				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.subtractDoubleOffset(Utils.doubleList2DoubleArray(times), offset), range), ' ', false);
				visibilityIntegerInterpolatorKeyValueX3DString += Utils.booleanList2X3DString(visibility, "0", "-1");
				break;
			case "WrapWithDelay":
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2DoubleArray(times), cycleInterval), ' ', false);
				visibilityIntegerInterpolatorKeyValueX3DString += Utils.booleanList2X3DString(visibility, "0", "-1");
				break;
			case "Cycle":
				cycleInterval = (cycleInterval - offset) * 2;

				double[] cyclizedTimes = Utils.doubleList2CyclizedDoubleArray(times);
				offset = cyclizedTimes[0];
				range = cyclizedTimes[timesSize * 2 - 2] - offset;
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.subtractDoubleOffset(cyclizedTimes, offset), range), ' ', false);
				visibilityIntegerInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.booleanList2X3DStringArray(visibility, "0", "-1")), ' ', false);
				break;
			case "CycleWithDelay":
				cycleInterval = cycleInterval * 2;
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2CyclizedDoubleArray(times), cycleInterval), ' ', false);
				visibilityIntegerInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.booleanList2X3DStringArray(visibility, "0", "-1")), ' ', false);
		}
		keyX3DString += "\"";
		visibilityIntegerInterpolatorKeyValueX3DString += "\"";
		
		String attachedSceneObjectName = SceneObject.get(attachedTo).getName();
		String X3DString =	"				<TimeTrigger DEF=\"" + name + "_Trigger\"/>\n" +
							"				<TimeSensor DEF=\"Clock_" + name + "\" enabled=\"true\" loop=\"false\" cycleInterval=\"" + Utils.double2StringFormat(cycleInterval) + "\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_Trigger\" fromField=\"triggerTime\" toNode=\"Clock_" + name + "\" toField=\"set_stopTime\"/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"stopTime_changed\" toNode=\"Clock_" + name + "\" toField=\"set_startTime\"/>\n" +
							"				<IntegerSequencer DEF=\"" + name + "_visibility\"" + keyX3DString + visibilityIntegerInterpolatorKeyValueX3DString + "/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "_visibility\" toField=\"set_fraction\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_visibility\" fromField=\"value_changed\" toNode=\"" + attachedSceneObjectName + "_Switch\" toField=\"whichChoice\"/>\n";
		if (!repeat.equals("Clamp")) { // if repeat is Wrap or Cycle
			X3DString +=	"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"isActive\" toNode=\"Clock_" + name + "\" toField=\"loop\"/>\n";
		}
		
		return X3DString;
	}
}