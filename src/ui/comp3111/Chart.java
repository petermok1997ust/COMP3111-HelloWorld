package ui.comp3111;

import java.io.Serializable;
import java.lang.Math;
import java.util.Timer;
import java.util.TimerTask;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataType;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;

/**
 * 
 * Chart is to help displaying difference type of chart to UI
 * 
 * @author lotusfa
 *
 */
public class Chart implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * This array store all chart type label
	 */
	public static String[] chartTypes = {
			"Line Chart",
			"Pie Chart",
			"Animation Line Chart"
			};
	
	/**
	 * This array store the input label for each type of chart
	 */
	public static String[][]chart_labels = {
			{"X-Axis","Y-Axis"},
			{"Label","Value"},
			{"X-Axis","Y-Axis"}
			};
	
	/**
	 * This array store the input data type for each type of chart
	 */
	public static String[][]chart_col_types = {
			{DataType.TYPE_NUMBER , DataType.TYPE_NUMBER },
			{ DataType.TYPE_STRING ,DataType.TYPE_NUMBER},
			{ DataType.TYPE_NUMBER ,DataType.TYPE_NUMBER}
			};

	/**
	 * This is UI component which have a children of chart object. 
	 */
	private static HBox chart_container =  new HBox(20);
	
	/**
	 * To store which chart is displaying
	 */
	private static int show_chart_index;
	
	/**
	 * Variable for plotting line chart
	 */
	private static NumberAxis xAxis = new NumberAxis();
	private static NumberAxis yAxis = new NumberAxis();
	private static LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

	/**
	 * Variable for plotting pie chart
	 */
	private static PieChart pieChart = new PieChart();
	ObservableList<PieChart.Data> pieChartData;
	
	/**
	 * Variable for plotting Animated Line Chart
	 */
	private static NumberAxis ani_xAxis = new NumberAxis();
	private static NumberAxis ani_yAxis = new NumberAxis();
	private final static LineChart<Number, Number> ani_lineChart = new LineChart<Number, Number>(ani_xAxis, ani_yAxis);
	private final static XYChart.Series anim_series = new XYChart.Series();
	private static Timer timer = null;
	private final static long intevalPeriod = 1* 1000; 
	
	/**
	 * Constructor. Create a Chart Object
	 */
	private Chart() {
		lineChart.getData().add(new XYChart.Series());
		ani_lineChart.getData().add(anim_series);
		chart_container.setAlignment(Pos.CENTER);
	}
	
	/**
	 * Get all chart type string
	 * 
	 * @return chartTypes
	 * 		- a array of all chart labels
	 */
	public static String [] getChartType() {
		return chartTypes;
	}
	
	/**
	 * Get the UI container of chart
	 * @return chart_container
	 * 		- UI component which have a children of a chart. 
	 */
	public static HBox container() {
		return chart_container;
	}
	
	/**
	 * To display one chart to UI
	 * @param chart_type
	 * 		- index of chart type
	 */
	public static void show_chart(int chart_type) {
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
	
	/**
	 * Input data to the chart and update it
	 * @param chart_type
	 * 		- index of chart type
	 * @param tittle
	 * 		- tittle of chart
	 * @param table
	 * 		- DataTable of data 
	 * @param col1Name
	 * 		- first column name of data in table
	 * @param col2Name
	 * 		- second column name of data in table
	 */
	public static void update_chart (int chart_type, String tittle ,DataTable table,  String col1Name ,String col2Name) {
		show_chart_index = chart_type;
		switch(chart_type) {
        case 0 :
        	lineChart_update( tittle , table,   col1Name , col2Name);
           break;
        case 1 :
        	pieChart_update( tittle , table,   col1Name , col2Name);
           break;
        case 2 :
        	Chart.ani_lineChart_update( tittle , table,   col1Name , col2Name);
        	break;
        default :
           System.out.println("Invalid chart_type");
		}
		show_chart(chart_type);
	}

	/**
	 * Input data to the line chart and update it
	 * @param tittle
	 * 		- tittle of chart
	 * @param table
	 * 		- DataTable of data 
	 * @param col1Name
	 * 		- first column name of data in table
	 * @param col2Name
	 * 		- second column name of data in table
	 */
	private static void lineChart_update (String tittle ,DataTable table,  String col1Name ,String col2Name) {
		
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
	
	/**
	 * 	the function to stop the animation for animation line chart
	 */
	public static void stop_animate() {
		if(show_chart_index == 2) {
			timer.cancel();
			timer.purge();
		}
	}
	
	/**
	 * Input data to the animation line chart and update it
	 * @param tittle
	 * 		- tittle of chart
	 * @param table
	 * 		- DataTable of data 
	 * @param col1Name
	 * 		- first column name of data in table
	 * @param col2Name
	 * 		- second column name of data in table
	 */
	private static void ani_lineChart_update (String tittle ,DataTable table,  String col1Name ,String col2Name) {
		
		ani_xAxis.setLabel(col1Name);
		ani_yAxis.setLabel(col2Name);
		ani_lineChart.setTitle(tittle+"(animation)");
		
		// Get 2 columns
		DataColumn xCol = table.getCol(col1Name);
		DataColumn yCol = table.getCol(col2Name);
		
    	// Ensure both columns exist and the type is number
  		if (xCol != null && yCol != null && xCol.getTypeName().equals(DataType.TYPE_NUMBER)
  				&& yCol.getTypeName().equals(DataType.TYPE_NUMBER)) {

  			anim_series.setName(tittle);

  			// populating the series with data
  			// As we have checked the type, it is safe to downcast to Number[]
  			final Number[] xValues = (Number[]) xCol.getData();
  			final Number[] yValues = (Number[]) yCol.getData();

  			// In DataTable structure, both length must be the same
  			final int len = xValues.length;
            
  			TimerTask task = new TimerTask() {
  		      @Override
  		      public void run() {
  		    	  
  		    	Platform.runLater(() -> {  	  		        
  	  		        anim_series.getData().clear();
  					for (int i = 0; i < len; i++) {
  						if (xValues[i] != null && yValues[i] != null) {
  							anim_series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
  							System.out.println("X: " + xValues[i]+ "Y: " + yValues[i]  );
  						}
  					}
  		    	});
  		      }
  		    };
  		    
  		    timer = new Timer();
  		    // schedules the task to be run in an interval 
  		    timer.scheduleAtFixedRate(task, 0, intevalPeriod);

  		    // add the new series as the only one series for this line chart
  		    if(ani_lineChart.getData().isEmpty()) 
  		    	ani_lineChart.getData().add(anim_series);
  		}
	}
	
	/**
	 * Input data to the pie chart and update it
	 * @param tittle
	 * 		- tittle of chart
	 * @param table
	 * 		- DataTable of data 
	 * @param col1Name
	 * 		- first column name of data in table
	 * @param col2Name
	 * 		- second column name of data in table
	 */
	private static void pieChart_update (String tittle ,DataTable table,  String col1Name ,String col2Name) {
		
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