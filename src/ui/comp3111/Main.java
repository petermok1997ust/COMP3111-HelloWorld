package ui.comp3111;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import core.comp3111.DataColumn;
import core.comp3111.DataManagement;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import core.comp3111.Transform;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
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
import javafx.util.Callback;

/**
 * The Main class of this GUI application
 * Nice!!
 * 
 * @author Lotusfa and Peter and Kelvin
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
	public static final int SCENE_NUM = 4;
	public static final int SCENE_MAIN_SCREEN = 0;
	public static final int SCENE_CHART = 1;
	public static final int SCENE_INIT_SCREEN = 2;
	public static final int SCENE_TRANSFORM_SCREEN = 3;
	private static final String[] SCENE_TITLES = { "COMP3111 Chart - [Team Name]", "Chart Screen" , "Init Screen", "Transform Screen"};
	private Stage stage = null;
	private Scene[] scenes = null;

	// Screen 2: paneSampleLineChartScreen
	private Button btLineChartBackMain = null;

	// Screen 3: Init
	private Button initImport, initExport, initSave, initLoad, initTransform, initPlot;
	private Label initDataSet, initChart;
	private static ObservableList<String> chartItems, dataItems;
	private static ListView<String> dataList, chartList;
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
	private static DataTable selected_table;
	private static int selected_dataset_index,selected_chart_index;
	private static String selected_col1,selected_col2;
	private static Label chart_col_1_text,chart_col_2_text;
	private static ComboBox<String> chartDataColName1 = new ComboBox<String>();
	private static ComboBox<String> chartDataColName2 = new ComboBox<String>();
	
	
	// Screen 4: Transform
	private ListView<TableView> splitedDataset;
	private ObservableList<TableView> dataSetItems;
	private TableView dataTableView;
	private Separator transformSeparator, percentageSeparator;
	private Label split, filter, filterError, percentageLTxt, percentageRTxt, splitError, OKError;
	private ComboBox<String> columnSelect, comparison;
	private TextField compareValue;
	private Slider percentage;
	private Button applyFilter, applySplit, transformOK, backFromTransform;
	private ToggleButton replace, create;
	private static  ToggleGroup rcGroup;
	String rcChoice = "create";
	private ArrayList<Transform> t;
	/**
	 * create all scenes in this application
	 */
	private void initScenes() {
		scenes = new Scene[SCENE_NUM];
		scenes[SCENE_MAIN_SCREEN] = new Scene(paneInitScreen(), 400, 500);
		scenes[SCENE_CHART] = new Scene(paneChartScreen(), 800, 600);
		scenes[SCENE_INIT_SCREEN] = new Scene(paneInitScreen(), 600, 600);
		scenes[SCENE_TRANSFORM_SCREEN] = new Scene(paneTransformScreen(), 600, 640);
		for (Scene s : scenes) {
			if (s != null)
				// Assumption: all scenes share the same stylesheet
				s.getStylesheets().add("Main.css");
		}
	}

	/**
	 * Create the chart screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneChartScreen() {
		btLineChartBackMain = new Button("Back");
		
		BorderPane pane = new BorderPane();
		
		// Layout the UI components
		VBox container = new VBox(20);
		
		container.getChildren().addAll(Chart.container() , btLineChartBackMain);
		container.setAlignment(Pos.CENTER);

		pane.setCenter(container);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");
		
		btLineChartBackMain.setOnAction(e -> {
			putSceneOnStage(SCENE_INIT_SCREEN);
			Chart.stop_animate();
		});

		return pane;
	}
	
	/**
	 * Create the main screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneInitScreen() {
	
		initDataSet = new Label("DataSets");
		initChart = new Label("Charts");
		initImport = new Button("Import");
		initExport = new Button("Export");
		initSave = new Button("Save");
		initLoad = new Button("Load");
		initTransform = new Button("Transform");

		initPlot = new Button("Plot");
		selected_dataset_index = -1;
		
		initImport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	UIController.onClickInitImportBtn();
            }
        });
		
		initLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
					UIController.onClickInitLoadBtn();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "The project file does not match the class of the application");
//					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "The project file failed to import with IOException");
//					e.printStackTrace();
				}
            }
        });
		
		initSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
					UIController.onClickInitSaveBtn();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "The project file failed to save with IOException");
//					e.printStackTrace();
				}
            }
        });
		initExport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
					UIController.onClickInitExportBtn();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "The project file failed to export with IOException");
					e.printStackTrace();
				}
            }
        });

		initTransform.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(selected_dataset_index != -1) {
            		DataTable selectedTable = dataManagementInstance.getDataTableByIndex(selected_dataset_index);
                	t = new ArrayList<Transform>();
            		t.add(new Transform(selectedTable));
        			settingDatasetView(t, t.get(0).getColName(), t.get(0).getNumColName());
        			putSceneOnStage(SCENE_TRANSFORM_SCREEN);
            	}
            	else {
            		System.out.println("dataset not selected");
            	}
            }
        });
		
		initPlot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if (selected_dataset_index > -1 && selected_chart_index > -1 && chartDataColName1.getValue() != null && chartDataColName1.getValue() != null) {
	            	update_chart ();
	            	putSceneOnStage(SCENE_CHART); 
            	}
            }
        });
		
		chartList = new ListView<String>();
		chartItems =FXCollections.observableArrayList ();
		chartList.setItems(chartItems);
		
		//add item to chart list 
		String [] chartTypes = Chart.getChartType();
		for (String chartType : chartTypes) {
		    // fruit is an element of the `fruits` array.
			chartItems.add(chartType);
		}
		
		dataList = new ListView<String>();
		dataItems =FXCollections.observableArrayList ();
		dataList.setItems(dataItems);
		dataList.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends String> ov, String old_val, 
                String new_val) -> {
                	selected_dataset_index = dataList.getSelectionModel().getSelectedIndex();
                	update_chartCol_comboBox();
        });
		
		chartList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
		{
		    public void changed(ObservableValue<? extends String> ov,
		            final String oldvalue, final String newvalue) 
		    {
		    	update_chartCol_comboBox();
		    }
		});
		
		// Layout the UI components
		HBox chartButtons = new HBox(10);
		chartButtons.getChildren().addAll(initLoad, initSave);
		HBox dataButtons = new HBox(10);
		dataButtons.getChildren().addAll(initImport, initExport, initTransform);
		
		//chart column ComboBox 1
		HBox chartComboBox1 = new HBox(10);
		chart_col_1_text = new Label();
		chartComboBox1.getChildren().addAll(chart_col_1_text,chartDataColName1);
		//chart column ComboBox 2
		HBox chartComboBox2 = new HBox(10);
		chart_col_2_text = new Label();
		chartComboBox2.getChildren().addAll(chart_col_2_text,chartDataColName2,initPlot);
		
		//data list
		VBox dataSets = new VBox(10);
		dataSets.getChildren().addAll(initDataSet, dataList, dataButtons, chartButtons);
		//chart list
		VBox charts = new VBox(10);
		charts.getChildren().addAll(initChart, chartList, chartComboBox1, chartComboBox2 );
		
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
		
		chartList.getSelectionModel().selectFirst();
		
		chartDataColName1.valueProperty().addListener(new ChangeListener<String>() {
	        @Override public void changed(ObservableValue ov, String t, String t1) {
	            selected_col1 = t1;
	        }    
	    });
		
		chartDataColName2.valueProperty().addListener(new ChangeListener<String>() {
	        @Override public void changed(ObservableValue ov, String t, String t1) {
	        	selected_col2 = t1;
	        }    
	    });
		return pane;
	}
	
  /**
	 * Setting up TableView in Transform pane with table list
	 * @param tList - list of table to be shown
	 * @param colName - list of all column name
	 * @param numColName - list of column name which column type is Number
	 */
	private void settingDatasetView(ArrayList<Transform> tList, String[] colName, ArrayList<String> numColName) {
		dataSetItems = FXCollections.observableArrayList();
		String prevCS = columnSelect.getSelectionModel().getSelectedItem();
		columnSelect.getItems().clear();
		if(numColName != null)
			for(int i = 0; i < numColName.size(); i++)
					columnSelect.getItems().add(numColName.get(i));	//setting comboBox
		if(columnSelect.getItems().contains(prevCS))
			columnSelect.getSelectionModel().select(prevCS);
		else columnSelect.getSelectionModel().selectFirst();
		
		System.out.println(tList.size());
		for(Transform x : tList) {
//			String[][] rowList = new String[x.getNumRowOfFilteredList()][x.getNumColOfFilteredList()];
			String[][] rowList = x.getFilteredList();
//			System.out.println("row size: " + rowList.length);
//			System.out.println("col size: " + rowList[0].length);
//			for(int i =0 ; i< rowList.length; i++) {
//				for(int j =0; j< rowList[i].length; j++) {
//					System.out.print(rowList[i][j]);
//				}
//				System.out.println();
//			}
			dataTableView = new TableView<>();
			ObservableList<String[]> data = FXCollections.observableArrayList();
			data.addAll(Arrays.asList(rowList));
			//remove last null row from data
//			boolean isRow = false;
//			for(int v = 1; v < colName.length; v++) {
//				try {
//					if(!rowList[rowList.length-1][v].isEmpty()) {isRow = true; break;}		
//				} catch(Exception e){
//					if(rowList[rowList.length-1][v] != null) {isRow = true; break;}
//				}
//			}
//			if(!isRow) data.remove(rowList.length-1);
			//printing rowLsit
//			for(int i = 0; i < rowList.length; i++)
//				for(int j = 0; j < rowList[i].length; j++)
//					System.out.print(rowList[i][j] + " ");
//			System.out.println();
			//printing data array
//			for(int i = 0; i < data.size(); i++)
//				for(int j = 0; j < data.get(i).length; j++)
//					System.out.print(data.get(i)[j] + " ");
//			System.out.println();
//			System.out.println(colName.length);
			for (int i = 0; i < colName.length; i++) {
				TableColumn tc = new TableColumn(colName[i]);
				final int colNo = i;
				tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
//						System.out.println(p.getValue()[colNo]);
						return new SimpleStringProperty((p.getValue()[colNo]));
					}
				});
				tc.setPrefWidth(90);
		        dataTableView.getColumns().add(tc);
			}
			dataTableView.setItems(data);
			dataTableView.setFixedCellSize(25);
			dataTableView.prefHeightProperty().bind(dataTableView.fixedCellSizeProperty().multiply(Bindings.size(dataTableView.getItems()).add(1.01)));
			dataTableView.minHeightProperty().bind(dataTableView.prefHeightProperty());
			dataTableView.maxHeightProperty().bind(dataTableView.prefHeightProperty());

	        //list
			dataSetItems.add(dataTableView);
		}
		splitedDataset.setItems(dataSetItems);
		splitedDataset.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	/**
	 * Create the transform screen and layout its UI components
	 * 
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneTransformScreen() {
		splitedDataset = new ListView<TableView>();
		transformSeparator = new Separator();
		filter = new Label("Filter: ");
		split = new Label("Split: ");
		filterError = new Label();
		columnSelect = new ComboBox<String>();
		comparison = new ComboBox<String>();
		compareValue = new TextField();
		percentage = new Slider(10,50,0);
		percentageLTxt = new Label(Double.toString(percentage.getValue()));
		percentageSeparator = new Separator();
		percentageRTxt = new Label(Double.toString(100 - percentage.getValue()));
		splitError = new Label();
		applyFilter = new Button("Apply");
		applySplit	= new Button("Apply");
		transformOK = new Button("OK");
		OKError = new Label();
		replace = new ToggleButton("Replace current dataset");
		create = new ToggleButton("Create new dataset");
		rcGroup = new ToggleGroup();
		backFromTransform = new Button("Back");
		
		//list view
//		splitedDataset.setPrefSize(500, 500);

		//Button
		applyFilter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	double cValueTmp = 0;
            	boolean error = false;
            	try {
            		cValueTmp = Double.parseDouble(compareValue.getText());
            	} catch (NumberFormatException e) {
            	    compareValue.setPromptText("Please enter double.");
            	    error = true;
            	}
            	
        		String cSelectTmp = columnSelect.getValue();
        		String comparisonTmp = comparison.getValue();
        		if(cSelectTmp == null || comparisonTmp == null || error) {
        			filterError.setText("Please complete the input.");
        			filterError.setVisible(true);
        		}
        		else {
        			filterError.setVisible(false);
        			if(splitedDataset.getItems().size() == 1) {
        				t.get(0).filterData(cSelectTmp, comparisonTmp, cValueTmp);
        				settingDatasetView(t, t.get(0).getColName(), t.get(0).getNumColName());
        			}
        			else if(splitedDataset.getSelectionModel().isEmpty()){
        				filterError.setText("Please select at list one table.");
        				filterError.setVisible(true);
        			}
        			else {
        				for(int x : splitedDataset.getSelectionModel().getSelectedIndices())
        					t.get(x).filterData(cSelectTmp, comparisonTmp, cValueTmp);
        				settingDatasetView(t, t.get(0).getColName(), t.get(0).getNumColName());
        			}
        		}
            }
        });
		applySplit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	splitError.setVisible(false);
            	if(splitedDataset.getItems().size() == 1) {
                	if(!t.get(0).splitData(percentage.getValue())) {
                		splitError.setText("Percentage not valid.");
                		splitError.setVisible(true);
                	}
                	else {
    	            	ArrayList<ArrayList<String>> one = t.get(0).getFirstSplitedT();
    	            	ArrayList<ArrayList<String>> two = t.get(0).getSecondSplitedT();
    	            	Transform onet = null;
    					try {
    						onet = t.get(0).clone();
    					} catch (CloneNotSupportedException e) {
    						System.out.println("clone failed");
    						e.printStackTrace();					
    					}
    	            	onet.setRowList(one);
    	            	t.add(onet);
    	            	Transform twot = null;
    					try {
    						twot = t.get(0).clone();
    					} catch (CloneNotSupportedException e) {
    						System.out.println("clone 2 failed");
    						e.printStackTrace();
    					}
    	            	twot.setRowList(two);
    	            	t.add(twot);
    	            	t.remove(0);
    	            	settingDatasetView(t, t.get(0).getColName(), t.get(0).getNumColName());
    	            }
    			}
    			else if(splitedDataset.getSelectionModel().isEmpty()){
    				splitError.setText("Please select at list one table.");
    				splitError.setVisible(true);
    			}
    			else {
    				boolean allPValid = true;
    				for(int x : splitedDataset.getSelectionModel().getSelectedIndices())
                    	if(!t.get(x).splitData(percentage.getValue())) {
                    		splitError.setText("Percentage not valid.");
                    		splitError.setVisible(true);
                    		allPValid = false;
                    		break;
                    	}
    				if(allPValid) {
    					int count = 0;
        				for(int x : splitedDataset.getSelectionModel().getSelectedIndices()) {
        					ArrayList<ArrayList<String>> one = t.get(x+count).getFirstSplitedT();
        	            	ArrayList<ArrayList<String>> two = t.get(x+count).getSecondSplitedT();
        	            	Transform onet = null;
        					try {
        						onet = t.get(x+count).clone();
        					} catch (CloneNotSupportedException e) {
        						System.out.println("clone failed");
        						e.printStackTrace();					
        					}
        	            	onet.setRowList(one);
        	            	t.add(x+1+count, onet);
        	            	Transform twot = null;
        					try {
        						twot = t.get(x+count).clone();
        					} catch (CloneNotSupportedException e) {
        						System.out.println("clone 2 failed");
        						e.printStackTrace();
        					}
        	            	twot.setRowList(two);
        	            	t.add(x+2+count, twot);
        	            	t.remove(x+count);
        	            	count++;
        				}
    					settingDatasetView(t, t.get(0).getColName(), t.get(0).getNumColName());
    				}
    			}
            	
            }
        });
 
		transformOK.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	OKError.setVisible(false);
            	if (rcGroup.getSelectedToggle() == null) {
					rcChoice = null;
					OKError.setText("Please select one choice. Replace/Create? ");
    				OKError.setVisible(true);
				} else {
					rcChoice = rcGroup.getSelectedToggle().getUserData().toString();
        			if(splitedDataset.getItems().size() == 1) {
        				if(t.get(0).getFilteredList().length == 0) {
        					OKError.setText("Cannot create an empty dataset.");
            				OKError.setVisible(true);
        				}else {
	        				try {
	        					if(rcChoice == "replace") {
	        						dataManagementInstance.removeTable(selected_dataset_index);
									removeDataItem(selected_dataset_index);
									renameDataItem();
	        					}
								dataManagementInstance.createDataTable(t.get(0).toListwTitle(), t.get(0).getNumRowOfFilteredList(), t.get(0).getNumColOfFilteredList()-1);
								String name = dataManagementInstance.addTable();
								setDataItem(name);	        						
	        				} catch (DataTableException e) {
								e.printStackTrace();
							}
	                    	putSceneOnStage(SCENE_INIT_SCREEN);
        				}
        			}
        			else if(splitedDataset.getSelectionModel().isEmpty()){
        				OKError.setText("Please select at list one table.");
        				OKError.setVisible(true);
        			}
        			else {
        				boolean allDValid = true;
        				for(int x : splitedDataset.getSelectionModel().getSelectedIndices()) {	
	        				if(t.get(x).getFilteredList().length == 0) {
	        					OKError.setText("Cannot create an empty dataset.");
	            				OKError.setVisible(true);
	            				allDValid = false;
	            				break;
	        				}
        				}
        				if(allDValid) {
        					if(rcChoice == "replace") {
        						dataManagementInstance.removeTable(selected_dataset_index);
								removeDataItem(selected_dataset_index);
								renameDataItem();
        					}
        					for(int x : splitedDataset.getSelectionModel().getSelectedIndices())
		        				try {
	        						dataManagementInstance.createDataTable(t.get(x).toListwTitle(), t.get(x).getNumRowOfFilteredList(), t.get(x).getNumColOfFilteredList()-1);
									String name = dataManagementInstance.addTable();
									setDataItem(name);
		        				} catch (DataTableException e) {
									e.printStackTrace();
								}
	                    	putSceneOnStage(SCENE_INIT_SCREEN);
        				}
        			}
				}
            }
        });

		backFromTransform.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	t = null;
            	putSceneOnStage(SCENE_INIT_SCREEN);
            }
        });

		//Toggle button
		replace.setUserData("replace");
		create.setUserData("create");
		replace.setToggleGroup(rcGroup);
		create.setToggleGroup(rcGroup);
		create.setSelected(true);	//default Create new datasets
		OKError.setVisible(false);
    	rcGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
    	    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
            	if (rcGroup.getSelectedToggle() == null) {
					rcChoice = null;
					OKError.setText("Please select one choice. Replace/Create? ");
    				OKError.setVisible(true);
				} else {
					rcChoice = rcGroup.getSelectedToggle().getUserData().toString();
					OKError.setVisible(false);
				}
    	    }
    	});

		//comboBox		
		comparison.getItems().addAll("<","<=","==","!=",">=",">");
		
		//text field
		compareValue.setPromptText("Enter float/integer number.");
		filterError.setVisible(false);
		//forcing double input, but with bug if user paste letters
//		UnaryOperator<TextFormatter.Change> txtFilter = new UnaryOperator<TextFormatter.Change>() {
//            @Override
//            public TextFormatter.Change apply(TextFormatter.Change t) {
//
//                if (t.isReplaced()) 
//                    if(t.getText().matches("[^0-9]"))
//                        t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));
//                
//
//                if (t.isAdded()) {
//                    if (t.getControlText().contains(".")) {
//                        if (t.getText().matches("[^0-9]")) {
//                            t.setText("");
//                        }
//                    } else if (t.getText().matches("[^0-9.]")) {
//                        t.setText("");
//                    }
//                }
//
//                return t;
//            }
//        };
//        compareValue.setTextFormatter(new TextFormatter<>(txtFilter));
        
		//slider
		percentage.setShowTickLabels(true);
		percentage.setShowTickMarks(true);
		percentage.setMajorTickUnit(10);
		percentage.setMinorTickCount(2);
		percentage.setBlockIncrement(10);
		percentage.valueProperty().addListener((
	            ObservableValue<? extends Number> ov, 
	            Number old_val, Number new_val) -> {
	            	percentage.setValue(Math.round(new_val.doubleValue()));
	                percentageLTxt.setText(String.format("%d", Math.round(new_val.doubleValue())));
	                percentageRTxt.setText(String.format("%d", 100 - Math.round(new_val.doubleValue())));
	        });
		percentageSeparator.setOrientation(Orientation.VERTICAL);
		splitError.setVisible(false);

		HBox hb1 = new HBox(20);
		hb1.getChildren().addAll(filter, columnSelect, comparison, compareValue, applyFilter);
//		hb1.setAlignment(Pos.CENTER);
		HBox hb2 = new HBox(20);
		hb2.getChildren().addAll(split, percentage, percentageLTxt, percentageSeparator, percentageRTxt, applySplit);
//		hb2.setAlignment(Pos.CENTER);
		HBox hb3 = new HBox(20);
		hb3.getChildren().addAll(replace, create, transformOK, OKError);
//		hb3.setAlignment(Pos.CENTER);
		HBox hb4 = new HBox(20);
		hb4.getChildren().addAll(backFromTransform);
//		hb4.setAlignment(Pos.CENTER);
		
		VBox vb = new VBox(10);
		vb.getChildren().addAll(hb1,filterError);

		VBox vb1 = new VBox(10);
		vb1.getChildren().addAll(hb2,splitError);
		
		VBox vb2 = new VBox(10);
		vb2.getChildren().addAll(vb ,vb1, hb3, hb4);
		
		VBox positionBox = new VBox(10);
		positionBox.getChildren().addAll(splitedDataset, vb2);
		
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
			putSceneOnStage(SCENE_INIT_SCREEN); 
		} catch (Exception e) {

			e.printStackTrace(); // exception handling: print the error message on the console
		}
	}

 	/**
	 * Set data item to be shown on init screen with data list
	 * @param list - list of data item name
	 */
	public static void setDataItem(List<String> list) {
		dataItems.clear();
		for(int i = 0; i<list.size();i++)
			dataItems.add(list.get(i));
	}
	
 	/**
	 * Add data item to be shown on init screen with data name
	 * @param name - a data item name
	 */
	public static void setDataItem(String name) {
			dataItems.add(name);
	}
  
	/**
	 * Remove specific data item in the list
	 * @param x - a data item index
	 */
	public static void removeDataItem(int x) {
		dataItems.remove(x);
	}
  
	/**
	 * Rename all the data item to ensure the order and no duplicate name
	 */	
	public static void renameDataItem() {
		for(int i =0 ; i< dataItems.size();i++) {
			dataItems.set(i, "dataset"+(i+1));
		}
	}

 	/**
	 * Change the dataManagementInstace to the specified one
	 * @param dataObj - DataMangement object to be changed
	 */
  public static void setDataObj(DataManagement dataObj) {
		dataManagementInstance = dataObj;
		setDataItem(dataObj.getTableName());
	}

  /**
	 * Get selected data set index in init screen
	 * @return selected data set index in init screen
	 */
	public static int getSelectedDataIdx() {
		System.out.println("Selected dataset index: "+dataList.getSelectionModel().getSelectedIndex());
		return dataList.getSelectionModel().getSelectedIndex();
	}

 	/**
	 * Get selected option in the comboBox in init screen
	 * @return selected option in the comboBox in init screen
	 */
	public static String getSelectedNumHandle() {
		System.out.println("Selected number handling: "+comboBox.getSelectionModel().getSelectedItem());
		return comboBox.getSelectionModel().getSelectedItem();
	}
	
	/**
	 * To help update 2 comBox to display the list of column base on the dataSet and chart selection.  
	 */
	public static void update_chartCol_comboBox() {
		
		selected_chart_index = chartList.getSelectionModel().getSelectedIndex();
		selected_dataset_index = dataList.getSelectionModel().getSelectedIndex(); 
        
		if (selected_chart_index > -1 ) {			
			chart_col_1_text.setText(Chart.chart_labels[selected_chart_index][0]);
			chart_col_2_text.setText(Chart.chart_labels[selected_chart_index][1]);
		}

		chartDataColName1.getItems().clear();
		chartDataColName2.getItems().clear();
		
		if (selected_dataset_index > -1) {
			List<DataTable> tables = dataManagementInstance.getDataTables();
			selected_table = tables.get(selected_dataset_index);
			 Set<String> key = selected_table.getKeys();
			Object[] key_arr = key.toArray();
			
			for (Object k : key_arr) {
				DataColumn col = selected_table.getCol((String)k);
				String type = col.getTypeName();

				if(type.equals(Chart.chart_col_types[selected_chart_index][0]))
					chartDataColName1.getItems().add((String) k);
				
				if(type.equals(Chart.chart_col_types[selected_chart_index][1]))
					chartDataColName2.getItems().add((String)k);
			}
		}
		chartDataColName1.getSelectionModel().selectFirst();
		chartDataColName2.getSelectionModel().selectFirst();
	}
	
	/**
	 * To help to pass the selected data to Chart in order to update it. 
	 */
	private void update_chart () {
		if (selected_table != null  && chartDataColName1.getValue() != null && chartDataColName1.getValue() != null) {
			String Name = dataManagementInstance.getTableName().get(selected_dataset_index);
			Chart.update_chart(selected_chart_index, Name,selected_table, selected_col1, selected_col2); 
		}
	}
	
	/**
	 * main method - only use if running via command line
	 * 
	 * @param args - arguments for main
	 */
	public static void main(String[] args) {
		UIController.started = true;
		launch(args);
	}
}
