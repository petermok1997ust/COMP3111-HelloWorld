package core.comp3111;

import java.security.SecureRandom;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
		colName = new String[selectedTable.getNumCol()+1];
	}
	
	public String[][] colToRow() {
	        DataColumn[] columnList = new DataColumn[selectedTable.getNumCol()];
	        DataColumn dc;
	        rowList = new String[selectedTable.getNumRow()][selectedTable.getNumCol()+1];
	        String[] tmpStrCol = new String[selectedTable.getNumRow()];
	        
	        //set column Name, number type column name, columnList
	        colName[0] = "Index";
	        for(int i = 0; i<selectedTable.getNumCol(); i++) {
				String a = selectedTable.getKeys().toArray()[i].toString();
				colName[i+1] = a;
		        columnList[i] = selectedTable.getCol(a);
		        if(columnList[i].getTypeName().equals("java.lang.Number")) numColName.add(a);
			}
			//adding index
			for(int i = 0; i < selectedTable.getNumRow(); i++)
					rowList[i][0] = String.valueOf(i+1);
			//saving row and printing them out immediately
			for(int i = 0; i < selectedTable.getNumRow(); i++) {
				for(int j = 0; j < selectedTable.getNumCol(); j++) {
		        	dc = columnList[j];
		        	if(dc.getTypeName().equals("java.lang.Number")) {
		        		tmpStrCol = Arrays.toString(dc.getData()).split("[\\[\\]]")[1].split(", ");
		        	}
		        	else tmpStrCol = (String[])dc.getData();
					rowList[i][j+1] = tmpStrCol[i];
		        }
			}
//			//printing saving row
//			for(int i = 0; i < rowList.length; i++) {
//				System.out.print(rowList[i][0]);
//				System.out.print(rowList[i][1]);
//				System.out.println();
//			}
			return rowList;
		}
	
	//Keep the row if row value in <cSelected> column <comparison> v 
	public String[][] filterData(String cSelected, String comparison, double v){
		ArrayList<ArrayList<String>> filteredRowList = new ArrayList<ArrayList<String>>();
		switch(comparison) {
			case "<":
				for(int i = 0; i < rowList.length; i++)
					if(Double.parseDouble(rowList[i][Arrays.asList(colName).indexOf(cSelected)]) < v)
						filteredRowList.add(new ArrayList<>(Arrays.asList(rowList[i])));
				break;
			case "<=":
				for(int i = 0; i < rowList.length; i++)
					if(Double.parseDouble(rowList[i][Arrays.asList(colName).indexOf(cSelected)]) <= v)
						filteredRowList.add(new ArrayList<>(Arrays.asList(rowList[i])));
				break;
			case "==":
				for(int i = 0; i < rowList.length; i++)
					if(Double.parseDouble(rowList[i][Arrays.asList(colName).indexOf(cSelected)]) == v)
						filteredRowList.add(new ArrayList<String>(Arrays.asList(rowList[i])));
				break;
			case "!=":
				for(int i = 0; i < rowList.length; i++)
					if(Double.parseDouble(rowList[i][Arrays.asList(colName).indexOf(cSelected)]) != v)
						filteredRowList.add(new ArrayList<>(Arrays.asList(rowList[i])));
				break;
			case ">=":
				for(int i = 0; i < rowList.length; i++)
					if(Double.parseDouble(rowList[i][Arrays.asList(colName).indexOf(cSelected)]) >= v)
						filteredRowList.add(new ArrayList<>(Arrays.asList(rowList[i])));
				break;
			case ">":
				for(int i = 0; i < rowList.length; i++)
					if(Double.parseDouble(rowList[i][Arrays.asList(colName).indexOf(cSelected)]) > v)
						filteredRowList.add(new ArrayList<String>(Arrays.asList(rowList[i])));
					break;
		}
		//changing index
		for(int i = 0; i < filteredRowList.size(); i++)
				filteredRowList.get(i).set(0, String.valueOf(i+1));

		String[][] frl = new String[filteredRowList.size()][colName.length];
		for(int i = 0; i < filteredRowList.size(); i++) {
			String [] tmplist = new String[filteredRowList.size()];
			tmplist = filteredRowList.get(i).toArray(tmplist);
			frl[i] = tmplist;
		}
		return frl;
	}
	
	public String[][] splitData(double percentage){
		int p = (int) Math.round(percentage);
		int newRLength = (int) (rowList.length * p/100);
		ArrayList<Integer> rowIndexList = new ArrayList<Integer>();
		SecureRandom random = new SecureRandom();
		for(int i = 0; i<newRLength; i++) {
			int row = random.nextInt(rowList.length);
			if(!rowIndexList.contains(row)) {
				rowIndexList.add(row);
			}
			else i--;
		}
		Collections.sort(rowIndexList);
		
		//printing rowIndexList
//		System.out.print("row index list: ");
//		for(int i = 0; i < rowIndexList.size(); i++) {
//			System.out.print(rowIndexList.get(i)+1);
//		}
//		System.out.println();
		
		ArrayList<ArrayList<String>> firstRowL = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> secondRowL = new ArrayList<ArrayList<String>>();

		for(int i = 0; i<rowIndexList.size(); i++) {
			ArrayList<String> z = new ArrayList<String>(Arrays.asList(rowList[rowIndexList.get(i)]));
			firstRowL.add(z);
		}
//		System.out.println("first table:");
//		for(int i = 0; i < firstRowL.size(); i++) {
//			for(int j = 0; j < firstRowL.get(i).size(); j++) {
//				System.out.print(firstRowL.get(i).get(j) + " ");
//			}
//			System.out.println();
//		}

		for(int i = 0; i < rowList.length; i++) {
			if(rowIndexList.contains(i)) continue;
			ArrayList<String> z = new ArrayList<String>(Arrays.asList(rowList[i]));
			secondRowL.add(z);
		}
//		System.out.println("\nsecond table:");
//		for(int i = 0; i < secondRowL.size(); i++) {
//			for(int j = 0; j < secondRowL.get(i).size(); j++) {
//				System.out.print(secondRowL.get(i).get(j) + " ");
//			}
//			System.out.println();
//		}
//		System.out.println();
		
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
	private ArrayList<ArrayList<ArrayList<String>>> splitedT;
	private String sCol, sComparison; 
	private Double sValue, sPercentage;
	private String[] colName;
	private ArrayList<String> numColName;
	private String[][] rowList;
//	private String[] splitedCol, splitedComparison; 
//	private Double[] splitedValue, splitedPercentage;
	
}
