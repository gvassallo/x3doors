package x3doors.properties ; 

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.*; 
import x3doors.DocInstance;
public class Background implements X3DExportable , X3DomExportable{
		/* The color */
		RGBColor color;
		
		/* Creates a background with the given properties.
		 * 
		 * @param r The red component
		 * @param g The green component
		 * @param b The blue component
		 */
		Background(int r, int g, int b) {
			this.color = new RGBColor(r, g, b);
		}
		
		/** @return The background X3D string */
		public String toX3D() {
			String colorX3DString = color.toX3D();
			return "		<Background groundColor=\"" + colorX3DString + "\" skyColor=\"" + colorX3DString + "\"/>\n";
		}
        public MyNodeList toX3Dom(){
             MyNodeList wrapper = new MyNodeList(); 
             Document doc = DocInstance.getInstance(); 
             Element background = doc.createElement("Background"); 
			 String colorX3DString = color.toX3D();
             background.setAttribute("groundColor", colorX3DString); 
             background.setAttribute("skyColor", colorX3DString); 
             wrapper.appendChild(background); 
             return wrapper; 
        }

	}
	
