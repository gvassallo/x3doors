package math;

import util.Printable;

public class Matrix4 implements Printable {
	/** The matrix values */
	public double[] values;
	
	/** Creates a new empty matrix. */
	public Matrix4() {
		values = new double[16];
	}
	
	/** Sets the matrix to the identity matrix.
	 * 
	 * @return An identity matrix
	 */
	public Matrix4 setIdentity() {
		values[ 0] = 1;		values[ 4] = 0;		values[ 8] = 0;		values[12] = 0;
		values[ 1] = 0;		values[ 5] = 1;		values[ 9] = 0;  	values[13] = 0;
		values[ 2] = 0;		values[ 6] = 0;		values[10] = 1;  	values[14] = 0;
		values[ 3] = 0;		values[ 7] = 0;		values[11] = 0;  	values[15] = 1;
		return this;
	};
	
	/** Sets the matrix to the translation matrix with the given parameters.
	 * 
	 * @param xAmount The x-axis translation
	 * @param yAmount The y-axis translation
	 * @param zAmount The z-axis translation
	 * @return A translation matrix
	 */
	public Matrix4 setTranslate(double xAmount, double yAmount, double zAmount) {
		values[ 0] = 1;		values[ 4] = 0;		values[ 8] = 0;		values[12] = xAmount;
		values[ 1] = 0;		values[ 5] = 1;		values[ 9] = 0;  	values[13] = yAmount;
		values[ 2] = 0;		values[ 6] = 0;		values[10] = 1;  	values[14] = zAmount;
		values[ 3] = 0;		values[ 7] = 0;		values[11] = 0;  	values[15] = 1;
		return this;
	};
	
	/** Sets the matrix to the rotation matrix with the given parameters.
	 * 
	 * @param rotationAxisX The x-component of the rotation axis
	 * @param rotationAxisY The y-component of the rotation axis
	 * @param rotationAxisZ The z-component of the rotation axis
	 * @param angle The rotation angle
	 * @return A rotation matrix
	 */
	public Matrix4 setRotate(double rotationAxisX, double rotationAxisY, double rotationAxisZ, double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double x = rotationAxisX;
		double y = rotationAxisY;
		double z = rotationAxisZ;
		double len, rlen;
		double nCos, xy, yz, zx, xSin, ySin, zSin;
		if (x!= 0 && y == 0 && z == 0) {
			// Rotation around X axis
			if (x < 0) {
				sin = -sin;
		    }
		    values[0] = 1; 		values[4] = 0;  	values[ 8] = 0;  	values[12] = 0;
		    values[1] = 0;  	values[5] = cos;  	values[ 9] =-sin;  	values[13] = 0;
		    values[2] = 0;  	values[6] = sin;  	values[10] = cos;  	values[14] = 0;
		    values[3] = 0;  	values[7] = 0;  	values[11] = 0;  	values[15] = 1;
		}
		else if (x == 0 && y != 0 && z == 0) {
		    // Rotation around Y axis
		    if (y < 0) {
		      sin = -sin;
		    }
		    values[0] = cos;  	values[4] = 0;  	values[ 8] = sin;	values[12] = 0;
		    values[1] = 0;  	values[5] = 1;  	values[ 9] = 0;  	values[13] = 0;
		    values[2] =-sin;  	values[6] = 0;  	values[10] = cos;	values[14] = 0;
		    values[3] = 0;  	values[7] = 0;  	values[11] = 0;		values[15] = 1;
		}
		else if (x == 0 && y == 0 && z == 0) {
		    // Rotation around Z axis
		    if (z < 0) {
		      sin = -sin;
		    }
		    values[0] = cos;	values[4] =-sin;	values[ 8] = 0;		values[12] = 0;
		    values[1] = sin;	values[5] = cos;	values[ 9] = 0;		values[13] = 0;
		    values[2] = 0;		values[6] = 0;		values[10] = 1;		values[14] = 0;
		    values[3] = 0;		values[7] = 0;		values[11] = 0;		values[15] = 1;
		}
		else {
		    // Rotation around another axis
		    len = Math.sqrt(x * x + y * y + z * z);
		    if (len != 1) {
		    	rlen = 1 / len;
		    	x *= rlen;
		    	y *= rlen;
		    	z *= rlen;
		    }
		    nCos = 1 - cos;
		    xy = x * y;
		    yz = y * z;
		    zx = z * x;
		    xSin = x * sin;
		    ySin = y * sin;
		    zSin = z * sin;

		    values[ 0] = x * x * nCos +  cos;
		    values[ 1] = xy * nCos + zSin;
		    values[ 2] = zx * nCos - ySin;
		    values[ 3] = 0;

		    values[ 4] = xy * nCos - zSin;
		    values[ 5] = y * y *nCos +  cos;
		    values[ 6] = yz * nCos + xSin;
		    values[ 7] = 0;

		    values[ 8] = zx * nCos + ySin;
		    values[ 9] = yz * nCos - xSin;
		    values[10] = z * z * nCos +  cos;
		    values[11] = 0;

		    values[12] = 0;
		    values[13] = 0;
		    values[14] = 0;
		    values[15] = 1;
		}
		
		return this;
	}
	
