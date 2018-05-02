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
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


import ui.comp3111.Main;

public class DataManagement implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -2309027075882808422L;
	private int num_table;
	private int num_chart;
	private List<DataTable> table_array;
	private List<Chart> chart_array;
	private List<String> table_name;
	private static DataManagement management_instance = null;
	private static final String COMMA = ",";
	private static final String NEW_LINE = "\n";
	private static final String EMPTY_STRING = "";


	//constructor
	private DataManagement() {
		num_table = 0;
		num_chart = 0;
		table_array = null;
		chart_array = null;
		table_array = new ArrayList<DataTable>();
		chart_array = new ArrayList<Chart>();
		table_name = new ArrayList<String>();
	}

//	private void setInstance(DataManagement dataManagementObj) {
//		management_instance.num_chart = dataManagementObj.num_chart;
//		management_instance.num_table = dataManagementObj.num_table;
//		//    System.arraycopy( dataManagementObj.table_array, 0, management_instance.table_array, 0, dataManagementObj.table_array.length );
//		//    System.arraycopy( dataManagementObj.chart_array, 0, management_instance.chart_array, 0, dataManagementObj.chart_array.length );
//		management_instance.table_array = dataManagementObj.table_array;
//		management_instance.chart_array = dataManagementObj.chart_array;
//	}

	//singleton getInstance
	public static DataManagement getInstance()
	{
		if (management_instance == null)
			management_instance = new DataManagement();
		return management_instance;
	}

	public void importCSV(File file) {
		System.out.println("Importing CSV "+ file.getAbsolutePath());
		Scanner inputStream;
		List<String> list = new ArrayList<String> ();  
		int num_row = 0;
		int num_col = 0;
		try{
			inputStream = new Scanner(file);
			while(inputStream.hasNextLine()) {
				String line = inputStream.nextLine();
				String[] line_split = line.split(",",-1);
//				for(int i=0; i<line_split.length;i++) {
//					System.out.print(line_split[i]);
//				}
				if(num_row == 0)
					num_col = line_split.length;
				for(int c=0; c<line_split.length; c++) {
					String input = line_split[c].isEmpty()?null:line_split[c];
					System.out.print(input + " ");
					list.add(input);
				}
				num_row++;
				System.out.print(" " + num_row);
				System.out.println();
			}
			inputStream.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			System.out.println("Finished importing CSV with "+ num_row+" rows and "+ num_col+" columns");
		}
		String empty = "";
		if(num_row <= 0 || num_col <=0)
			empty = "_(empty)";
		System.out.println("Creating Table");
		createDataTable(list, num_row-1, num_col);	
		num_table++;
		String name = "dataset"+num_table+empty;
		table_name.add(name);
		Main.setDataItem(name);
		System.out.println("Finished create Table");
	}

	public void exportTableToCSV(DataTable table, File file) {
		//write to csv
		int num_row = table.getNumRow();
		int num_col = table.getNumCol();
		Set<String> keys = table.getKeys();
		String[] headers = keys.toArray(new String[keys.size()]);
		System.out.println("Export CSV "+file.getAbsolutePath()+" with rows = "+num_row+"columns = "+num_col);
		FileWriter fileWriter = null;
		try {
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
		} catch (Exception e) {
			System.out.println("Error in exporting");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing");
				e.printStackTrace();
			}
		}
	}


	public void saveProject(File file) {
		try {
			DataManagement projectObj = null;
			System.out.println("Copying DataObject");
			projectObj = DataManagement.getInstance();
			FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(projectObj);
			oos.flush();
			oos.close();
			System.out.println("Successfully saved in "+ file.getAbsolutePath());
		} catch (Exception e) {
			System.out.println("Error while Saving");
			e.printStackTrace();
		}
	}

	public void loadProject(File file) {
		try {
			DataManagement load_object;
			System.out.println("Loading DataObject");
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			load_object = (DataManagement) ois.readObject();
			System.out.println("Copying DataObject");
			table_array = load_object.table_array;
			chart_array = load_object.chart_array;
			num_chart = load_object.num_chart;
			num_table = load_object.num_table;
			table_name = load_object.table_name;
			ois.close();
			Main.setDataObj(load_object);
			System.out.println("Object is already loaded");
		} catch (Exception e) {
			System.out.println("Error while loading Project");
			e.printStackTrace();
		}
	}

	public DataTable createDataTable(List<String> list, int num_row, int num_col) {
		boolean[] isNum = isColumnNum(list, num_col);
		//make column
		List<Object> columns = new ArrayList<Object>();
		//prepare columns
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
			if(isNum[j%num_col] == false) {
				//null case
				try {
					((String[]) columns.get(j%isNum.length))[idx] = list.get(j);
				}catch(Exception e) {
					//null string
					((String[]) columns.get(j%isNum.length))[idx] = EMPTY_STRING;	
				}
			}else{
				try {
					((Number[]) columns.get(j%isNum.length))[idx] = Float.parseFloat(list.get(j));
				}catch(Exception e) {
					//null number for missing data handling
					((Number[]) columns.get(j%isNum.length))[idx] = null;
					problematic_col[j%isNum.length] = true;
					num_problem++;
				}
			}
		}
		System.out.println("");
		if(num_problem>0) {
		System.out.println("Handle Missing Number with "+ Main.getSelectedNumHandle());
		handleMissingData(columns, problematic_col);
		System.out.println("Finished Handling");
		}
		//create column
		DataTable t = new DataTable();
		for(int i=0; i <columns.size(); i++) {
			DataColumn col = null;
			if(isNum[i])
				col = new DataColumn(DataType.TYPE_NUMBER, (Number[])columns.get(i));
			else
				col = new DataColumn(DataType.TYPE_STRING, (String[])columns.get(i));
			try {
				t.addCol(list.get(i), col);
			} catch (DataTableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		management_instance.table_array.add(t);
		return t;
	}
	
	public boolean[] isColumnNum(List<String> list, int numCol) {
		boolean[] isNum = new boolean[numCol];
		Arrays.fill(isNum, true);
		for(int j = numCol+1; j < list.size(); j++) {
			if(list.get(j) != null) {
				try{
					Float.parseFloat(list.get(j));
//					System.out.println(list.size()+", "+numCol+", "+j);
				}catch (NumberFormatException e){
					isNum[j%numCol] = false;
				}finally {
//					System.out.println(list.get(j)+", "+isNum[j%numCol]);
				}
			}
		}
		return isNum;

	}
	
	public List<String> getTableName() {
		return table_name;
	}

	public List<DataTable> getDataTables(){
		return table_array;
	}
	
	public DataTable getDataTableByIndex(int n){
//		if(table_array == null) return null;
		return table_array.get(n);
	}
	
	public void handleMissingData(List<Object> columns, boolean[] problematic_col) {
		for(int i=0; i<problematic_col.length;i++) {
			if(problematic_col[i]) {
				handleNumColumnByCase((Number[])columns.get(i));
			}
		}
	}
	
	public void handleNumColumnByCase(Number[] numbers) {
		String handleType = Main.getSelectedNumHandle();
		switch(handleType) {
		case Main.string_zero:
			fillAllMissingWith(numbers, 0);
			break;
		case Main.string_mean:
			Float num_of_valid_col = 0f;
			Float total = 0f;
			for(int i=0;i<numbers.length;i++) {
				if(numbers[i] != null) {
					total+=(Float)numbers[i];
					num_of_valid_col++;
				}
			}
			double mean = (double)(total/num_of_valid_col);
			fillAllMissingWith(numbers, mean);
			break;
		case Main.string_median:
			List<Float> list = new ArrayList<Float>();
			for(int i=0;i<numbers.length;i++) {
				if(numbers[i] != null) {
					list.add((Float) numbers[i]);
				}
			}
			Collections.sort(list);
			double median = 0;
			int idx = list.size()/2;
			if(list.size()%2 == 0) {
				median = (double) (list.get(idx - 1) + list.get(idx))/2;
			}else {
				median = (double) list.get((list.size()+1)/2-1);
			}

			fillAllMissingWith(numbers, median);
			break;
		}
	}

	public void fillAllMissingWith(Number[] numbers, double num) {
		for(int i =0; i<numbers.length;i++) {
			if(numbers[i] == null)
				numbers[i] = num;
		}
	}

}
