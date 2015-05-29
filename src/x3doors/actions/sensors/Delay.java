package x3doors.actions.sensors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.MyNodeList;
import util.Utils;

import x3doors.DocInstance;

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
	
	public MyNodeList toX3Dom() {
		MyNodeList wrapper = new MyNodeList(); 
        Document doc = DocInstance.getInstance(); 
        Element filter = 	doc.createElement("BooleanFilter"); 
        filter.setAttribute("DEF", name+"_Filter"); 
        Element route1 = doc.createElement("ROUTE"); 
        Element delaySensor = doc.createElement("DelaySensor"); 
        delaySensor.setAttribute("DEF", name); 
        delaySensor.setAttribute("delay", Utils.double2StringFormat(delayTime)); 
        route1.setAttribute("fromNode", name); 
        route1.setAttribute("fromField","delayComplete" ); 
        route1.setAttribute("toNode", name+"_Filter"); 
        route1.setAttribute("toField", "set_boolean"); 
        wrapper.appendChild(filter); 
        wrapper.appendChild(delaySensor); 
        wrapper.appendChild(route1); 
        return wrapper;
	}

}
