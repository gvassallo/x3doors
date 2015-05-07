package x3doors.actions.controllers;

import java.util.ArrayList;

import math.Matrix4;
import math.Vec3;
import math.Vec4;
import util.Utils;
import x3doors.nodes.Light;
import x3doors.nodes.SceneObject;

public class Pose extends Controller {
	/* A list containing the key frames times */
	private ArrayList<Double> times;
	/* A list containing the key frames world translation coordinates */
	private ArrayList<Vec3> translations;
	/* A list containing the key frames world rotation coordinates */
	private ArrayList<Vec4> rotations;
	/* A list containing the key frames world direction coordinates for light scene objects */
	private ArrayList<Vec3> directions;
	
	/** Creates a controller with the given properties.
	 * 
	 * @param name The name
	 * @param repeat The repeat mode
	 * @param attachedTo The handle of the scene object which this controller is attached to
	 */
	public Pose(String name, Repeat repeat, SceneObject attachedTo) {
		super(name, Type.POSE, repeat, attachedTo);
		this.times = new ArrayList<Double>();
		this.translations = new ArrayList<Vec3>();
		this.rotations = new ArrayList<Vec4>();
		this.directions = new ArrayList<Vec3>();
	}
	
	/** Adds a key frame with the given properties.
	 * 
	 * @param time The key frame time
	 * @param translation The key frame world translation coordinates
	 * @param rotation The key frame world rotation coordinates
	 */
	public void addKeyFrame(double time, Vec3 translation, Vec4 rotation) {
		times.add(time);
		SceneObject attachedSceneObject = SceneObject.get(attachedTo);
		if (attachedSceneObject.parent != null) {
			Vec4[] newLocalCoordinates = attachedSceneObject.parent.transform.localToWorldMatrix.concat(new Matrix4()
																												.setTranslate(translation.x, translation.y, translation.z)
																												.rotate(rotation.x, rotation.y, rotation.z, rotation.w))
																												// .scale(attachedSceneObject.transform.worldScaleFactor.x, attachedSceneObject.transform.worldScaleFactor.y, attachedSceneObject.transform.worldScaleFactor.z))
																												.decompose();
			translation = newLocalCoordinates[2].toVec3();
			rotation = newLocalCoordinates[1];
		}
		translations.add(translation);
		rotations.add(rotation);
		if (attachedSceneObject instanceof Light) {
			directions.add((new Vec3(0, 0, -1).toVec4().times(new Matrix4().setRotate(rotation.x, rotation.y, rotation.z, rotation.w)).toVec3()));
		}
	}
	
