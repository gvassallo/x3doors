package x3doors;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import math.Vec3;
import math.Vec4;

import org.w3c.dom.Document;

import util.RGBColor;

import x3doors.actions.Behaviour;
import x3doors.actions.controllers.Controller;
import x3doors.actions.controllers.Pose;
import x3doors.actions.sensors.Click;
import x3doors.actions.sensors.Delay;
import x3doors.actions.sensors.OR;
import x3doors.exporters.X3DExporter;
import x3doors.exporters.X3DomExporter;
import x3doors.nodes.Camera;
import x3doors.nodes.Light;
import x3doors.nodes.Mesh;
import x3doors.properties.SceneProperties;

public class Test {
    public static void main(String[] args) throws Exception {
    	Camera camera1 = new Camera("User_Camera",
				"Interactive user camera", 
				new Vec3(0, 0, 10),
				new Vec4(0, 1, 0, 0),
				0.1,
				10000.0,
				44.0,
				true,
				5.0,
				1.0,
				Camera.exploreModeType.MANIPULATOR);

SceneProperties.setActiveCamera(camera1);
SceneProperties.setBackgroundColor(300,300,300);     	
    	
    	
    	Mesh mesh1 = new Mesh(  "Box1",
                Mesh.Type.BOX,
                new Vec3(-1, 1, 1),
                new Vec4(1, 0, 0, 0),
                new Vec3(1, 1, 1),
                true,
                false
                );
    mesh1.setEmissive(new RGBColor(0,100,100));       
		Light light1 = new Light(	"Spot1",
				Light.Type.DIRECTIONAL,
				new Vec3(-1, 35, 0),
				new Vec4(1, 0, 0, -1.57) ,
				mesh1 	
);
		
		Pose pose1 = new Pose("PosCtrl1", Controller.Repeat.CLAMP, mesh1);
		pose1.addKeyFrame(0.0, mesh1.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0));
		pose1.addKeyFrame(1.0, mesh1.transform.worldTranslationCoordinates.sum(0, 10, 0), new Vec4(1, 0, 0, -0.785));
		pose1.addKeyFrame(2.0, mesh1.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0));
        Document doc = DocInstance.getInstance(); 
      
        
      
             
		Delay delay1 = new Delay("Delay1", false, false, 5.000);
        Click click = new Click("Click", false, true, mesh1); 
        OR or = new OR("or", true, true, delay1, click);
//		Behaviour behaviour1 = new Behaviour("Behaviour1", click, pose1);
//		Behaviour behaviour2 = new Behaviour("Behaviour2", delay1, pose1);  
		Behaviour behaviour3 = new Behaviour("Behaviour3", or, pose1);
		SceneProperties.setDebugMode(true); 
        X3DomExporter.export(); 
//        X3DExporter.setEmbeddedScripts(false);
//		X3DExporter.setDrawLightSources(true);
 		X3DExporter.export();
        
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
