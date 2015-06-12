package x3doors.actions;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import util.Actionable;
import util.MyNodeList;
import util.X3DomExportable;
import x3doors.DocInstance;
import x3doors.actions.sensors.Sensor;

public class Behavior implements  X3DomExportable {
    /* An array list containing the current scene declared Behaviors */
    private static ArrayList<Behavior> register = new ArrayList<Behavior>();

    /* This counter is used to assign the data name automatically when it is not specified */
    private static int counter = 0;

    /* The Behavior name */
    private String name;
    /* The Behavior sensor */
    private Sensor sensor;
    /* The Behavior action, can be either a controller or a data */
    private Actionable action;

    /** Stores a Behavior in the behavior register.
     * 
     * @param The Behavior to be stored
     */
    public static void store(Behavior behavior) {
        register.add(behavior);
    }

    /** Returns the Behavior with the given register index.
     * 
     * @param index The Behavior register index
     * @return The Behavior
     */
    public static Behavior get(int index) {
        return register.get(index);
    }

    /** @return The Behavior register size */
    public static int registerSize() {
        return register.size();
    }

    /** Creates a Behavior with the given properties.
     * 
     * @param name The name
     * @param sensor The sensor
     * @param action The action
     */
    public Behavior(String name, Sensor sensor, Actionable action) {
        this.name = (name == null || name.equals("")) ? ("Behavior_" + counter++) : name;
        this.sensor = sensor;
        this.action = action;
        Behavior.store(this);
    }

    /** @return The sensor */
    public Sensor getSensor() {
        return sensor;
    }

    /** @return The action, either a controller or data */
    public Actionable getAction() {
        return action;
    }

    public MyNodeList toX3Dom(){

        String sensorName = sensor.getName(); 
        String actionName = action.getName(); 
        MyNodeList wrapper = new MyNodeList(); 
        Document doc = DocInstance.getInstance();
        Element BehaviorGroup = doc.createElement("Group"); 
        BehaviorGroup.setAttribute("DEF", this.name); 
        Element group = doc.createElement("Group"); 
        group.setAttribute("DEF", sensorName + "_Group"); 
        Element route = doc.createElement("ROUTE"); 
        route.setAttribute("fromNode", sensorName + "_Filter"); 
        route.setAttribute("fromField", "inputTrue"); 
        route.setAttribute("toNode", actionName + "_Trigger"); 
        route.setAttribute("toField", "set_boolean");
        for (Node n : sensor.toX3Dom().getChildren())
            group.appendChild((Element) n); 

        group.appendChild(route);  
        for (Node n : action.toX3Dom().getChildren())
            BehaviorGroup.appendChild((Element)n);  
        BehaviorGroup.appendChild(group); 
        wrapper.appendChild(BehaviorGroup); 
        return wrapper;  
    }
}
