package x3doors.nodes; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
/* Root class (maybe ) */ 





import math.Matrix4;
import math.Vec3;
import math.Vec4;
import util.Printable;
import util.X3DExportable;


public abstract class SceneObject implements Printable, X3DExportable {
    /* A map which retrieves a scene object given its handle */
    private static HashMap<Integer, SceneObject> register = new HashMap<Integer, SceneObject>();
    /** The scene object handle. */
    protected int handle;
    /** The scene object name, assigned in subclasses only */
    protected String name;
    // TODO: Add and manage the following variables for a scene object
    // protected String DBName;
    // protected int downloadPriority;
    /** The scene object transform */
    public Transform transform;
    /** If true then the scene object and its children are rendered. */
    public boolean visible;
    /** The scene object parent */
    public SceneObject parent;
    /** The scene object children list, used to handle hierarchies. */
    private ArrayList<SceneObject> children;

    /** Stores a scene object in the scene object register by using a hash function.
     * 
     * @param sceneObject The scene object to map (key = handle)
     * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
     */
    public static void store(SceneObject sceneObject) throws Exception {
        int handle = sceneObject.getHandle();
        if (register.get(handle) != null)
            throw new Exception("Collision detected in storing object \"" + handle + "\"");
        register.put(handle, sceneObject);
    }

    /** @return The scene object register key set */
    public static Set<Integer> getRegisterKeySet() {
        return register.keySet();
    }

    /** Return the scene object with the specified handle
     * 
     * @param key The key of the scene object
     * @return The scene object associated to the specified handle
     */
    public static SceneObject get(int key) {
        return register.get(key);
    }

    /** @return The scene object register size */
    public static int registerSize() {
        return register.size();
    }

    /** Creates a scene object with the given properties.
     * 
     * @param worldTranslationCoordinates The initial world translation coordinates
     * @param worldRotationCoordinates The initial world rotation coordinates
     * @param worldScaleFactor The initial world scale factor
     * @param visible The initial rendering state, if true then the scene object and its children are rendered
     * @param parent The parent
     * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
     */
    public SceneObject(Vec3 worldTranslationCoordinates, Vec4 worldRotationCoordinates, Vec3 worldScaleFactor, boolean visible, SceneObject parent) throws Exception {
        // A light object can't have any child
        if (parent instanceof Light) {
            throw new Exception("The " + parent.name + " scene object can't be set as parent.");
        }
        this.handle = HandleFactory.getNewHandle();
        this.parent = parent;
        this.transform = new Transform(worldTranslationCoordinates, worldRotationCoordinates, worldScaleFactor);
        // If this scene object is not Root (Root.parent is null)
        if (parent != null) {
            this.transform.setLocalCoordinatesToEquivalentWorldCoordinates();
        }
        this.visible = visible;
        setChildren(new ArrayList<SceneObject>());
        if (parent != null) {
            parent.addChild(this);
        }
        // Only the active camera must be stored into the register, you can have only one active camera at time
        if (!(this instanceof Camera)) {
            SceneObject.store(this);
        }
    }

    /** Creates a scene object with root as parent and the given properties.
     * 
     * @param worldTranslationCoordinates The initial world translation coordinates
     * @param worldRotationCoordinates The initial world rotation coordinates
     * @param worldScaleFactor The initial world scale factor
     * @param visible The initial rendering state, if true then the scene object and its children are rendered
     * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
     */
    public SceneObject(Vec3 worldTranslationCoordinates, Vec4 worldRotationCoordinates, Vec3 worldScaleFactor, boolean visible) throws Exception {
        this(worldTranslationCoordinates, worldRotationCoordinates, worldScaleFactor, visible, Root.getInstance());
    }

    /** @return The handle */
    public int getHandle() {
        return this.handle;
    }

    /** @param name The new name */
    public void setName(String name) {
        this.name = name;
    }

    /** @return The name */
    public String getName() {
        return this.name;
    }

    /** @param visible The new visible value */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /** @param child The child to add to this scene object */
    public void addChild(SceneObject child) {
        getChildren().add(child);
    }

    /** Print the properties to screen. */
    public void print() {
        System.out.print(	"Handle:\t\t\t" + handle + "\n" +
                "Name:\t\t\t" + name +"\n" +
                "Visible:\t\t\t" + visible + "\n" +
                "Children:\n"
                );
        for (int i = 0; i < getChildren().size(); i++) {
            System.out.print("\t" + getChildren().get(i).name + "\n");
        }
    }
    public ArrayList<SceneObject> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<SceneObject> children) {
		this.children = children;
	}
	public class Transform  {
        /** The position of the transform in world space */
        public Vec3 worldTranslationCoordinates;
        /** The rotation of the transform in world space */
        public Vec4 worldRotationCoordinates;
        /** The scaling of the transform in world space */
        public Vec3 worldScaleFactor;
        /** Position of the transform relative to the parent transform */
        public Vec3 localTranslationCoordinates;
        /** Rotation of the transform relative to the parent transform */
        public Vec4 localRotationCoordinates;
        /** Scaling of the transform relative to the parent transform */
        public Vec3 localScaleFactor;
        /** Matrix that transforms a point from world space into local space */
        protected Matrix4 worldToLocalMatrix;
        /** Matrix that transforms a point from local space into world space */
        public Matrix4 localToWorldMatrix;

        /** Creates a transform with the given components.
         * 
         * @param worldTranslationCoordinates The position of the transform in world space
         * @param worldRotationCoordinates The rotation of the transform in world space
         * @param worldScaleFactor The scaling of the transform in world space
         */
        public Transform(Vec3 worldTranslationCoordinates, Vec4 worldRotationCoordinates, Vec3 worldScaleFactor) {
            this.worldTranslationCoordinates = worldTranslationCoordinates;
            this.worldRotationCoordinates = worldRotationCoordinates;
            this.worldScaleFactor = worldScaleFactor;
            localTranslationCoordinates = worldTranslationCoordinates;
            localRotationCoordinates = worldRotationCoordinates;
            localScaleFactor = worldScaleFactor;
            // worldToLocalMatrix * v' converts v in local space
            worldToLocalMatrix = new Matrix4()
                .setTranslate(this.worldTranslationCoordinates.x, this.worldTranslationCoordinates.y, this.worldTranslationCoordinates.z)
                .rotate(this.worldRotationCoordinates.x, this.worldRotationCoordinates.y, this.worldRotationCoordinates.z, this.worldRotationCoordinates.w)
                .scale(this.worldScaleFactor.x, this.worldScaleFactor.y, this.worldScaleFactor.z);
            // localToWorldMatrix * v' converts v in world space
            localToWorldMatrix = worldToLocalMatrix.invert();
        }

        /** Set this transform local coordinates so that they will mime the behaviours of the world coordinates.<p>
         * Using this method you can set a scene object as child of another one by not noticing any of its visual transform change effects.  */
        public void setLocalCoordinatesToEquivalentWorldCoordinates() {
            Vec4[] newLocalCoordinates = parent.transform.localToWorldMatrix.concat(this.worldToLocalMatrix).decompose();
            localTranslationCoordinates = newLocalCoordinates[2].toVec3();
            localRotationCoordinates = newLocalCoordinates[1];
            localScaleFactor = newLocalCoordinates[0].toVec3();
        }
    }
}
