package x3doors.nodes; 

import math.Matrix4;
import math.Vec3;
import math.Vec4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.MyNodeList;
import util.RGBColor;
import util.Utils;

import x3doors.DocInstance;



public class Light extends SceneObject {
	public static boolean drawLightSources = false;
	/* The light type */
	Type type;
	/** The chromatic light components. */
	protected RGBColor ambient;
	protected RGBColor diffuse;
	protected RGBColor specular;
	/** The light attenuation components */
	protected double constant;
	protected double linear;
	protected double quadratic;
	protected double intensity;
	/** The light angle. */
	// 3Doors range: [0.0001, 90.0]
	protected double angle;
	/** The light attenuation. */
	protected double exponent;
	
	/* Defines the light types */
	public enum Type {
		AMBIENT,
		DIRECTIONAL,
		POINT,
		SPOT
	}
	
	/** Creates a light using the given parameters.
	 * 
	 * @param name The name
	 * @param type The type
	 * @param worldTranslationCoordinates The initial world translation coordinates
	 * @param worldRotationCoordinates The initial world rotation coordinates
	 * @param worldScaleFactor The initial world scale factor
	 * @param parent The parent
	 * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
	 */
	public Light(String name, Type type, Vec3 worldTranslationCoordinates, Vec4 worldRotationCoordinates, SceneObject parent) throws Exception {
		// worldScaleFactor attribute doesn't do anything for a light scene object
		super(worldTranslationCoordinates, worldRotationCoordinates, new Vec3(1, 1, 1), true, parent);
		switch (type) {
			case AMBIENT:
				this.name = (name == null || name.equals("")) ? ("Ambient_" + handle) : name;
				break;
			case DIRECTIONAL:
				this.name = (name == null || name.equals("")) ? ("Directional_" + handle) : name;
				break;
			case POINT:
				this.name = (name == null || name.equals("")) ? ("Point_" + handle) : name;
				break;
			case SPOT:
				this.name = (name == null || name.equals("")) ? ("Spot_" + handle) : name;
				break;
		}
		this.type = type;
		this.ambient = new RGBColor(25, 25, 25);
		this.diffuse = new RGBColor(255, 255, 255);
		this.specular = new RGBColor(255, 255, 255);
		this.constant = 1.0;
		this.linear = 0.0;
		this.quadratic = 0.0;
		this.intensity = 1.0;
		this.angle = 45.0;
		this.exponent = 1.0;
	}
	
	/** Creates a camera using root as parent and the given parameters.
	 * 
	 * @param name The name
	 * @param type The type
	 * @param worldTranslationCoordinates The initial world translation coordinates
	 * @param worldRotationCoordinates The initial world rotation coordinates
	 * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
	 */
	public Light(String name, Type type, Vec3 worldTranslationCoordinates, Vec4 worldRotationCoordinates) throws Exception {
		this(name, type, worldTranslationCoordinates, worldRotationCoordinates, Root.getInstance());
	}
	

	@Override
	public MyNodeList toX3Dom() {
        Document doc = DocInstance.getInstance();
		Vec4 direction = new Vec3(0, 0, -1).toVec4().times(new Matrix4().setRotate(transform.localRotationCoordinates.x, transform.localRotationCoordinates.y, transform.localRotationCoordinates.z, transform.localRotationCoordinates.w));
		double angleRad = angle * Math.PI / 180;
        MyNodeList wrapper= new MyNodeList(); 
        Element light; 
        switch(type){ 
            case AMBIENT : 
                return wrapper;
            case DIRECTIONAL : 	
                light = doc.createElement("DirectionalLight"); 
                light.setAttribute("DEF", name);
                light.setAttribute("color", diffuse.toX3D()); 
                light.setAttribute("direction", direction.toVec3().toX3D());
                light.setAttribute("intensity", Utils.double2StringFormat(intensity)); 
                light.setAttribute("ambientIntensity", "0.1"); 
                break; 
            case POINT : 
                light = doc.createElement("PointLight"); 
                light.setAttribute("DEF", name);
                light.setAttribute("color", diffuse.toX3D()); 
                light.setAttribute("intensity", Utils.double2StringFormat(intensity)); 
                light.setAttribute("location", transform.localTranslationCoordinates.toX3D());
                light.setAttribute("ambientIntensity", "0.1"); 
                light.setAttribute("attuation", Utils.double2StringFormat(constant) + " " + Utils.double2StringFormat(linear) + " " + Utils.double2StringFormat(quadratic));
                break; 
            case SPOT: 
                light = doc.createElement("SpotLight"); 
                light.setAttribute("DEF", name);
                light.setAttribute("color", diffuse.toX3D()); 
                light.setAttribute("direction", direction.toVec3().toX3D());
                light.setAttribute("intensity", Utils.double2StringFormat(intensity)); 
                light.setAttribute("ambientIntensity", "0.1"); 
                light.setAttribute("attuation", Utils.double2StringFormat(constant) + " " + Utils.double2StringFormat(linear) + " " + Utils.double2StringFormat(quadratic));
                light.setAttribute("location", transform.localTranslationCoordinates.toX3D());
                light.setAttribute("beamWidth", Utils.double2StringFormat(angleRad)); 
                light.setAttribute("cutOffAngle", Utils.double2StringFormat(Math.PI / 2 - angleRad));
                break;
            default:
                light = doc.createElement("Default"); 
        }
        wrapper.appendChild(light);                 
		return wrapper;
	}
}
