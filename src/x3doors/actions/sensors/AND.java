package x3doors.actions.sensors;


public class AND extends LogicalOperator {
	/** Creates an AND sensor with the given parameters.
	 * 
	 * @param name The name
	 * @param negated If true then the related sensor event is triggered on the (!sensor1 || !sensor2) condition
	 * @param repeatable If true then this sensor can trigger its related event more than once
	 * @param sensor1 First sensor which condition will be evaluated by this sensor
	 * @param sensor2 Second sensor which condition will be evaluated by this sensor
	 */
	public AND(String name, boolean negated, boolean repeatable, Sensor sensor1, Sensor sensor2) {
		super(name, negated, repeatable, sensor1, sensor2, Type.AND);
	}
	
	/** @return This sensor X3D string */
	public String toX3D() {
		return super.toX3D();
	}
}