package x3doors.actions.controllers;

import java.util.ArrayList;

import util.RGBColor;
import util.Utils;
import x3doors.nodes.SceneObject;

public class Material extends Controller {
	/* A list containing the key frames times */
	private ArrayList<Double> times;
	/* A list containing the key frames ambient chromatic components */
	private ArrayList<RGBColor> ambient;
	/* A list containing the key frames diffuse chromatic components */
	private ArrayList<RGBColor> diffuse;
	/* A list containing the key frames emissive chromatic components */
	private ArrayList<RGBColor> emissive;
	/* A list containing the key frames shininess values */
	private ArrayList<Double> shininess;
	/* A list containing the key frames specular chromatic components */
	private ArrayList<RGBColor> specular;
	/* A list containing the key frames transparencies */
	private ArrayList<Double> transparencies;
	
	/** Creates a controller with the given properties.
	 * 
	 * @param name The name
	 * @param repeat The repeat mode
	 * @param attachedTo The handle of the scene object which this controller is attached to
	 */
	public Material(String name, Repeat repeat, SceneObject attachedTo) {
		super(name, Type.MATERIAL, repeat, attachedTo);
		times = new ArrayList<Double>();
		ambient = new ArrayList<RGBColor>();
		diffuse = new ArrayList<RGBColor>();
		emissive = new ArrayList<RGBColor>();
		shininess = new ArrayList<Double>();
		specular = new ArrayList<RGBColor>();
		transparencies = new ArrayList<Double>();
		
	}
	
	/** Adds a key frame with the given properties.
	 * 
	 * @param time The key frame time
	 * @param alpha The key frame alpha
	 * @param ambient The key frame ambient chromatic component
	 * @param diffuse The key frame diffuse chromatic component
	 * @param emissive The key frame emissive chromatic component
	 * @param shininess The key frame shininess value
	 * @param specular The key frame specular chromatic component
	 */
	public void addKeyFrame(double time, double alpha, RGBColor ambient, RGBColor diffuse, RGBColor emissive, double shininess, RGBColor specular) {
		this.times.add(time);
		this.ambient.add(ambient);
		this.diffuse.add(diffuse);
		this.emissive.add(emissive);
		this.shininess.add(shininess);
		this.specular.add(specular);
		this.transparencies.add(1.0 - alpha);
	}
	
