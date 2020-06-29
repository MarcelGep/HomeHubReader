package homeHubReader;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContentDisplay;
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
	private TreeTableView<Custom> ttvCustoms = new TreeTableView<Custom>();
	@FXML
	private TreeTableView<Category> ttvCategories = new TreeTableView<Category>();
	
	public static File customFilePath;
	public static File categoryFilePath;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCustomTable();
		initCategoryTable();
	}
	
	@SuppressWarnings("unchecked")
	private void initCategoryTable() {
		TreeTableColumn<Category, String> colName1 = new TreeTableColumn<Category, String>("Name");
		TreeTableColumn<Category, String> colDisplayName1 = new TreeTableColumn<Category, String>("Display Name");
		TreeTableColumn<Category, String> colIcon1 = new TreeTableColumn<Category, String>("Icon");
		TreeTableColumn<Category, String> colAppendDivider1 = new TreeTableColumn<Category, String>("Divider");
		ttvCategories.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		ttvCategories.getColumns().addAll(colName1, colDisplayName1, colIcon1, colAppendDivider1);
		
		colName1.setMinWidth(100);		
		colName1.setCellValueFactory(new TreeItemPropertyValueFactory<Category, String>("name"));
		colDisplayName1.setCellValueFactory(new TreeItemPropertyValueFactory<Category, String>("display_name"));
		colIcon1.setCellValueFactory(new TreeItemPropertyValueFactory<Category, String>("icon"));
		colAppendDivider1.setCellValueFactory(new TreeItemPropertyValueFactory<Category, String>("append_divider"));
		colAppendDivider1.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();

            TreeTableCell<Category, String> cell = new TreeTableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                	if (empty || getTreeTableRow().getTreeItem().getChildren().size() == 0) {
                        setGraphic(null);
                    } else {
                        checkBox.setSelected(Boolean.valueOf(item));
                        setGraphic(checkBox);
                    }
                }
            };

            checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) ->
            {
                Category category = cell.getTreeTableRow().getItem();

                if (category != null) {
                	category.setAppend_divider(isSelected.toString());
                }
            });

            return cell ;
        });
	}

	@SuppressWarnings("unchecked")
	private void initCustomTable() {
		TreeTableColumn<Custom, String> colName = new TreeTableColumn<Custom, String>("Name");
		TreeTableColumn<Custom, String> colDisplayName = new TreeTableColumn<Custom, String>("Display Name");
		TreeTableColumn<Custom, String> colIcon = new TreeTableColumn<Custom, String>("Icon");
		TreeTableColumn<Custom, String> colColor = new TreeTableColumn<Custom, String>("Farbe");
		TreeTableColumn<Custom, String> colAppendDivider = new TreeTableColumn<Custom, String>("Divider");
		ttvCustoms.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		ttvCustoms.getColumns().addAll(colName, colDisplayName, colIcon, colColor, colAppendDivider);
        
		colName.setCellValueFactory(new TreeItemPropertyValueFactory<Custom, String>("name"));
		colDisplayName.setCellValueFactory(new TreeItemPropertyValueFactory<Custom, String>("display_name"));
		colIcon.setCellValueFactory(new TreeItemPropertyValueFactory<Custom, String>("icon"));
		colAppendDivider.setCellValueFactory(new TreeItemPropertyValueFactory<Custom, String>("append_divider"));
		colAppendDivider.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();

            TreeTableCell<Custom, String> cell = new TreeTableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                	if (empty || getTreeTableRow().getTreeItem().getChildren().size() > 0) {
                        setGraphic(null);
                    } else {
                        checkBox.setSelected(Boolean.valueOf(item));
                        setGraphic(checkBox);
                    }
                }
            };

            checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) ->
            {
            	Custom custom = cell.getTreeTableRow().getItem();

                if (custom != null) {
                	custom.setAppend_divider(isSelected.toString());
                }
            });

            return cell ;
        });
		
		colColor.setCellValueFactory(new TreeItemPropertyValueFactory<Custom, String>("color"));
		colColor.setCellFactory(ColorTableCell::new);
	}

	public class ColorTableCell<T> extends TreeTableCell<T, Color> {    
	    private final ColorPicker colorPicker;

	    public ColorTableCell(TreeTableColumn<T, Color> column) {
	        this.colorPicker = new ColorPicker();
	        this.colorPicker.editableProperty().bind(column.editableProperty());
	        this.colorPicker.disableProperty().bind(column.editableProperty().not());
	        this.colorPicker.setOnShowing(event -> {
	            final TreeTableView<T> treeTableView = getTreeTableView();
	            treeTableView.getSelectionModel().select(getTreeTableRow().getIndex());
	            treeTableView.edit(treeTableView.getSelectionModel().getSelectedIndex(), column);       
	        });
	        this.colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
	            if(isEditing()) {
	                commitEdit(newValue);
	            }
	        });     
	        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	    }

	    @Override
	    protected void updateItem(Color item, boolean empty) {
	        super.updateItem(item, empty);  

	        setText(null);  
	        
	        if(empty || item == null) {     
	            setGraphic(null);
	        } else {        
	            this.colorPicker.setValue(item);
	            this.setGraphic(this.colorPicker);
	        } 
	    }
	}

	@FXML
	private void onBtnReadFileClicked() {
		try {
			JsonReader jsonReader = new JsonReader();
			readCategories(jsonReader);
			readCustoms(jsonReader);
		} catch (Exception e) {
			showError(e);
			e.printStackTrace();
		}
	}

	private void readCategories(JsonReader jsonReader) {
		if (categoryFilePath != null) {			
			List<Category> categories = jsonReader.readCategoriesFile(categoryFilePath);
			
			TreeItem<Category> rootCategories = new TreeItem<Category>();
		
			for (Category c : categories) {
				
				TreeItem<Category> entryCategory = new TreeItem<Category>(c);

				for (SubCategory su : c.getSubcategories()) {
					Category subCategory = new Category();
					subCategory.setName(su.getName());
					subCategory.setDisplay_name(su.getDisplay_name());
					
					TreeItem<Category> subCategroy = new TreeItem<Category>(subCategory);
					entryCategory.getChildren().add(subCategroy);
				}
				
				rootCategories.getChildren().add(entryCategory);
				
				if (Boolean.valueOf(c.getAppend_divider())) {
					Category div = new Category();
					div.setName("-------------");
					TreeItem<Category> divider = new TreeItem<Category>(div);
					rootCategories.getChildren().add(divider);
				}
			}
			
			rootCategories.setExpanded(true);
			ttvCategories.setShowRoot(false);
			ttvCategories.setRoot(rootCategories);
		}
	}

	private void readCustoms(JsonReader jsonReader) {
		TreeItem<Custom> rootCustoms = new TreeItem<Custom>();
		
		if (customFilePath != null) {
			HashMap<String, List<Custom>> customs = jsonReader.readCustomFile(customFilePath);
			HashMap<String, List<Custom>> sortedMap = 
				      customs.entrySet().stream()
									    .sorted(Entry.comparingByKey())
									    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			
			for (String key : sortedMap.keySet()) {
				Custom entryCustom = new Custom();
				entryCustom.setName(key);
				TreeItem<Custom> custom = new TreeItem<Custom>(entryCustom);
				
				for (Custom sc : sortedMap.get(key)) {
					TreeItem<Custom> entry = new TreeItem<Custom>(sc);
					custom.getChildren().add(entry);						
				}
				
				rootCustoms.getChildren().add(custom);
			}
			
			rootCustoms.setExpanded(true);
			ttvCustoms.setShowRoot(false);
			ttvCustoms.setRoot(rootCustoms);
		}
	}

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
