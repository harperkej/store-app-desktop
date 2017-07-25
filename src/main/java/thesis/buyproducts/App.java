package thesis.buyproducts;

import javafx.application.Application;
import javafx.stage.Stage;
import thesis.buyproducts.scenebuilder.MainSceneBuilder;

import static thesis.buyproducts.constant.Constants.TITLE_MAIN_WINDOW;

public class App extends Application {

    Stage primaryWindow;

    private MainSceneBuilder mainSceneBuilder;

    @Override
    public  void init() {
        mainSceneBuilder = new MainSceneBuilder();
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryWindow = stage;
        stage.setTitle(TITLE_MAIN_WINDOW);
        primaryWindow.setScene(mainSceneBuilder.createScene(primaryWindow));
        primaryWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
