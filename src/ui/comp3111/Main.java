package ui.comp3111;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import core.comp3111.DataColumn;
import core.comp3111.DataManagement;
import core.comp3111.DataTable;
import core.comp3111.DataType;
import core.comp3111.SampleDataGenerator;
import core.comp3111.UIController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The Main class of this GUI application
 * 
 * @author cspeter
 *
 */
public class Main extends Application {

	// Attribute: DataTable
	// In this sample application, a single data table is provided
	// You need to extend it to handle multiple data tables
	// Hint: Use java.util.List interface and its implementation classes (e.g.
	// java.util.ArrayList)
	private DataTable sampleDataTable = null;
	private static DataManagement dataManagementInstance = DataManagement.getInstance();

	// Attributes: Scene and Stage
<<<<<<< HEAD
	public static final int SCENE_NUM = 4;
	public static final int SCENE_MAIN_SCREEN = 0;
	public static final int SCENE_LINE_CHART = 1;
	public static final int SCENE_INIT_SCREEN = 2;
	public static final int SCENE_TRANSFORM_SCREEN = 3;
	private static final String[] SCENE_TITLES = { "COMP3111 Chart - [Team Name]", "Sample Line Chart Screen" , "Init Screen", "Transform Screen"};
=======
	private static final int SCENE_NUM = 2;
	private static final int SCENE_MAIN_SCREEN = 0;
	private static final int SCENE_LINE_CHART = 1;
	private static final String[] SCENE_TITLES = { "COMP3111 Chart - HELLO WORLD", "Sample Line Chart Screen" };
>>>>>>> HOFA
	private Stage stage = null;
	private Scene[] scenes = null;

	// To keep this application more structural,
	// The following UI components are used to keep references after invoking
	// createScene()

	// Screen 1: paneMainScreen
	private Button btSampleLineChartData, btSampleLineChartDataV2, btSampleLineChart;
	private Label lbSampleDataTable, lbMainScreenTitle;

	// Screen 2: paneSampleLineChartScreen
	private LineChart<Number, Number> lineChart = null;
	private BarChart<String, Number> barChart = null;
	private NumberAxis xAxis = null;
	private NumberAxis yAxis = null;
	private Button btLineChartBackMain = null;

	// Screen 3: Init
	private Button initImport, initExport, initSave, initLoad, initTransform;
	private Label initDataSet, initChart;
	private static ObservableList<String> chartItems;
	private static ObservableList<String> dataItems;
	private static ListView<String> dataList;
	public static final String string_zero = "Zero";
	public static final String string_median = "Median";
	public static final String string_mean = "Mean";
	private static final ObservableList<String> options = 
		    FXCollections.observableArrayList(
		    	string_zero,
		    	string_median,
		    	string_mean
		    );
	private static final ComboBox<String> comboBox = new ComboBox<String>(options);
	
	// Screen 4: Transform
	private ListView<TableView> splitedDataset;
	private ObservableList<TableView> dataSetItems;
	private TableView dataTable;
	private Separator transformSeparator, percentageSeparator;
	private Label split, filter, percentageLTxt, percentageRTxt;
	private ComboBox<String> columnSelect, comparison;
	private TextField compareValue;
	private Slider percentage;
	private Button applyFilter, applySplit, transformOK;
	private ToggleButton replace, create;
	private static  ToggleGroup rcGroup;
	String rcChoice = null;
	
	/**
	 * create all scenes in this application
	 */
	private void initScenes() {
		scenes = new Scene[SCENE_NUM];
		scenes[SCENE_MAIN_SCREEN] = new Scene(paneMainScreen(), 400, 500);
		scenes[SCENE_LINE_CHART] = new Scene(paneLineChartScreen(), 800, 600);
		scenes[SCENE_INIT_SCREEN] = new Scene(paneInitScreen(), 600, 600);
		scenes[SCENE_TRANSFORM_SCREEN] = new Scene(paneTransformScreen(), 600, 600);
		for (Scene s : scenes) {
			if (s != null)
				// Assumption: all scenes share the same stylesheet
				s.getStylesheets().add("Main.css");
		}
	}

	/**
	 * This method will be invoked after createScenes(). In this stage, all UI
	 * components will be created with a non-NULL references for the UI components
	 * that requires interaction (e.g. button click, or others).
	 */
	private void initEventHandlers() {
		initMainScreenHandlers();
		initLineChartScreenHandlers();
	}

