package x3doors;

import java.util.ArrayList;
import x3doors.nodes.*;


import math.Matrix4;
import math.Vec3;
import math.Vec4;
import util.RGBColor;
import x3doors.*;
import x3doors.properties.*;

@SuppressWarnings("unused")
public class ProvaAudioPlayer {
	public static void main (String[] args) throws Exception {
		SceneProperties.setTitle("ProvaAudioPlayer2");
		SceneProperties.setDescription("Prova di Audio Player");
		SceneProperties.setCreator("Luca Martini");
				
		Mesh mesh1 = new Mesh("Box",
				Mesh.Type.BOX,
				new Vec3(0, 0, 0),
				new Vec4(1, 0, 0, 0),
				new Vec3(100, 200, 50),
				false,
				false
		);
		
		Camera camera1 = new Camera("UserCamera",
									"Interactive user camera", 
									new Vec3(0, 0, 30),
									new Vec4(0, 1, 0, 0),
									0.1,
									10000.0,
									44.0,
									true,
									50.0,
									1.0,
									Camera.exploreModeType.HUMAN,
									mesh1);
		
		Camera camera2 = new Camera(mesh1);
		
		camera2.setName("DefaultCamera");
		camera2.setVisible(false);
		
		SceneProperties.setActiveCamera(camera1);
		
		ArrayList<Behaviour> behaviourList = new ArrayList<Behaviour>();
		double steps = 10;
		double duration = 10;
		for (double i = 0; i < steps; i++) {
			behaviourList.add(new Behaviour("DelayedAudioEffect" + (int) (i * steps), new Delay("Delay" + (int) (i * steps), false, false, duration / steps * i), new AudioPlayer("AudioEffect" + (int) (i * steps), Data.UserInterface._2D_MODELESS, "C:/Users/Luca/Downloads/paperSound.wav", false)));
		}

//		Pose pose1 = new Pose("Pose1", Controller.Repeat.WRAP_WITH_DELAY, mesh1);
//		pose1.addKeyFrame(0.0, new Vec3(0, 0, 0), camera2.transform.worldRotationCoordinates);
//		pose1.addKeyFrame(5.0, new Vec3(0, 0, -10), camera2.transform.worldRotationCoordinates.times(new Matrix4().setRotate(0, 1, 0, -2.355)));
//		Behaviour behaviour1 = new Behaviour("Behaviour1", new Distance("Distance1", false, true, 50, mesh1, camera1), new AudioPlayer("AudioEffect", UserInterface._3D_MODELESS, "C:/Users/Luca/Downloads/paperSound.wav", false));
//		Behaviour behaviour2 = new Behaviour("Behaviour2", new Distance("Distance2", false, true, 50, mesh1, camera1), pose1);
		
		X3DExporter.setEmbeddedScripts(true);
//		X3DExporter.setDrawLightSources(true);
		X3DExporter.export();
	}
}
