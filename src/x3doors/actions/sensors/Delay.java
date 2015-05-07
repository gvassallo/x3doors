package x3doors.actions.sensors;

import util.Utils;

public class Delay extends Sensor {

	/* The delay time in seconds */
	private double delayTime;
	
	// negated and repeatable attributes have no sense for this kind of sensor
	
	/** Creates a delay sensor with the given properties.
	 * 
	 * @param name The name
	 * @param negated If true then the related sensor event is triggered on the complementary of its original logical condition
	 * @param repeatable If true then this sensor can trigger its related event more than once
	 * @param delayTime The delay time in seconds.
	 */
	public Delay(String name, boolean negated, boolean repeatable, double delayTime) {
		super(name, negated, repeatable, Type.DELAY);
		this.delayTime = delayTime;
	}
	
	/** @return This sensor X3D string */
	public String toX3D() {
		if (delayTime == 0) {
			delayTime = 0.01;
		}
		String X3DString =	"					<BooleanFilter DEF=\"" + name + "_Filter\"/>\n" +
							"					<TimeSensor DEF=\"Clock_" + name + "\" enabled=\"true\" loop=\"true\" cycleInterval=\"" + Utils.double2StringFormat(delayTime) + "\"/>\n" +
							"					<Script DEF=\"" + name + "\"" + (scriptType == ScriptType.EXTERNAL ? " url=\'\"Scripts/DelaySensor.js\" \"http://someURI/DelaySensor.js\"\'>\n" : ">\n") +
							"						<field name=\"set_fraction\" accessType=\"inputOnly\" type=\"SFFloat\" value=\"\"/>\n" +
							"						<field name=\"delayComplete\" accessType=\"outputOnly\" type=\"SFBool\" value=\"\"/>\n" +
					        "						<field name=\"LocalTimeDelayClock\" accessType=\"initializeOnly\" type=\"SFNode\" value=\"\">\n" +
							"							<TimeSensor USE=\"Clock_" + name + "\"/>\n" +
					        "						</field>\n" +
					        "						<field name=\"priorDelayInterval\" accessType=\"initializeOnly\" type=\"SFTime\" value=\"1\"/>\n" +
					        "						<field name=\"delayStarted\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"false\"/>\n" +
					        "						<field name=\"loopClock\" accessType=\"outputOnly\" type=\"SFBool\" value=\"\"/>\n" +
					        "						<field name=\"startFraction\" accessType=\"initializeOnly\" type=\"SFFloat\" value=\"-1\"/>\n" +
					        "						<field name=\"traceEnabled\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"true\"/>\n" +
					        "						<field name=\"previousFraction\" accessType=\"initializeOnly\" type=\"SFFloat\" value=\"1\"/>\n" + (scriptType == ScriptType.EMBEDDED ?
					        "							<![CDATA[\n" +
					        "								ecmascript:\n" +
					        "\n" +
					        "								function tracePrint(outputString) {\n" +
					        "									if (traceEnabled) {\n" +
					        "										Browser.print ('[DelaySensor] ' + outputString + '\\n');\n" +
					        "									}\n" +
					        "								}\n" +
					        "\n" +
					        "								function set_fraction (currentFraction, timestamp) {\n" +
					        "									if (!delayStarted) {\n" +
					        "										delayStarted = true;\n" +
					        "										startFraction = currentFraction;\n" +
					        "										tracePrint ('startFraction = ' + startFraction);\n" +
					        "										tracePrint ('delayStartTime = ' + timestamp);\n" +
					        "									}\n" +
					    	"									if (previousFraction < startFraction && currentFraction >= startFraction) {\n" +
					        "										delayComplete = true;\n" +
					    	"										tracePrint ('previousFraction = ' + previousFraction);\n" +
					        "										tracePrint ('endFraction = ' + currentFraction);\n" +
					    	"										loopClock = false;\n" +
					        "									}\n" +
					    	"									previousFraction = currentFraction;\n" +
					        "								}\n" +
					        "							]]>\n" : "") +
					        "					</Script>\n" +
							"					<ROUTE fromNode=\"Clock_" + name + "\" fromField=\"fraction_changed\" toNode=\"" + name + "\" toField=\"set_fraction\"/>\n" +
							"					<ROUTE fromNode=\"" + name + "\" fromField=\"loopClock\" toNode=\"Clock_" + name + "\" toField=\"loop\"/>\n" +
							"					<ROUTE fromNode=\"" + name + "\" fromField=\"delayComplete\" toNode=\"" + name + "_Filter\" toField=\"set_boolean\"/>\n";
		return X3DString;
	}
}