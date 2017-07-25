package thesis.buyproducts.scenebuilder;

import static thesis.buyproducts.constant.Constants.getWindowsHeight;
import static thesis.buyproducts.constant.Constants.getWindowsWidth;

import java.sql.Timestamp;
import java.util.Map;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import thesis.buyproducts.restcall.model.CustomerDto;
import thesis.buyproducts.restcall.model.ExceptionDetails;
import thesis.buyproducts.restcall.strategy.ConfigurationRequestsStrategy;
import thesis.buyproducts.restcall.strategy.UserRequestsStrategy;

public class MainSceneBuilder {

	private PurchaseSceneBuilder purchaseSceneBuilder;
	private BuyWithPointsSceneBuilder buyWithPointsSceneBuilder;
	private RegisterNewUserOrEditUserSceneBuilder registerNewUserSceneBuilder;
	private ConfigurationSceneBuilder configurationSceneBuilder;
	private AlertSceneBuilder alertSceneBuilder;

	public Scene createScene(Stage primaryWindow) {

		BorderPane rootNodeOfMainWindow = new BorderPane();

		Button purchaseButton = new Button("Buy");
		purchaseButton.setMaxSize(270, 80);
		purchaseButton.setMinSize(270, 80);

		ImageView imageViewPurchaseButton = new ImageView("Buy.png");
		imageViewPurchaseButton.setFitHeight(40);
		imageViewPurchaseButton.setFitWidth(40);

		purchaseButton.setGraphic(imageViewPurchaseButton);

		purchaseButton.setOnAction(e -> {

			if (purchaseSceneBuilder == null) {
				purchaseSceneBuilder = new PurchaseSceneBuilder();
			}
			primaryWindow.setScene(purchaseSceneBuilder.createScene(primaryWindow));
		});

		Button buyWithPointsButton = new Button("Buy with points");
		buyWithPointsButton.setMaxSize(270, 80);
		buyWithPointsButton.setMinSize(270, 80);

		ImageView imageViewBuyWithPoints = new ImageView("BuyWithPoints.png");
		imageViewBuyWithPoints.setFitHeight(45);
		imageViewBuyWithPoints.setFitWidth(45);

		buyWithPointsButton.setGraphic(imageViewBuyWithPoints);

		buyWithPointsButton.setOnAction(e -> {

			if (buyWithPointsSceneBuilder == null) {
				buyWithPointsSceneBuilder = new BuyWithPointsSceneBuilder();
			}
			primaryWindow.setScene(buyWithPointsSceneBuilder.createScene(primaryWindow));
		});

		Button registerNewUser = new Button("Add customer");
		registerNewUser.setMaxSize(270, 80);
		registerNewUser.setMinSize(270, 80);

		ImageView saveImage = new ImageView("Save.png");
		saveImage.setFitWidth(40);
		saveImage.setFitHeight(40);
		registerNewUser.setGraphic(saveImage);

		registerNewUser.setOnAction(e -> {
			if (registerNewUserSceneBuilder == null) {
				registerNewUserSceneBuilder = new RegisterNewUserOrEditUserSceneBuilder();
			}
			primaryWindow.setScene(registerNewUserSceneBuilder.createSceneBuilder(primaryWindow, null, false));
		});

		Button configureButton = new Button("Configure");
		configureButton.setMaxSize(270, 80);
		configureButton.setMinSize(270, 80);

		ImageView imageViewConfigurationButton = new ImageView("Configuration.png");
		imageViewConfigurationButton.setFitHeight(40);
		imageViewConfigurationButton.setFitWidth(40);
		configureButton.setGraphic(imageViewConfigurationButton);

		configureButton.setOnAction(e -> {
			if (configurationSceneBuilder == null) {
				configurationSceneBuilder = new ConfigurationSceneBuilder();
			}
			Map<Integer, Object> getPointMappingRes = ConfigurationRequestsStrategy.getPointMapping();
			if (getPointMappingRes.containsKey(new Integer(200))) {
				String message = "Point mapping: ";
				Double pointMapping = (Double) getPointMappingRes.get(new Integer(200));
				if (pointMapping.doubleValue() == 0) {
					message += "Default value!";
				} else {
					message += pointMapping.toString();
				}
				System.out.println(pointMapping);
				primaryWindow.setScene(configurationSceneBuilder.createScene(primaryWindow, message));

			}
		});

		TilePane buttonGrid = new TilePane(Orientation.VERTICAL);
		buttonGrid.setVgap(25);
		buttonGrid.setHgap(25);

		buttonGrid.getChildren().addAll(purchaseButton, buyWithPointsButton, registerNewUser, configureButton);

		BorderPane.setAlignment(buttonGrid, Pos.CENTER);
		rootNodeOfMainWindow.setCenter(buttonGrid);

		Label label = new Label();
		label.setMinSize(270, 80);
		label.setMaxSize(270, 80);

		Label label1 = new Label();
		label1.setMinSize(270, 80);

		rootNodeOfMainWindow.setLeft(label);
		BorderPane.setAlignment(label, Pos.CENTER);

		rootNodeOfMainWindow.setTop(label1);
		BorderPane.setAlignment(label1, Pos.CENTER_LEFT);

		Label label2 = new Label();
		label2.setMinSize(270, 80);
		label2.setMaxSize(270, 80);

		BorderPane.setAlignment(label2, Pos.CENTER);
		rootNodeOfMainWindow.setBottom(label2);

		FlowPane rightLayout = new FlowPane();

		rightLayout.setHgap(10);
		rightLayout.setVgap(10);

		TextField searchUserTextArea = new TextField();
		searchUserTextArea.setMinSize(270, 35);
		searchUserTextArea.setMaxSize(270, 35);
		searchUserTextArea.setPromptText("Search");

		searchUserTextArea.setOnAction(e -> {

			if (searchUserTextArea.getText() == null || searchUserTextArea.getText().isEmpty()) {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				String message = "Please type the username";
				alertSceneBuilder.popUpAlertWindowTextAreEmpt(message, primaryWindow);
				return;
			}

			Map<Integer, Object> response = UserRequestsStrategy
					.findUserByUserNameRequest(searchUserTextArea.getText());

			if (response.containsKey(new Integer(302))) {
				if (registerNewUserSceneBuilder == null) {
					registerNewUserSceneBuilder = new RegisterNewUserOrEditUserSceneBuilder();
				}
				CustomerDto customerDto = (CustomerDto) response.get(new Integer(302));
				primaryWindow.setScene(registerNewUserSceneBuilder.createSceneBuilder(primaryWindow, customerDto, true));
			} else if (response.containsKey(new Integer(404))) {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				ExceptionDetails exception = (ExceptionDetails) response.get(new Integer(404));
				alertSceneBuilder.popUpAlertWindowWhenSearchUserNotFound(primaryWindow, exception);
			} else {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				ExceptionDetails exception = new ExceptionDetails();
				exception.setMessage("The server might be temporary down!");
				exception.setTimestamp(new Timestamp(System.currentTimeMillis()));
				alertSceneBuilder.popUpAlertWindowWhenSearchUserNotFound(primaryWindow, exception);
			}
			return;
		});

		BorderPane.setAlignment(rightLayout, Pos.TOP_LEFT);

		rightLayout.getChildren().add(searchUserTextArea);

		Button dummyButton = new Button();
		dummyButton.setId("dummyButton");
		rightLayout.getChildren().add(dummyButton);

		rootNodeOfMainWindow.setRight(rightLayout);

		Scene scene = new Scene(rootNodeOfMainWindow, getWindowsWidth(), getWindowsHeight());
		scene.getStylesheets().add("DarkTheme.css");
		return scene;
	}
}
