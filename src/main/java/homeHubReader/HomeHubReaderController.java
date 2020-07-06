package homeHubReader;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

public class HomeHubReaderController implements Initializable {

	@FXML
	private TextField tfFileChooser;
	
	@FXML
	private Button btnReadFile;

	@FXML
	private TreeTableView<JsonItem> ttvCategories = new TreeTableView<JsonItem>();
	
	public static File customFilePath;
	public static File categoryFilePath;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCategoryTable();
		
		customFilePath = new File("C:\\Users\\Marcel\\workspace\\HomeHubReader\\custom.json");
		categoryFilePath = new File("C:\\Users\\Marcel\\workspace\\HomeHubReader\\categories.json");
		onBtnReadFileClicked();
	}
	
	@SuppressWarnings("unchecked")
	private void initCategoryTable() {
		TreeTableColumn<JsonItem, String> colName = new TreeTableColumn<JsonItem, String>("Name");
		TreeTableColumn<JsonItem, String> colDisplayName = new TreeTableColumn<JsonItem, String>("Display Name");
		TreeTableColumn<JsonItem, String> colIcon = new TreeTableColumn<JsonItem, String>("Icon");
		TreeTableColumn<JsonItem, Color> colColor = new TreeTableColumn<JsonItem, Color>("Color");
		TreeTableColumn<JsonItem, String> colAppendDivider = new TreeTableColumn<JsonItem, String>("Divider");
		ttvCategories.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		ttvCategories.getColumns().addAll(colName, colDisplayName, colIcon, colColor, colAppendDivider);
		
		colName.setMinWidth(100);		
		colName.setCellValueFactory(new TreeItemPropertyValueFactory<JsonItem, String>("name"));
		colDisplayName.setCellValueFactory(new TreeItemPropertyValueFactory<JsonItem, String>("display_name"));
		colIcon.setCellValueFactory(new TreeItemPropertyValueFactory<JsonItem, String>("icon"));
		colColor.setCellValueFactory(new TreeItemPropertyValueFactory<JsonItem, Color>("color"));
		colColor.setCellFactory(ColorTableCell::new);	
		colAppendDivider.setCellValueFactory(new TreeItemPropertyValueFactory<JsonItem, String>("append_divider"));
		colAppendDivider.setCellFactory(CheckTableCell::new);
	}

	public class ColorTableCell<T> extends TreeTableCell<T, Color> {    
	    private final ColorPicker colorPicker;
	    private final VBox vBox;

	    public ColorTableCell(TreeTableColumn<T, Color> column) {
	        colorPicker = new ColorPicker();
	        vBox = new VBox();
	        vBox.setAlignment(Pos.CENTER);
	        vBox.getChildren().add(colorPicker);
	        
	        colorPicker.editableProperty().bind(column.editableProperty());
	        colorPicker.disableProperty().bind(column.editableProperty().not());
	        colorPicker.setOnShowing(event -> {
	            final TreeTableView<T> treeTableView = getTreeTableView();
	            treeTableView.getSelectionModel().select(getTreeTableRow().getIndex());
	            treeTableView.edit(treeTableView.getSelectionModel().getSelectedIndex(), column);       
	        });
	        colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
	            if(isEditing()) {
	                commitEdit(newValue);
	            }
	        });     
	        //setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	    }

	    @Override
	    protected void updateItem(Color item, boolean empty) {
	        super.updateItem(item, empty);  

	        setText(null);  
	        
	        if(empty || item == null) {     
	            setGraphic(null);
	        } else {        
	            colorPicker.setValue(item);
	            setGraphic(vBox);
	        } 
	    }
	}
	
	public class CheckTableCell<T> extends TreeTableCell<T, String> {   
		private final CheckBox checkBox;
		private final VBox vBox;
		
	    public CheckTableCell(TreeTableColumn<T, String> column) {
	    	checkBox = new CheckBox();
	        vBox = new VBox();
	        vBox.setAlignment(Pos.CENTER);
	        vBox.getChildren().add(checkBox);
	        
	        checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
	            JsonItem category = (JsonItem) getTreeTableRow().getItem();
	
	            if (category != null) {
	            	category.setAppend_divider(isSelected.toString());
	            }
	        });
	    }

	    @Override
	    public void updateItem(String item, boolean empty) {
	    	if (empty) {
	    		setGraphic(null);
	    	} else {
	    		checkBox.setSelected(Boolean.valueOf(item));
	    		setGraphic(vBox);
	    	}
	    }
	}

	@FXML
	private void onBtnReadFileClicked() {
		try {
			JsonReader jsonReader = new JsonReader();
			readCategories(jsonReader);
//			readCustoms(jsonReader);
		} catch (Exception e) {
			showError(e);
			e.printStackTrace();
		}
	}

	private void readCategories(JsonReader jsonReader) {
		if (categoryFilePath != null && customFilePath != null) {	
			TreeItem<JsonItem> root = new TreeItem<JsonItem>();

			List<JsonItem> categories = jsonReader.readCategoriesFile(categoryFilePath);
			HashMap<String, List<JsonItem>> customs = jsonReader.readCustomFile(customFilePath);
		
			for (JsonItem category : categories) {
				TreeItem<JsonItem> categoryItem = new TreeItem<JsonItem>(category);

				for (SubCategory sc : category.getSubcategories()) {
					JsonItem subCategory = new JsonItem();
					subCategory.setName(sc.getName());
					subCategory.setDisplay_name(sc.getDisplay_name());
					
					TreeItem<JsonItem> subCategoryItem = new TreeItem<JsonItem>(subCategory);

					if (customs.containsKey(sc.getName())) {
						List<JsonItem> items = customs.get(sc.getName());
						for (JsonItem item : items) {
							TreeItem<JsonItem> customItem = new TreeItem<JsonItem>(item);
							subCategoryItem.getChildren().add(customItem);
							
							if (Boolean.valueOf(item.getAppend_divider())) {
								subCategoryItem.getChildren().add(createDivider());
							}
						}
					}
					
					categoryItem.getChildren().add(subCategoryItem);
				}
				
				root.getChildren().add(categoryItem);
				
				if (Boolean.valueOf(category.getAppend_divider())) {
					root.getChildren().add(createDivider());
				}
			}
			
			root.setExpanded(true);
			ttvCategories.setShowRoot(false);
			ttvCategories.setRoot(root);
		}
	}
	
	private TreeItem<JsonItem> createDivider() {
		JsonItem div = new JsonItem();
		String divStr = "-------------";
		div.setName(divStr);
		div.setDisplay_name(divStr);
		div.setIcon(divStr);
		
		return new TreeItem<JsonItem>(div);
	}