	/** Sets the matrix to the scaling matrix with the given components.
	 * 
	 * @param scaleX The scaling factor along the x-axis
	 * @param scaleY The scaling factor along the y-axis
	 * @param scaleZ The scaling factor along the z-axis
	 * @return A scaling matrix
	 */
	public Matrix4 setScale(double scaleX, double scaleY, double scaleZ) {
		values[ 0] = scaleX;	values[ 4] = 0;			values[ 8] = 0;			values[12] = 0;
		values[ 1] = 0;			values[ 5] = scaleY;	values[ 9] = 0;			values[13] = 0;
		values[ 2] = 0;			values[ 6] = 0;			values[10] = scaleZ;	values[14] = 0;
		values[ 3] = 0;			values[ 7] = 0;			values[11] = 0;			values[15] = 1;
		return this;
	}
	
	/** @return The inverted matrix */
	public Matrix4 invert() {
		double[] inv = new double[16];
		inv[ 0] =		values[ 5] * values[10] * values[15] - values[ 5] * values[11] * values[14] - values[ 9] * values[ 6] * values[15] +
						values[ 9] * values[ 7] * values[14] + values[13] * values[ 6] * values[11] - values[13] * values[ 7] * values[10];
		inv[ 4] = 	  - values[ 4] * values[10] * values[15] + values[ 4] * values[11] * values[14] + values[ 8] * values[ 6] * values[15] +
	               	  - values[ 8] * values[ 7] * values[14] - values[12] * values[ 6] * values[11] + values[12] * values[ 7] * values[10];
		inv[ 8] =		values[ 4] * values[ 9] * values[15] - values[ 4] * values[11] * values[13] - values[ 8] * values[ 5] * values[15] +
	            		values[ 8] * values[ 7] * values[13] + values[12] * values[ 5] * values[11] - values[12] * values[ 7] * values[ 9];
		inv[12] = 	  - values[ 4] * values[ 9] * values[14] + values[ 4] * values[10] * values[13] + values[ 8] * values[ 5] * values[14] +
	              	  - values[ 8] * values[ 6] * values[13] - values[12] * values[ 5] * values[10] + values[12] * values[ 6] * values[ 9];

		inv[ 1] = 	  - values[ 1] * values[10] * values[15] + values[ 1] * values[11] * values[14] + values[ 9] * values[ 2] * values[15] +
	              	  - values[ 9] * values[ 3] * values[14] - values[13] * values[ 2] * values[11] + values[13] * values[ 3] * values[10];
		inv[ 5] =   	values[ 0] * values[10] * values[15] - values[ 0] * values[11] * values[14] - values[ 8] * values[ 2] * values[15] +
	            		values[ 8] * values[ 3] * values[14] + values[12] * values[ 2] * values[11] - values[12] * values[ 3] * values[10];
		inv[ 9] = 	  - values[ 0] * values[ 9] * values[15] + values[ 0] * values[11] * values[13] + values[ 8] * values[ 1] * values[15] +
	              	  - values[ 8] * values[ 3] * values[13] - values[12] * values[ 1] * values[11] + values[12] * values[ 3] * values[ 9];
		inv[13] =   	values[ 0] * values[ 9] * values[14] - values[ 0] * values[10] * values[13] - values[ 8] * values[ 1] * values[14] +
	            		values[ 8] * values[ 2] * values[13] + values[12] * values[ 1] * values[10] - values[12] * values[ 2] * values[ 9];

		inv[ 2] =   	values[ 1] * values[ 6] * values[15] - values[ 1] * values[ 7] * values[14] - values[ 5] * values[ 2] * values[15] +
	            		values[ 5] * values[ 3] * values[14] + values[13] * values[ 2] * values[ 7] - values[13] * values[ 3] * values[ 6];
		inv[ 6] = 	  - values[ 0] * values[ 6] * values[15] + values[ 0] * values[ 7] * values[14] + values[ 4] * values[ 2] * values[15] +
	              	  - values[ 4] * values[ 3] * values[14] - values[12] * values[ 2] * values[ 7] + values[12] * values[ 3] * values[ 6];
		inv[10] =   	values[ 0] * values[ 5] * values[15] - values[ 0] * values[ 7] * values[13] - values[ 4] * values[ 1] * values[15] +
	            		values[ 4] * values[ 3] * values[13] + values[12] * values[ 1] * values[ 7] - values[12] * values[ 3] * values[ 5];
		inv[14] = 	  - values[ 0] * values[ 5] * values[14] + values[ 0] * values[ 6] * values[13] + values[ 4] * values[ 1] * values[14] +
	              	  - values[ 4] * values[ 2] * values[13] - values[12] * values[ 1] * values[ 6] + values[12] * values[ 2] * values[ 5];

		inv[ 3] = 	  - values[ 1] * values[ 6] * values[11] + values[ 1] * values[ 7] * values[10] + values[ 5] * values[ 2] * values[11] +
	            	  - values[ 5] * values[ 3] * values[10] - values[ 9] * values[ 2] * values[ 7] + values[ 9] * values[ 3] * values[ 6];
		inv[ 7] =   	values[ 0] * values[ 6] * values[11] - values[ 0] * values[ 7] * values[10] - values[ 4] * values[ 2] * values[11] +
	            		values[ 4] * values[ 3] * values[10] + values[ 8] * values[ 2] * values[ 7] - values[ 8] * values[ 3] * values[ 6];
		inv[11] = 	  - values[ 0] * values[ 5] * values[11] + values[ 0] * values[ 7] * values[ 9] + values[ 4] * values[ 1] * values[11] +
	            	  - values[ 4] * values[ 3] * values[ 9] - values[ 8] * values[ 1] * values[ 7] + values[ 8] * values[ 3] * values[ 5];
		inv[15] =   	values[ 0] * values[ 5] * values[10] - values[ 0] * values[ 6] * values[ 9] - values[ 4] * values[ 1] * values[10] +
	            		values[ 4] * values[ 2] * values[ 9] + values[ 8] * values[ 1] * values[ 6] - values[ 8] * values[ 2] * values[ 5];

		double det;
		det = values[0] * inv[0] + values[1] * inv[4] + values[2] * inv[8] + values[3] * inv[12];
		if (det == 0) {
			return this;
		}

		det = 1 / det;
		for (int i = 0; i < 16; i++) {
			inv[i] = inv[i] * det;
		}
		
		Matrix4 invMatrix = new Matrix4();
		invMatrix.values = inv;
		return invMatrix;
	}
	
