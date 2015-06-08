package x3doors.nodes; 

import math.Vec3;
import math.Vec4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.MyNodeList;
import util.RGBColor;

import x3doors.DocInstance;

public class Mesh extends SceneObject {
	/* The mesh type */
	private Type type;
	/* The mesh material */
	private Material material;
	private MyNodeList wrapper ; 
    private Element elementToAppend; 
	public enum Type {
		BOX,
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
			case SPHERE:
				this.name = (name == null || name.equals("")) ? ("Sphere_" + handle) : name;
				break;
		}
		this.type = type;
		wrapper = new MyNodeList(); 
        elementToAppend = wrapper.get(0) ; 
        this.material = new Material();
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
    
    public void appendElement(Element e){ 
        if (elementToAppend == null) {
            elementToAppend = e ;  
        }
        else {
            elementToAppend.appendChild(e); 
            elementToAppend = e ; 
        }

    }

    public Element getElementToAppend(){
         return this.elementToAppend; 
    }
        	

    public MyNodeList toX3Dom()  {
       Document doc = DocInstance.getInstance();  
       /* define the transform  */
       String meshType = new String();  
       switch (this.type){
           case BOX : 
               meshType = "Box"; 
               break ; 
           case SPHERE : 
               meshType = "Sphere"; 
               break ; 
       }

       Element mesh = doc.createElement(meshType); 
       mesh.setAttribute("DEF", this.name); 
       Element appearance = doc.createElement("Appearance");  
       Element material = this.material.toX3Dom().get(0) ;
       material.setAttribute("DEF", this.name+"_Material"); 
       appearance.appendChild(material); 
       Element shape = doc.createElement("Shape"); 
       shape.appendChild(mesh); 
       shape.appendChild(appearance);
       MyNodeList wrapper = new MyNodeList(); 
       wrapper.appendChild(shape); 
       	   
       return wrapper; 
    }
    
}
