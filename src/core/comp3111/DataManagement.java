package core.comp3111;
import java.io.File;

public class DataManagement {
	private int num_table;
	private int num_chart;
	private DataTable[] table_array;
	private Chart[] chart_array;
	private static DataManagement management_instance = null;
	
	//constructor
	private DataManagement() {
		num_table = 0;
		num_chart = 0;
		table_array = null;
		chart_array = null;
	}
	
	//singleton getInstance
    public static DataManagement getInstance()
    {
        if (management_instance == null)
        	management_instance = new DataManagement();
        return management_instance;
    }
    
    public void importCSV(File file) {
		
    }
    
    public DataTable createDataTable() {
		return null;
    }
	
	
	

}
