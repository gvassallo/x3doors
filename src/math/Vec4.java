package math;

import util.Utils;

public class Vec4 extends Vec3 {
	/** The w-component of this vector */
	public double w;
	
	/** Creates a vector with the following components
	 * x = 0.0;
	 * y = 0.0;
	 * z = 0.0;
	 */
	public Vec4() {
		super();
		this.w = 0.0;
	}
	
	/** Creates a vector with the given components
	 * 
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 * @param w The w-component
	 */
	public Vec4(double x, double y, double z, double w) {
		super(x, y, z);
		this.w = w;
	}
	/** Multiplies this vector for the given matrix.
	 * 
	 * @param mat The matrix
	 * @return The product this * the given matrix
	 */
	public Vec4 times(Matrix4 mat) {
		double[] matValues = mat.values;
		double x = this.x * matValues[ 0] + this.y * matValues[ 4] + this.z * matValues[ 8] + this.w * matValues[12];
		double y = this.x * matValues[ 1] + this.y * matValues[ 5] + this.z * matValues[ 9] + this.w * matValues[13];
		double z = this.z * matValues[ 2] + this.y * matValues[ 6] + this.z * matValues[10] + this.w * matValues[14];
		double w = this.x * matValues[ 3] + this.y * matValues[ 7] + this.z * matValues[11] + this.w * matValues[15];
		return new Vec4(x, y, z, w);
	}
	
	/** @return A Vec3 containing the first three components of the Vec4 */
	public Vec3 toVec3() {
		return new Vec3(this.x, this.y, this.z);
	}
	
	/** @return The euclidian length */
	public static double len (double x, double y, double z, double w) {
		return (double) Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	/** @return This vector X3D string */
	public String toX3D() {
		String X3DString = super.toX3D() + " " + Utils.double2StringFormat(w);
		return X3DString;
	}
}
