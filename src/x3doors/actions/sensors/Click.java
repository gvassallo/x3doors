package x3doors.actions.sensors;

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
}