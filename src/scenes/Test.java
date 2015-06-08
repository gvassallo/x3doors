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
import x3doors.actions.controllers.Material;
import x3doors.actions.sensors.Delay;
import x3doors.actions.sensors.Distance;
import x3doors.exporters.X3DomExporter;
import x3doors.nodes.Camera;
import x3doors.nodes.Light;
import x3doors.nodes.Mesh;
import x3doors.properties.SceneProperties;

public class Test {
    public static void main(String[] args) throws Exception {
        /* Camera camera1 = new Camera("User_Camera", */
        /*         "Interactive user camera",  */
        /*         new Vec3(0, 0, 30), */
        /*         new Vec4(0, 1, 0, 0), */
        /*         0.1, */
        /*         10000.0, */
        /*         44.0, */
        /*         true, */
        /*         5.0, */
        /*         1.0, */
        /*         Camera.exploreModeType.HUMAN); */
        /*  */
        /* SceneProperties.setActiveCamera(camera1); */
        /* SceneProperties.setBackgroundColor(300,300,300);     	 */
        /*  */
        /* Mesh mesh1 = new Mesh(  "Box1", */
        /*         Mesh.Type.BOX, */
        /*         new Vec3(-5, 0, 0), */
        /*         new Vec4(1, 0, 0, 0), */
        /*         new Vec3(1, 1, 1), */
        /*         true, */
        /*         false */
        /*         ); */
        /* mesh1.setEmissive(new RGBColor(0,100,100));        */
        /* Light light1 = new Light(	"Spot1", */
        /*         Light.Type.DIRECTIONAL, */
        /*         new Vec3(-1, 35, 0), */
        /*         new Vec4(1, 0, 0, -1.57) , */
        /*         mesh1 	 */
        /*         ); */
        /* Mesh mesh2 = new Mesh(  "Sphere1", */
        /*         Mesh.Type.SPHERE, */
        /*         new Vec3(5, 0, 0), */
        /*         new Vec4(1, 0, 0, 0), */
        /*         new Vec3(1, 1, 1), */
        /*         true, */
        /*         false */
        /*         ); */
        /* mesh2.setEmissive(new RGBColor(256, 0, 0)); */
        /*  */
        /* Material material1 = new Material ("Material1", Repeat.CLAMP, mesh1); */
		/* #<{(| REMEMBER: key frames must be inserted according to their time order |)}># */
		/* material1.addKeyFrame(1.12, 1.0, new RGBColor(), new RGBColor(49, 143, 0), new RGBColor(), 1.0, new RGBColor()); */
		/* material1.addKeyFrame(2.16, 0.8, new RGBColor(), new RGBColor(255, 0, 0), new RGBColor(0, 0, 128), 0.5, new RGBColor()); */
		/* material1.addKeyFrame(2.80, 0.3, new RGBColor(), new RGBColor(), new RGBColor(128, 128, 128), 0.8, new RGBColor(255, 255, 0)); */
        /*  */
        /* Pose pose1 = new Pose("PosCtrl1", Controller.Repeat.CYCLE, mesh1); */
        /* pose1.addKeyFrame(0.0, mesh1.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0)); */
        /* pose1.addKeyFrame(1.0, mesh1.transform.worldTranslationCoordinates.sum(0, 10, 0), new Vec4(1, 0, 0, -0.785)); */
        /* pose1.addKeyFrame(2.0, mesh1.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0)); */
        /* Pose pose2 = new Pose("PosCtrl2", Controller.Repeat.CLAMP, mesh2); */
        /* pose2.addKeyFrame(0.0, mesh2.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0)); */
        /* pose2.addKeyFrame(1.0, mesh2.transform.worldTranslationCoordinates.sum(0, 10, 0), new Vec4(1, 0, 0, -0.785)); */
        /* pose2.addKeyFrame(2.0, mesh2.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0)); */
        /* Document doc = DocInstance.getInstance();  */
        /* Distance distance2 = new Distance("Distance2", false, true, 20.0, camera1, mesh2); */
        /* Behaviour behaviour2 = new Behaviour("Behaviour2", distance1, pose2);   */
        /* Behaviour behaviour3 = new Behaviour("Behaviour3", distance1, material1); */
        /* SceneProperties.setDebugMode(true);  */
		SceneProperties.setBackgroundColor(0, 0, 0);
		// Define user camera
		Camera camera1 = new Camera("User_Camera",
									"Interactive user camera", 
									new Vec3(0, 0, 30),
									new Vec4(0, 1, 0, 0),
									0.1,
									10000.0,
									10.0,
									true,
									10.0,
									1.0,
									Camera.exploreModeType.HUMAN);
		
		SceneProperties.setActiveCamera(camera1);
		
		// Define geometries
		Mesh mesh1 = new Mesh(	"Box1",
								Mesh.Type.BOX,
								new Vec3(1, 1, 1),
								new Vec4(1, 0, 0, 0),
								// new Vec3(0.02, 0.08, 1),
								new Vec3(1,1, 1),
								true,
								false
		);
		
		Light light1 = new Light(	"Spot1",
									Light.Type.DIRECTIONAL,
									new Vec3(0, 0, 0),
									new Vec4(1, 0, 0, -1.57),
									mesh1
		);
		Material material1 = new Material("MatCtrl1", Controller.Repeat.CYCLE, mesh1);
		material1.addKeyFrame(1.12, 1.0, new RGBColor(), new RGBColor(49, 143, 0), new RGBColor(), 1.0, new RGBColor());
		material1.addKeyFrame(2.16, 0.8, new RGBColor(), new RGBColor(255, 0, 0), new RGBColor(0, 0, 128), 0.5, new RGBColor());
		material1.addKeyFrame(2.80, 0.3, new RGBColor(), new RGBColor(), new RGBColor(128, 128, 128), 0.8, new RGBColor(255, 255, 0));
        Distance distance1 = new Distance("Distance1", false, true, 20.0, mesh1, camera1);
		Delay delay3 = new Delay("Delay3", false, false, 2.000);
		Behaviour behaviour3 = new Behaviour("Behaviour3", distance1, material1);
        X3DomExporter.export(); 
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
