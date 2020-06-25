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

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainView.fxml")); 

		Scene mainScene = new Scene(root);

		Stage mainWindow = new Stage();
		mainWindow.setScene(mainScene);
		mainWindow.setTitle("HomeHubReader");
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
