package x3doors.properties ; 

import util.RGBColor;
import util.X3DExportable;

class Background implements X3DExportable {
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
	}
	
