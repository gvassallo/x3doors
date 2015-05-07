package x3doors.nodes; 

import util.Printable;
import util.RGBColor;
import util.Utils;
import util.X3DExportable;

public class Material implements Printable, X3DExportable {
    /* The transparency of this material. When alpha equals 1.0 the scene object is opaque,
     * when 0.0 it is fully transparent (transparency = 1 - alpha) */
    // 3Doors range: [0.0, 1.0]
    double alpha;
    /* The material chromatic components specified in the [0, 255] range */
    RGBColor ambient;
    RGBColor diffuse;
    RGBColor emissive;
    RGBColor specular;
    /* The shininess of this material */
    // 3Doors range: [0.0, 1.0]
    double shininess;

    /** Creates a material using the 3Doors default values.<p>
     * transparency: 0<p>
     * ambient color: 51 51 51<p>
     * diffuse color: one of the default ones, see {@link RGBColor} static method defaultColorArray() for more details<p>
     * emissive color: 0 0 0<p>
     * specular color: 0 0 0<p>
     * shininess: 1*/
    public Material() {
        this(1.0, new RGBColor(51, 51, 51), RGBColor.getDefaultColor(), new RGBColor(), 1.0, new RGBColor());
    }

    /** Creates a material using the given values.
     * 
     * @param alpha The initial transparency (transparency = 1 - alpha)
     * @param ambient The initial ambient color
     * @param diffuse The initial diffuse color
     * @param emissive The initial emissive color
     * @param shininess The initial shininess
     * @param specular The initial specular color
     */
    public Material(double alpha, RGBColor ambient, RGBColor diffuse, RGBColor emissive, double shininess, RGBColor specular) {
        this.alpha = alpha;
        this.ambient = ambient;
        this.diffuse = diffuse == null ? RGBColor.getDefaultColor() : diffuse;
        this.emissive = emissive;
        this.shininess = shininess;
        this.specular = specular;
    }

    /** Print the properties to screen. */
    public void print() {
        System.out.println(	"Alpha:\t\t\t" + alpha + "\n" +
                "Ambient:\t\t" + ambient.r + "\t" + ambient.g + "\t" + ambient.b + "\n" +
                "Diffuse:\t\t" + diffuse.r + "\t" + diffuse.g + "\t" + diffuse.b + "\n" +
                "Emissive:\t\t" + emissive.r + "\t" + emissive.g + "\t" + emissive.b + "\n" +
                "Shininess:\t\t" + shininess + "\n" +
                "Specular:\t\t" + specular.r + "\t" + specular.g + "\t" + specular.b
                );
    }

    /** @return The material X3D string */
    public String toX3D() {
        // TODO: Add a shader to capture the material ambient color in the X3D exporting process. At the moment it is
        // exported using the default value 0.2
        double ambientIntensity = ambient.r == ambient.g && ambient.g == ambient.b ? Utils.normalizeDouble(ambient.r, 255.0) : 0.2; // LOSSY solution
        String X3DString = " transparency=\"" + Utils.double2StringFormat(1 - alpha) + "\" ambientIntensity=\"" + Utils.double2StringFormat(ambientIntensity) + "\" shininess=\"" + Utils.double2StringFormat(shininess) + "\" diffuseColor=\"" + diffuse.toX3D() + "\" specularColor=\"" + specular.toX3D() + "\" emissiveColor=\"" + emissive.toX3D() + "\"/>\n";
        return X3DString;
    }

}

