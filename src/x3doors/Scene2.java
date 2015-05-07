package x3doors;

import math.Vec3;
import math.Vec4;
import x3doors.nodes.*;
public class Scene2 {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		SceneProperties.setTitle("PROVA3");
		SceneProperties.setDescription("Prova3");
		SceneProperties.setCreator("Luca Martini");
		SceneProperties.setBackgroundColor(0, 0, 0);
		// Define user camera
		Camera camera1 = new Camera("User_Camera",
									"Interactive user camera", 
									new Vec3(0, 0, 30),
									new Vec4(0, 1, 0, 0),
									0.1,
									10000.0,
									44.0,
									true,
									50.0,
									1.0,
									Camera.exploreModeType.HUMAN);
		
		SceneProperties.setActiveCamera(camera1);
		
		// Define geometries
		Mesh mesh1 = new Mesh(	"Box1",
								Mesh.Type.BOX,
								new Vec3(-1, 1, 1),
								new Vec4(1, 0, 0, 0),
								// new Vec3(0.02, 0.08, 1),
								new Vec3(6, 6, 6),
								true,
								false
		);

		/* Mesh mesh2 = new Mesh(	"Box2",
								Mesh.Type.BOX,
								new Vec3(3, 0, 0),
								new Vec4(1, 0, 0, 0),
								new Vec3(2, 2, 2),
								// new Vec3(1, 2, 4),
								true,
								false,
								mesh1
		); */
		
		/* Mesh mesh3 = new Mesh(	"Box3",
								Mesh.Type.BOX,
								new Vec3(1, 1, 1),
								new Vec4(0, 0, 1, 0),
								new Vec3(0.5, 2.9, 1.0),
								true,
								false,
								mesh2
		);
		
		Mesh mesh4 = new Mesh(	"Box4",
								Mesh.Type.RECTANGLE,
								new Vec3(-1, -1, -1),
								new Vec4(0, 0, 1, 0),
								new Vec3(0.06, 0.04, 1.0),
								true,
								false,
								mesh3
		); */
		
		Light light1 = new Light(	"Spot1",
									Light.Type.DIRECTIONAL,
									new Vec3(-1, 35, 0),
									new Vec4(1, 0, 0, -1.57)/* ,
									mesh1 */
		);
		
		// mesh1.print();
		// mesh2.print();
		// mesh3.print();
		// mesh4.print();
				
		// Define actions
		// Pose pose1 = new Pose("PosCtrl1", Controller.Repeat.CLAMP, mesh1);
		// Pose pose2 = new Pose("PosCtrl2", Controller.Repeat.CLAMP, light1);
		// pose1.print();
		// pose2.print();
		// Material material1 = new Material("MatCtrl1", Controller.Repeat.CYCLE, mesh1);
		// material1.print()
		Visibility visibility1 = new Visibility("VisCtrl1", Controller.Repeat.CYCLE_WITH_DELAY, mesh1);
		// visibility1.print();
		
		// REMEMBER: key frames must be inserted according to their time order
		// pose1.addKeyFrame(0.0, mesh1.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0));
		// pose1.addKeyFrame(1.0, mesh1.transform.worldTranslationCoordinates.sum(0, 0, 10), new Vec4(1, 0, 0, -0.785));
		// pose1.addKeyFrame(2.0, mesh1.transform.worldTranslationCoordinates, new Vec4(1, 0, 0, 0));
		
		/* pose2.addKeyFrame(0.0, mesh2.transform.worldTranslationCoordinates.sum(0, 0, 10), new Vec4(1, 0, 0, 0));
		pose2.addKeyFrame(1.0, mesh2.transform.worldTranslationCoordinates.sum(0, 0, 35), new Vec4(1, 0, 0, 0.785));
		pose2.addKeyFrame(2.0, mesh2.transform.worldTranslationCoordinates.sum(0, 0, 10), new Vec4(1, 0, 0, 0)); */
		
		// REMEMBER: key frames must be inserted according to their time order
		// material1.addKeyFrame(1.12, 1.0, new RGBColor(), new RGBColor(49, 143, 0), new RGBColor(), 1.0, new RGBColor());
		// material1.addKeyFrame(2.16, 0.8, new RGBColor(), new RGBColor(255, 0, 0), new RGBColor(0, 0, 128), 0.5, new RGBColor());
		// material1.addKeyFrame(2.80, 0.3, new RGBColor(), new RGBColor(), new RGBColor(128, 128, 128), 0.8, new RGBColor(255, 255, 0));
		
		// REMEMBER: key frames must be inserted according to their time order
		visibility1.addKeyFrame(1.0, true);
		visibility1.addKeyFrame(2.0, false);
		visibility1.addKeyFrame(4.0, true);
		
		// AudioPlayer audioPlayer1 = new AudioPlayer("AudioEffect", Data.UserInterface._3D_MODELESS, "C:/Users/Luca/Downloads/paperSound.wav", false);
		
		// Define sensors
		// Delay delay1 = new Delay("Delay1", false, false, 2.000);
		// Delay delay2 = new Delay("Delay2", false, false, 2.000);
		// Delay delay3 = new Delay("Delay3", false, false, 2.000);
		// delay1.print();
		Click click1 = new Click("Click1", false, true, mesh1);
		// click1.print();
		// Click click2 = new Click("Click2", false, true, mesh2);
		// Distance distance1 = new Distance("Distance1", false, true, 10.0, mesh1, camera1);
		// AND and1 = new AND("", false, true, click2, distance1);
		// Define behaviours
		// Behaviour behaviour1 = new Behaviour("Behaviour1", delay1, material1);
		// behaviour1.print();
		Behaviour behaviour2 = new Behaviour("Behaviour2", click1, visibility1);
		// behaviour2.print();
		// Behaviour behaviour3 = new Behaviour("Behaviour3", distance1, material1);
		// behaviour3.print();
		// Behaviour behaviour4 = new Behaviour("Behaviour4", delay3, pose1);
		// behaviour4.print();
		// Behaviour behaviour5 = new Behaviour("Behaviour5", distance1, visibility1);
		// behaviour5.print();
		// Behaviour behaviour6 = new Behaviour("Behaviour6", and1, pose1);
		// Behaviour behaviour7 = new Behaviour("Behaviour7", click1, new AudioPlayer("AudioEffect", Data.UserInterface._3D_MODELESS, "C:/Users/Luca/Downloads/paperSound.wav", false));
		X3DExporter.setEmbeddedScripts(true);
		X3DExporter.setDrawLightSources(true);
		X3DExporter.export();
	}
}