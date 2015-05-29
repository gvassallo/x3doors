package x3doors.actions.sensors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import util.MyNodeList;

import x3doors.DocInstance;


public class OR extends LogicalOperator {
    /** Creates an OR sensor with the given parameters.
     * 
     * @param name The name
     * @param negated If true then the related sensor event is triggered on the (!sensor1 && !sensor2) condition
     * @param repeatable If true then this sensor can trigger its related event more than once
     * @param sensor1 First sensor which condition will be evaluated by this sensor
     * @param sensor2 Second sensor which condition will be evaluated by this sensor
     */
    public OR(String name, boolean negated, boolean repeatable, Sensor sensor1, Sensor sensor2) {
        super(name, negated, repeatable, sensor1, sensor2, Type.OR);
    }

    public MyNodeList toX3Dom(){ 
        MyNodeList wrapper = new MyNodeList(); 
        Document doc = DocInstance.getInstance(); 
        Element orSensor = doc.createElement("BooleanOr"); 
        orSensor.setAttribute("DEF", name); 
        for (Node n : sensor1.toX3Dom().getChildren())
            wrapper.appendChild((Element) n); 
        for (Node n : sensor2.toX3Dom().getChildren())
            wrapper.appendChild((Element) n); 
        Element filter = doc.createElement("BooleanFilter"); 
        filter.setAttribute("DEF", name + "_Filter"); 
        Element route1 = doc.createElement("ROUTE"); 
        route1.setAttribute("fromNode", sensor1.getName() + "_Filter"); 
        route1.setAttribute("fromField", "inputTrue"); 
        route1.setAttribute("toNode", name) ; 
        route1.setAttribute("toField", "firstValue"); 
        Element route2 = doc.createElement("ROUTE"); 
        route2.setAttribute("fromNode", sensor2.getName() + "_Filter"); 
        route2.setAttribute("fromField", "inputTrue"); 
        route2.setAttribute("toNode", name) ; 
        route2.setAttribute("toField", "secondValue"); 
        Element route3 = doc.createElement("ROUTE"); 
        route3.setAttribute("fromNode", name); 
        route3.setAttribute("fromField", negated ? "inputFalse" : "inputTrue"); 
        route3.setAttribute("toNode", name + "_Filter") ; 
        route3.setAttribute("toField", "set_boolean"); 
        wrapper.appendChild(filter).appendChild(orSensor).appendChild(route1).appendChild(route2).appendChild(route3); 
        return wrapper;      
    }	
}
