package homeHubReader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeHubReaderApp extends Application {

//	static {
//        InputStream stream = RechnungsAppFx.class.getClassLoader().getResourceAsStream("logging.properties");
//
//        try {
//            LogManager.getLogManager().readConfiguration(stream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

	@Override
	public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mainView.fxml")); 

        Parent root = fxmlLoader.load();
        
		Scene mainScene = new Scene(root);

		Stage mainWindow = new Stage();
		mainWindow.setScene(mainScene);
		mainWindow.setTitle("HomeHubReader");
		mainWindow.setMaximized(true);
		mainWindow.setOnCloseRequest(e -> {
			e.consume();
			mainWindow.close();
		});
				
		mainWindow.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
