package x3doors.actions.controllers;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.MyNodeList;
import util.RGBColor;
import util.Utils;

import x3doors.DocInstance;
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
    
    private String transparencyScalarInterpolatorKeyValueX3DString = "";
    private String diffuseColorInterpolatorKeyValueX3DString = "";
    private String emissiveColorInterpolatorKeyValueX3DString = "";
    private String shininessScalarInterpolatorKeyValueX3DString = "";
    private String specularColorInterpolatorKeyValueX3DString = "";
    private String keyX3DString = "";
    
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

    public double setKeyValues() {
        int timesSize = times.size();
        double cycleInterval = times.get(timesSize - 1);
        double offset = times.get(0);
        double range = times.get(timesSize - 1) - offset;

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
        /* keyX3DString += "\""; */
        /* diffuseColorInterpolatorKeyValueX3DString += "\""; */
        /* emissiveColorInterpolatorKeyValueX3DString += "\""; */
        /* specularColorInterpolatorKeyValueX3DString += "\""; */
        /* shininessScalarInterpolatorKeyValueX3DString += "\""; */
        /* transparencyScalarInterpolatorKeyValueX3DString += "\""; */
        return cycleInterval;
        }
        
    public MyNodeList toX3Dom(){
        double cycleInterval = this.setKeyValues();
        MyNodeList wrapper = new MyNodeList(); 
        Document doc = DocInstance.getInstance(); 
        getTimeElement(wrapper, cycleInterval);
        getColorInterpolator(wrapper, "diffuseColor", diffuseColorInterpolatorKeyValueX3DString);
        getColorInterpolator(wrapper, "emissiveColor", emissiveColorInterpolatorKeyValueX3DString); 
        getColorInterpolator(wrapper, "specularColor", specularColorInterpolatorKeyValueX3DString); 
        getScalarInterpolator(wrapper, "shininess", shininessScalarInterpolatorKeyValueX3DString);
        getScalarInterpolator(wrapper, "transparency", transparencyScalarInterpolatorKeyValueX3DString);
        if (!repeat.equals("Clamp")) { // if repeat is Wrap or Cycle
            Element route = doc.createElement("ROUTE");
            route.setAttribute("fromNode", "Clock_"+ name); 
            route.setAttribute("fromField", "isActive"); 
            route.setAttribute("toNode", "Clock_"+ name); 
            route.setAttribute("toField", "loop");
        }
        return wrapper; 
    }
    private void getTimeElement(MyNodeList wrapper, double cycleInterval){
         Document doc = DocInstance.getInstance();
        Element timeTrigger = doc.createElement("TimeTrigger"); 
        timeTrigger.setAttribute("DEF", name + "_Trigger"); 
        Element timeSensor = doc.createElement("TimeSensor"); 
        timeSensor.setAttribute("DEF", "Clock_"+ name); 
        timeSensor.setAttribute("enabled", "true"); 
        timeSensor.setAttribute("loop", "false"); 
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


    private void getColorInterpolator(MyNodeList wrapper, String property, String keyValue){
            Document doc = DocInstance.getInstance(); 
            Element colorInterpolator = doc.createElement("ColorInterpolator"); 
            colorInterpolator.setAttribute("DEF", name +"_" + property); 
            colorInterpolator.setAttribute("key",keyX3DString ); 
            colorInterpolator.setAttribute("keyValue", keyValue); 
            Element route1 = doc.createElement("ROUTE");
            route1.setAttribute("fromNode", "Clock_" + name);
            route1.setAttribute("fromField", "fraction_changed");
            route1.setAttribute("toNode", name + "_" + property);
            route1.setAttribute("toField", "set_fraction");
            Element route2 = doc.createElement("ROUTE");
            route2.setAttribute("fromNode", name + "_" + property);
            route2.setAttribute("fromField", "value_changed");
            route2.setAttribute("toNode", SceneObject.get(attachedTo).getName() + "_Material");
            route2.setAttribute("toField", "set_" + property );
            wrapper.appendChild(colorInterpolator).appendChild(route1).appendChild(route2); 
    }

    private void getScalarInterpolator(MyNodeList wrapper, String property, String keyValue){
            Document doc = DocInstance.getInstance(); 
            Element scalarInterpolator = doc.createElement("ScalarInterpolator"); 
            scalarInterpolator.setAttribute("DEF", name +"_" + property); 
            scalarInterpolator.setAttribute("key", keyX3DString); 
            scalarInterpolator.setAttribute("keyValue", keyValue); 
            Element route1 = doc.createElement("ROUTE");
            route1.setAttribute("fromNode", "Clock_" + name);
            route1.setAttribute("fromField", "fraction_changed");
            route1.setAttribute("toNode", name + "_" + property);
            route1.setAttribute("toField", "set_fraction");
            Element route2 = doc.createElement("ROUTE");
            route2.setAttribute("fromNode", name + "_" + property);
            route2.setAttribute("fromField", "value_changed");
            route2.setAttribute("toNode",  SceneObject.get(attachedTo).getName()+ "_Material");
            route2.setAttribute("toField",  "set_"+ property );
            wrapper.appendChild(scalarInterpolator).appendChild(route1).appendChild(route2); 
    }
}
