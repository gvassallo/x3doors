package x3doors.nodes; 

import util.RGBColor;
import math.Vec3;
import math.Vec4;

public class Mesh extends SceneObject {
	/* The mesh type */
	private Type type;
	/* The mesh material */
	private Material material;
	/* If true back faces are rendered too */
	private boolean backFaceCulling;
	
	/** Defines the mesh types, can assume the following values<p>
	 * BOX: a box of size 1<p>
	 * RECTANGLE: a squared plane of size 50 */
	public enum Type {
		BOX,
		IMPORTED,
		// TODO: Implement the imported Wavefront .obj files in their own scene object class
		RECTANGLE,
		SPHERE
	}
	
	/** Creates a mesh using root as parent and the given parameters.
	 * 
	 * @param name The name
	 * @param type The type
	 * @param worldTranslationCoordinates The initial world translation coordinates
	 * @param worldRotationCoordinates The initial world rotation coordinates
	 * @param worldScaleFactor The initial world scale factor
	 * @param visible The initial rendering state, if true then the scene object and its children are rendered
	 * @param backFaceCulling The initial back face culling state
	 * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
	 */
	public Mesh(String name, Type type, Vec3 worldTranslationCoordinates, Vec4 worldRotationCoordinates, Vec3 worldScaleFactor, boolean visible, boolean backFaceCulling) throws Exception {
		this(name, type, worldTranslationCoordinates, worldRotationCoordinates, worldScaleFactor, visible, backFaceCulling, Root.getInstance());
	}
	
	/** Creates a mesh using the given parameters.
	 * 
	 * @param name The name
	 * @param type The type
	 * @param worldTranslationCoordinates The initial world translation coordinates
	 * @param worldRotationCoordinates The initial world rotation coordinates
	 * @param worldScaleFactor The initial world scale factor
	 * @param visible The initial rendering state, if true then the scene object and its children are rendered
	 * @param backFaceCulling The initial back face culling state
	 * @param parent The parent
	 * @throws Exception When trying to map a scene object with an already existing handle (should never happen)
	 */
	public Mesh(String name, Type type, Vec3 worldTranslationCoordinates, Vec4 worldRotationCoordinates, Vec3 worldScaleFactor, boolean visible, boolean backFaceCulling, SceneObject parent) throws Exception {
		super(worldTranslationCoordinates, worldRotationCoordinates, worldScaleFactor, visible, parent);
		switch (type) {
			case BOX:
				this.name = (name == null || name.equals("")) ? ("Box_" + handle) : name;
				break;
			case IMPORTED:
				this.name = (name == null || name.equals("")) ? ("Imported_" + handle) : name;
			case RECTANGLE:
				this.name = (name == null || name.equals("")) ? ("Rectangle_" + handle) : name;
				break;
			case SPHERE:
				this.name = (name == null || name.equals("")) ? ("Sphere_" + handle) : name;
				break;
		}
		this.type = type;
		// TODO: Remove the following condition when implementing the Wavefront .obj parser for importing .obj files
		if (this.type != Mesh.Type.IMPORTED) {
			this.material = new Material();
		}
		this.backFaceCulling = backFaceCulling;
	}
	/** Defines a new alpha for this mesh material.
	 * 
	 * @param alpha The new alpha
	 */
	public void setAlpha(double alpha) {
		material.alpha = alpha;
	}
	
	/** Defines a new ambient chromatic component for this mesh material.
	 * 
	 * @param ambient The new ambient chromatic component
	 */
	public void setAmbient(RGBColor ambient) {
		material.ambient = ambient;
	}
	
	/** Defines a new diffuse chromatic component for this mesh material.
	 * 
	 * @param diffuse The new diffuse chromatic component
	 */
	public void setDiffuse(RGBColor diffuse) {
		material.diffuse = diffuse;
	}
	
	/** Defines a new emissive chromatic component for this mesh material.
	 * 
	 * @param emissive The new emissive chromatic component
	 */
	public void setEmissive(RGBColor emissive) {
		material.emissive = emissive;
	}
	
	/** Defines a new shininess for this mesh material.
	 * 
	 * @param shininess The new shininess
	 */
	public void setShininess(double shininess) {
		material.shininess = shininess;
	}
	
	/** Defines a new specular chromatic component for this mesh material.
	 * 
	 * @param specular
	 */
	public void setSpecular(RGBColor specular) {
		material.specular = specular;
	}
	
	/** Print the properties to screen. */
	public void print() {
		super.print();
		material.print();
		System.out.print(	"BackfaceCulling:\t" + backFaceCulling + "\n");
	}
	
	/** @return This material X3D string */
	public String toX3D() {
		if (type == Mesh.Type.IMPORTED) {
			return "";
		}
		String X3DString =		// "		<Switch DEF=\"" + name + "_Switch\" whichChoice=\"" + (visible ? "0" : "-1") + "\">\n" +
								"			<Shape DEF=\"" + name + "_Shape\">\n";
		X3DString +=			"				<Appearance>\n" +
								"					<Material DEF=\"" + name + "_Material\"" + material.toX3D() +
								"				</Appearance>\n";
		String coordIndex = "";
		String geometryX3DString = "";
		switch (type) {
			case BOX:
				coordIndex += "0 2 1 -1 0 3 2 -1 0 1 5 -1 0 5 4 -1 0 4 7 -1 0 7 3 -1 6 4 5 -1 6 7 4 -1 6 5 1 -1 6 1 2 -1 6 2 3 -1 6 3 7";
				geometryX3DString +=	"				<Coordinate point=\"-0.5 -0.5 -0.5 0.5 -0.5 -0.5 0.5 0.5 -0.5 -0.5 0.5 -0.5 -0.5 -0.5 0.5 0.5 -0.5 0.5 0.5 0.5 0.5 -0.5 0.5 0.5\"/>\n" +
										"				<Normal vector=\"-0.57735 -0.57735 -0.57735 0.816497 -0.408248 -0.408248 0.408248 0.408248 -0.816497 -0.408248 0.816497 -0.408248 -0.408248 -0.408248 0.816497 0.408248 -0.816497 0.408248 0.57735 0.57735 0.57735 -0.816497 0.408248 0.408248\"/>\n" +
										"				<TextureCoordinate point=\"0.25 0.75 0.75 0.75 0.75 0.25 0.25 0.25 0 1 1 1 1 0 0 0\"/>\n";
				break;
			case IMPORTED:
				// TODO: Write a Wavefront .obj parser to import .obj files.
				// At the moment you must:
				// - create a new scene in 3Doors
				// - import the requested .obj file
				// - export it in X3D
				// - copy and paste the obj X3D code under the corresponding transform nodes
				// See http://en.wikipedia.org/wiki/Wavefront_.obj_file
				break;
			case RECTANGLE:
				coordIndex += "0 1 3 -1 0 3 2";
				geometryX3DString +=	"				<Coordinate point=\"-50 -50 -0 50 -50 0 -50 50 0 50 50 0\"/>\n" +
										"				<Normal vector=\"0 0 1 0 0 1 0 0 1 0 0 1\"/>\n" +
										"				<TextureCoordinate point=\"0 0 1 0 0 1 1 1\"/>\n";
				break;
			case SPHERE:
				break;
		}
		X3DString +=			"				<IndexedFaceSet DEF=\"IndexedFaceSet_" + name + "\" colorPerVertex=\"false\" solid=\"" + backFaceCulling + "\" coordIndex=\"" + coordIndex + "\">\n" +
													geometryX3DString +
								"				</IndexedFaceSet>\n" +
								"			</Shape>\n";
								// "		</Switch>\n";
		return X3DString;
	}
}
