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

	// TODO: Add more type mapping here...
	public static String getObjStr() {
		return TYPE_OBJECT;
	}
	
	public static String getNumStr() {
		return TYPE_NUMBER;
	}
	
	public static String getStrStr() {
		return TYPE_STRING;
	}
	
	public DataType() {
		
	}
	
	public String getNumberStr() {
		return TYPE_NUMBER;
	}

}
