package util; 

public class Metadata implements X3DExportable {
    /* The metadata field content */
    private String content;
    /* The metadata field name */
    private String name;

    /** Creates a metadata with the given properties.
     * 
     * @param content The content of this metadata field
     * @param name The name of this metadata field
     */
    public Metadata(String content, String name) {
        this.content = content;
        this.name = name;
    }

    /** @return This metadata X3D string */
    public String toX3D() {
        String X3DString = "<meta content=\"" + content + "\" name=\"" + name + "\"/>\n";
        return X3DString;
    }
}
