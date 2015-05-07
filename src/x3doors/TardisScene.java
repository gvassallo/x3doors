package x3doors;

import math.Vec3;
import math.Vec4;
import x3doors.nodes.*;
class TardisScene {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		SceneProperties.setTitle("DemoTesi");
		SceneProperties.setDescription("Animation of the Doctor Who Tardis");
		SceneProperties.setCreator("Luca Martini");
		SceneProperties.setSkybox(	"C:/Users/Luca/Downloads/Skybox360_001/Skybox360 001 Front +z.png",
									"C:/Users/Luca/Downloads/Skybox360_001/Skybox360 001 Back -z.png",
									"C:/Users/Luca/Downloads/Skybox360_001/Skybox360 001 Left -x.png",
									"C:/Users/Luca/Downloads/Skybox360_001/Skybox360 001 Right +x.png",
									"C:/Users/Luca/Downloads/Skybox360_001/Skybox360 001 Up +y.png",
									"C:/Users/Luca/Downloads/Skybox360_001/Skybox360 001 Down -y.png");
		// Define user camera
		Camera userCamera = new Camera(	"User_Camera",
										"Interactive user camera", 
										new Vec3(50, 70, 50),
										new Vec4(-0.6784, 0.6786, 0.2815, 1.0955),
										0.1,
										10000.0,
										44.0,
										true,
										50.0,
										1.0,
										Camera.exploreModeType.HUMAN);
				
		// Define geometries
		Mesh tardis = new Mesh(	"Tardis",
								Mesh.Type.IMPORTED,
								new Vec3(0, 0, 0),
								new Vec4(1, 0, 0, 0),
								new Vec3(1, 1, 1),
								true,
								false
		);
		
		Light directional = new Light(	"DirectionalLight",
										Light.Type.DIRECTIONAL,
										new Vec3(0, 0, 10),
										new Vec4(1, 0, 0, 0)
		);
		
		SceneProperties.setActiveCamera(userCamera);
		
		// Define controllers
		Pose tardisPoseController = new Pose("TardisPoseController", Controller.Repeat.WRAP, tardis);
		Pose userCameraPoseController = new Pose("User_CameraPoseController", Controller.Repeat.CYCLE, userCamera);
		Visibility tardisVisibilityController = new Visibility("TardisVisibilityController", Controller.Repeat.WRAP, tardis);
		
		// REMEMBER: key frames must be inserted according to their time order
		double radPass = Math.PI * 0.2;
		double radius = 0.0;
		double duration = 10.0;
		int i = 0;
		tardisPoseController.addKeyFrame(i * (duration / 100), new Vec3(0, 0, 0), new Vec4(0, 1, 0, 0));
		for (i = 0; i < 100; i++) {
			if (i < 50) {
				radius += 1;
			}
			else {
				radius -= 1;
			}
			tardisPoseController.addKeyFrame(i * (duration / 100), new Vec3(radius * Math.cos(i * radPass), 0, radius * Math.sin(i * radPass)), new Vec4(0, 1, 0, -i * radPass));
			// System.out.println(i * (duration / 100));
			// System.out.println(radius);
			// System.out.println("t: " + i * (duration / 100) + "\npos: " + radius * Math.sin(i * radPass) + ", " + radius * Math.cos(i * radPass) + ", 0\nrot: " + ((-180.0 + -i * radPass * 180.0 / Math.PI) % 360) + "\n");
		}
		tardisPoseController.addKeyFrame(i * (duration / 100), new Vec3(radius * Math.cos(i * radPass), 0, radius * Math.sin(i * radPass)), new Vec4(0, 1, 0, 0));
		// System.out.println("t: " + i * (duration / 100) + "\npos: " + radius * Math.sin(i * radPass) + ", " + radius * Math.cos(i * radPass) + ", 0\nrot: " + ((-180.0 + -i * radPass * 180.0 / Math.PI) % 360) + "\n");
		tardisPoseController.addKeyFrame(duration + 4.0, new Vec3(0, 0, 0), new Vec4(0, 1, 0, 0));
		// REMEMBER: key frames must be inserted according to their time order
		tardisVisibilityController.addKeyFrame(0.0, true);
		tardisVisibilityController.addKeyFrame(11.0, false);
		tardisVisibilityController.addKeyFrame(12.0, true);
		tardisVisibilityController.addKeyFrame(13.0, false);
		tardisVisibilityController.addKeyFrame(14.0, true);
		// tardisVisibilityController.addKeyFrame(duration + 4.0, true);

		// Define data
		AudioPlayer doctorWhoTheme = new AudioPlayer("DoctorWhoTheme", Data.UserInterface._2D_MODELESS, "C:/Users/Luca/Downloads/doctorWhoTheme.wav", true);
		AudioPlayer tardisLandingEffect = new AudioPlayer("TardisLandingEffect", Data.UserInterface.NONE, "C:/Users/Luca/Desktop/tardis2.wav", true);
				
		// Define sensors
		Delay audioPlayerDelay = new Delay("AudioPlayerDelay", false, false, 0.0);
		Delay tardisLiftOffDelay = new Delay("TardisLiftOffDelay", false, false, 0.0);
		Delay tardisLandingDelay = new Delay("TardisLandingDelay", false, false, 0.0);
		
		// Click tardisLiftOffClick = new Click("TardisLiftOffClick", false, true, tardis);
		// Click tardisClick = new Click(	"TardisClick", false, true, tardis);

		// Distance tardisLiftOffDistance = new Distance("TardisLandingDistance", false, true, 10, tardis, userCamera);
		
		// AND tardisLiftOffConditions = new AND("TardisLiftOffConditions", false, true, tardisLiftOffDistance, tardisLiftOffClick);
		// Define behaviours
		// Behaviour tardisLiftOff = new Behaviour("TardisLiftOff", tardisLiftOffConditions, tardisPoseController);
		// Behaviour tardisLanding1 = new Behaviour("TardisLanding1", tardisLiftOffDelay, tardisPoseController);
		Behaviour behaviour1 = new Behaviour("Behaviour1", tardisLiftOffDelay, tardisPoseController);
		Behaviour behaviour2 = new Behaviour("Behaviour2", audioPlayerDelay, doctorWhoTheme);
		Behaviour behaviour3 = new Behaviour("Behaviour3", audioPlayerDelay, tardisLandingEffect);
		Behaviour behaviour4 = new Behaviour("Behaviour4", tardisLandingDelay, tardisVisibilityController);

		X3DExporter.setEmbeddedScripts(false);
		X3DExporter.setDrawLightSources(false);
		X3DExporter.export();
	}
}
