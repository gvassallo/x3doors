package x3doors.actions.controllers;

import util.Actionable;
import x3doors.nodes.HandleFactory;
import x3doors.nodes.SceneObject;

public abstract class Controller implements Actionable {
	/* This counter is used to assign the controller name automatically when it is not specified */
	private static int counter = 0;

	/** The controller name. */
	protected String name;
	/** The controller type. */
	protected String type;
	/** The controller repeat mode. */
	protected String repeat;
	/** The controller handle. */
	protected int handle;
	/** The handle of the scene object which this controller is attached to. */
	protected int attachedTo;
	
	/** Defines the controller types, can assume the following values<p>
	 * POSE: allows to animate a scene object by interpolating its key frames translation and rotation values<p>
	 * MATERIAL: allows to change a scene object material by interpolating its key frames appearance features<p>
	 * VISIBILITY: allows to switch the rendering state of a scene object by interpolating its key frames visibility values */
	public enum Type {
		POSE,
		MATERIAL,
		VISIBILITY,
		PARTICLE_EMITTER,
		PIXEL_SHADER_TIME,
		TEXTURE_ANIMATION
	}

	/** Defines the controller repeat modes. */
	public  enum Repeat {
		/* Repeat disabled */
		CLAMP,
		/* Repeat from the start to the end */
		WRAP,
		/* Repeat from the start to the end but keep the delay if the initial key frame time is not 0.0 and/or the last key frame time is not 1.0 */
		WRAP_WITH_DELAY,
		/* Repeat from the start to the end and then back to start */
		CYCLE,
		/* Repeat from the start to the end and then back to start but keep the delay if the initial key frame time is not 0.0 and/or the last
		 * key frame time is not 1.0 */
		CYCLE_WITH_DELAY
	}
	
	/** Creates a controller with the given properties.
	 * 
	 * @param name The name
	 * @param type The type
	 * @param repeat The repeat mode
	 * @param attachedTo The handle of the scene object which this controller is attached to
	 */
	public Controller(String name, Type type, Repeat repeat, SceneObject attachedTo) {
		switch (type) {
			case POSE:
				this.type = "Pose";
				break;
			case MATERIAL:
				this.type = "Material";
				break;
			case VISIBILITY:
				this.type = "Visibility";
				break;
			case PARTICLE_EMITTER:
				this.type = "ParticleEmitter";
				break;
			case PIXEL_SHADER_TIME:
				this.type ="PixelShaderTime";
				break;
			case TEXTURE_ANIMATION:
				this.type = "TextureAnimation";
				break;
		}
		this.name = (name == null || name.equals("")) ? (this.type + "Controller_" + counter++) : name;
		switch (repeat) {
			case CLAMP:
				this.repeat = "Clamp";
				break;
			case WRAP:
				this.repeat = "Wrap";
				break;
			case WRAP_WITH_DELAY:
				this.repeat = "WrapWithDelay";
				break;
			case CYCLE:
				this.repeat = "Cycle";
				break;
			case CYCLE_WITH_DELAY:
				this.repeat = "CycleWithDelay";
				break;
		}
		this.handle = HandleFactory.getNewHandle();
		this.attachedTo = attachedTo.getHandle();
	}
	
	/** @return The name */
	public String getName() {
		return name;
	}
	
	/** @return The type */
	public String getType() {
		return type;
	}
	
	/** @return The handle of the scene object which this controller is attached to */
	public int getAttachedTo() {
		return attachedTo;
	}
	
	/** Print the properties to screen. */
	public void print() {
		System.out.print(	"Name:\t\t\t" + name + "\n" +
							"Type:\t\t\t" + type + "\n" +
							"Repeat:\t\t\t" + repeat + "\n" +
							"Handle:\t\t\t" + handle + "\n" +
							"AttachedTo:\t\t" + attachedTo + "\n"
		);
	}
}