	// Compute mat1 * mat2
	/** Multiplies this matrix for the given one, that is this * mat 
	 * 
	 * @param mat The other matrix
	 * @return this * mat
	 */
	public Matrix4 concat(Matrix4 mat) {
		double[] mat1Values = values;
		double[] mat2Values = mat.values;
		double[] conc = new double[16];
		double ai0, ai1, ai2, ai3;
		
		for (int i = 0; i < 4; i++) {
			ai0 = mat1Values[i];
			ai1 = mat1Values[i + 4];
			ai2 = mat1Values[i + 8];
			ai3 = mat1Values[i + 12];
			
			conc[i] = 		ai0 * mat2Values[ 0] + ai1 * mat2Values[ 1] + ai2 * mat2Values[ 2] + ai3 * mat2Values[ 3];
			conc[i + 4] = 	ai0 * mat2Values[ 4] + ai1 * mat2Values[ 5] + ai2 * mat2Values[ 6] + ai3 * mat2Values[ 7];
			conc[i + 8] = 	ai0 * mat2Values[ 8] + ai1 * mat2Values[ 9] + ai2 * mat2Values[10] + ai3 * mat2Values[11];
			conc[i + 12] = 	ai0 * mat2Values[12] + ai1 * mat2Values[13] + ai2 * mat2Values[14] + ai3 * mat2Values[15];
		}
		
		Matrix4 conMatrix = new Matrix4();
		conMatrix.values = conc;
		return conMatrix;
	}
	
