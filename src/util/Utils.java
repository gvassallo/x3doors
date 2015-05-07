package util;

import java.io.File;
import java.util.ArrayList;

public class Utils {
	
	private final static int DECIMAL_PRECISION = 6;
	
	/** Normalizes the given value according to the given normalization base.
	 * 
	 * @param value The value
	 * @param normalizationBase The base used by the normalization process
	 * @return The value normalized according to the normalization base
	 */
	public static double normalizeDouble(double value, double normalizationBase) {
		return value / normalizationBase;
	}
	
	/** Converts the given value into a string according to the DECIMAL_PRECISION constant value.
	 * 
	 * @param value The value
	 * @return A string representing the formatted value. The decimal precision is defined by the DECIMAL_PRECISION constant
	 */
	public static String double2StringFormat(double value) {
		return value % 1 == 0 ? Integer.toString((int) value) : // else
			(value = Math.round(value * Math.pow(10, DECIMAL_PRECISION)) / Math.pow(10.0, DECIMAL_PRECISION)) % 1 == 0 ? Integer.toString((int) value) : Double.toString(Math.round(value * Math.pow(10, DECIMAL_PRECISION)) / Math.pow(10.0, DECIMAL_PRECISION));
	}
	
	/** Converts a double array into a string interspersing its elements by using the given token.
	 * 
	 * @param array The array
	 * @param token The token
	 * @param lastToken If true a last token will be inserted after the last converted array element
	 * @return A string containing the formatted array elements interspersed with the token
	 */
	public static String doubleArray2StringFormat(double[] array, char token, boolean lastToken) {
		String s = "";
		int length = array.length;
		for (int i = 0; i < length; i++) {
			s += double2StringFormat(array[i]);
			// s += array[i] % 1 == 0? Integer.toString((int) array[i]) : Double.toString(Math.round(array[i] * Math.pow(10, DECIMAL_PRECISION)) / Math.pow(10.0, DECIMAL_PRECISION));
			if (lastToken || (!lastToken && i != length - 1)) {
				s += token;
			}
		}
		return s;
	}
	
	/** Mirrors the given array using its last element as mirroring pivot.
	 * 
	 * @param array The array
	 * @return The mirrored array
	 */
	public static String[] mirror(String[] array) {
		int length = array.length;
		int newLength = length * 2 - 1;
		String[] mirroredArray = new String[newLength];
		for (int i = 0; i < length; i++) {
			mirroredArray[i] = array[i];
			mirroredArray[newLength - i - 1] = array[i];
		}
		return mirroredArray;
	}
	
	/** Merges the given string array elements into a single string.
	 * 
	 * @param array The array
	 * @param token The token
	 * @param lastToken If true a last token will be inserted after the last converted array element
	 * @return A string containing the converted array elements interspersed with the token
	 */
	public static String stringArray2String (String[] array, char token, boolean lastToken) {
		String s = "";
		int length = array.length;
		for (int i = 0; i < length; i++) {
			s += array[i];
			if (lastToken || (!lastToken && i != length - 1)) {
				s += token;
			}
		}
		return s;
	}
	
	/** Normalizes the given array according to the given normalization base.<p>
	 * This method does not modify the given array but returns a new one.
	 * 
	 * @param array The array to normalize
	 * @param normalizationBase The base used by the normalization process
	 * @return The array normalized according to the normalization base
	 */
	public static double[] normalizeDoubleArray(double[] array, double normalizationBase) {
		int length = array.length;
		double[] newArray = new double[length];
		for (int i = 0; i < length; i++) {
			newArray[i] = array[i] / normalizationBase;
		}
		return newArray;
	}
	
	/** Subtracts the given offset from each element of the given array.<p>
	 * This method does not modify the given array but returns a new one.
	 * 
	 * @param array The array to process
	 * @param offset The offset to subtract from each element of the array
	 * @return A new array which for each element: array[i] = array[i] - offset
	 */
	public static double[] subtractDoubleOffset(double[] array, double offset) {
		int length = array.length;
		double[] newArray = new double[length];
		for (int i = 0; i < length; i++) {
			newArray[i] = array[i] - offset;
		}
		return newArray;
	}
	
	/** Converts each given list element to its X3D representation.
	 * 
	 * @param list The list
	 * @return A string array containing the X3D list elements representation
	 */
	public static String[] arrayList2X3DStringArray(ArrayList<? extends X3DExportable> list) {
		int size = list.size();
		String[] stringArray = new String[size];
		for (int i = 0; i < size; i++) {
			stringArray[i] = list.get(i).toX3D();
		}
		return stringArray;
	}
	
	/** Converts the given list to its X3D representation.
	 * 
	 * @param list The list
	 * @return A string containing the X3D list representation
	 */
	public static String arrayList2X3DString(ArrayList<? extends X3DExportable> list) {
		String X3DString = "";
		int size = list.size();
		for (int i = 0; i < size; i++) {
			X3DString += list.get(i).toX3D();
			if (i != size - 1) {
				X3DString += " ";
			}
		}
		return X3DString;
	}
	
