package x3doors.actions.controllers;

import java.util.ArrayList;

import math.Matrix4;
import math.Vec3;
import math.Vec4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import util.MyNodeList;
import util.Utils;

import x3doors.DocInstance;
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

    private String keyX3DString = "";
    private String positionKV= "";
    private String orientationKV= "";
    private String directionKV= "";
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
    }


    public MyNodeList toX3Dom(){ 
        MyNodeList wrapper = new MyNodeList(); 

        int timesSize = times.size();
        double cycleInterval = times.get(timesSize - 1);
        SceneObject attachedSceneObject = SceneObject.get(attachedTo);
        boolean isLight= attachedSceneObject instanceof Light;

        setKeyValues(cycleInterval); 
        getTimeElement(cycleInterval, wrapper); 
        getTranslationRoutes(wrapper);  
        if (isLight)
             getDirectionRoutes(wrapper); 
        else 
            getOrientationRoutes(wrapper); 
        if(!repeat.equals("Clamp")){
            Element route = DocInstance.getInstance().createElement("ROUTE"); 
            route.setAttribute("fromField", "isActive"); 
            route.setAttribute("fromNode", "Clock_" + name); 
            route.setAttribute("toField", "loop"); 
            route.setAttribute("toNode", "Clock_"+ name); 
            wrapper.appendChild(route); 
        }
        return wrapper;         
    }

    private double setKeyValues(double cycleInterval){
    double offset = times.get(0); 
    boolean attachedSceneObjectIsLight = SceneObject.get(attachedTo) instanceof Light;
    int timesSize = times.size();
    double range = times.get(timesSize - 1) - offset;
        switch (repeat) {
            case "Clamp":
                keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2DoubleArray(times), cycleInterval), ' ', false);
                positionKV += Utils.arrayList2X3DString(translations);
                if (attachedSceneObjectIsLight) {
                    directionKV += Utils.arrayList2X3DString(directions);
                }
                orientationKV += Utils.arrayList2X3DString(rotations);
                break;
            case "Wrap":
                cycleInterval -= offset;

                keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.subtractDoubleOffset(Utils.doubleList2DoubleArray(times), offset), range), ' ', false);
                positionKV += Utils.arrayList2X3DString(translations);
                if (attachedSceneObjectIsLight) {
                    directionKV += Utils.arrayList2X3DString(directions);
                }
                orientationKV += Utils.arrayList2X3DString(rotations);
                break;
            case "WrapWithDelay":
                keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2DoubleArray(times), cycleInterval), ' ', false);
                positionKV += Utils.arrayList2X3DString(translations);
                if (attachedSceneObjectIsLight) {
                    directionKV += Utils.arrayList2X3DString(directions);
                }
                orientationKV += Utils.arrayList2X3DString(rotations);
                break;
            case "Cycle":
                cycleInterval = (cycleInterval - offset) * 2;

                double[] cyclizedTimes = Utils.doubleList2CyclizedDoubleArray(times);
                offset = cyclizedTimes[0];
                range = cyclizedTimes[timesSize * 2 - 2] - offset;
                keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.subtractDoubleOffset(cyclizedTimes, offset), range), ' ', false);
                positionKV += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(translations)), ' ', false);
                if (attachedSceneObjectIsLight) {
                    directionKV += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(directions)), ' ', false);
                }
                orientationKV += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(rotations)), ' ', false);
                break;
            case "CycleWithDelay":
                cycleInterval = cycleInterval * 2;
                keyX3DString += Utils.doubleArray2StringFormat(Utils.normalizeDoubleArray(Utils.doubleList2CyclizedDoubleArray(times), cycleInterval), ' ', false);
                positionKV += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(translations)), ' ', false);
                if (attachedSceneObjectIsLight) {
                    directionKV += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(directions)), ' ', false);
                }
                orientationKV += Utils.stringArray2String(Utils.mirror(Utils.arrayList2X3DStringArray(rotations)), ' ', false);
        }
    
        return cycleInterval;     
    }
    private void getTimeElement(double cycleInterval, MyNodeList wrapper){ 
        Document doc = DocInstance.getInstance();
        Element timeTrigger = doc.createElement("TimeTrigger"); 
        timeTrigger.setAttribute("DEF", name + "_Trigger"); 
        Element timeSensor = doc.createElement("TimeSensor") ; 
        timeSensor.setAttribute("DEF", "Clock_" + name); 
        timeSensor.setAttribute("enabled", "true") ; 
        timeSensor.setAttribute("loop", "false") ; 
        timeSensor.setAttribute("cycleInterval", Utils.double2StringFormat(cycleInterval)); 
        Element route1 = doc.createElement("ROUTE") ; 
        route1.setAttribute("fromNode", name+"_Trigger"); 
        route1.setAttribute("fromField", "triggerTime"); 
        route1.setAttribute("toNode", "Clock_"+ name); 
        route1.setAttribute("toField", "startTime");
        wrapper.appendChild(timeTrigger) ; 
        wrapper.appendChild(timeSensor); 
        wrapper.appendChild(route1) ; 
        
    }
    private void getTranslationRoutes(MyNodeList wrapper){
        Document doc= DocInstance.getInstance(); 
        SceneObject attachedSceneObject = SceneObject.get(attachedTo); 
        boolean isLight = attachedSceneObject instanceof Light ; 
        Element route1 = doc.createElement("ROUTE") ; 
        Element route2 = doc.createElement("ROUTE") ; 
        route1.setAttribute("fromNode", "Clock_" + name); 
        route1.setAttribute("fromField", "fraction_changed"); 
        route1.setAttribute("toNode", name + "_translation"); 
        route1.setAttribute("toField", "set_fraction"); 
        route2.setAttribute("fromNode", name + "_translation"); 
        route2.setAttribute("fromField", "value_changed"); 
        route2.setAttribute("toNode", attachedSceneObject.getName() + (isLight ? "" : "_Translation") ); 
        route2.setAttribute("toField", "set_" + (isLight ? "location" : "translation")); 
        Element posInterpolator = doc.createElement("PositionInterpolator"); 
        posInterpolator.setAttribute("DEF", name + "_translation"); 
        posInterpolator.setAttribute("key", keyX3DString);
        posInterpolator.setAttribute("keyValue", positionKV);
        wrapper.appendChild(posInterpolator) ; 
        wrapper.appendChild(route1); 
        wrapper.appendChild(route2); 
    }
    private void getDirectionRoutes(MyNodeList wrapper){ 
        Document doc= DocInstance.getInstance(); 
        Element route1 = doc.createElement("ROUTE") ; 
        Element route2 = doc.createElement("ROUTE") ; 
        route1.setAttribute("fromNode", "Clock_" + name); 
        route1.setAttribute("fromField", "fraction_changed"); 
        route1.setAttribute("toNode", name + "_Rotation"); 
        route1.setAttribute("toField", "set_fraction"); 
        route2.setAttribute("fromNode", name + "_Rotation"); 
        route2.setAttribute("fromField", "value_changed"); 
        route2.setAttribute("toNode", SceneObject.get(attachedTo).getName() ); 
        route2.setAttribute("toField", "set_direction"); 
        Element posInterpolator = doc.createElement("PositionInterpolator"); 
        posInterpolator.setAttribute("DEF", name + "_Rotation"); 
        posInterpolator.setAttribute("key", keyX3DString);
        posInterpolator.setAttribute("keyValue", directionKV);
        wrapper.appendChild(posInterpolator) ; 
        wrapper.appendChild(route1); 
        wrapper.appendChild(route2); 
    }

    private void getOrientationRoutes(MyNodeList wrapper){ 
        Document doc= DocInstance.getInstance(); 
        SceneObject attachedSceneObject = SceneObject.get(attachedTo); 
        Element route1 = doc.createElement("ROUTE") ; 
        Element route2 = doc.createElement("ROUTE") ; 
        route1.setAttribute("fromNode", "Clock_" + name); 
        route1.setAttribute("fromField", "fraction_changed"); 
        route1.setAttribute("toNode", name + "_Rotation"); 
        route1.setAttribute("toField", "set_fraction"); 
        route2.setAttribute("fromNode", name + "_Rotation"); 
        route2.setAttribute("fromField", "value_changed"); 
        route2.setAttribute("toNode", attachedSceneObject.getName() + "_Rotation" ); 
        route2.setAttribute("toField", "set_rotation"); 
        Element orInterpolator = doc.createElement("OrientationInterpolator"); 
        orInterpolator.setAttribute("DEF", name + "_Rotation"); 
        orInterpolator.setAttribute("key", keyX3DString);
        orInterpolator.setAttribute("keyValue", orientationKV);
        wrapper.appendChild(orInterpolator) ; 
        wrapper.appendChild(route1); 
        wrapper.appendChild(route2); 
    }
}







