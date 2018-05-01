package core.comp3111;

import java.util.ArrayList;
import java.util.Arrays;
import core.comp3111.DataColumn;
import core.comp3111.DataManagement;
import core.comp3111.DataTable;
import core.comp3111.DataType;
import core.comp3111.SampleDataGenerator;


public class Transform {
	public Transform(int selectedTableIndex){
		dataManagementInstance = DataManagement.getInstance();
		DataTable selectedTable = dataManagementInstance.getDataTableByIndex(selectedTableIndex);
		this.selectedTable = selectedTable;
		sCol = "NOT YET SELECTED";
		sComparison = "NOT YET SELECTED"; 
		Double sValue = Double.MAX_VALUE;
		sPercentage = Double.MAX_VALUE;
		numColName = new ArrayList<String>();
		colName = new String[selectedTable.getNumCol()];
	}
	
	public String[][] colToRow() {
	        DataColumn[] columnList = new DataColumn[selectedTable.getNumCol()];
	        DataColumn dc;
	        String[] dr = new String[selectedTable.getNumCol()];
	        String[][] rowList = new String[selectedTable.getNumRow()][];
	        String[] tmpStrCol = new String[selectedTable.getNumRow()];
	        
	        //set column Name, number type column name, columnList
			for(int i = 0; i<selectedTable.getNumCol(); i++) {
				String a = selectedTable.getKeys().toArray()[i].toString();
				colName[i] = a;
		        columnList[i] = selectedTable.getCol(a);
		        if(columnList[i].getTypeName().equals("java.lang.Number")) numColName.add(a);
		        System.out.println(numColName.get(0));
			}
			//saving row and printing them out immediately
			for(int i = 0; i < selectedTable.getNumRow(); i++) {
				for(int j = 0; j < selectedTable.getNumCol(); j++) {
		        	dc = columnList[j];
		        	if(dc.getTypeName().equals("java.lang.Number")) {
		        		tmpStrCol = Arrays.toString(dc.getData()).split("[\\[\\]]")[1].split(", ");
		        	}
		        	else tmpStrCol = (String[])dc.getData();
		        	dr[j] = tmpStrCol[i];
		        }
				rowList[i] = dr;
				System.out.print(rowList[i][0]);
				System.out.print(rowList[i][1]);
				System.out.println();
			}
			//printing saving row
			for(int i = 0; i < rowList.length; i++) {
				System.out.print(rowList[i][0]);
				System.out.print(rowList[i][1]);
				System.out.println();
			}
			return rowList;
		}
	public ArrayList<String> getNumColName() {
		return numColName;
	}
	public String[] getColName() {
		return colName;
	}
	private DataManagement dataManagementInstance;
	private DataTable selectedTable;
//	private DataTable[] splitedT;
	private String sCol, sComparison; 
	private Double sValue, sPercentage;
	private String[] colName;
	private ArrayList<String> numColName;
//	private String[] splitedCol, splitedComparison; 
//	private Double[] splitedValue, splitedPercentage;
	
}
