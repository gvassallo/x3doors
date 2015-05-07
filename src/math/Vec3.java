package math;

import util.Utils;
import util.X3DExportable;

public class Vec3 implements X3DExportable {
	/** The x-component of this vector */
	public double x;
	/** The y-component of this vector */
	public double y;
	/** The z-component of this vector */
	public double z;
	
	/** Creates a vector with the following components
	 * x = 0.0;
	 * y = 0.0;
	 * z = 0.0; */
	public Vec3() {
		this.x = this.y = this.z = 0.0;
	}
	
	/** Creates a vector with the given components
	 * 
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component */
	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/** @return The euclidean length */
	public static double len (double x, double y, double z) {
		return (double) Math.sqrt(x * x + y * y + z * z);
	}
	
	/** @return The cross product between the two vectors */
	public static Vec3 cross(double x1, double y1, double z1, double x2, double y2, double z2) {
		return new Vec3(((y1 * z2) - (z1 * y2)), ((z1 * x2) - (x1 * z2)), ((x1 * y2) - (y1 * x2)));
	}
	
	/** Perform a component by component sum of this vector and the given one.
	 * 
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 * @return The sum this + the vector with the given components
	 */
	public Vec3 sum(double x, double y, double z) {
		return new Vec3(this.x + x, this.y + y, this.z + z);
	}
	
	/** @return Return the homogeneous coordinates representation of this vector, which is a Vec4 */
	public Vec4 toVec4() {
		return new Vec4(this.x, this.y, this.z, 1);
	}
	
	/** @return The dot product between the two vectors */
	public static double dot (double x1, double y1, double z1, double x2, double y2, double z2) {
		return x1 * x2 + y1 * y2 + z1 * z2;
	}

	public double dot (Vec3 vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	/** Returns the dot product between this and the given vector.
	 * @param x The x-component of the other vector
	 * @param y The y-component of the other vector
	 * @param z The z-component of the other vector
	 * @return The dot product */
	public double dot (double x, double y, double z) {
		return this.x * x + this.y * y + this.z * z;
	}
	
	/** Normalizes this vector to unit length
	 * 
	 * @return This vector normalized
	 */
	public Vec3 nor() {
		double length = x * x + y * y + z * z;
		if (length != 1f && length != 0f){
			length = 1.0f / Math.sqrt(length);
			return new Vec3(x * length, y * length, z * length);
		}
		return this;
	}
	
	/** @return This vector X3D string */
	public String toX3D() {
		return 	Utils.double2StringFormat(x) + " " +
				Utils.double2StringFormat(y) + " " +
				Utils.double2StringFormat(z);
	}
}