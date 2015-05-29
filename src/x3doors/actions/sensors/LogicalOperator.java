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

}



