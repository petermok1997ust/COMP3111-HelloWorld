package ui.comp3111;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	
	
	public static void onClickInitImportBtn(){
		File fileObtained = openFileChooser(EXT_NAME_CSV, EXT_CSV, false);
		if(fileObtained != null) {
			String name = null;
			try {
				name = dataManagementInstance.importCSV(fileObtained);
			} catch (DataTableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(name != null)
				Main.setDataItem(name);
		}
			
	}
	
	public static void onClickInitExportBtn(){
		int idx = Main.getSelectedDataIdx();
		if(idx>=0){
			File directoryObtained = openFileChooser(EXT_NAME_CSV, EXT_CSV, true);
			if(directoryObtained != null)
				dataManagementInstance.exportTableToCSV(dataManagementInstance.getDataTables().get(idx), directoryObtained);
		}else
			System.out.println("Nothing is selected");
	}
	
	public static void onClickInitLoadBtn(){
		File fileObtained = openFileChooser(EXT_NAME_3111, EXT_3111, false);
		if(fileObtained != null) {
			DataManagement load_object = dataManagementInstance.loadProject(fileObtained);
			if(load_object != null)
				Main.setDataObj(load_object);
		}
			
	}
	public static void printDT(DataTable s) {
		DataColumn[] columnList = new DataColumn[s.getNumCol()];
		String[] tmpStrCol = new String[s.getNumRow()];
		String[][] rowList = new String[s.getNumRow()][s.getNumCol()];
		for(int i = 0; i<s.getNumCol(); i++) {
			String a = s.getKeys().toArray()[i].toString();
	        columnList[i] = s.getCol(a);
		}
		for(int i = 0; i < s.getNumRow(); i++) {
			for(int j = 0; j < s.getNumCol(); j++) {
	        	if(columnList[j].getTypeName().equals("java.lang.Number")) {
	        		tmpStrCol = Arrays.toString(columnList[j].getData()).split("[\\[\\]]")[1].split(", ");
	        	}
	        	else tmpStrCol = (String[])columnList[j].getData();
	        	rowList[i][j] = tmpStrCol[i];
			}
		}
		for(int i = 0; i < s.getNumRow(); i++) {
			for(int j = 0; j < s.getNumCol(); j++) {
	        	System.out.print(rowList[i][j]);
			}
			System.out.println(" "+i);
		}

	}
	public static void onClickInitSaveBtn(){
		File fileObtained = openFileChooser(EXT_NAME_3111, EXT_3111, true);
		if(fileObtained != null)
			dataManagementInstance.saveProject(fileObtained);
	}
		
	public static void handleNumColumnByCase(Number[] numbers) {
		
		String handleType = null;
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

	public static void fillAllMissingWith(Number[] numbers, double num) {
		for(int i =0; i<numbers.length;i++) {
			if(numbers[i] == null)
				numbers[i] = num;
		}
	}

	public static void handleMissingData(List<Object> columns, boolean[] problematic_col) {
//		System.out.println("Handle Missing Number with "+ Main.getSelectedNumHandle());
		for(int i=0; i<problematic_col.length;i++) {
			if(problematic_col[i]) {
				if(started)
					handleNumColumnByCase((Number[])columns.get(i));
			}
		}
	}
	
}