	/** @return This controller X3D string */
	public String toX3D() {
		int timesSize = times.size();
		double cycleInterval = times.get(timesSize - 1);
		double offset = times.get(0);
		double range = times.get(timesSize - 1) - offset;
		
		String keyX3DString = " key=\"";
		String transparencyScalarInterpolatorKeyValueX3DString = " keyValue=\"";
		String diffuseColorInterpolatorKeyValueX3DString = " keyValue=\"";
		String emissiveColorInterpolatorKeyValueX3DString = " keyValue=\"";
		String shininessScalarInterpolatorKeyValueX3DString = " keyValue=\"";
		String specularColorInterpolatorKeyValueX3DString = " keyValue=\"";
		switch (repeat) {
			case "Clamp":
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2DoubleArray(times), cycleInterval), ' ', false);
				diffuseColorInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(diffuse);
				emissiveColorInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(emissive);
				shininessScalarInterpolatorKeyValueX3DString += Utils.doubleList2X3DString(shininess);
				specularColorInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(specular);
				transparencyScalarInterpolatorKeyValueX3DString += Utils.doubleList2X3DString(transparencies);
				break;
			case "Wrap":
				cycleInterval -= offset;

				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.subtractDoubleOffset(Utils.doubleList2DoubleArray(times), offset), range), ' ', false);
				diffuseColorInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(diffuse);
				emissiveColorInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(emissive);
				shininessScalarInterpolatorKeyValueX3DString += Utils.doubleList2X3DString(shininess);
				specularColorInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(specular);
				transparencyScalarInterpolatorKeyValueX3DString += Utils.doubleList2X3DString(transparencies);
				break;
			case "WrapWithDelay":
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2DoubleArray(times), cycleInterval), ' ', false);
				diffuseColorInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(diffuse);
				emissiveColorInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(emissive);
				shininessScalarInterpolatorKeyValueX3DString += Utils.doubleList2X3DString(shininess);
				specularColorInterpolatorKeyValueX3DString += Utils.arrayList2X3DString(specular);
				transparencyScalarInterpolatorKeyValueX3DString += Utils.doubleList2X3DString(transparencies);
				break;
			case "Cycle":
				cycleInterval = (cycleInterval - offset) * 2;

				double[] cyclizedTimes = Utils.doubleList2CyclizedDoubleArray(times);
				offset = cyclizedTimes[0];
				range = cyclizedTimes[timesSize * 2 - 2] - offset;
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.subtractDoubleOffset(cyclizedTimes, offset), range), ' ', false);
				diffuseColorInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(diffuse)), ' ', false);
				emissiveColorInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(emissive)), ' ', false);
				specularColorInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(specular)), ' ', false);
				shininessScalarInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.doubleList2X3DStringArray(shininess)), ' ', false);
				transparencyScalarInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.doubleList2X3DStringArray(transparencies)), ' ', false);
				break;
			case "CycleWithDelay":
				cycleInterval = cycleInterval * 2;
				keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2CyclizedDoubleArray(times), cycleInterval), ' ', false);
				diffuseColorInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(diffuse)), ' ', false);
				emissiveColorInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(emissive)), ' ', false);
				specularColorInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(specular)), ' ', false);
				shininessScalarInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.doubleList2X3DStringArray(shininess)), ' ', false);
				transparencyScalarInterpolatorKeyValueX3DString += Utils.stringArray2String(Utils.mirror(Utils.doubleList2X3DStringArray(transparencies)), ' ', false);
		}
		keyX3DString += "\"";
		diffuseColorInterpolatorKeyValueX3DString += "\"";
		emissiveColorInterpolatorKeyValueX3DString += "\"";
		specularColorInterpolatorKeyValueX3DString += "\"";
		shininessScalarInterpolatorKeyValueX3DString += "\"";
		transparencyScalarInterpolatorKeyValueX3DString += "\"";
		
		String attachedSceneObjectName = SceneObject.get(attachedTo).getName();
		String X3DString =	"				<TimeTrigger DEF=\"" + name + "_Trigger\"/>\n" +
							"				<TimeSensor DEF=\"Clock_" + name + "\" enabled=\"true\" loop=\"false\" cycleInterval=\"" + Utils.double2StringFormat(cycleInterval) + "\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_Trigger\" fromField=\"triggerTime\" toNode=\"Clock_" + name + "\" toField=\"set_stopTime\"/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"stopTime_changed\" toNode=\"Clock_" + name + "\" toField=\"set_startTime\"/>\n" +
							"				<ColorInterpolator DEF=\"" + name + "_DiffuseColor\"" + keyX3DString + diffuseColorInterpolatorKeyValueX3DString + "/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "_DiffuseColor\" toField=\"set_fraction\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_DiffuseColor\" fromField=\"value_changed\" toNode=\"" + attachedSceneObjectName + "_Material\" toField=\"set_diffuseColor\"/>\n" +
							"				<ColorInterpolator DEF=\"" + name + "_EmissiveColor\"" + keyX3DString + emissiveColorInterpolatorKeyValueX3DString + "/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "_EmissiveColor\" toField=\"set_fraction\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_EmissiveColor\" fromField=\"value_changed\" toNode=\"" + attachedSceneObjectName + "_Material\" toField=\"set_emissiveColor\"/>\n" +
							"				<ColorInterpolator DEF=\"" + name + "_SpecularColor\"" + keyX3DString + specularColorInterpolatorKeyValueX3DString + "/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "_SpecularColor\" toField=\"set_fraction\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_SpecularColor\" fromField=\"value_changed\" toNode=\"" + attachedSceneObjectName + "_Material\" toField=\"set_specularColor\"/>\n" +
							"				<ScalarInterpolator DEF=\"" + name + "_Shininess\"" + keyX3DString + shininessScalarInterpolatorKeyValueX3DString + "/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "_Shininess\" toField=\"set_fraction\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_Shininess\" fromField=\"value_changed\" toNode=\"" + attachedSceneObjectName + "_Material\" toField=\"set_shininess\"/>\n" +
							"				<ScalarInterpolator DEF=\"" + name + "_Transparency\"" + keyX3DString + transparencyScalarInterpolatorKeyValueX3DString + "/>\n" +
							"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "_Transparency\" toField=\"set_fraction\"/>\n" +
							"				<ROUTE fromNode=\"" + name + "_Transparency\" fromField=\"value_changed\" toNode=\"" + attachedSceneObjectName + "_Material\" toField=\"set_transparency\"/>\n";
		if (!repeat.equals("Clamp")) { // if repeat is Wrap or Cycle
			X3DString +=	"				<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"isActive\" toNode=\"Clock_" + name + "\" toField=\"loop\"/>\n";
		}
		
		return X3DString;
	}
}