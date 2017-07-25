package thesis.buyproducts.scenebuilder;

import static thesis.buyproducts.constant.Constants.getWindowsHeight;
import static thesis.buyproducts.constant.Constants.getWindowsWidth;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UserRegistredSucessfullySceneBuilder {

	private MainSceneBuilder mainSceneBuilder;

	public Scene createScene(Stage primaryWindow, String firstName, String lastName, String userName, Double points) {

		BorderPane rootNode = new BorderPane();

		GridPane mainLayout = new GridPane();

		Label firstNameLabel = new Label("First Name: ");
		firstNameLabel.setMinSize(270, 60);

		Label lastNameLabel = new Label("Last Name: ");
		lastNameLabel.setMinSize(270, 60);

		Label userNameLabel = new Label("CustomerDto Name: ");
		userNameLabel.setMinSize(270, 60);

		Label pointLabel = new Label("Points: ");
		pointLabel.setMinSize(270, 60);

		mainLayout.add(firstNameLabel, 0, 0);
		mainLayout.add(lastNameLabel, 0, 1);
		mainLayout.add(userNameLabel, 0, 2);
		mainLayout.add(pointLabel, 0, 3);

		Label firstname = new Label(firstName);
		firstname.setMinSize(270, 60);

		Label lastname = new Label(lastName);
		lastname.setMinSize(270, 60);

		Label username = new Label(userName);
		username.setMinSize(270, 60);

		Label pointslabel = new Label(points.toString());
		pointslabel.setMinSize(270, 60);

		mainLayout.add(firstname, 1, 0);
		mainLayout.add(lastname, 1, 1);
		mainLayout.add(username, 1, 2);
		mainLayout.add(pointslabel, 1, 3);

		Label dummyLabel = new Label("");
		dummyLabel.setMinSize(270, 60);
		dummyLabel.setGraphic(new ImageView("Tick.png"));

		Label dummyLabel1 = new Label();
		dummyLabel1.setMinSize(270, 60);

		BorderPane.setAlignment(dummyLabel1, Pos.CENTER_LEFT);
		rootNode.setLeft(dummyLabel1);

		BorderPane.setAlignment(dummyLabel, Pos.TOP_CENTER);
		rootNode.setTop(dummyLabel);

		BorderPane.setAlignment(mainLayout, Pos.CENTER);
		rootNode.setCenter(mainLayout);

		Button homeButton = new Button();
		homeButton.setMinSize(270, 60);
		ImageView imageViewHomeButton = new ImageView("Forward.png");
		imageViewHomeButton.setFitHeight(40);
		imageViewHomeButton.setFitWidth(40);

		homeButton.setGraphic(imageViewHomeButton);
		homeButton.setOnAction(e -> {

			if (mainSceneBuilder == null) {
				mainSceneBuilder = new MainSceneBuilder();
			}
			primaryWindow.setScene(mainSceneBuilder.createScene(primaryWindow));
		});

		BorderPane.setAlignment(homeButton, Pos.BOTTOM_LEFT);
		rootNode.setRight(homeButton);

		Scene scene = new Scene(rootNode, getWindowsWidth() - 200, getWindowsHeight() - 200);
		scene.getStylesheets().add("DarkTheme.css");
		rootNode.requestFocus();

		return scene;
	}

}