//	private void readCustoms(JsonReader jsonReader) {
//		TreeItem<Custom> rootCustoms = new TreeItem<Custom>();
//		
//		if (customFilePath != null) {
//			HashMap<String, List<Custom>> customs = jsonReader.readCustomFile(customFilePath);
//			HashMap<String, List<Custom>> sortedMap = 
//				      customs.entrySet().stream()
//									    .sorted(Entry.comparingByKey())
//									    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//			
//			for (String key : sortedMap.keySet()) {
//				Custom entryCustom = new Custom();
//				entryCustom.setName(key);
//				TreeItem<Custom> custom = new TreeItem<Custom>(entryCustom);
//				
//				for (Custom sc : sortedMap.get(key)) {
//					TreeItem<Custom> entry = new TreeItem<Custom>(sc);
//					custom.getChildren().add(entry);						
//				}
//				
//				rootCustoms.getChildren().add(custom);
//			}
//			
//			rootCustoms.setExpanded(true);
//			ttvCustoms.setShowRoot(false);
//			ttvCustoms.setRoot(rootCustoms);
//		}
//	}

	@FXML
	private void onBtnBrowseClicked() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("JSON Dir auswählen...");

		File selectedDir = directoryChooser.showDialog(null);
		
		if (selectedDir != null) {
			boolean customFileExist = false;
			boolean categoryFileExist = false;
			
			File[] list = selectedDir.listFiles();
			if (list != null) {
				for (File file : list)
		        {
		            if (!file.isDirectory()) {
		                if (file.getName().equals("custom.json")) {
		                	customFilePath = file;
		                	customFileExist = true;
		                }
		                if (file.getName().equals("categories.json")) {
		                	categoryFilePath = file;
		                	categoryFileExist = true;
		                }
		            }
		        }
			}
			
			btnReadFile.setDisable(!(customFileExist && categoryFileExist));
			tfFileChooser.setText(selectedDir.getAbsolutePath());
		}       
	}
	
	private void showError(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(e.getMessage());
		alert.setResizable(true);

		VBox dialogPaneContent = new VBox();
		
		Label label = new Label("Stack Trace:");

		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        
        String stackTrace = sw.toString();
        
     	TextArea textArea = new TextArea();
		textArea.setText(stackTrace);
		dialogPaneContent.getChildren().addAll(label, textArea);

		// Set content for Dialog Pane
		alert.getDialogPane().setContent(dialogPaneContent);
		alert.showAndWait();
	}
}
