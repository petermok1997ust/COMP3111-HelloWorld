package core.comp3111;

import java.io.File;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ui.comp3111.Main;

public class UIController {
	private static DataManagement dataManagementInstance = DataManagement.getInstance();
	private static final String EXT_3111 = "*.comp3111";
	private static final String EXT_NAME_3111 = "COMP3111 file(.comp3111)";
	private static final String EXT_CSV = "*.csv";
	private static final String EXT_NAME_CSV = "CSV file(.csv)";
	
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
			dataManagementInstance.importCSV(fileObtained);
		}
			
	}
	
	public static void onClickInitExportBtn(){
		File directoryObtained = openFileChooser(EXT_NAME_CSV, EXT_CSV, true);
		int idx = Main.getSelectedDataIdx();
		if(directoryObtained != null && idx>=0)
			dataManagementInstance.exportTableToCSV(dataManagementInstance.getDataTables().get(idx), directoryObtained);
		else
			System.out.println("Nothing is exported");
	}
	
	public static void onClickInitLoadBtn(){
		File fileObtained = openFileChooser(EXT_NAME_3111, EXT_3111, false);
		if(fileObtained != null) {
			dataManagementInstance.loadProject(fileObtained);
		}
			
	}
	
	public static void onClickInitSaveBtn(){
		File fileObtained = openFileChooser(EXT_NAME_3111, EXT_3111, true);
		if(fileObtained != null)
			dataManagementInstance.saveProject(fileObtained);
	}
}
