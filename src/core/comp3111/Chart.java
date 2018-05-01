package core.comp3111;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.comp3111.Main;


public class Chart implements Serializable{
	
	public static String[] chartTypes = {"Line Chart","Pie Chart"};
	
	public static String[][]chart_labels = {{"X-Axit","Y-Axit"},{"Lebel","Value"}};
	
	public static String[][]chart_col_types = {{DataType.TYPE_NUMBER , DataType.TYPE_NUMBER },{ DataType.TYPE_STRING ,DataType.TYPE_NUMBER}};
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Chart management_instance = null;
	
	//Line Chart
	private LineChart<Number, Number> lineChart = null;
	private NumberAxis xAxis = null;
	private NumberAxis yAxis = null;
	
	//Pie Chart
	private PieChart pieChart = null;
	ObservableList<PieChart.Data> pieChartData;
	
	//constructor
	private Chart() {
		//create line chart
		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		XYChart.Series series = new XYChart.Series();
		lineChart.getData().add(series);
		
		//create pie chart
		
		pieChartData = FXCollections.observableArrayList( 
				   new PieChart.Data("Iphone 5S", 13), 
				   new PieChart.Data("Samsung Grand", 25), 
				   new PieChart.Data("MOTO G", 10), 
				   new PieChart.Data("Nokia Lumia", 22)); 
		pieChart = new PieChart(pieChartData);
		
	}
	
	public static Chart getInstance()
	{
		if (management_instance == null)
			management_instance = new Chart();
		return management_instance;
	}
	
	public int getChartTypeIndex(String type) {
		int x = 0; 
		for (String chartType : chartTypes) {
			if (chartType == type ) return x;
			x++;
		}
		return -1;
	}
	
	public String [] getChartType() {
		return chartTypes;
	}
	
	public LineChart lineChart() {
		return lineChart;
	}
	
	public PieChart pieChart() {
		return pieChart;
	}
	
	public void lineChart_update (String tittle ,DataTable table,  String col1Name ,String col2Name) {
		
		xAxis.setLabel(col1Name);
		yAxis.setLabel(col2Name);
		lineChart.setTitle(tittle);
		
		// Get 2 columns
		DataColumn xCol = table.getCol(col1Name);
		DataColumn yCol = table.getCol(col2Name);
		
		// Ensure both columns exist and the type is number
		if (xCol != null && yCol != null && xCol.getTypeName().equals(DataType.TYPE_NUMBER)
				&& yCol.getTypeName().equals(DataType.TYPE_NUMBER)) {
				
			// defining a series
			XYChart.Series series = new XYChart.Series();
			
			series.setName(tittle);

			// populating the series with data
			// As we have checked the type, it is safe to downcast to Number[]
			Number[] xValues = (Number[]) xCol.getData();
			Number[] yValues = (Number[]) yCol.getData();

			// In DataTable structure, both length must be the same
			int len = xValues.length;

			for (int i = 0; i < len; i++) {
				if (xValues[i] != null && yValues[i] != null) {
					series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
					//System.out.println("X: " + xValues[i]+ "Y: " + yValues[i]  );
				}
			}

			// clear all previous series
			lineChart.getData().clear();

			// add the new series as the only one series for this line chart
			lineChart.getData().add(series);
		}
	}
	
	public void pieChart_update (String tittle ,DataTable table,  String col1Name ,String col2Name) {
		
		pieChart.setTitle(tittle);
		
		// Get 2 columns
		DataColumn xCol = table.getCol(col1Name);
		DataColumn yCol = table.getCol(col2Name);
		
//		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
//		         new PieChart.Data("Iphone 5S", 13), 
//		         new PieChart.Data("Samsung Grand", 25), 
//		         new PieChart.Data("MOTO G", 10), 
//		         new PieChart.Data("Nokia Lumia", 22));
		
	}
}
