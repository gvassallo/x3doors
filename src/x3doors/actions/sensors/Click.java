package x3doors.actions.sensors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.MyNodeList;

import x3doors.DocInstance;
import x3doors.nodes.SceneObject;

public class Click extends Sensor {
    /* The handle of the scene object which click will trigger the related event */
    private int objectToClick;

    /** Creates a click sensor with the given properties.
     * 
     * @param name The name 
     * @param negated If true then the related sensor event is triggered on the complementary of its original logical condition
     * @param repeatable If true then this sensor can trigger its related event more than once
     * @param objectToClick The handle of the scene object which click will trigger the related event
     */
    public Click(String name, boolean negated, boolean repeatable, SceneObject objectToClick) {
        super(name, negated, repeatable, Type.CLICK);
        this.objectToClick = objectToClick.getHandle();
    }

    /** @return The handle of the scene object which click will trigger the related event */
    public int getObjectToClick() {
        return objectToClick;
    }

    /** @return This sensor X3D string */
    public String toX3D() {
        String X3DString =	"					<BooleanFilter DEF=\"" + name + "_Filter\"/>\n" +
            "					<BooleanFilter DEF=\"NOT" + name + "_Filter\"/>\n";
        if (!repeatable) {
            X3DString += 	"					<ROUTE fromNode=\"" + name + "\" fromField=\"isActive\" toNode=\"" + name + "\" toField=\"enabled\"/>\n";
        }
        X3DString +=		"					<ROUTE fromNode=\"" + name + "\" fromField=\"isActive\" toNode=\"NOT"+ name + "_Filter\" toField=\"set_boolean\"/>\n" +
            "					<ROUTE fromNode=\"NOT" + name + "_Filter\" fromField=\"inputNegate\" toNode=\""+ name + "_Filter\" toField=\"set_boolean\"/>\n";
        return X3DString;
    }

    public MyNodeList toX3Dom(){
        MyNodeList wrapper = new MyNodeList(); 
        Document doc = DocInstance.getInstance(); 
        Element booleanFilter = doc.createElement("BooleanFilter") ; 
        Element booleanFilterNot = doc.createElement("BooleanFilter") ; 
        booleanFilter.setAttribute("DEF", name + "_Filter") ; 
        booleanFilterNot.setAttribute("DEF", "NOT" + name + "_Filter") ; 
        wrapper.appendChild(booleanFilter).appendChild(booleanFilterNot); 
        if (!repeatable){ 
            Element route1 = doc.createElement("ROUTE"); 
            route1.setAttribute("fromNode", name); 
            route1.setAttribute("fromField", "isActive"); 
            route1.setAttribute("toNode", name); 
            route1.setAttribute("toField", "enabled"); 
            wrapper.appendChild(route1); 
        }
        Element route2 = doc.createElement("ROUTE"); 
        route2.setAttribute("fromNode", name); 
        route2.setAttribute("fromField", "isActive"); 
        route2.setAttribute("toNode", "NOT" + name + "_Filter"); 
        route2.setAttribute("toField", "set_boolean"); 
        Element route3 = doc.createElement("ROUTE"); 
        route3.setAttribute("fromNode", "NOT" + name +"_Filter"); 
        route3.setAttribute("fromField", "inputNegate"); 
        route3.setAttribute("toNode", name+"_Filter"); 
        route3.setAttribute("toField", "set_boolean"); 
        wrapper.appendChild(route2).appendChild(route3); 
        return wrapper;  
    }
}