	/** Translate this matrix of the given amounts along the corresponding axes.
	 * 
	 * @param xAmount The x-axis translation
	 * @param yAmount The y-axis translation
	 * @param zAmount The z-axis translation
	 * @return The translated matrix
	 */
	public Matrix4 translate(double xAmount, double yAmount, double zAmount) {
		double[] values = this.values.clone();
		values[12] += values[ 0] * xAmount + values[ 4] * yAmount + values[ 8] * zAmount;
		values[13] += values[ 1] * xAmount + values[ 5] * yAmount + values[ 9] * zAmount;
		values[14] += values[ 2] * xAmount + values[ 6] * yAmount + values[10] * zAmount;
		values[15] += values[ 3] * xAmount + values[ 7] * yAmount + values[11] * zAmount;
		Matrix4 translMatrix = new Matrix4();
		translMatrix.values = values;
		return translMatrix;
	}
	
	/** Rotates this matrix around the given axis of the given angle in radians.
	 * 
	 * @param rotationAxisX The x-component of the rotation axis
	 * @param rotationAxisY The y-component of the rotation axis
	 * @param rotationAxisZ The z-component of the rotation axis
	 * @param angle The angle amount in radians
	 * @return The rotated matrix
	 */
	public Matrix4 rotate(double rotationAxisX, double rotationAxisY, double rotationAxisZ, double angle) {
		return this.concat(new Matrix4().setRotate(rotationAxisX, rotationAxisY, rotationAxisZ, angle));
	}
	
	/** Scales this matrix according to the given scaling factors.
	 * 
	 * @param scaleX The scaling factor along the x-axis
	 * @param scaleY The scaling factor along the y-axis
	 * @param scaleZ The scaling factor along the z-axis
	 * @return The scaled matrix
	 */
	public Matrix4 scale(double scaleX, double scaleY, double scaleZ) {
		return this.concat(new Matrix4().setScale(scaleX, scaleY, scaleZ));
	}
	
	/** @return The axis-angle representation of the matrix */
	public Vec4 toAxisAngle() {
		double angle, x, y, z;
		double epsilon1 = 0.01; // Margin to allow for rounding errors
		double epsilon2 = 0.1; // Margin to distinguish between 0 and 180 degrees
		// Optional check that input is pure rotation, 'isRotationMatrix' is defined at:
		// http://www.euclideanspace.com/maths/algebra/matrix/orthogonal/rotation/
		if (	(Math.abs(values[ 4] - values[ 1]) < epsilon1)
			&& 	(Math.abs(values[ 8] - values[ 2]) < epsilon1)
			&& 	(Math.abs(values[ 9] - values[ 6]) < epsilon1)) {
			// Singularity found
			// First check for identity matrix which must have +1 for all terms in leading diagonal and zero in other terms
			if ((Math.abs(values[ 4] + values[ 1]) < epsilon2)
			&& 	(Math.abs(values[ 8] + values[ 2]) < epsilon2)
			&&	(Math.abs(values[ 9] + values[ 6]) < epsilon2)
			&&	(Math.abs(values[ 0] + values[ 5] + values[10] - 3) < epsilon2)) {
				// This singularity is identity matrix so angle = 0
				return new Vec4(1, 0, 0, 0); // zero angle, arbitrary axis
			}
			// Otherwise this singularity is angle = 180
			angle = Math.PI;
			double xx = (values[ 0] + 1) / 2;
			double yy = (values[ 5] + 1) / 2;
			double zz = (values[10] + 1) / 2;
			double xy = (values[ 4] + values[ 1]) / 4;
			double xz = (values[ 8] + values[ 2]) / 4;
			double yz = (values[ 9] + values[ 6]) / 4;
			if ((xx > yy) && (xx > zz)) { // values[0] is the largest diagonal term
				if (xx < epsilon1) {
					x = 0;
					y = 0.7071;
					z = 0.7071;
				}
				else {
					x = Math.sqrt(xx);
					y = xy / x;
					z = xz / x;
				}
			}
			else if (yy > zz) { // values[5] is the largest diagonal term
				if (yy < epsilon1) {
					x = 0.7071;
					y = 0;
					z = 0.7071;
				}
				else {
					y = Math.sqrt(yy);
					x = xy / y;
					z = yz / y;
				}	
			}
			else { // values[10] is the largest diagonal term so base result on this
				if (zz < epsilon1) {
					x = 0.7071;
					y = 0.7071;
					z = 0;
				}
				else {
					z = Math.sqrt(zz);
					x = xz / z;
					y = yz / z;
				}
			}
			return new Vec4(x, y, z, angle); // Return 180 degrees rotation
		}
		
		// As we have reached here there are no singularities so we can handle normally
		double s = Math.sqrt(	(values[ 6] - values[ 9]) * (values[ 6] - values[ 9]) +
								(values[ 8] - values[ 2]) * (values[ 8] - values[ 2]) +
								(values[ 1] - values[ 4]) * (values[ 1] - values[ 4])); // Used to normalize
		if (Math.abs(s) < 0.001) s = 1; 
		// Prevent divide by zero, should not happen if matrix is orthogonal and should be caught by singularity test
		// above, but I've left it in just in case
		angle = Math.acos((values[ 0] + values[ 5] + values[10] - 1) / 2);
		x = (values[ 6] - values[ 9]) / s;
		y = (values[ 8] - values[ 2]) / s;
		z = (values[ 1] - values[ 4]) / s;
		return new Vec4(x, y, z, angle);
	}
	
