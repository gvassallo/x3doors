package x3doors.nodes; 

import math.Matrix4;
import math.Vec3;
import math.Vec4;
import util.RGBColor;
import util.Utils;



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
	
	public String toX3D() {
		String X3DString = "";
		Vec4 direction = new Vec3(0, 0, -1).toVec4().times(new Matrix4().setRotate(transform.localRotationCoordinates.x, transform.localRotationCoordinates.y, transform.localRotationCoordinates.z, transform.localRotationCoordinates.w));
		double angleRad = angle * Math.PI / 180;
		switch (type) {
			case AMBIENT:
				break;
			case DIRECTIONAL:
				X3DString +=	"		<DirectionalLight DEF=\"" + name + "\" color=\"" + diffuse.toX3D() + "\" direction=\"" + direction.toVec3().toX3D() + "\" intensity=\"" + Utils.double2StringFormat(intensity) + "\" ambientIntensity=\"0.1\"/>\n" + (Light.drawLightSources ?
								"		<Transform DEF=\"" + name + "_Placeholder_Translation\" translation=\"" + transform.localTranslationCoordinates.toX3D() + "\">\n" +
								"		<Transform DEF=\"" + name + "_Placeholder_Rotation\" rotation=\"" + transform.localRotationCoordinates.toX3D() + "\">\n" +
								"		<Transform scale=\"" + Utils.double2StringFormat(transform.localScaleFactor.x) + " " + Utils.double2StringFormat(transform.localScaleFactor.y) + " " + Utils.double2StringFormat(transform.localScaleFactor.z) + "\">\n" +
								"		<Transform rotation=\"1 0 0 -1.57\">\n" +
								"		<Transform translation=\"0 0 2\">\n" +
								"			<Shape>\n" +
								"				<Cone height=\"1\" bottomRadius=\"0.5\"/>" +
								"				<Appearance>\n" +
								"					<Material emissiveColor=\"1 1 0\" transparency=\"0.5\"/>\n" +
								"				</Appearance>\n" +
								"			</Shape>\n" +
								"			<Transform translation=\"0 -1 0\">\n" +
								"			<Shape>\n" +
								"				<Cylinder height=\"1\" radius=\"0.4\"/>" +
								"				<Appearance>\n" +
								"					<Material emissiveColor=\"1 1 0\" transparency=\"0.5\"/>\n" +
								"				</Appearance>\n" +
								"			</Shape>\n" +
								"		</Transform>\n" +
								"		</Transform>\n" +
								"		</Transform>\n" +
								"		</Transform>\n" +
								"		</Transform>\n" +
								"		</Transform>\n" : "");
				break;
			case POINT:
				X3DString +=	"		<PointLight DEF=\"" + name + "\" color=\"" + diffuse.toX3D() + "\" location=\"" + transform.localTranslationCoordinates.toX3D() + "\" intensity=\"" + Utils.double2StringFormat(intensity) + "\" ambientIntensity=\"0.1\" attenuation=\"" + Utils.double2StringFormat(constant) + " " + Utils.double2StringFormat(linear) + " " + Utils.double2StringFormat(quadratic) + "\"/>\n" + (Light.drawLightSources ?
								"		<Transform DEF=\"" + name + "_Placeholder_Translation\" translation=\"" + transform.localTranslationCoordinates.toX3D() + "\">\n" +
								"		<Transform DEF=\"" + name + "_Placeholder_Rotation\" rotation=\"" + transform.localRotationCoordinates.toX3D() + "\">\n" +
								"		<Transform scale=\"" + Utils.double2StringFormat(transform.localScaleFactor.x) + " " + Utils.double2StringFormat(transform.localScaleFactor.y) + " " + Utils.double2StringFormat(transform.localScaleFactor.z) + "\">\n" +
								"			<Shape>\n" +
								"				<Sphere/>" +
								"				<Appearance>\n" +
								"					<Material emissiveColor=\"1 1 0\" transparency=\"0.5\"/>\n" +
								"				</Appearance>\n" +
								"			</Shape>\n" +
								"		</Transform>\n" +
								"		</Transform>\n" +
								"		</Transform>\n" : "");
				break;
			// TODO: Find a way to implement the light ambient chromatic component, maybe with a shader?
			case SPOT:
				X3DString +=	"		<SpotLight DEF=\"" + name + "\" color=\"" + diffuse.toX3D() + "\" location=\"" + transform.localTranslationCoordinates.toX3D() + "\" direction=\"" + direction.toVec3().toX3D() + "\" intensity=\"" + Utils.double2StringFormat(intensity) + "\" ambientIntensity=\"0.1\" attenuation=\"" + Utils.double2StringFormat(constant) + " " + Utils.double2StringFormat(linear) + " " + Utils.double2StringFormat(quadratic) +  "\" beamWidth=\"" + Utils.double2StringFormat(angleRad) + "\" cutOffAngle=\"" + Utils.double2StringFormat(Math.PI / 2 - angleRad) + "\"/>\n" + (Light.drawLightSources ?
								"		<Transform DEF=\"" + name + "_Placeholder_Translation\" translation=\"" + transform.localTranslationCoordinates.toX3D() + "\">\n" +
								"		<Transform DEF=\"" + name + "_Placeholder_Rotation\" rotation=\"" + transform.localRotationCoordinates.toX3D() + "\">\n" +
								"		<Transform scale=\"" + Utils.double2StringFormat(transform.localScaleFactor.x) + " " + Utils.double2StringFormat(transform.localScaleFactor.y) + " " + Utils.double2StringFormat(transform.localScaleFactor.z) + "\">\n" +
								"		<Transform rotation=\"1 0 0 1.57\">\n" +
								"			<Shape>\n" +
								"				<Cone/>" +
								"				<Appearance>\n" +
								"					<Material emissiveColor=\"1 1 0\" transparency=\"0.5\"/>\n" +
								"				</Appearance>\n" +
								"			</Shape>\n" +
								"		</Transform>\n" +
								"		</Transform>\n" +
								"		</Transform>\n" +
								"		</Transform>\n" : "");
				break;
		}
		return X3DString;
	}
}
