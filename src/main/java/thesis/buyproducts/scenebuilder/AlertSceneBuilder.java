package thesis.buyproducts.scenebuilder;

import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import thesis.buyproducts.restcall.model.ExceptionDetails;
import thesis.buyproducts.restcall.model.FieldErrorDto;
import thesis.buyproducts.restcall.model.ValidationErrorDto;

public class AlertSceneBuilder {

	public void popUpAlertWindowWhenPurchaseOrUsePointsIsInvalid(String message, Stage primaryWindow) {
		Stage alertWindow = new Stage();
		alertWindow.initModality(Modality.WINDOW_MODAL);

		alertWindow.setTitle("Warning");
		alertWindow.setMinWidth(250);
		VBox layout = new VBox(10);
		Label userNameErrorMessage = new Label();
		userNameErrorMessage.setText(message.split("\n")[0]);
		layout.getChildren().add(userNameErrorMessage);
		if (message.split("\n").length > 1) {
			Label amountErrorMessage = new Label();
			amountErrorMessage.setText(message.split("\n")[1]);
			layout.getChildren().add(amountErrorMessage);
		}
		Scene scene = new Scene(layout);
		alertWindow.setScene(scene);
		alertWindow.initOwner(primaryWindow.getScene().getWindow());
		scene.getStylesheets().add("DarkTheme.css");
		alertWindow.show();
	}

	public void popUpAlertWindowWhenRegsiteringUserUserIsInValid(String message, Stage primaryWindow) {
		Stage alertWindow = new Stage();
		alertWindow.initModality(Modality.WINDOW_MODAL);
		alertWindow.setTitle("Warning");
		alertWindow.setMinWidth(250);
		VBox layout = new VBox(25);
		Label firstNameErrorMessage = new Label();
		firstNameErrorMessage.setText(message.split("\n")[0]);
		layout.getChildren().add(firstNameErrorMessage);
		if (message.split("\n").length > 1) {
			Label lastNameErrormessage = new Label();
			lastNameErrormessage.setText(message.split("\n")[1]);
			layout.getChildren().add(lastNameErrormessage);
		}
		if (message.split("\n").length > 2) {
			Label userNameErrorMessage = new Label();
			userNameErrorMessage.setText(message.split("\n")[2]);
			layout.getChildren().add(userNameErrorMessage);
		}
		Scene scene = new Scene(layout);
		alertWindow.setScene(scene);
		alertWindow.initOwner(primaryWindow.getScene().getWindow());
		scene.getStylesheets().add("DarkTheme.css");
		alertWindow.show();
	}

	public void popUpAlertWindowWhenUpdatinConfigurationIsInvalid(String message, Stage primaryWindow) {
		Stage alertWindow = new Stage();

		alertWindow.initModality(Modality.WINDOW_MODAL);
		alertWindow.setTitle("Warning");
		alertWindow.setMinHeight(250);

		VBox layout = new VBox(10);
		Label errorMessage = new Label();
		errorMessage.setText(message);
		layout.getChildren().add(errorMessage);

		Scene scene = new Scene(layout);
		alertWindow.setScene(scene);
		scene.getStylesheets().add("DarkTheme.css");
		alertWindow.initOwner(primaryWindow.getScene().getWindow());
		alertWindow.show();
	}

	public void popUpAlertWindowInvalidData(ValidationErrorDto validationErrorVO, Stage primaryWindow) {
		Stage alertWindow = new Stage();
		alertWindow.initModality(Modality.WINDOW_MODAL);
		alertWindow.setTitle("Error registring client!");

		ImageView errorImageView;

		VBox layout = new VBox(10);
		Label errorMessage;
		for (FieldErrorDto fieldErrorDto : validationErrorVO.getListOfErrors()) {
			errorMessage = new Label();
			errorImageView = new ImageView("Error.png");
			errorImageView.setFitHeight(30);
			errorImageView.setFitWidth(30);
			errorMessage.setGraphic(errorImageView);
			errorMessage.setText(fieldErrorDto.getMessage());
			errorMessage.setContentDisplay(ContentDisplay.RIGHT);
			layout.getChildren().add(errorMessage);
		}
		Scene scene = new Scene(layout);
		alertWindow.setScene(scene);
		scene.getStylesheets().add("DarkTheme.css");
		alertWindow.initOwner(primaryWindow.getScene().getWindow());
		alertWindow.show();
	}

