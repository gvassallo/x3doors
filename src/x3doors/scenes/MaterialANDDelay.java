package x3doors.scenes;

import math.Vec3;
import math.Vec4;

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

public class MaterialANDDelay {

    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {

        SceneProperties.setTitle("MaterialANDDelay");
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
        /* Delay of 3 sec, negated : false, repeatable : false   */
        Delay delay3 = new Delay("Delay3", false, false, 2.000);
        /* Behaviour : after two seconds start the material animation  */
        Behaviour behaviour3 = new Behaviour("Behaviour3", distance1, material1);
        X3DomExporter.export(); 

    }
}
