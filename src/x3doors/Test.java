package x3doors;
import math.Vec3;
import math.Vec4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import x3doors.nodes.Mesh;

public class Test {
    public static void main(String[] args)  throws Exception{

        Mesh mesh1 = new Mesh(	"Box1",
                Mesh.Type.RECTANGLE,
                new Vec3(-1, 1, 1),
                new Vec4(1, 0, 0, 0),
                new Vec3(6, 6, 6),
                true,
                false
                );
        Document document = DocInstance.getInstance(); 
        DOMImplementationLS domImplLS = (DOMImplementationLS) document
        	    .getImplementation();
        	LSSerializer serializer = domImplLS.createLSSerializer();
        Element elem = mesh1.toX3Dom(); 
       System.out.println( serializer.writeToString(elem));
    }
}
