package homeHubReader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

public class HomeHubReaderController implements Initializable {

	@FXML private Button btnButton1;
	@FXML private Button btnButton2;

	@FXML private TreeTableView<String> treeTableView  = new TreeTableView<String>();
	
	public final static String categoriesPath = "categories.json";
	public final static String customPath = "custom.json";
	
	public TreeItem<String> root = new TreeItem<String>("Categories");

	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TreeTableColumn<String, String> colName = new TreeTableColumn<String, String>("Name");
//		TreeTableColumn<String, String> colIcon = new TreeTableColumn<String, String>("Icon");
//		TreeTableColumn<String, String> colAppendDivider = new TreeTableColumn<String, String>("Append Divider");

		treeTableView.getColumns().addAll(colName);
		
		colName.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> param) -> new SimpleStringProperty(param.getValue().getValue()));
//		colName.setCellValueFactory(new TreeItemPropertyValueFactory<String, String>("name"));
//		colIcon.setCellValueFactory(new TreeItemPropertyValueFactory<String, String>("icon"));
//		colAppendDivider.setCellValueFactory(new TreeItemPropertyValueFactory<String, String>("append_divider"));

//		Category ug = new Category();
//		ug.setName("Untergeschoss");
//		ug.setIcon("icon1.png");
//		ug.setAppend_divider("true");
////		
//		Category eg = new Category();
//		eg.setName("Erdgeschoss");
//		eg.setIcon("icon2.png");
//		eg.setAppend_divider("false");
////				
////		Category garten = new Category();
////		garten.setName("Garten");
////		garten.setIcon("icon3.png");
////		garten.setAppend_divider("true");
//		
		 
		// Set root Item for Tree
	}
	
	@FXML
	private void onBtnButton1Clicked() {
		JsonReader jsonReader = new JsonReader();
		List<Category> categories = jsonReader.readCategoriesFile(categoriesPath);
		
		for (Category c : categories) {
			TreeItem<String> category = new TreeItem<String>(c.getName());
			
			for (SubCategory su : c.getSubcategories()) {
				category.getChildren().add(new TreeItem<String>(su.getName()));
			}
			
			root.getChildren().add(category);
		}
		
		treeTableView.setRoot(root);
	}
	
	@FXML
	private void onBtnButton2Clicked() {
		
	}
}
