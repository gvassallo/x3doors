package x3doors.actions.sensors;


public abstract class LogicalOperator extends Sensor {
    /** The first sensor. */
    protected Sensor sensor1;
    /** The second sensor. */
    protected Sensor sensor2;

    /** Creates a logical sensor with the given properties.
     * 
     * @param name The name
     * @param negated If true then the related sensor event is triggered on the complementary of its original logical condition
     * @param repeatable If true then this sensor can trigger its related event more than once
     * @param sensor1 First sensor which condition will be evaluated by this sensor
     * @param sensor2 Second sensor which condition will be evaluated by this sensor
     * @param type The sensor type, it can assume the following values<p>
     * AND: set this logical sensor to become an AND sensor<p>
     * OR: set this logical sensor to become an OR sensor
     */
    public LogicalOperator(String name, boolean negated, boolean repeatable, Sensor sensor1, Sensor sensor2, Type type) {
        super(name, negated, repeatable, type);
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
    }

    /** @return The first sensor */
    public Sensor getSensor1() {
        return sensor1;
    }

    /** @return The second sensor */
    public Sensor getSensor2() {
        return sensor2;
    }

    /** @return This sensor X3D string */
    public String toX3D() {
        // TODO: negated implementation both in embedded and external scripts
        String sensorType = this.getType();
        String X3DString =	sensor1.toX3D() +
            sensor2.toX3D() +
            "					<BooleanFilter DEF=\"" + name + "_Filter\"/>\n" +
            "					<Script DEF=\"" + name + "\"" + (scriptType == ScriptType.EXTERNAL ? " url=\'\"Scripts/" + sensorType + "Sensor.js\" \"http://someURI/" + sensorType + "Sensor.js\"\'>\n" : ">\n") +
            "						<field name=\"sensor1Value\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"false\"/>\n" +
            "						<field name=\"sensor2Value\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"false\"/>\n" +
            "						<field name=\"updateSensor1Value\" accessType=\"inputOnly\" type=\"SFBool\"/>\n" +
            "						<field name=\"updateSensor2Value\" accessType=\"inputOnly\" type=\"SFBool\"/>\n" +
            "						<field name=\"triggerEvent\" accessType=\"outputOnly\" type=\"SFBool\"/>\n" +
            "						<field name=\"newTriggerEvent\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"false\"/>\n" +
            "						<field name=\"traceEnabled\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"true\"/>\n" +
            "						<field name=\"enabled\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"true\"/>\n" +
            "						<field name=\"repeatable\" accessType=\"initializeOnly\" type=\"SFBool\" value=\"" + repeatable + "\"/>\n" + (scriptType == ScriptType.EMBEDDED ?
                    "						<![CDATA[\n" +
                    "							ecmascript:\n" +
                    "\n" +
                    "							function tracePrint(outputString) {\n" +
                    "								if (traceEnabled) {\n" +
                    "									Browser.print('[" + sensorType + "Sensor] ' + outputString + '\\n');\n" +
                    "								}\n" +
                    "							}\n" +
                    "\n" +
                    "							function updateSensor1Value(newValue) {\n" +
                    "								resetSensorIfNecessary();\n" +
                    "								if (enabled) {\n" +
                    "									tracePrint('[update1] sensor1Value = ' + sensor1Value + '\\tnewValue = ' + newValue);\n" +
                    "									if (newValue && !sensor1Value) {\n" +
                    "										sensor1Value = newValue;\n" +
                    "									}\n" +
                    "									checkTrigger();\n" +
                    "								}\n" +
                    "								else {\n" +
                    "									tracePrint('[update1] " + sensorType + "Sensor disabled');\n" +
                    "								}\n" +
                    "							}\n" +
                    "\n" +
                    "							function updateSensor2Value(newValue) {\n" +
                    "								resetSensorIfNecessary();\n" +
                    "								if (enabled) {\n" +
                    "									tracePrint('[update2] sensor2Value = ' + sensor2Value + '\\tnewValue = ' + newValue);\n" +
                    "									if (newValue && !sensor2Value) {\n" +
                    "										sensor2Value = newValue;\n" +
                    "									}\n" +
                    "									checkTrigger();\n" +
                    "								}\n" +
                    "								else {\n" +
                    "									tracePrint('[update2] " + sensorType + "Sensor disabled');\n" +
                    "								}\n" +
                    "							}\n" +
                    "\n" +
                    "							function resetSensorIfNecessary() {\n" +
                    "								if (triggerEvent) {\n" +
                    "									sensor1Value = false;\n" +
                    "									sensor2Value = false;\n" +
                    "									newTriggerEvent = false;\n" +
                    "									triggerEvent = false;\n" +
                    "								}\n" +
                    "							}\n" +
                    "\n" +
                    "							function checkTrigger() {\n" +
                    "								resetSensorIfNecessary()\n" +
                    "								if (sensor1Value " + (sensorType.equals("AND") ? "&&" : "||") + " sensor2Value) {\n" +
                    "									newTriggerEvent = true;\n" +
                    "								}\n" +
                    "								else {\n" +
                    "									newTriggerEvent = false;\n" +
                    "								}\n" +
                    "								if (newTriggerEvent != triggerEvent) {\n" +
                    "									if (!repeatable && newTriggerEvent) {\n" +
                    "										enabled = false;\n" +
                    "									}" +
                    "									triggerEvent = newTriggerEvent;\n" +    
                    "								}\n" +
                    "								tracePrint('[checkTrigger] triggerEvent = ' + triggerEvent);\n" +
                    "							}\n" +
                    "						]]>\n" : "") +
                    "					</Script>" +
                    "					<ROUTE fromNode=\"" + sensor1.getName() + "_Filter\" fromField=\"inputTrue\" toNode=\"" + name + "\" toField=\"updateSensor1Value\"/>\n" +
                    "					<ROUTE fromNode=\"" + sensor1.getName() + "_Filter\" fromField=\"inputFalse\" toNode=\"" + name + "\" toField=\"updateSensor1Value\"/>\n" +
                    "					<ROUTE fromNode=\"" + sensor2.getName() + "_Filter\" fromField=\"inputTrue\" toNode=\"" + name + "\" toField=\"updateSensor2Value\"/>\n" +
                    "					<ROUTE fromNode=\"" + sensor2.getName() + "_Filter\" fromField=\"inputFalse\" toNode=\"" + name + "\" toField=\"updateSensor2Value\"/>\n" +
                    "					<ROUTE fromNode=\"" + name + "\" fromField=\"triggerEvent\" toNode=\"" + name + "_Filter\" toField=\"set_boolean\"/>\n";
        return X3DString;
    }


}



