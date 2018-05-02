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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.comp3111.Main;
import java.lang.Math;


public class Chart implements Serializable{
	
	public static String[] chartTypes = {
			"Line Chart",
			"Pie Chart",
			"Animation Line Chart"
			};
	
	public static String[][]chart_labels = {
			{"X-Axit","Y-Axit"},
			{"Lebel","Value"},
			{"X-Axit","Y-Axit"}
			};
	
	public static String[][]chart_col_types = {
			{DataType.TYPE_NUMBER , DataType.TYPE_NUMBER },
			{ DataType.TYPE_STRING ,DataType.TYPE_NUMBER},
			{ DataType.TYPE_NUMBER ,DataType.TYPE_NUMBER}
			};
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Chart chart_instance = null;
	private static HBox chart_container =  new HBox(20);
	
	//Line Chart
	private NumberAxis xAxis = new NumberAxis();
	private NumberAxis yAxis = new NumberAxis();
	private LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

	
	//Pie Chart
	private PieChart pieChart = new PieChart();
	ObservableList<PieChart.Data> pieChartData;
	
	//Animated Line Chart
	private NumberAxis ani_xAxis = new NumberAxis();
	private NumberAxis ani_yAxis = new NumberAxis();
	private LineChart<Number, Number> ani_lineChart = new LineChart<Number, Number>(ani_xAxis, ani_yAxis);
	
	public static Chart getInstance()
	{
		if (chart_instance == null)
			chart_instance = new Chart();
		return chart_instance;
	}
	
	//constructor
	private Chart() {
		XYChart.Series series = new XYChart.Series();
		XYChart.Series series1 = new XYChart.Series();
		lineChart.getData().add(series);
		
		ani_lineChart.getData().add(series1);
		chart_container.setAlignment(Pos.CENTER);
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
	
	public LineChart ani_lineChart() {
		return ani_lineChart;
	}
	
	public static HBox container() {
		return chart_container;
	}
	
	public void setVisble(int chart_type, boolean isVisible ) {
		switch(chart_type) {
        case 0 :
        	chart_container.getChildren().clear();
        	chart_container.getChildren().addAll(lineChart);
           break;
        case 1 :
        	chart_container.getChildren().clear();
        	chart_container.getChildren().addAll(pieChart);
           break;
        case 2 :
        	chart_container.getChildren().clear();
        	chart_container.getChildren().addAll(ani_lineChart);
        	break;
        default :
           System.out.println("Invalid chart_type");
     }
	}
	
	public int getChartTypeIndex(String type) {
		int x = 0; 
		for (String chartType : chartTypes) {
			if (chartType == type ) return x;
			x++;
		}
		return -1;
	}
	
	public void update_chart (int chart_type, String tittle ,DataTable table,  String col1Name ,String col2Name) {
		switch(chart_type) {
        case 0 :
        	lineChart_update( tittle , table,   col1Name , col2Name);
           break;
        case 1 :
        	pieChart_update( tittle , table,   col1Name , col2Name);
           break;
        case 2 :
        	ani_lineChart_update( tittle , table,   col1Name , col2Name);
        	break;
        default :
           System.out.println("Invalid chart_type");
		}
		
		setVisble(chart_type, true );
	}
	
	public void lineChart_update (String tittle ,DataTable table,  String col1Name ,String col2Name) {
		
		System.out.println("line chart update" + tittle + table + col1Name + col2Name);
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
	
	public void ani_lineChart_update (String tittle ,DataTable table,  String col1Name ,String col2Name) {
		
		ani_xAxis.setLabel(col1Name);
		ani_yAxis.setLabel(col2Name);
		ani_lineChart.setTitle(tittle+"(animation)");
		
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
			ani_lineChart.getData().clear();

			// add the new series as the only one series for this line chart
			ani_lineChart.getData().add(series);
		}
	}
	
	public void pieChart_update (String tittle ,DataTable table,  String col1Name ,String col2Name) {
		
		pieChart.setTitle(tittle);
		
		// Get 2 columns
		DataColumn xCol = table.getCol(col1Name);
		DataColumn yCol = table.getCol(col2Name);
		
		// Ensure both columns exist and the type is number
		if (xCol != null && yCol != null && xCol.getTypeName().equals(DataType.TYPE_STRING)
				&& yCol.getTypeName().equals(DataType.TYPE_NUMBER)) {
				
			// defining a series
			ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
			
			// populating the series with data
			// As we have checked the type, it is safe to downcast to Number[]
			String[] xValues = (String[]) xCol.getData();
			Number[] yValues = (Number[]) yCol.getData();

			// In DataTable structure, both length must be the same
			int len = xValues.length;

			for (int i = 0; i < len; i++) {
				if (xValues[i] != null && yValues[i] != null) {
					
					int y = Math.round((float) yValues[i]);
					pieChartData.add(new PieChart.Data(xValues[i], y ));
					//System.out.println("X: " + xValues[i]+ "Y: " + y  );
					
				}
			}
			// add the new series as the only one series for this line chart
			pieChart.setData(pieChartData);
		}
		
	}
}