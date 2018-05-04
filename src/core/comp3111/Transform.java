package core.comp3111;

import java.security.SecureRandom;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataType;
import core.comp3111.SampleDataGenerator;


public class Transform implements Cloneable {
	
	/**
	 * Construct - Create an Transform object by DataTable
	 * one Transform object is responsible for one DataTable
	 * @param selectedTable
	 *            - DataTable for making Transformation
	 */
	public Transform(DataTable selectedTable){
		this.selectedTable = selectedTable;
		numColName = new ArrayList<String>();
		colName = new String[selectedTable.getNumCol()+1];
		colToRow();
	}

	/**
	 * clone the Transform object
	 * @return the cloned Transform object
	 * @throws CloneNotSupportedException
	 * 					- throws exception if there are elements that clone does not support
	 */
	public Transform clone() throws CloneNotSupportedException {
		Transform clonedObj = (Transform) super.clone();
		clonedObj.splitedOne = (ArrayList<ArrayList<String>>) this.splitedOne.clone();
		clonedObj.splitedTwo = (ArrayList<ArrayList<String>>) this.splitedTwo.clone();
		clonedObj.colName = this.colName.clone();
        clonedObj.numColName = (ArrayList<String>) this.numColName.clone();
        clonedObj.rowList = this.rowList.clone();
        clonedObj.filteredList = this.filteredList.clone();
        return clonedObj;
    }
		
	/**
	 * Convert selected DataTable to rowList.
	 * Used only in constructor.
	 * @return the rowList, 2D array corresponding to the DataTable
	 */
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
			filteredList = rowList.clone();
			return rowList;
		}
	
	/**
	 * Filter the data.
	 * Keep the row if row value in [cSelected] column [comparison] v.
	 * @param cSelected - column which the filter based on
	 * @param comparison - comparison operator
	 * @param v - value for comparison
	 * @return the filtered list
	 */
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
			String [] tmplist = new String[filteredRowList.get(i).size()];
			tmplist = filteredRowList.get(i).toArray(tmplist);
			frl[i] = tmplist;
		}
		filteredList = frl.clone();
		return frl;
	}
	
	/**
	 * Split the data into 2 sets with percentage:100-percentage.
	 * If there is case such as only one column, the data cannot be split and return false
	 * @param percentage  - the percentage for splitting
	 * @return true if the data set can be split corresponding to the percentage
	 */
	public boolean splitData(double percentage){
		int p = (int) Math.round(percentage);
		int newRLength = (int) (filteredList.length * p/100);
		if(newRLength == 0) return false;
		ArrayList<Integer> rowIndexList = new ArrayList<Integer>();
		SecureRandom random = new SecureRandom();
		for(int i = 0; i<newRLength; i++) {
			int row = random.nextInt(filteredList.length);
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
			ArrayList<String> z = new ArrayList<String>(Arrays.asList(filteredList[rowIndexList.get(i)]));
			firstRowL.add(z);
		}
		for(int i = 0; i < firstRowL.size(); i++)
			firstRowL.get(i).set(0, String.valueOf(i+1));
		splitedOne = firstRowL;
//		System.out.println("first table:");
//		for(int i = 0; i < firstRowL.size(); i++) {
//			for(int j = 0; j < firstRowL.get(i).size(); j++) {
//				System.out.print(firstRowL.get(i).get(j) + " ");
//			}
//			System.out.println();
//		}

		for(int i = 0; i < filteredList.length; i++) {
			if(rowIndexList.contains(i)) continue;
			ArrayList<String> z = new ArrayList<String>(Arrays.asList(filteredList[i]));
			secondRowL.add(z);
		}
		for(int i = 0; i < secondRowL.size(); i++)
			secondRowL.get(i).set(0, String.valueOf(i+1));
		splitedTwo = secondRowL;
//		System.out.println("\nsecond table:");
//		for(int i = 0; i < secondRowL.size(); i++) {
//			for(int j = 0; j < secondRowL.get(i).size(); j++) {
//				System.out.print(secondRowL.get(i).get(j) + " ");
//			}
//			System.out.println();
//		}
//		System.out.println();
		
		return true;
	}
	
	/**
	 * Get the list of all column names which column type is Number
	 * @return list of all column names which column type is Number
	 */
	public ArrayList<String> getNumColName() {
		return numColName;
	}
	
	/**
	 * Get the list of all column names
	 * @return list of all column names
	 */
	public String[] getColName() {
		return colName;
	}
	
	/**
	 * Get the 2d list of selected data from init screen
	 * It will not be changed even if filtering has applied
	 * @return 2d list of original data
	 */	
	public String[][] getRowList(){
		return rowList;
	}
	
	/**
	 * Get the 2d list of data after applying filter
	 * It will be equal to rowList if filtering has not yet applied
	 * @return 2d list of filtered data
	 */
	public String[][] getFilteredList(){
		return filteredList;
	}
	
	/**
	 * Get the number of row of filteredList
	 * @return number of row of filteredList
	 */
	public int getNumRowOfFilteredList() {
		return filteredList.length;
	}
	
	/**
	 * Get the number of column of filteredList
	 * @return number of column of filteredList
	 */
	public int getNumColOfFilteredList() {
		return filteredList[0].length;
	}
	
	/**
	 * Get the 2D array of 1st split table
	 * @return 2D array of first split table
	 */
	public ArrayList<ArrayList<String>> getFirstSplitedT(){
		return splitedOne;
	}
	
	/**
	 * Get the 2D array of 2nd split table
	 * @return 2D array of second split table
	 */
	public ArrayList<ArrayList<String>> getSecondSplitedT(){
		return splitedTwo;
	}
	
	/**
	 * setting rowList to para rowlist
	 * @param rowlist - 2D ArrayList of data to replace original rowList
	 */
	public void setRowList(ArrayList<ArrayList<String>> rowlist) {
		rowList = new String[rowlist.size()][rowlist.get(0).size()];
		for(int i = 0; i < rowlist.size(); i++) {
			String [] tmplist = new String[rowlist.get(i).size()];
			tmplist = rowlist.get(i).toArray(tmplist);
			rowList[i] = tmplist;
		}
		filteredList = rowList.clone();
	}
	
	/**
	 * convert the 2D filtered list without title to 1D list with title
	 * @return List of table with title
	 */
	public List<String> toListwTitle(){
		List<String> list = new ArrayList<String> ();
		for(int i = 1; i<colName.length;i++)
			list.add(colName[i]);
		for(int i = 0; i<filteredList.length;i++)
			for(int j = 1; j<filteredList[i].length;j++)
				list.add(filteredList[i][j]);
		for(int i = 0; i<list.size();i++)
				System.out.print(list.get(i)+ " ");
		
		return list;
	}
	
	private DataTable selectedTable;
	private ArrayList<ArrayList<String>> splitedOne, splitedTwo;
	private String[] colName;
	private ArrayList<String> numColName;
	private String[][] rowList, filteredList;	
}
