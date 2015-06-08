package scenes; 

import math.Vec3;
import math.Vec4;

import x3doors.exporters.X3DomExporter;
import x3doors.nodes.Camera;
import x3doors.nodes.Mesh;
import x3doors.properties.SceneProperties;

public class Simple{

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

        SceneProperties.setBackgroundColor(300,300,300);     	

        Mesh mesh1 = new Mesh(  "Box1",
                Mesh.Type.BOX,
                new Vec3(-5, 0, 0),
                new Vec4(1, 0, 0, 0),
                new Vec3(1, 1, 1),
                true,
                false
                );

        X3DomExporter.export(); 
    }
    
}
