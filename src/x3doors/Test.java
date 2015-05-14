package x3doors;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import math.Vec3;
import math.Vec4;
import x3doors.exporters.X3DExporter;
import x3doors.exporters.X3DomExporter;
import x3doors.nodes.Light;
import x3doors.nodes.Mesh;

public class Test {
    public static void main(String[] args) throws Exception {
        Mesh mesh1 = new Mesh(  "Box1",
                Mesh.Type.BOX,
                new Vec3(-1, 1, 1),
                new Vec4(1, 0, 0, 0),
                // new Vec3(0.02, 0.08, 1),
                new Vec3(1, 1, 1),
                true,
                false
                );
		Light light1 = new Light(	"Spot1",
				Light.Type.DIRECTIONAL,
				new Vec3(-1, 35, 0),
				new Vec4(1, 0, 0, -1.57)/* ,
				mesh1 */
);
        Document doc = DocInstance.getInstance(); 
     
        X3DomExporter.export(); 
//        X3DExporter.setEmbeddedScripts(false);
//		X3DExporter.setDrawLightSources(true);
//		X3DExporter.export();
        
    }	  
    public static final void prettyPrint(Document xml) throws Exception {
    	
    	        Transformer tf = TransformerFactory.newInstance().newTransformer();
    	        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    	        tf.setOutputProperty(OutputKeys.INDENT, "yes");
                Writer out = new StringWriter();
    	        tf.transform(new DOMSource(xml), new StreamResult(out));
    	        System.out.println(out.toString());
    	    }

}