	/** @return The translation amount of the matrix */
	public Vec3 getTranslation() {
		return new Vec3(values[12], values[13], values[14]);
	}
	
	/** @return The matrix decomposition values corresponding to (transformation order S -> R -> T):<p>
	 * - the scaling factors of the scaling matrix<p>
	 * - the axis-angle representation of the rotation matrix<p>
	 * - the translation amounts of the translation matrix */ 
	public Vec4[] decompose() {
		// Applied algorithm:
		// 1. Compute the scaling factors as the magnitudes of the first three basis vectors (columns or rows) of the matrix
		// 2. Divide the first three basis vectors by these values (thus normalizing them)
		// 3. The upper-left 3x3 part of the matrix now represents the rotation (you can use this as is, or convert it to quaternion form)
		// 4. The translation is the fourth basis vector of the matrix (in homogeneous coordinates - it'll be the first three elements that you're interested in)
		Vec3 translation = this.getTranslation();
		Vec3 c0 = new Vec3(values[ 0], values[ 1], values[ 2]);
		Vec3 c1 = new Vec3(values[ 4], values[ 5], values[ 6]);
		Vec3 c2 = new Vec3(values[ 8], values[ 9], values[10]);
		Vec3 scale = new Vec3();
		scale.x = Vec3.len(c0.x, c0.y, c0.z);
		scale.y = Vec3.len(c1.x, c1.y, c1.z);
		scale.z = Vec3.len(c2.x, c2.y, c2.z);
		Matrix4 mat = new Matrix4();
		mat.values[ 0] = c0.x / scale.x;		mat.values[ 4] = c1.x / scale.y;		mat.values[ 8] = c2.x / scale.z;	mat.values[12] = 0;
		mat.values[ 1] = c0.y / scale.x;		mat.values[ 5] = c1.y / scale.y;		mat.values[ 9] = c2.y / scale.z;	mat.values[13] = 0;
		mat.values[ 2] = c0.z / scale.x;		mat.values[ 6] = c1.z / scale.y;		mat.values[10] = c2.z / scale.z;	mat.values[14] = 0;
		mat.values[ 3] = 0;						mat.values[ 7] = 0;						mat.values[11] = 0;					mat.values[15] = 1;
		Vec4 rotationAxisAndAngle = mat.toAxisAngle();

		return new Vec4[]{scale.toVec4(), rotationAxisAndAngle, translation.toVec4()};
	}
	
	/** Print the matrix values to screen */
	public void print() {
		for (int i = 0; i < 4; i++) {
			System.out.println("\t" + util.Utils.double2StringFormat(values[i]) + "\t" + util.Utils.double2StringFormat(values[i + 4]) + "\t" + util.Utils.double2StringFormat(values[i + 8]) + "\t" + util.Utils.double2StringFormat(values[i + 12]));
		}
	}
}
