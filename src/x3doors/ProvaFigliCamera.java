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
public class ProvaFigliCamera {
	public static void main (String[] args) throws Exception {
		SceneProperties.setTitle("ProvaFigliCamera");
		SceneProperties.setDescription("Prova dei figli della camera utente");
		SceneProperties.setCreator("Luca Martini");
		SceneProperties.setBackgroundColor(0, 0, 0);
		Root.getInstance().transform.localRotationCoordinates = new Vec4(1, 0, 0, 1.57);
		Camera camera1 = new Camera("UserCamera",
									"Interactive user camera", 
									new Vec3(0, 0, 0),
									new Vec4(0, 1, 0, Math.PI),
									0.1,
									10000.0,
									44.0,
									true,
									50.0,
									1.0,
									Camera.exploreModeType.HUMAN);
		
//		Camera camera2 = new Camera();
		
		int pillarRows = 1000;
		Mesh temp;
		Vec3 pillarWorldScaleFactor = new Vec3(1, 2, 5);
		Vec3 floorWorldScaleFactor = new Vec3(10000, 10000, 10000);
		
		/* Mesh floor = new Mesh(	"Floor",
								Mesh.Type.RECTANGLE,
								new Vec3(pillarWorldScaleFactor.z * pillarRows / 2, -pillarWorldScaleFactor.y / 2, pillarWorldScaleFactor.z * pillarRows / 2),
								new Vec4(0, 0, 1, -1.57),
								floorWorldScaleFactor,
								true,
								false
							);
		floor.setEmissive(new RGBColor(0, 255, 255)); */
		
		for (int i = 0; i < pillarRows; i++) {
			temp = new Mesh("PillarLeft" + i,
							Mesh.Type.BOX,
							new Vec3(-5, 0, pillarWorldScaleFactor.z * i + pillarWorldScaleFactor.z / 2),
							new Vec4(1, 0, 0, 0),
							pillarWorldScaleFactor,
							true,
							false
			);
			temp.setEmissive(RGBColor.getDefaultColor());
			
			temp = new Mesh("PillarRight" + i,
							Mesh.Type.BOX,
							new Vec3(5, 0, pillarWorldScaleFactor.z * i + pillarWorldScaleFactor.z / 2),
							new Vec4(1, 0, 0, 0),
							pillarWorldScaleFactor,
							true,
							false
			);
			temp.setEmissive(RGBColor.getDefaultColor());
		}
		
		Light spot = new Light(	"SpotLight",
								Light.Type.SPOT,
								new Vec3(0, 0, 10),
								new Vec4(1, 0, 0, 0),
								camera1
		);
		
		Mesh mesh1 = new Mesh("Box",
				Mesh.Type.BOX,
				new Vec3(0, 0, 10),
				new Vec4(1, 0, 0, 0),
				new Vec3(1, 2, 3),
				true,
				false,
				camera1
		);
		mesh1.setEmissive(new RGBColor(0, 0, 0));
		mesh1.setAlpha(0.2);
		
		
//		camera2.setName("DefaultCamera");
//		camera2.setVisible(false);
		
		SceneProperties.setActiveCamera(camera1);
//		ArrayList<Behaviour> behaviourList = new ArrayList<Behaviour>();
//		double steps = 5;
//		double duration = 10;
//		for (double i = 0; i < steps; i++) {
//			behaviourList.add(new Behaviour("DelayedAudioEffect" + (int) (i * steps), new Delay("Delay" + (int) (i * steps), false, false, duration / steps * i), new AudioPlayer("AudioEffect" + (int) (i * steps), UserInterface._3D_MODELESS, "C:/Users/Luca/Downloads/paperSound.wav", false)));
//		}

//		Pose pose1 = new Pose("Pose1", Controller.Repeat.WRAP_WITH_DELAY, mesh1);
//		pose1.addKeyFrame(0.0, new Vec3(0, 0, 0), camera2.transform.worldRotationCoordinates);
//		pose1.addKeyFrame(5.0, new Vec3(0, 0, -10), camera2.transform.worldRotationCoordinates.times(new Matrix4().setRotate(0, 1, 0, -2.355)));
//		Behaviour behaviour1 = new Behaviour("Behaviour1", new Distance("Distance1", false, true, 50, mesh1, camera1), new AudioPlayer("AudioEffect", UserInterface._3D_MODELESS, "C:/Users/Luca/Downloads/paperSound.wav", false));
//		Behaviour behaviour2 = new Behaviour("Behaviour2", new Distance("Distance2", false, true, 50, mesh1, camera1), pose1);
		
		X3DExporter.setEmbeddedScripts(false);
		X3DExporter.setDrawLightSources(false);
		X3DExporter.export();
	}
}
