package x3doors.actions.sensors;

import x3doors.nodes.Camera;
import x3doors.nodes.SceneObject;

public class Distance extends Sensor {
	/* The distance between the two elements */
	private double distance;
	/* The first element */
	private SceneObject element1;
	/* The second element */
	private SceneObject element2;
	/* true if the first element is a camera, need to check this for the X3D exporting process */
	private boolean element1IsCamera;
	
	/** Creates a distance click sensor with the given properties.
	 * 
	 * @param name The name
	 * @param negated If true the related sensor event will be triggered when the distance between the two elements will be within the
	 * specified one, if false the event will be triggered when the distance between the two elements will be greater then the specified
	 * one
	 * @param repeatable If true then this sensor can trigger its related event more than once
	 * @param distance The distance
	 * @param element1 The first element which coordinates will be used in the distance computation
	 * @param element2 The second element which coordinates will be used in the distance computation
	 */
	public Distance(String name, boolean negated, boolean repeatable, double distance, SceneObject element1, SceneObject element2) {
		super(name, negated, repeatable, Type.DISTANCE);
		// Check for a Camera scene object and always assign it to the first element to simplify the X3D exporting
		// REMEMBER: at least one of the elements must NOT be a Camera
		this.distance = distance;
		if (element1 instanceof Camera) {
			this.element1 = element1;
			this.element2 = element2;
			element1IsCamera = true;
		}
		else if (element2 instanceof Camera) {
			this.element1 = element2;
			this.element2 = element1;
			element1IsCamera = true;
		}
		else {
			this.element1 = element1;
			this.element2 = element2;
			element1IsCamera = false;
		}
		this.name = (name == null || name.equals("")) ? "DistanceSensor_" + element1.getName() + element2.getName() : this.name;
	}
	
