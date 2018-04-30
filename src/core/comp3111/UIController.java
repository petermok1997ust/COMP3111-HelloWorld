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
			dataManagementInstance.loadProject(fileObtained);
		}
			
	}
	
	public static void onClickInitSaveBtn(){
		File fileObtained = openFileChooser(EXT_NAME_3111, EXT_3111, true);
		if(fileObtained != null)
			dataManagementInstance.saveProject(fileObtained);
	}
	
	//Transform scene
	public static void onClickApplyFilterBtn(double v, boolean e){
		System.out.print("apply filter");
		if(!e) System.out.print(v);
		else System.out.println("error");
	}	
	public static void onClickApplySplitBtn(){
		System.out.print("apply split");
	}	
	public static void onClickTransformOKBtn(String choice){
		System.out.print("apply ok ");
		System.out.println(choice);
		
	}	
	
}
