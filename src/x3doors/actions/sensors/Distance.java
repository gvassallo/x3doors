package x3doors.actions.sensors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.MyNodeList;

import x3doors.DocInstance;
import x3doors.nodes.Camera;
import x3doors.nodes.SceneObject;


public class Distance extends Sensor {
    /* The distance between the two elements */
    private double distance;
    /* The first element */
    private SceneObject element1;
    /* The second element */
    private SceneObject element2;


    private Element script; 
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
        this.distance = distance;
        this.element1 = element1; 
        this.element2 = element2; 
        this.name = (name == null || name.equals("")) ? "DistanceSensor_" + element1.getName() + element2.getName() : this.name;
    }

    public MyNodeList toX3Dom(){
        MyNodeList wrapper = new MyNodeList(); 
        Document doc = DocInstance.getInstance(); 
        boolean isCamera1 = element1 instanceof Camera; 
        boolean isCamera2 = element2 instanceof Camera; 

        Element filter = doc.createElement("BooleanFilter"); 
        filter.setAttribute("DEF", name + "_Filter"); 
        Element route1 = doc.createElement("ROUTE");    
        Element route2 = doc.createElement("ROUTE");    
        if(!isCamera1){
            route1.setAttribute("fromNode", element1.getName() + "_Translation" ); 
            route1.setAttribute("fromField", "translation_changed" ); 
            route1.setAttribute("toNode", name ); 
            route1.setAttribute("toField", "element1Coordinates" ); 
        }
        if(!isCamera2){
            route2.setAttribute("fromNode", element2.getName() + "_Translation"); 
            route2.setAttribute("fromField", "translation_changed"); 
            route2.setAttribute("toNode", name ); 
            route2.setAttribute("toField", "element2Coordinates"); 
        }
        Element distanceSensor = doc.createElement("DistanceSensor"); 
        distanceSensor.setAttribute("DEF", name); 
        distanceSensor.setAttribute("id", name); 
        distanceSensor.setAttribute("distance", ((Double) distance).toString()); 
        distanceSensor.setAttribute("negated", negated? "true" : "false"); 
        distanceSensor.setAttribute("element1Coordinates",element1.transform.localTranslationCoordinates.toX3D());  
        distanceSensor.setAttribute("element2Coordinates",element2.transform.localTranslationCoordinates.toX3D());  
        Element route3 = doc.createElement("ROUTE");    
        route3.setAttribute("fromNode", name); 
        route3.setAttribute("fromField", "triggerEvent"); 
        route3.setAttribute("toNode", name + "_Filter" ); 
        route3.setAttribute("toField", "set_boolean"); 
        wrapper.appendChild(filter)
            .appendChild(distanceSensor);
        if (!isCamera1)
            wrapper.appendChild(route1);
        if(!isCamera2)
            wrapper.appendChild(route2);
        wrapper.appendChild(route3);
        return wrapper; 
    } 

    public Element getScript(){
        String coordinates = ""; 
        if(element1 instanceof Camera)
            coordinates = "element1Coordinates"; 
        else 
            coordinates = "element2Coordinates"; 
        script=DocInstance.getInstance().createElement("script"); 
        script.setAttribute("name", name); 
        String scriptText = "function " + name + "() {\n"+ 
            "var vp=document.getElementById(\"viewpoint\");\n" +
            "vp.addEventListener(\"viewpointChanged\", " + name + "Handler);\n"+
            "}\n"+
            "function "+ name +"Handler(evt){\n"+
            /* "   console.log(evt.position);\n"+ */
            "   var dist = document.getElementById(\""+ name +"\"); \n"+
            "   dist.setFieldValue(\""+ coordinates + "\", evt.position); }\n";
        script.setTextContent(scriptText); 
        return this.script; 
    }
    public boolean hasCameraElement(){
        if (element1 instanceof Camera || element2 instanceof Camera )
            return true;
        return false ; 
    }

}
