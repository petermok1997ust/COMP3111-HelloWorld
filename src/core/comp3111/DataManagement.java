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
import java.util.Set;

import ui.comp3111.UIController;
/**
 * An implementation of Data Import, Export , Save and Load
 * @author mok00
 *
 */
public class DataManagement implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -2309027075882808422L;
	private int num_table;
	private int num_chart;
	private List<DataTable> table_array;
//	private List<Chart> chart_array;
	private List<String> table_name;
	private static DataManagement management_instance = null;
	private static final String COMMA = ",";
	private static final String NEW_LINE = "\n";
	private static final String EMPTY_STRING = "";


	/**
	 * Constructor of DataManagement
	 */
	public DataManagement() {
		num_table = 0;
		num_chart = 0;
		table_array = null;
		table_array = new ArrayList<DataTable>();
		table_name = new ArrayList<String>();
	}

	/**
	 * Get instance object of DataManagement
	 * @return Instance of DataManagement Obj
	 */
	public static DataManagement getInstance()
	{
		if (management_instance == null)
			management_instance = new DataManagement();
		return management_instance;
	}

	/**
	 * Import the CSV from file
	 * @param file
	 *	- CSV file to import
	 * @return Name of the dataset
	 * @throws DataTableException
	 * - Fail to scan csv file
	 */
	public String importCSV(File file) throws DataTableException {
		if(file == null)
			return null;
		System.out.println("Importing CSV "+ file.getAbsolutePath());
		Scanner inputStream;
		
		List<String> list = new ArrayList<String> ();  
		int num_row = 0;
		int num_col = 0;
		try{
			inputStream = new Scanner(file);
			while(inputStream.hasNextLine()) {
				String line = inputStream.nextLine();
				System.out.print(line);
				String[] line_split = line.split(",",-1);
				num_col = line_split.length;
				for(int c=0; c<line_split.length; c++) {
					String input = line_split[c].isEmpty()?null:line_split[c];
//					System.out.print(input + " ");
					list.add(input);
				}
				num_row++;
//				System.out.print(" " + num_row);
				System.out.println();
			}
			inputStream.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Finished importing CSV with "+ num_row+" rows and "+ num_col+" columns");
		if(num_row >0 && num_col >0) {
			System.out.println("Creating Table");
			createDataTable(list, num_row-1, num_col);	
			num_table++;
			String name = "dataset"+num_table;
			table_name.add(name);
			System.out.println("Finished create Table");
			return name;
		}else
			return null;
	}

	/**
	 * Export the table to the csv to the directory of file
	 * @param table
	 * - DataTable to export
	 * @param file
	 * - File DIrectory to export
	 * @return
	 * - Fail to write csv file
	 * @throws IOException 
	 */
	public boolean exportTableToCSV(DataTable table, File file) throws IOException {
		//write to csv
		if(file == null)
			return false;
		int num_row = table.getNumRow();
		int num_col = table.getNumCol();
		Set<String> keys = table.getKeys();
		String[] headers = keys.toArray(new String[keys.size()]);
//		System.out.println("Export CSV "+file.getAbsolutePath()+" with rows = "+num_row+"columns = "+num_col);
		FileWriter fileWriter = null;
		System.out.println("num_row"+num_row);
			fileWriter = new FileWriter(file);
			//add headers
			for(int i = 0; i< num_col; i++) {
				fileWriter.append(headers[i]);
				if(i != num_col-1)
					fileWriter.append(COMMA);
			}
			fileWriter.append(NEW_LINE);
			//add content(-1 because without header)
			for(int i = 0; i<num_row;i++) {
				for(int j=0;j<num_col;j++) {
					try {
						String row_ele = (String)(table.getCol(headers[j]).getData())[i];
						fileWriter.append(row_ele);
						System.out.println(row_ele);
					}catch(ClassCastException e) {
						Number row_ele = (Number)(table.getCol(headers[j]).getData())[i];
						fileWriter.append(row_ele.toString());
						System.out.println(row_ele);
					}
					if(j != num_col-1)
						fileWriter.append(COMMA);
				}
				fileWriter.append(NEW_LINE);
			}
			System.out.println("CSV file was created in "+ file.getAbsolutePath());
			fileWriter.flush();
			fileWriter.close();
			return true;

		
	}

	/**
	 * Save the project into .3111 extension
	 * @param file
	 * - Save this project to .3111 file
	 * @return file to be saved
	 */
	public File saveProject(File file)throws IOException {
		if(file == null)
			throw new IOException();
			DataManagement projectObj = null;
			System.out.println("Copying DataObject");
			projectObj = DataManagement.getInstance();
			FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(projectObj);
			oos.flush();
			oos.close();
			System.out.println("Successfully saved in "+ file.getAbsolutePath());
			return file;
	}

	/**
	 * Load the project from the file
	 * @param file
	 * - Load .3111 file to project
	 * @return DataManagement Object loaded from the project file
	 */
	public DataManagement loadProject(File file)throws IOException,  ClassNotFoundException{
			if(file ==null)
				return null;
			DataManagement load_object;
			System.out.println("Loading DataObject");
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			load_object = (DataManagement) ois.readObject();
			System.out.println("Copying DataObject");
			table_array = load_object.table_array;
//			chart_array = load_object.chart_array;
			num_chart = load_object.num_chart;
			num_table = load_object.num_table;
			table_name = load_object.table_name;
			ois.close();
			System.out.println("Object is already loaded");
			return load_object;

	}
	
	/**
	 * Create Data Table from the data list
	 * @param list
	 * - List of data from the CSV
	 * @param num_row
	 * - number of row of csv(not including header)
	 * @param num_col
	 * - number of column of csv
	 * @return The datatable created from the data list
	 * @throws DataTableException
	 * - Fail to create columns of table
	 */
	public DataTable createDataTable(List<String> list, int num_row, int num_col) throws DataTableException {
			//make column
			DataTable t = new DataTable();
			if(num_row > 0) {
				List<Object> columns = new ArrayList<Object>();
				//prepare columns
				boolean[] isNum = isColumnNum(list, num_col);
				for(int i = 0; i<isNum.length;i++) {
					if(isNum[i] == false)
						columns.add(new String[num_row]);
					else
						columns.add(new Number[num_row]);
				}
				
				//save problematic columns;
				boolean[] problematic_col = new boolean[num_col];
				int num_problem = 0;
				//add items into the columns
				for(int j = num_col; j < list.size(); j++) {
					//idx in column
					int idx = j/num_col - 1;
//					System.out.println(list.size());
					if(isNum[j%num_col] == false) {
						//null case
							if(list.get(j) != null)
								((String[]) columns.get(j%isNum.length))[idx] = list.get(j);
							else
								((String[]) columns.get(j%isNum.length))[idx] = EMPTY_STRING;	
					}else{
//						System.out.println(list.get(j)+" is num");
						try {
							((Number[]) columns.get(j%isNum.length))[idx] = Float.parseFloat(list.get(j));
						}catch(Exception e) {
							//null number for missing data handling
							((Number[]) columns.get(j%isNum.length))[idx] = null;
							problematic_col[j%isNum.length] = true;
							num_problem++;
//							System.out.println("from col"+j%isNum.length);
						}
					}
				}
				System.out.println("num_problem: "+num_problem);
				if(num_problem>0) {
						columns = UIController.handleMissingData(columns, problematic_col);
						System.out.println("Finished Handling");	
				}
				//create column
				for(int i=0; i <columns.size(); i++) {
					DataColumn col = null;
					if(isNum[i])
						col = new DataColumn(DataType.TYPE_NUMBER, (Number[])columns.get(i));
					else
						col = new DataColumn(DataType.TYPE_STRING, (String[])columns.get(i));
					t.addCol(list.get(i), col);
				}	
			}else {
				for(int i=0; i <num_col; i++) {
						t.addCol(list.get(i), new DataColumn(DataType.TYPE_OBJECT, null));
				}
				
			}
			
			management_instance.table_array.add(t);
			return t;
		
		
	}
	
	/**
	 * To get the columns that are number
	 * @param list
	 * - list of data from the csv
	 * @param numCol
	 * - number of columns of csv
	 * @return array of boolean(number is true)
	 */
	public boolean[] isColumnNum(List<String> list, int numCol) {
		boolean[] isNum = new boolean[numCol];
		Arrays.fill(isNum, true);
		for(int j = numCol; j < list.size(); j++) {
			if(list.get(j) != null) {
				try{
					Float.parseFloat(list.get(j));
				}catch (NumberFormatException e){
					isNum[j%numCol] = false;
					System.out.println(j+": "+list.get(j)+" is str");
				}
			}
		}
		return isNum;

	}
	
	/**
	 * Get the array of table name
	 * @return List of table name
	 */
	public List<String> getTableName() {
		return table_name;
	}

	/**
	 * Get the array of data table
	 * @return  the array of data table
	 */
	public List<DataTable> getDataTables(){
		return table_array;
	}
	
	/**
	 * Get the data table from the index n
	 * @param n
	 * - The index position from the data list
	 * @return DataTable from the index n
	 */
	public DataTable getDataTableByIndex(int n){
		return table_array.get(n);
	}
	
	/**
	 * 	Get the number of tables
	 * @return number of tables
	 */
	public int getNumTable() {
		return num_table;
	}

	
//	/**
//	 * Get number of charts
//	 * @return number of charts
//	 */
//	public int getNumChart() {
//		return num_chart;
//	}

}
