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
//		chart_array = null;
		table_array = new ArrayList<DataTable>();
//		chart_array = new ArrayList<Chart>();
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
	 * @return Name of the dataset
	 * @throws DataTableException
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
		}
		return null;
	}

	/**
	 * Export the table to the csv to the directory of file
	 * @param table
	 * @param file
	 * @return status of exporting
	 */
	public boolean exportTableToCSV(DataTable table, File file) {
		//write to csv
	
		int num_row = table.getNumRow();
		int num_col = table.getNumCol();
		Set<String> keys = table.getKeys();
		String[] headers = keys.toArray(new String[keys.size()]);
//		System.out.println("Export CSV "+file.getAbsolutePath()+" with rows = "+num_row+"columns = "+num_col);
		FileWriter fileWriter = null;
		try {
			if(file == null)
				throw new IOException();
			fileWriter = new FileWriter(file);
			//add headers
			for(int i = 0; i< num_col; i++) {
				fileWriter.append(headers[i]);
				if(i != num_col-1)
					fileWriter.append(COMMA);
			}
			fileWriter.append(NEW_LINE);
			//add content(-1 because without header)
			for(int i = 0; i<num_row -1;i++) {
				for(int j=0;j<num_col;j++) {
					try {
						String row_ele = (String)(table.getCol(headers[j]).getData())[i];
						fileWriter.append(row_ele);
					}catch(ClassCastException e) {
						Number row_ele = (Number)(table.getCol(headers[j]).getData())[i];
						fileWriter.append(row_ele.toString());
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
		} catch(IOException e) {
			System.out.println("Error in exporting");
			e.printStackTrace();
			return false;
		} 
		
	}

	/**
	 * Save the project into .3111 extension
	 * @param file
	 * @return file to be saved
	 */
	public File saveProject(File file) {
		try {
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
		} catch (IOException e) {
			System.out.println("Error while Saving");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Load the project from the file
	 * @param file
	 * @return DataManagement Object loaded from the project file
	 */
	public DataManagement loadProject(File file) {
		try {
			DataManagement load_object;
			if(file == null)
				throw new IOException();
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
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error while loading Project");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Create Data Table from the data list
	 * @param list
	 * @param num_row
	 * @param num_col
	 * @return The datatable created from the data list
	 * @throws DataTableException
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
						UIController.handleMissingData(columns, problematic_col);
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
	 * @param numCol
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
				}
			}
		}
		return isNum;

	}
	
	/**
	 * Add DataTable to the array
	 * @return name of newly added table
	 */
	public String addTable() {
		num_table++;
		String name = "dataset"+num_table;
		table_name.add(name);
		System.out.println("Finished creating Table");
		return name;
	}

	/**
	 * Remove DataTable by index in the array
	 * @param DataTable index to be removed
	 * @return
	 */
	public void removeTable(int x) {
		num_table--;
		table_name.remove(x);
		table_array.remove(x);
		for(int i = 0; i < table_name.size(); i++) {
			String n = "dataset"+(i+1);
			table_name.set(i, n);
		}
		System.out.println("Finished removing Table");
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
	 * @return DataTable from the index n
	 */
	public DataTable getDataTableByIndex(int n){
		return table_array.get(n);
	}
	
//	/**
//	 * 	Get the array of chart
//	 * @return array of chart
//	 */
//	public List<Chart> getChartArray() {
//		return chart_array;
//	}
	
	/**
	 * 	Get the number of tables
	 * @return number of tables
	 */
	public int getNumTable() {
		return num_table;
	}

	
	/**
	 * Get number of charts
	 * @return number of charts
	 */
	public int getNumChart() {
		return num_chart;
	}

}
