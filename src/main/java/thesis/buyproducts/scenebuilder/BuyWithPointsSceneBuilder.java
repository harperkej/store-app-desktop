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
import thesis.buyproducts.restcall.model.ExceptionDetails;
import thesis.buyproducts.restcall.model.BuyWithPointsDto;
import thesis.buyproducts.restcall.strategy.UserRequestsStrategy;

public class BuyWithPointsSceneBuilder {

	private MainSceneBuilder mainSceneBuilder;

	private AlertSceneBuilder alertSceneBuilder;

	private UserAccountBuyWithPointsSceneBuilder userAccountBuyWithPointsSceneBuilder;

	public Scene createScene(Stage primaryWindow) {
		BorderPane rootNodeOfBuyWithPointsScene = new BorderPane();

		TilePane layout = new TilePane();
		layout.setOrientation(Orientation.VERTICAL);

		layout.setHgap(25);
		layout.setVgap(25);

		TextField userNameTextField = new TextField();
		userNameTextField.setPromptText("CustomerDto name");

		TextField amountTextField = new TextField();
		amountTextField.setPromptText("Amount");

		Button processBuyWithPointsButton = new Button("Process");
		processBuyWithPointsButton.setMinSize(270, 60);

		ImageView imageViewProcessBuyWithPointsButton = new ImageView("Tick.png");
		imageViewProcessBuyWithPointsButton.setFitHeight(40);
		imageViewProcessBuyWithPointsButton.setFitWidth(40);

		processBuyWithPointsButton.setGraphic(imageViewProcessBuyWithPointsButton);

		processBuyWithPointsButton.setOnAction(e -> {
			boolean isNotDoubleAmount = false;
			String message = "";
			String userName = userNameTextField.getText();
			String amount = amountTextField.getText();
			try {
				Double.parseDouble(amount);
			} catch (Exception ex) {
				isNotDoubleAmount = true;
			}
			if (userName == null || userName.isEmpty() || isNotDoubleAmount) {
				if (userName == null || userName.isEmpty()) {
					message = "Please type the username!\n";
				}
				if (isNotDoubleAmount) {
					message += "Not a valid amount!";
				}
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				alertSceneBuilder.popUpAlertWindowWhenPurchaseOrUsePointsIsInvalid(message, primaryWindow);
				return;
			}

			Map<Integer, Object> buyWithPointsResult = UserRequestsStrategy.buyWithPointsRequest(userName,
					Double.parseDouble(amount));
			if (buyWithPointsResult.containsKey(new Integer(200))) {
				if (userAccountBuyWithPointsSceneBuilder == null) {
					userAccountBuyWithPointsSceneBuilder = new UserAccountBuyWithPointsSceneBuilder();
				}
				BuyWithPointsDto userSatate = (BuyWithPointsDto) buyWithPointsResult.get(new Integer(200));

				primaryWindow.setScene(userAccountBuyWithPointsSceneBuilder.createScene(primaryWindow,
						userSatate.getFirstName(), userSatate.getLastName(), userSatate.getUsername(),
						userSatate.getPointsLeft(), userSatate.getHasToPay()));
			} else if (buyWithPointsResult.containsKey(new Integer(404))) {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				ExceptionDetails exception = (ExceptionDetails) buyWithPointsResult.get(new Integer(404));
				alertSceneBuilder.popUpAlertWindowWhenSearchUserNotFound(primaryWindow, exception);
			} else if (buyWithPointsResult.containsKey(new Integer(409))) {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				ExceptionDetails exception = (ExceptionDetails) buyWithPointsResult.get(new Integer(404));
				alertSceneBuilder.popUpAlertWhenPurchaseDataInvalid(primaryWindow, exception);
			} else if (buyWithPointsResult.containsKey(new Integer(400))) {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				ExceptionDetails exception = (ExceptionDetails) buyWithPointsResult.get(new Integer(400));
				alertSceneBuilder.popUpAlertWhenProcessingPointsFail(primaryWindow, exception);
			} else if (buyWithPointsResult.containsKey(new Integer(404))) {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				ExceptionDetails exception = (ExceptionDetails) buyWithPointsResult.get(new Integer(400));
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
		});

		Button backButton = new Button("Back");
		ImageView imageViewBackButton = new ImageView("Back.png");
		imageViewBackButton.setFitWidth(40);
		backButton.setMinSize(270, 60);
		imageViewBackButton.setFitHeight(40);

		backButton.setGraphic(imageViewBackButton);

		backButton.setOnAction(e -> {
			if (mainSceneBuilder == null) {
				mainSceneBuilder = new MainSceneBuilder();
			}
			primaryWindow.setScene(mainSceneBuilder.createScene(primaryWindow));
		});

		layout.getChildren().addAll(userNameTextField, amountTextField, processBuyWithPointsButton);

		BorderPane.setAlignment(layout, Pos.CENTER);
		rootNodeOfBuyWithPointsScene.setCenter(layout);

		FlowPane leftLayout = new FlowPane();

		Label dummyLabel = new Label();
		dummyLabel.setMinSize(270, 60);

		BorderPane.setAlignment(dummyLabel, Pos.TOP_CENTER);
		rootNodeOfBuyWithPointsScene.setTop(dummyLabel);

		Label dummyLabel1 = new Label();
		dummyLabel1.setMinSize(270, 60);

		leftLayout.getChildren().add(dummyLabel1);
		leftLayout.getChildren().add(backButton);

		BorderPane.setAlignment(leftLayout, Pos.BOTTOM_CENTER);
		rootNodeOfBuyWithPointsScene.setLeft(leftLayout);

		Scene scene = new Scene(rootNodeOfBuyWithPointsScene, getWindowsWidth(), getWindowsHeight());
		scene.getStylesheets().add("DarkTheme.css");
		rootNodeOfBuyWithPointsScene.requestFocus();
		return scene;
	}
	
}
