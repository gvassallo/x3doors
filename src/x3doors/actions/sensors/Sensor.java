package x3doors.actions.sensors;

import util.Printable;
import util.*;

public abstract class Sensor implements Printable, X3DomExportable {

    /* This counter is used to assign the sensor name automatically when it is not specified */
    private static int counter = 0;

    /** The sensor name. */
    protected String name;
    protected boolean negated;
    protected boolean repeatable;
    protected String type;

     /* Defines the sensor type. */
    protected enum Type {
        CLICK,
        DELAY,
        DISTANCE,
        AND,
        OR
    }

    /** Creates a sensor with the given properties.
     * 
     * @param name The name
     * @param negated If true then the related sensor event is triggered on the complementary of its original logical condition
     * @param repeatable If true then this sensor can trigger its related event more than once
     * @param type The type
     */
    public Sensor(String name, boolean negated, boolean repeatable, Type type) {
        switch (type) {
            case CLICK:
                this.type = "Click";
                break;
            case DELAY:
                this.type = "Delay";
                break;
            case DISTANCE:
                this.type = "Distance";
                break;
            case AND:
                this.type = "AND";
                break;
            case OR:
                this.type = "OR";
                break;
        }
        this.name = (name == null || name.equals("")) ? (this.type + "Sensor_" + counter++) : name;
        this.negated = negated;
        this.repeatable = repeatable;
    }

    /** @return The name */
    public String getName() {
        return name;
    }

    /** @return The type */
    public String getType() {
        return type;
    }

}