	/** @return This controller X3D string */
	public String toX3D() {
		int timesSize = times.size();
		double cycleInterval = times.get(timesSize - 1);
		double offset = times.get(0);
		double range = times.get(timesSize - 1) - offset;
		
		SceneObject attachedSceneObject = SceneObject.get(attachedTo);
		boolean attachedSceneObjectIsLight = attachedSceneObject instanceof Light;
		
		String keyX3DString = " key=\"";
		String positionInterpolatorKeyValueX3DString = " keyValue=\"";
		String orientationInterpolatorKeyValueX3DString = " keyValue=\"";
		String directionInterpolatorKeyValueX3DString = " keyValue=\"";
		switch (repeat) {
			case "Clamp":
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2DoubleArray(times), cycleInterval), ' ', false);
				positionInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(translations);
				if (attachedSceneObjectIsLight) {
					directionInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(directions);
				}
				orientationInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(rotations);
				break;
			case "Wrap":
				cycleInterval -= offset;

				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.subtractDoubleOffset(Utils.doubleList2DoubleArray(times), offset), range), ' ', false);
				positionInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(translations);
				if (attachedSceneObjectIsLight) {
					directionInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(directions);
				}
				orientationInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(rotations);
				break;
			case "WrapWithDelay":
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2DoubleArray(times), cycleInterval), ' ', false);
				positionInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(translations);
				if (attachedSceneObjectIsLight) {
					directionInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(directions);
				}
				orientationInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(rotations);
				break;
			case "Cycle":
				cycleInterval = (cycleInterval - offset) * 2;

				double[] cyclizedTimes = Utils.doubleList2CyclizedDoubleArray(times);
				offset = cyclizedTimes[0];
				range = cyclizedTimes[timesSize * 2 - 2] - offset;
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.subtractDoubleOffset(cyclizedTimes, offset), range), ' ', false);
				positionInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(translations)), ' ', false);
				if (attachedSceneObjectIsLight) {
					directionInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(directions)), ' ', false);
				}
				orientationInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(rotations)), ' ', false);
				break;
			case "CycleWithDelay":
				cycleInterval = cycleInterval * 2;
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2CyclizedDoubleArray(times), cycleInterval), ' ', false);
				positionInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(translations)), ' ', false);
				if (attachedSceneObjectIsLight) {
					directionInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(directions)), ' ', false);
				}
				orientationInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(rotations)), ' ', false);
		}
		keyX3DString += "\"";
		positionInterpolatorKeyValueX3DString += "\"";
		if (attachedSceneObjectIsLight) {
			directionInterpolatorKeyValueX3DString += "\"";
		}
		orientationInterpolatorKeyValueX3DString += "\"";
		
		String X3DString =	"				<TimeTrigger DEF=\"" + name + "_Trigger\"/>\n" +
							"				<TimeSensor DEF=\"Clock_" + name + "\" enabled=\"true\" loop=\"false\" cycleInterval=\"" + Utils.double2StringFormat(cycleInterval) + "\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_Trigger\" fromField=\"triggerTime\" toNode=\"Clock_" + name + "\" toField=\"set_stopTime\"/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"stopTime_changed\" toNode=\"Clock_" + name + "\" toField=\"set_startTime\"/>\n" +
							"				<PositionInterpolator DEF=\"" + name + "_translation\"" + keyX3DString + positionInterpolatorKeyValueX3DString + "/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "_translation\" toField=\"set_fraction\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_translation\" fromField=\"value_changed\" toNode=\"" + attachedSceneObject.getName() + (attachedSceneObjectIsLight ? "" : "_Translation") + "\" toField=\"set_" + (attachedSceneObjectIsLight ? "location" : "translation") + "\"/>\n" + (attachedSceneObjectIsLight ?
							"				<PositionInterpolator DEF=\"" + name + "_rotation\"" + keyX3DString + directionInterpolatorKeyValueX3DString + "/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "_rotation\" toField=\"set_fraction\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_rotation\" fromField=\"value_changed\" toNode=\"" + attachedSceneObject.getName() + "\" toField=\"set_direction\"/>\n" :			
							"				<OrientationInterpolator DEF=\"" + name + "_rotation\"" + keyX3DString + orientationInterpolatorKeyValueX3DString + "/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "_rotation\" toField=\"set_fraction\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_rotation\" fromField=\"value_changed\" toNode=\"" + attachedSceneObject.getName() + "_Rotation\" toField=\"set_rotation\"/>\n") + (attachedSceneObjectIsLight && Light.drawLightSources ?
							"				<ROUTE fromNode=\"" + name + "_translation\" fromField=\"value_changed\" toNode=\"" + attachedSceneObject.getName() + "_Placeholder_Translation\" toField=\"set_translation\"/>\n" +
							"				<OrientationInterpolator DEF=\"" + name + "_Placeholder_rotation\"" + keyX3DString + orientationInterpolatorKeyValueX3DString + "/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "_Placeholder_rotation\" toField=\"set_fraction\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_Placeholder_rotation\" fromField=\"value_changed\" toNode=\"" + attachedSceneObject.getName() + "_Placeholder_Rotation\" toField=\"set_rotation\"/>\n" : "");
									
		if (!repeat.equals("Clamp")) { // if repeat is Wrap or Cycle
			X3DString +=	"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"isActive\" toNode=\"Clock_" + name + "\" toField=\"loop\"/>\n";
		}
		return X3DString;
	}}
