package x3doors.scenes; 

import math.Vec3;
import math.Vec4;

import org.w3c.dom.Document;

import util.RGBColor;

import x3doors.DocInstance;
import x3doors.actions.Behaviour;
import x3doors.actions.controllers.Controller;
import x3doors.actions.controllers.Pose;
import x3doors.actions.sensors.Distance;
import x3doors.exporters.X3DomExporter;
import x3doors.nodes.Camera;
import x3doors.nodes.Light;
import x3doors.nodes.Mesh;
import x3doors.properties.SceneProperties;

public class DistanceANDPose {

    public static void main() throws Exception {
        SceneProperties.setTitle("DistanceANDPose");
                
        Camera camera1 = new Camera("User_Camera",
                "Interactive user camera", 
                new Vec3(0, 0, 30),
                new Vec4(0, 1, 0, 0),
                0.1,
                10000.0,
                44.0,
                true,
                2.0,
                1.0,
                Camera.exploreModeType.MANIPULATOR);

        SceneProperties.setActiveCamera(camera1);
        SceneProperties.setBackgroundColor(300,300,300);     	

        Mesh mesh1 = new Mesh(  "Box1",
                Mesh.Type.BOX,
                new Vec3(-5, 0, 0),
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
        Mesh mesh2 = new Mesh(  "Sphere1",
                Mesh.Type.SPHERE,
                new Vec3(5, 0, 0),
                new Vec4(1, 0, 0, 0),
                new Vec3(1, 1, 1),
                true,
                false
                );
        mesh2.setEmissive(new RGBColor(256, 0, 0));

        Pose pose1 = new Pose("PosCtrl1", Controller.Repeat.CYCLE, mesh1);
        pose1.addKeyFrame(0.0, mesh1.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0));
        pose1.addKeyFrame(1.0, mesh1.transform.worldTranslationCoordinates.sum(0, 10, 0), new Vec4(1, 0, 0, -0.785));
        pose1.addKeyFrame(2.0, mesh1.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0));
        Pose pose2 = new Pose("PosCtrl2", Controller.Repeat.CLAMP, mesh2);
        pose2.addKeyFrame(0.0, mesh2.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0));
        pose2.addKeyFrame(1.0, mesh2.transform.worldTranslationCoordinates.sum(0, 10, 0), new Vec4(1, 0, 0, -0.785));
        pose2.addKeyFrame(2.0, mesh2.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0));
        Document doc = DocInstance.getInstance(); 
        Distance distance1 = new Distance("Distance2", false, false, 20.0, camera1, mesh1);
        Distance distance2 = new Distance("Distance2", false, true, 10.0, mesh1, mesh2);
        Behaviour behaviour2 = new Behaviour("Behaviour2", distance1, pose1);  
        Behaviour behaviour3 = new Behaviour("Behaviour3", distance2, pose2);
        X3DomExporter.export(); 
    }
    
}
