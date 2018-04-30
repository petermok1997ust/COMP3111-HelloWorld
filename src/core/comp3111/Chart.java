package core.comp3111;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.comp3111.Main;

public class Chart implements Serializable{
	
	public static String[] chartTypes = {"Bar Chart","Line Chart"};
	
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
	

	
	//constructor
	private Chart() {
		//setChartItem("hi");
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
	
	public LineChart lineChart(String x,String y, String tittle ) {
		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		xAxis.setLabel(x);
		yAxis.setLabel(y);
		lineChart.setTitle(tittle);
		
		return lineChart;
		
	}
	
}
