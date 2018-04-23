package core.comp3111;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DataManagement implements Serializable{
	private int num_table;
	private int num_chart;
	private DataTable[] table_array;
	private Chart[] chart_array;
	private static DataManagement management_instance = null;
	private static final String COMMA = ",";
	private static final String NEW_LINE = "\n";

//	private static final int TABLE = 1000;
//	private static final int CHART = 1001;
	
	//constructor
	private DataManagement() {
		num_table = 0;
		num_chart = 0;
		table_array = null;
		chart_array = null;
	}
	
	private void setInstance(DataManagement dataManagementObj) {
		management_instance.num_chart = dataManagementObj.num_chart;
		management_instance.num_table = dataManagementObj.num_table;
		System.arraycopy( dataManagementObj.table_array, 0, management_instance.table_array, 0, dataManagementObj.table_array.length );
		System.arraycopy( dataManagementObj.chart_array, 0, management_instance.chart_array, 0, dataManagementObj.chart_array.length );
//		management_instance.table_array = dataManagementObj.table_array;
//		management_instance.chart_array = dataManagementObj.chart_array;
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
//    	List<List<String>> lines = new ArrayList<>();
        try{
            inputStream = new Scanner(file);
            while(inputStream.hasNextLine()) {
            	String line = inputStream.nextLine();
            	Scanner lineScanner = new Scanner(line);
            	lineScanner.useDelimiter(COMMA);
            	System.out.println("next: ");
            	 while(lineScanner.hasNext()){
                     String line_item= lineScanner.next();
                     if(line_item.isEmpty())
                    	 line_item = "";
                     System.out.print(":"+line_item);
                 }
            	 lineScanner.close();
            }
            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    
    public void exportTableToCSV(DataTable table, String fileName) {
    	//write to csv
    	FileWriter fileWriter = null;
//    	String fileName = "test_ex";

    	//https://examples.javacodegeeks.com/core-java/writeread-csv-files-in-java-example/
		try {
			fileWriter = new FileWriter(fileName);
			fileWriter.append("try");
			fileWriter.append(",");
			fileWriter.append(NEW_LINE);
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
    }
    
    public void saveProject() {
    	 try {
    		 DataManagement projectObj = this;
    		 this.num_chart = 10;
             System.out.println("obj1: " + projectObj.num_chart);
             FileOutputStream fos = new FileOutputStream("obj.comp3111");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
             oos.writeObject(projectObj);
             oos.flush();
             oos.close();
             this.num_chart = 0;
             System.out.println("obj1: " + projectObj.num_chart);
             System.out.println("this: " + this.num_chart);
         } catch (Exception e) {
        	 System.out.println("Error while Saving !!!");
             e.printStackTrace();
         }
    }
    
    public void loadProject(File file) {
    	 try {
    		 DataManagement load_object;
             FileInputStream fis = new FileInputStream("obj.comp3111");
             ObjectInputStream ois = new ObjectInputStream(fis);
             load_object = (DataManagement) ois.readObject();
             ois.close();
             System.out.println("obj: " + load_object.num_chart);
         } catch (Exception e) {
         }
    }
    
    public DataTable createDataTable() {
		return null;
    }
	
	
	

}