	/** @return This sensor X3D string */
	public String toX3D() {
		String X3DString = "";
		X3DString +=	"					<BooleanFilter DEF=\"" + name + "_Filter\"/>\n";
	    X3DString += 	"					<Script DEF=\"" + name + "\"" + (scriptType == ScriptType.EXTERNAL ? " url=\'\"Scripts/DistanceSensor.js\" \"http://someURI/DistanceSensor.js\"\'>\n" : ">\n") +
						"						<field name=\"enabled\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"true\"/>\n" +
	    				"						<field name=\"repeatable\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"" + repeatable + "\"/>\n" +
	    				"						<field name=\"negated\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"" + negated + "\"/>\n" +
	    				"						<field name=\"triggerEvent\" accessType=\"outputOnly\" type=\"SFBool\"/>\n" +
	    				"						<field name=\"newTriggerEvent\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"false\"/>\n" +
	    				"						<field name=\"element1Coordinates\" accessType=\"initializeOnly\" type=\"SFVec3f\" value=\"" + element1.transform.localTranslationCoordinates.toX3D() + "\"/>\n" +
	    				"						<field name=\"element2Coordinates\" accessType=\"initializeOnly\" type=\"SFVec3f\" value=\"" + element2.transform.localTranslationCoordinates.toX3D() + "\"/>\n" +
	    				"						<field name=\"updateElement1Coordinates\" accessType=\"inputOnly\" type=\"SFVec3f\" value=\"\"/>\n" +
	    				"						<field name=\"updateElement2Coordinates\" accessType=\"inputOnly\" type=\"SFVec3f\" value=\"\"/>\n" +
	    				"						<field name=\"squaredThresholdDistance\" accessType=\"initializeOnly\" type=\"SFFloat\" value=\"" + distance * distance + "\"/>\n" +
	    				"						<field name=\"traceEnabled\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"true\"/>\n" + (scriptType == ScriptType.EMBEDDED ?
	    				"						<![CDATA[\n" +
	    	            "							ecmascript:\n" +
	    	            "\n" +
	    	            "							function initialize() {\n" +
	    	            "								updateElement1Coordinates(element1Coordinates);\n" +
	    	            "								updateElement2Coordinates(element2Coordinates);\n" +
	    	            "							}\n" +
	    	            "\n" +
	    	            "							function tracePrint(outputString) {\n" +
	    	            "								if (traceEnabled) {\n" +
	    	            "									Browser.print('[DistanceSensor] ' + outputString + '\\n');\n" +
	    	            "								}\n" +
	    	            "							}\n" +
	    	            "\n" +
	    	            "							function computeAndCheckSquaredDistance(coord1, coord2) {\n" +
	    	            "								tracePrint('x = ' + coord1[0] + '\\ty = ' + coord1[1] + '\\tz = ' + coord1[2]);\n" +
	    	            "								tracePrint('x = ' + coord2[0] + '\\ty = ' + coord2[1] + '\\tz = ' + coord2[2]);\n" +
	    	            "								tracePrint('squaredDistance = ' + ((coord2[0] - coord1[0]) * (coord2[0] - coord1[0]) + (coord2[1] - coord1[1]) * (coord2[1] - coord1[1]) + (coord2[2] - coord1[2]) * (coord2[2] - coord1[2])));\n" +
	    	            "								return ((coord2[0] - coord1[0]) * (coord2[0] - coord1[0]) + (coord2[1] - coord1[1]) * (coord2[1] - coord1[1]) + (coord2[2] - coord1[2]) * (coord2[2] - coord1[2])) <= squaredThresholdDistance ? true : false;\n" +
	    	            "							}\n" +
	    	            "\n" +
	    	            "							function updateElement1Coordinates(newCoordinates) {\n" +
	    	            "								if (enabled) {\n" +
	    	            "									element1Coordinates = newCoordinates;\n" +
	    	            "									newTriggerEvent = computeAndCheckSquaredDistance(element1Coordinates, element2Coordinates);\n" +
	    	            "									checkTrigger();\n" +
	    	            "								}\n" +
	    	            "								else {\n" +
	    	            "									tracePrint('[update1] DistanceSensor disabled');\n" +
	    	            "								}\n" +	
	    				"							}\n" +
	    				"\n" +
	    				"							function updateElement2Coordinates(newCoordinates) {\n" +
	    				"								if (enabled) {\n" +
	    				"									element2Coordinates = newCoordinates;\n" +
	    				"									newTriggerEvent = computeAndCheckSquaredDistance(element1Coordinates, element2Coordinates);\n" +
	    				"									checkTrigger();\n" +
	    	    		"								}\n" +
	    				"								else {\n" +
	    	    		"									tracePrint('[update 2] DistanceSensor disabled');\n" +
	    				"								}\n" +
	    	    		"							}\n" +
	    	    		"\n" +
	    	    		"							function checkTrigger() {\n" +
	    	    		"								if (negated) {\n" +
	    	    		"									newTriggerEvent = !newTriggerEvent;\n" +
	    	    		"								}\n" +
	    	    		"								if (newTriggerEvent != triggerEvent) {\n" +
	    	    		"									if (!repeatable && newTriggerEvent) {\n" +
	    	            "										enabled = false;\n" +
	    	            "									}\n" +
	    	            "									triggerEvent = newTriggerEvent;\n" +
	    	            "								}\n" +
	    	            "								tracePrint('[checkTrigger] triggerEvent = ' + triggerEvent);\n" +
	    	            "							}\n" +
	    	            "						]]>\n" : "") +
	    				"					</Script>\n" +
	    				"					<ROUTE fromNode=\"" + (element1IsCamera ? "HereIAm_World" : element1.getName() + "_Translation") + "\" fromField=\"" + (element1IsCamera ? "position" : "translation")  + "_changed\" toNode=\"" + name + "\" toField=\"updateElement1Coordinates\"/>\n" +
	    				"					<ROUTE fromNode=\"" + element2.getName() + "_Translation\" fromField=\"translation_changed\" toNode=\"" + name + "\" toField=\"updateElement2Coordinates\"/>\n" +
	    				"					<ROUTE fromNode=\"" + name + "\" fromField=\"triggerEvent\" toNode=\"" + name + "_Filter\" toField=\"set_boolean\"/>\n";
		return X3DString;
	}
}