	/**
	 * Initialize event handlers of the line chart screen
	 */
	private void initLineChartScreenHandlers() {

		// click handler
		btLineChartBackMain.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});
	}

	/**
	 * Populate sample data table values to the chart view
	 */
	private void populateSampleDataTableValuesToChart(String seriesName) {

		// Get 2 columns
		DataColumn xCol = sampleDataTable.getCol("X");
		DataColumn yCol = sampleDataTable.getCol("Y");
		
		
		// Ensure both columns exist and the type is number
		if (xCol != null && yCol != null && xCol.getTypeName().equals(DataType.TYPE_NUMBER)
				&& yCol.getTypeName().equals(DataType.TYPE_NUMBER)) {
			
			lineChart.setTitle("Sample Line Chart");
			xAxis.setLabel("X");
			yAxis.setLabel("Y");

			// defining a series
			XYChart.Series series = new XYChart.Series();

			series.setName(seriesName);

			// populating the series with data
			// As we have checked the type, it is safe to downcast to Number[]
			Number[] xValues = (Number[]) xCol.getData();
			Number[] yValues = (Number[]) yCol.getData();

			// In DataTable structure, both length must be the same
			int len = xValues.length;

			for (int i = 0; i < len; i++) {
				series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
			}

			// clear all previous series
			lineChart.getData().clear();

			// add the new series as the only one series for this line chart
			lineChart.getData().add(series);
		}

	}
	/**
	 * Populate sample data table values to the bar chart view
	 */
	private void populateSampleDataTableValuesToBarChart(String seriesName) {

		// Get 2 columns
		DataColumn xCol = sampleDataTable.getCol("X");
		DataColumn yCol = sampleDataTable.getCol("Y");

		// Ensure both columns exist and the type is number
		if (xCol != null && yCol != null && xCol.getTypeName().equals(DataType.TYPE_NUMBER)
				&& yCol.getTypeName().equals(DataType.TYPE_NUMBER)) {

			barChart.setTitle("Sample Line Chart");
			xAxis.setLabel("X");
			yAxis.setLabel("Y");

			// defining a series
			XYChart.Series series = new XYChart.Series();

			series.setName(seriesName);

			// populating the series with data
			// As we have checked the type, it is safe to downcast to Number[]
			Number[] xValues = (Number[]) xCol.getData();
			Number[] yValues = (Number[]) yCol.getData();

			// In DataTable structure, both length must be the same
			int len = xValues.length;

			for (int i = 0; i < len; i++) {
				series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
			}

			// clear all previous series
			barChart.getData().clear();

			// add the new series as the only one series for this line chart
			barChart.getData().add(series);
		}
	}
	
	
	/**
	 * Initialize event handlers of the main screen
	 */
	private void initMainScreenHandlers() {

		// click handler
		btSampleLineChartData.setOnAction(e -> {

			// In this example, we invoke SampleDataGenerator to generate sample data
			sampleDataTable = SampleDataGenerator.generateSampleLineData();
			lbSampleDataTable.setText(String.format("SampleDataTable: %d rows, %d columns", sampleDataTable.getNumRow(),
					sampleDataTable.getNumCol()));

			populateSampleDataTableValuesToChart("Sample 1");

		});

		// click handler
		btSampleLineChartDataV2.setOnAction(e -> {

			// In this example, we invoke SampleDataGenerator to generate sample data
			sampleDataTable = SampleDataGenerator.generateSampleLineDataV2();
			lbSampleDataTable.setText(String.format("SampleDataTable: %d rows, %d columns", sampleDataTable.getNumRow(),
					sampleDataTable.getNumCol()));

			populateSampleDataTableValuesToChart("Sample 2");

		});

		// click handler
		btSampleLineChart.setOnAction(e -> {
			putSceneOnStage(SCENE_LINE_CHART);
		});

	}

	/**
	 * Create the line chart screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneLineChartScreen() {

		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		btLineChartBackMain = new Button("Back");

		xAxis.setLabel("undefined");
		yAxis.setLabel("undefined");
		lineChart.setTitle("An empty line chart");

		// Layout the UI components
		VBox container = new VBox(20);
		container.getChildren().addAll(lineChart, btLineChartBackMain);
		container.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setCenter(container);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}

	/**
	 * Creates the main screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneMainScreen() {

		lbMainScreenTitle = new Label("COMP3111 Chart");
		btSampleLineChartData = new Button("Sample 1");
		btSampleLineChartDataV2 = new Button("Sample 2");
		btSampleLineChart = new Button("Sample Line Chart");
		lbSampleDataTable = new Label("DataTable: empty");

		// Layout the UI components

		HBox hc = new HBox(20);
		hc.setAlignment(Pos.CENTER);
		hc.getChildren().addAll(btSampleLineChartData, btSampleLineChartDataV2);

		VBox container = new VBox(20);
		container.getChildren().addAll(lbMainScreenTitle, hc, lbSampleDataTable, new Separator(), btSampleLineChart);
		container.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setCenter(container);

		// Apply style to the GUI components
		btSampleLineChart.getStyleClass().add("menu-button");
		lbMainScreenTitle.getStyleClass().add("menu-title");
		pane.getStyleClass().add("screen-background");

		return pane;
	}
	
	private Pane paneInitScreen() {
	
		initDataSet = new Label("DataSets");
		initChart = new Label("Charts");
		initImport = new Button("Import");
		initExport = new Button("Export");
		initSave = new Button("Save");
		initLoad = new Button("Load");
		initTransform = new Button("Transform");
		
		initImport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	UIController.onClickInitImportBtn();
            }
        });
		
		initLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	UIController.onClickInitLoadBtn();
            }
        });
		
		initSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	UIController.onClickInitSaveBtn();
            }
        });
		initExport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	UIController.onClickInitExportBtn();
            }
        });

		initTransform.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
    			putSceneOnStage(SCENE_TRANSFORM_SCREEN);
            }
        });

		ListView<String> chartList = new ListView<String>();
		chartItems =FXCollections.observableArrayList ();
		chartList.setItems(chartItems);

		
		dataList = new ListView<String>();
		dataItems =FXCollections.observableArrayList ();
		dataList.setItems(dataItems);

		// Layout the UI components


		HBox chartButtons = new HBox(10);
		chartButtons.getChildren().addAll(initLoad, initSave);
		HBox dataButtons = new HBox(10);
		dataButtons.getChildren().addAll(initImport, initExport, initTransform);
		//data list
		VBox dataSets = new VBox(10);
		dataSets.getChildren().addAll(initDataSet, dataList, dataButtons);
		//chart list
		VBox charts = new VBox(10);
		charts.getChildren().addAll(initChart, chartList, chartButtons);
		
		HBox hc = new HBox(10);
		hc.getChildren().addAll(dataSets, charts);
		hc.setAlignment(Pos.CENTER);
		
		//combo box

		comboBox.getSelectionModel().selectFirst();
		Text num_handle_text = new Text(10, 50, "Replace empty numeric data with: ");
		HBox num_handle = new HBox(20);
		num_handle.getChildren().addAll(num_handle_text, comboBox);
		num_handle.setAlignment(Pos.CENTER);
		//UI
		VBox vb = new VBox(20);
		vb.getChildren().addAll(hc, num_handle);
		BorderPane pane = new BorderPane();
		pane.setCenter(vb);
		


		return pane;
	}
	
	private Pane paneTransformScreen() {		
		splitedDataset = new ListView<TableView>();
		dataTable = new TableView();
		transformSeparator = new Separator();
		filter = new Label("Filter: ");
		split = new Label("Split: ");
		columnSelect = new ComboBox<String>();
		comparison = new ComboBox<String>();
		compareValue = new TextField();
		percentage = new Slider(0,50,0);
		percentageLTxt = new Label(Double.toString(percentage.getValue()));
		percentageSeparator = new Separator();
		percentageRTxt = new Label(Double.toString(100 - percentage.getValue()));
		applyFilter = new Button("Apply");
		applySplit	= new Button("Apply");
		transformOK = new Button("OK");
		replace = new ToggleButton("Replace current dataset");
		create = new ToggleButton("Create new dataset");
		rcGroup = new ToggleGroup();
		
		//list
		dataSetItems = FXCollections.observableArrayList(dataTable);
		splitedDataset.setItems(dataSetItems);
		splitedDataset.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		//table
		TableColumn firstNameCol = new TableColumn("First Name");
        TableColumn lastNameCol = new TableColumn("Last Name");
        dataTable.getColumns().addAll(firstNameCol, lastNameCol);

		//Button
		applyFilter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	double v = 0;
            	boolean error = false;
            	try {
            		v = Double.parseDouble(compareValue.getText());
            	} catch (NumberFormatException e) {
            	    error = true;
            	}
            	UIController.onClickApplyFilterBtn(v, error);
            }
        });
		applySplit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	UIController.onClickApplySplitBtn();
            }
        });

 
		transformOK.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	rcGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            	    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
						if (rcGroup.getSelectedToggle() == null) {
							rcChoice = null;
						}
						if (rcGroup.getSelectedToggle() != null) {
							rcChoice = rcGroup.getSelectedToggle().getUserData().toString();
						}
            	    }
            	});
            	UIController.onClickTransformOKBtn(rcChoice);
            }
        });
		//Toggle button
		replace.setUserData("replace");
		create.setUserData("create");
		replace.setToggleGroup(rcGroup);
		create.setToggleGroup(rcGroup);
		create.setSelected(true);	//default Create new datasets
		//comboBox
		 columnSelect.getItems().addAll(
            "jacob.smith@example.com",
            "isabella.johnson@example.com",
            "ethan.williams@example.com",
            "emma.jones@example.com",
            "michael.brown@example.com"  
        );
		comparison.getItems().addAll("<","<=","==","!=",">=",">");
		//slider
		percentage.setShowTickLabels(true);
		percentage.setShowTickMarks(true);
		percentage.setMajorTickUnit(10);
		percentage.setMinorTickCount(2);
		percentage.setBlockIncrement(10);
		percentage.valueProperty().addListener((
	            ObservableValue<? extends Number> ov, 
	            Number old_val, Number new_val) -> {
	                percentageLTxt.setText(String.format("%.2f", new_val));
	                percentageRTxt.setText(String.format("%.2f", 100.0 - new_val.doubleValue()));
	        });

		HBox hb1 = new HBox(20);
		hb1.getChildren().addAll(filter, columnSelect, comparison, compareValue, applyFilter);
//		hb1.setAlignment(Pos.CENTER);
		HBox hb2 = new HBox(20);
		hb2.getChildren().addAll(split, percentage, percentageLTxt, percentageSeparator, percentageRTxt, applySplit);
//		hb2.setAlignment(Pos.CENTER);
		HBox hb3 = new HBox(20);
		hb3.getChildren().addAll(replace, create, transformOK);
//		hb3.setAlignment(Pos.CENTER);
		
		VBox vb = new VBox(30);
		vb.getChildren().addAll(hb1,hb2,hb3);
		
		VBox positionBox = new VBox(100);
		positionBox.getChildren().addAll(splitedDataset, vb);
		
		BorderPane pane = new BorderPane();
		pane.setCenter(positionBox);
		
		return pane;
	}

	/**
	 * This method is used to pick anyone of the scene on the stage. It handles the
	 * hide and show order. In this application, only one active scene should be
	 * displayed on stage.
	 * 
	 * @param sceneID
	 *            - The sceneID defined above (see SCENE_XXX)
	 */
	private void putSceneOnStage(int sceneID) {

		// ensure the sceneID is valid
		if (sceneID < 0 || sceneID >= SCENE_NUM)
			return;

		stage.hide();
		stage.setTitle(SCENE_TITLES[sceneID]);
		stage.setScene(scenes[sceneID]);
		stage.setResizable(true);
		stage.show();
	}

	/**
	 * All JavaFx GUI application needs to override the start method You can treat
	 * it as the main method (i.e. the entry point) of the GUI application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {

			stage = primaryStage; // keep a stage reference as an attribute
			initScenes(); // initialize the scenes
			initEventHandlers(); // link up the event handlers			
			putSceneOnStage(SCENE_INIT_SCREEN); 
		} catch (Exception e) {

			e.printStackTrace(); // exception handling: print the error message on the console
		}
	}

	public static void setDataItem(List<String> list) {
		dataItems.clear();
		for(int i = 0; i<list.size();i++)
			dataItems.add(list.get(i));
	}
	
	public static void setDataItem(String name) {
			dataItems.add(name);
	}
	
	
	public static void setDataObj(DataManagement dataObj) {
		dataManagementInstance = dataObj;
		setDataItem(dataObj.getTableName());
	}
	
	public static int getSelectedDataIdx() {
		System.out.println("Selected dataset index: "+dataList.getSelectionModel().getSelectedIndex());
		return dataList.getSelectionModel().getSelectedIndex();
	}
	
	public static String getSelectedNumHandle() {
		System.out.println("Selected number handling: "+comboBox.getSelectionModel().getSelectedItem());
		return comboBox.getSelectionModel().getSelectedItem();
	}
		
	/**
	 * main method - only use if running via command line
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
