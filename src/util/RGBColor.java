package util;

public class RGBColor implements X3DExportable {
    /** The red component. */
    public double r;
    /** The green component. */
    public double g;
    /** The blue component. */
    public double b;

    /* The colorIndex variable is used to cycle through the 3Doors default colors */
    private static int colorIndex = -1;
    /* The 3Doors default colors cyclic array */
    private static RGBColor[] defaultColorArray = new RGBColor[] {
        new RGBColor(49, 143, 0),
            new RGBColor(122, 149, 206),
            new RGBColor(209, 228, 89),
            new RGBColor(219, 44, 190),
            new RGBColor(77, 130, 181),
            new RGBColor(92, 23, 3),
            new RGBColor(252, 42, 37),
            new RGBColor(1, 30, 113),
            new RGBColor(135, 96, 2),
            new RGBColor(154, 153, 145),
            new RGBColor(114, 169, 42),
            new RGBColor(154, 14, 89),
            new RGBColor(132, 204, 199),
            new RGBColor(185, 223, 76),
            new RGBColor(137, 236, 243),
            new RGBColor(60, 117, 36),
            new RGBColor(198, 53, 219),
            new RGBColor(254, 254, 215),
            new RGBColor(67, 100, 155),
            new RGBColor(6, 214, 75),
            new RGBColor(172, 23, 95),
            new RGBColor(234, 2, 14),
            new RGBColor(149, 69, 70),
            new RGBColor(185, 213, 176),
            new RGBColor(189, 52, 123),
            new RGBColor(242, 116, 119),
            new RGBColor(152, 27, 189)
    };

    /** Creates a color with the following components:<p>
     * r = 0<p>
     * g = 0<p>
     * b = 0<p>
     */
    public RGBColor() {
        this.r = this.g = this.b = 0.0;
    }

    /** Creates a color with the given components.
     * 
     * @param r The red component
     * @param g The green component
     * @param b The blue component
     */
    public RGBColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /** @return The next 3Doors default color */
    public static RGBColor getDefaultColor() {
        return defaultColorArray[colorIndex = (colorIndex + 1) % defaultColorArray.length];

    }

    /** @return This color X3D string */
    public String toX3D() {
        String X3DString =	Utils.double2StringFormat(Utils.normalizeDouble(r, 255.0)) + " " +
            Utils.double2StringFormat(Utils.normalizeDouble(g, 255.0)) + " " +
            Utils.double2StringFormat(Utils.normalizeDouble(b, 255.0));
        return X3DString;
    }
}