	/** Converts each given list element to its X3D representation.
	 * 
	 * @param list The list
	 * @return A string array containing the X3D list elements representation
	 */
	public static String[] doubleList2X3DStringArray(ArrayList<Double> list) {
		int size = list.size();
		String[] stringArray = new String[size];
		for (int i = 0; i < size; i++) {
			stringArray[i] = Utils.double2StringFormat(list.get(i));
		}
		return stringArray;
	}
	
	/** Converts the given list to its X3D representation.
	 * 
	 * @param list The list
	 * @return A string containing the X3D list representation
	 */
	public static String doubleList2X3DString(ArrayList<Double> list) {
		String X3DString = "";
		int size = list.size();
		for (int i = 0; i < size; i++) {
			X3DString += Utils.double2StringFormat(list.get(i));
			if (i != size - 1) {
				X3DString += " ";
			}
		}
		return X3DString;
	}
	
	/** Converts each given list element to its X3D representation.
	 * 
	 * @param list The list
	 * @param trueValueMapping The string which will replace true values
	 * @param falseValueMapping The string which will replace false values
	 * @return A string array containing the X3D list elements representation
	 */
	public static String[] booleanList2X3DStringArray(ArrayList<Boolean> list, String trueValueMapping, String falseValueMapping) {
		int size = list.size();
		String[] stringArray = new String[size];
		for (int i = 0; i < size; i++) {
			stringArray[i] = list.get(i) ? trueValueMapping : falseValueMapping;
		}
		return stringArray;
	}
	
	/** Converts the given list to its X3D representation.
	 * 
	 * @param list The list
	 * @param trueValueMapping The corresponding true value string
	 * @param falseValueMapping The corresponding false value string
	 * @return A string containing the X3D list representation
	 */
	public static String booleanList2X3DString(ArrayList<Boolean> list, String trueValueMapping, String falseValueMapping) {
		String X3DString = "";
		int size = list.size();
		for (int i = 0; i < size; i++) {
			X3DString += list.get(i) ? trueValueMapping : falseValueMapping;
			if (i != size - 1) {
				X3DString += " ";
			}
		}
		return X3DString;
	}
	
	/** Converts the given list into an array.
	 * 
	 * @param list The list
	 * @return A double array containing the list elements
	 */
	public static double[] doubleList2DoubleArray(ArrayList<Double> list) {
		int size = list.size();
		double[] array = new double[size];
		for (int i = 0; i < size; i++) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	/** Converts the given list into a cyclized array.<p>
	 * It basically doubles the number of intervals insiede the list[lastElement] - list[firstElement] range.
	 * 
	 * @param list The list
	 * @return A double array containing the cyclized list elements
	 */
	public static double[] doubleList2CyclizedDoubleArray(ArrayList<Double> list) {
		int size = list.size();
		int newSize = size * 2 - 1; // newSize will always be odd
		double[] array = new double[newSize];
		
		// The last key value will always respectively be list.get(0) / 2 and list.get(size - 1) - list.get(0);
		array[0] = list.get(0);
		array[newSize - 1] = 2 * list.get(size - 1) - list.get(0);
		double temp;
		for (int i = 1; i < size; i++) {
			temp = (list.get(i) - list.get(i - 1));
			array[i] = array[i - 1] + temp;
			array[newSize - 1 - i] = array[newSize - i] - temp;
		}
		/* for (int i = 0; i < newSize; i++) {
			System.out.println("array[" + i + "] = " + array[i]);
		} */
		return array;
	}
	
	/** Checks the existence of each given path.
	 * 
	 * @param paths A list of paths
	 * @throws Exception When one of the given path does not exist
	 */
	public static void checkContentsExistences(String... paths) throws Exception {
		for (String path : paths) {
			if (!new File(path).exists()) {
				throw new Exception (path + " does not exist!");
			}
		}
	}
	
	/** Checks the consistency of the given content list with the supported extensions.
	 * 
	 * @param supportedExtensions The supported extensions
	 * @param contents The contents
	 * @throws Exception When one of the given contents is found to have an unsupported extension
	 */
	public static void checkContentsExtensions(String[] supportedExtensions, String... contents) throws Exception {
		String contentName = "";
		String contentExtension = "";
		for (String content : contents) {
			contentName = new File(content).getName();
			contentExtension = "";
			int i = contentName.lastIndexOf('.');
			if (i > 0) {
			    contentExtension = contentName.substring(i + 1);
			    for (i = 0; i < supportedExtensions.length; i++) {
			    	if (contentExtension.equals(supportedExtensions[i])) {
			    		return;
			    	}
			    }
			}
			throw new Exception("Unsupported extension for content " + contentName);
		}
	}
}