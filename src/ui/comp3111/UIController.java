package ui.comp3111;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import core.comp3111.DataColumn;
import core.comp3111.DataManagement;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class UIController {
	private static DataManagement dataManagementInstance = DataManagement.getInstance();
	private static final String EXT_3111 = "*.comp3111";
	private static final String EXT_NAME_3111 = "COMP3111 file(.comp3111)";
	private static final String EXT_CSV = "*.csv";
	private static final String EXT_NAME_CSV = "CSV file(.csv)";
	public static boolean started = false;
	
	
	/**
	 * 
	 * @param extName
	 * - the typename of the extension of a folder e.g. (.csv)
	 * @param ext
	 * - the extension type
	 * @param isSave
	 * - True means to save a file into the directory, false means to open a file from the file chooser
	 * @return
	 * - The file or directory selected from the file chooser
	 */
	public static File openFileChooser(String extName, String ext, boolean isSave) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter(extName,ext));
		File selectedFile = null;
		if(isSave)
			selectedFile = fileChooser.showSaveDialog(null);
		else
			selectedFile = fileChooser.showOpenDialog(null);
		return selectedFile;
	}
	
	/**
	 * The handling when the InitImport button is clicked in UI
	 */
	public static void onClickInitImportBtn(){
		File fileObtained = openFileChooser(EXT_NAME_CSV, EXT_CSV, false);
		if(fileObtained != null) {
			String name = null;
			try {
				name = dataManagementInstance.importCSV(fileObtained);
				if(name != null)
					JOptionPane.showMessageDialog(null, "Data is imported successfully");
				else
					JOptionPane.showMessageDialog(null, "there may be inconsistent number of column or empty header ,so data is not imported");
			} catch (DataTableException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "There is column duplication, data cannot be imported");
			}
			if(name != null)
				Main.setDataItem(name);
		}
			
	}
	/**
	 * The handling when the InitExport button is clicked in UI
	 * @throws IOException -when the file is unable to export(e.g. directory does not exist)
	 */
	public static void onClickInitExportBtn() throws IOException{
		int idx = Main.getSelectedDataIdx();
		if(idx>=0){
			File directoryObtained = openFileChooser(EXT_NAME_CSV, EXT_CSV, true);
			boolean isExported = false;
			if(directoryObtained != null)
				isExported = dataManagementInstance.exportTableToCSV(dataManagementInstance.getDataTables().get(idx), directoryObtained);
			JOptionPane.showMessageDialog(null, "Data is exported with result:"+isExported+" to "+directoryObtained.getAbsolutePath());
		}else
			JOptionPane.showMessageDialog(null, "Please first select a dataset");
	}
	
	/**
	 * The handling when the InitLoad button is clicked in UI
	 * @throws ClassNotFoundException -when the .comp3111 file is not from the same application
	 * @throws IOException -when the file is not loaded into the application (e.g. non-exist directory)
	 */
	public static void onClickInitLoadBtn() throws ClassNotFoundException, IOException{
		File fileObtained = openFileChooser(EXT_NAME_3111, EXT_3111, false);
		if(fileObtained != null) {
			DataManagement load_object = dataManagementInstance.loadProject(fileObtained);
			if(load_object != null)
				Main.setDataObj(load_object);
			JOptionPane.showMessageDialog(null, "Data is loaded into the project");
		}
		else JOptionPane.showMessageDialog(null, "the file is null");
			
	}
	
//	
//	public static void printDT(DataTable s) {
//		DataColumn[] columnList = new DataColumn[s.getNumCol()];
//		String[] tmpStrCol = new String[s.getNumRow()];
//		String[][] rowList = new String[s.getNumRow()][s.getNumCol()];
//		for(int i = 0; i<s.getNumCol(); i++) {
//			String a = s.getKeys().toArray()[i].toString();
//	        columnList[i] = s.getCol(a);
//		}
//		for(int i = 0; i < s.getNumRow(); i++) {
//			for(int j = 0; j < s.getNumCol(); j++) {
//	        	if(columnList[j].getTypeName().equals("java.lang.Number")) {
//	        		tmpStrCol = Arrays.toString(columnList[j].getData()).split("[\\[\\]]")[1].split(", ");
//	        	}
//	        	else tmpStrCol = (String[])columnList[j].getData();
//	        	rowList[i][j] = tmpStrCol[i];
//			}
//		}
//		for(int i = 0; i < s.getNumRow(); i++) {
//			for(int j = 0; j < s.getNumCol(); j++) {
//	        	System.out.print(rowList[i][j]);
//			}
//			System.out.println(" "+i);
//		}
//
//	}
	/**
	 * The handling when the InitSave button is clicked in UI
	 * @throws IOException
	 * - when the file save failed
	 */
	public static void onClickInitSaveBtn() throws IOException{
		File fileObtained = openFileChooser(EXT_NAME_3111, EXT_3111, true);
		if(fileObtained != null)
			dataManagementInstance.saveProject(fileObtained);
		JOptionPane.showMessageDialog(null, "Data is saved to "+fileObtained.getAbsolutePath());
	}
	
	/**
	 * Handling of columns with missing data and fill them with zero, mean or median
	 * @param numbers
	 * - The column which has missing data
	 */
	public static void handleNumColumnByCase(Number[] numbers) {
		
		String handleType = Main.string_zero;
		System.out.println("started: "+ started);
		if(started)
			handleType = Main.getSelectedNumHandle();
		
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

	
	/**
	 * Fill the missing column with a input number
	 * @param numbers
	 * - The column which has missing data
	 * @param num
	 * - the number to fill into the missing place
	 */
	public static void fillAllMissingWith(Number[] numbers, double num) {
		for(int i =0; i<numbers.length;i++) {
			if(numbers[i] == null)
				numbers[i] = num;
		}
	}

	/**
	 * To handle the missing data of Columns
	 * @param columns
	 * - List of all columns
	 * @param problematic_col
	 * - Indication of the columns which has missing data
	 * @return
	 * - The List of columns with missing data handling
	 */
	public static List<Object> handleMissingData(List<Object> columns, boolean[] problematic_col) {
//		System.out.println("Handle Missing Number with "+ Main.getSelectedNumHandle());
		for(int i=0; i<problematic_col.length;i++) {
			if(problematic_col[i]) {
				handleNumColumnByCase((Number[])columns.get(i));
			}
		}
		return columns;
	}
	
}
