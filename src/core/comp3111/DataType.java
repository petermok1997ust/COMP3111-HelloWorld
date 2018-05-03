package core.comp3111;

/**
 * DataType helper class In the sample project, 3 types are supported
 * 
 * @author cspeter
 *
 */
public class DataType {

	public static final String TYPE_OBJECT = "java.lang.Object";
	public static final String TYPE_NUMBER = "java.lang.Number";
	public static final String TYPE_STRING = "java.lang.String";

	/**
	 * Get the string of object
	 * @return TYPE_OBJECT
	 */
	public static String getObjStr() {
		return TYPE_OBJECT;
	}
	
	/**
	 * Get the string of number
	 * @return TYPE_NUMBER
	 */
	public static String getNumStr() {
		return TYPE_NUMBER;
	}
	
	/**
	 * Get the string of string
	 * @return TYPE_STRING
	 */
	public static String getStrStr() {
		return TYPE_STRING;
	}
	
	/**
	 * Constructor of DataType
	 */
	public DataType() {
		
	}
	
	/**
	 * Get the string of number
	 * @return TYPE_STRING
	 */
	public String getNumberStr() {
		return TYPE_NUMBER;
	}

}
