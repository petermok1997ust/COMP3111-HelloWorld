package core.comp3111;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
    	System.out.println("Importing CSV....");
    	Scanner inputStream;
    	List<List<String>> lines = new ArrayList<>();
        try{
            inputStream = new Scanner(file);
            while(inputStream.hasNextLine()) {
            	String line = inputStream.nextLine();
            	Scanner lineScanner = new Scanner(line);
            	lineScanner.useDelimiter(",");
            	System.out.println("next: ");
            	 while(lineScanner.hasNext()){
                     String line_item= lineScanner.next();
                     System.out.print(":"+line_item);
                 }
            	 lineScanner.close();
            }
            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    
    public DataTable createDataTable() {
		return null;
    }
	
	
	

}
