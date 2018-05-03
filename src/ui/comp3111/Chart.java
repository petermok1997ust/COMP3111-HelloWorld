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

public class Chart implements Serializable{
	
	public static String[] chartTypes = {
			"Line Chart",
			"Pie Chart",
			"Animation Line Chart"
			};
	
	public static String[][]chart_labels = {
			{"X-Axis","Y-Axis"},
			{"Label","Value"},
			{"X-Axis","Y-Axis"}
			};
	
	public static String[][]chart_col_types = {
			{DataType.TYPE_NUMBER , DataType.TYPE_NUMBER },
			{ DataType.TYPE_STRING ,DataType.TYPE_NUMBER},
			{ DataType.TYPE_NUMBER ,DataType.TYPE_NUMBER}
			};
	
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
	private static NumberAxis ani_xAxis = new NumberAxis();
	private static NumberAxis ani_yAxis = new NumberAxis();
	private final static LineChart<Number, Number> ani_lineChart = new LineChart<Number, Number>(ani_xAxis, ani_yAxis);
	private final static XYChart.Series anim_series = new XYChart.Series();
	private static Timer timer = null;
	private final static long intevalPeriod = 1* 1000; 

	/**
	 * Get instance object of Chart
	 * 
	 * @return Instance of Chart Object
	 */
	public static Chart getInstance()
	{
		if (chart_instance == null)
			chart_instance = new Chart();
		return chart_instance;
	}
	
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
	 * Get the Animation lineChart Object
	 * 
	 * @return ani_lineChart
	 * 		- the object of lineChart with animation  
	 */
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
	
//	public int getChartTypeIndex(String type) {
//		int x = 0; 
//		for (String chartType : chartTypes) {
//			if (chartType == type ) return x;
//			x++;
//		}
//		return -1;
//	}
	
	public void update_chart (int chart_type, String tittle ,DataTable table,  String col1Name ,String col2Name) {
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
	
	public void stop_animate() {
		timer.cancel();
		timer.purge();
	}
	
	public static void ani_lineChart_update (String tittle ,DataTable table,  String col1Name ,String col2Name) {
		
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

  			for (int i = 0; i < len; i++) {
  				if (xValues[i] != null && yValues[i] != null) {
  					anim_series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
  					//System.out.println("X: " + xValues[i]+ "Y: " + yValues[i]  );
  				}
  			}
            
  			TimerTask task = new TimerTask() {
  		      @Override
  		      public void run() {
  		    	  
  		    	Platform.runLater(() -> {  	  		        
  	  		        anim_series.getData().clear();
  					for (int i = 0; i < len; i++) {
  						if (xValues[i] != null && yValues[i] != null) {
  							anim_series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
  							//System.out.println("X: " + xValues[i]+ "Y: " + yValues[i]  );
  						}
  					}
  		    	});
  		      }
  		    };
  		    
  		    timer = new Timer();
  		    // schedules the task to be run in an interval 
  		    timer.scheduleAtFixedRate(task, 0, intevalPeriod);
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