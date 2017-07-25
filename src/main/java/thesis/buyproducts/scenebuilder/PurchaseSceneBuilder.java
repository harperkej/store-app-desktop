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
import thesis.buyproducts.restcall.strategy.UserRequestsStrategy;

public class PurchaseSceneBuilder {

	private MainSceneBuilder mainSceneBuilder;

	private AlertSceneBuilder alertSceneBuilder;

	private UserRegistredSucessfullySceneBuilder userRegistredSucessfullySceneBuilder;
	
	public Scene createScene(Stage primaryWindow) {

		BorderPane rootNodeOfPurchaseWindow = new BorderPane();

		Button backButton = new Button("Back");
		backButton.setMinSize(270, 60);

		ImageView imageViewBackButton = new ImageView("Back.png");
		backButton.setGraphic(imageViewBackButton);

		backButton.setOnAction(e -> {

			if (mainSceneBuilder == null) {
				mainSceneBuilder = new MainSceneBuilder();
			}
			primaryWindow.setScene(mainSceneBuilder.createScene(primaryWindow));
		});

		TextField userNameTextField = new TextField();
		userNameTextField.setPromptText("CustomerDto name");

		TextField amountTextField = new TextField();
		amountTextField.setPromptText("Amount");

		Button processPurchaseButton = new Button("Process Purchase");
		processPurchaseButton.setMinSize(270, 60);

		ImageView imageViewprocessPurchaseButton = new ImageView("Tick.png");
		imageViewprocessPurchaseButton.setFitWidth(40);
		imageViewprocessPurchaseButton.setFitHeight(40);
		processPurchaseButton.setGraphic(imageViewprocessPurchaseButton);

		processPurchaseButton.setOnAction(e -> {
			String answerWhenPurchaseDataAreError = "";
			boolean isNotDoubleAmount = true;
			String userName = userNameTextField.getText();
			String amount = amountTextField.getText();
			try {
				Double.parseDouble(amount.trim());
			} catch (Exception ex) {
				isNotDoubleAmount = false;
			}
			if (userName == null || userName.isEmpty() || !isNotDoubleAmount) {

				if (userName == null || userName.isEmpty()) {
					answerWhenPurchaseDataAreError = "Please type the username of the user!\n";
				}
				if (!isNotDoubleAmount) {
					answerWhenPurchaseDataAreError += "Not a valid amount!";
				}
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				alertSceneBuilder.popUpAlertWindowWhenPurchaseOrUsePointsIsInvalid(answerWhenPurchaseDataAreError,
						primaryWindow);
				return;
			}

			Map<Integer, Object> purchaseReusltRequest = UserRequestsStrategy.processPurchaseRequest(userName,
					Double.parseDouble(amount));

			if (purchaseReusltRequest.containsKey(new Integer(200))) {
				CustomerDto customerDtoDetails = (CustomerDto) purchaseReusltRequest.get(new Integer(200));
				if (userRegistredSucessfullySceneBuilder == null) {
					userRegistredSucessfullySceneBuilder = new UserRegistredSucessfullySceneBuilder();
				}
				primaryWindow.setScene(
						userRegistredSucessfullySceneBuilder.createScene(primaryWindow, customerDtoDetails.getFirstName(),
								customerDtoDetails.getLastName(), customerDtoDetails.getUserName(), customerDtoDetails.getPoints()));
			} else if (purchaseReusltRequest.containsKey(new Integer(409))) {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				ExceptionDetails exception = (ExceptionDetails) purchaseReusltRequest.get(new Integer(409));
				alertSceneBuilder.popUpAlertWhenPurchaseDataInvalid(primaryWindow, exception);
			} else if (purchaseReusltRequest.containsKey(new Integer(404))) {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				ExceptionDetails exception = (ExceptionDetails) purchaseReusltRequest.get(new Integer(404));
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

		TilePane buttonAndTextFieldGridOfPurchaseWindow = new TilePane();
		buttonAndTextFieldGridOfPurchaseWindow.setOrientation(Orientation.VERTICAL);

		buttonAndTextFieldGridOfPurchaseWindow.setHgap(25);
		buttonAndTextFieldGridOfPurchaseWindow.setVgap(25);

		buttonAndTextFieldGridOfPurchaseWindow.getChildren().addAll(userNameTextField, amountTextField,
				processPurchaseButton);

		BorderPane.setAlignment(buttonAndTextFieldGridOfPurchaseWindow, Pos.CENTER_RIGHT);
		rootNodeOfPurchaseWindow.setCenter(buttonAndTextFieldGridOfPurchaseWindow);

		FlowPane leftLayout = new FlowPane();

		Label label = new Label();
		label.setMinSize(270, 60);
		leftLayout.getChildren().add(label);

		leftLayout.getChildren().add(backButton);

		BorderPane.setAlignment(leftLayout, Pos.BASELINE_LEFT);
		rootNodeOfPurchaseWindow.setLeft(leftLayout);

		Label label1 = new Label();
		label1.setMinSize(270, 60);
		BorderPane.setAlignment(label1, Pos.CENTER_RIGHT);
		rootNodeOfPurchaseWindow.setRight(label1);

		Label label2 = new Label();
		label2.setMinSize(270, 80);
		BorderPane.setAlignment(label2, Pos.BOTTOM_CENTER);
		rootNodeOfPurchaseWindow.setTop(label2);

		Scene scene = new Scene(rootNodeOfPurchaseWindow, getWindowsWidth(), getWindowsHeight());
		scene.getStylesheets().add("DarkTheme.css");
		rootNodeOfPurchaseWindow.requestFocus();
		return scene;
	}
}