	public void popUpWindowUserNameNotUnique(ExceptionDetails exceptionDetails, Stage primaryWindow) {

		Stage alertWindow = new Stage();
		alertWindow.initModality(Modality.WINDOW_MODAL);
		alertWindow.setTitle("Error registring client!");

		VBox layout = new VBox(10);

		Label errorMessage = new Label();
		errorMessage.setText(exceptionDetails.getMessage());

		ImageView imageViewErrorMessage = new ImageView("Error.png");
		imageViewErrorMessage.setFitHeight(30);
		imageViewErrorMessage.setFitWidth(30);
		errorMessage.setGraphic(imageViewErrorMessage);
		errorMessage.setContentDisplay(ContentDisplay.RIGHT);

		Label errorTime = new Label();
		errorTime.setText("Error time: " + exceptionDetails.getTimestamp().toString());

		layout.getChildren().add(errorMessage);
		layout.getChildren().add(errorTime);

		Scene scene = new Scene(layout);
		alertWindow.setScene(scene);
		scene.getStylesheets().add("DarkTheme.css");
		alertWindow.initOwner(primaryWindow.getScene().getWindow());
		alertWindow.show();
	}

	public void popUpAlertWhenProcessingPointsFail(Stage primaryWindow, ExceptionDetails exception) {

		Stage alertWindow = new Stage();
		alertWindow.initModality(Modality.WINDOW_MODAL);
		alertWindow.setTitle("Error registring client!");

		VBox layout = new VBox(10);

		Label errorMessage = new Label();
		errorMessage.setText(exception.getMessage());

		ImageView imageViewErrorMessage = new ImageView("Error.png");
		imageViewErrorMessage.setFitHeight(30);
		imageViewErrorMessage.setFitWidth(30);
		errorMessage.setGraphic(imageViewErrorMessage);
		errorMessage.setContentDisplay(ContentDisplay.RIGHT);

		Label errorTime = new Label();
		errorTime.setText("Error time: " + exception.getTimestamp().toString());

		layout.getChildren().add(errorMessage);
		layout.getChildren().add(errorTime);

		Scene scene = new Scene(layout);
		alertWindow.setScene(scene);
		scene.getStylesheets().add("DarkTheme.css");
		alertWindow.initOwner(primaryWindow.getScene().getWindow());
		alertWindow.show();

	}

	public void popUpAlertWindowWhenSearchUserNotFound(Stage primaryWindow, ExceptionDetails exception) {
		Stage alertWindow = new Stage();
		alertWindow.initModality(Modality.WINDOW_MODAL);
		alertWindow.setTitle("CustomerDto not found!");

		VBox layout = new VBox(10);

		System.out.println(exception);

		Label message = new Label(exception.getMessage());
		Label timeStamp = new Label(exception.getTimestamp().toString());

		layout.getChildren().add(message);
		layout.getChildren().add(timeStamp);

		Scene scene = new Scene(layout);
		alertWindow.setScene(scene);
		scene.getStylesheets().add("DarkTheme.css");
		alertWindow.initOwner(primaryWindow.getScene().getWindow());
		alertWindow.show();
	}

	public void popUpAlertWindowTextAreEmpt(String message, Stage primaryWindow) {

		Stage alertWindow = new Stage();
		alertWindow.initModality(Modality.WINDOW_MODAL);
		alertWindow.setTitle("Warning");

		VBox layout = new VBox(10);
		Label messageLabel = new Label();
		messageLabel.setText(message);

		layout.getChildren().add(messageLabel);

		Scene scene = new Scene(layout);
		alertWindow.setScene(scene);

		alertWindow.initOwner(primaryWindow.getScene().getWindow());
		scene.getStylesheets().add("DarkTheme.css");
		alertWindow.show();

	}

	public void popUpAlertWhenPurchaseDataInvalid(Stage primaryWindow, ExceptionDetails exception) {
		Stage alertWindow = new Stage();
		alertWindow.initModality(Modality.WINDOW_MODAL);
		alertWindow.setTitle("CustomerDto not found!");

		VBox layout = new VBox(10);

		Label message = new Label(exception.getMessage());
		Label timeStamp = new Label(exception.getTimestamp().toString());

		layout.getChildren().add(message);
		layout.getChildren().add(timeStamp);

		Scene scene = new Scene(layout);
		alertWindow.setScene(scene);
		scene.getStylesheets().add("DarkTheme.css");
		alertWindow.initOwner(primaryWindow.getScene().getWindow());
		alertWindow.show();
	}

	public void popUpAlertWindowUpdatePoingMappingOK(Stage primaryWindow) {
		Stage alertWindow = new Stage();
		alertWindow.initModality(Modality.WINDOW_MODAL);
		alertWindow.setTitle("CustomerDto not found!");

		VBox layout = new VBox(10);

		Label tickImage = new Label();

		ImageView imageView = new ImageView("Tick.png");
		tickImage.setGraphic(imageView);

		layout.getChildren().add(tickImage);

		Scene scene = new Scene(layout);
		alertWindow.setScene(scene);
		scene.getStylesheets().add("DarkTheme.css");
		alertWindow.initOwner(primaryWindow.getScene().getWindow());
		alertWindow.show();

	}

}
