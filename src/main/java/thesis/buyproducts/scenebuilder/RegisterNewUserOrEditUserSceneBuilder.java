package thesis.buyproducts.scenebuilder;

import static thesis.buyproducts.constant.Constants.getWindowsHeight;
import static thesis.buyproducts.constant.Constants.getWindowsWidth;

import java.util.Iterator;
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
import thesis.buyproducts.restcall.model.ValidationErrorDto;
import thesis.buyproducts.restcall.strategy.UserRequestsStrategy;

public class RegisterNewUserOrEditUserSceneBuilder {

	private AlertSceneBuilder alertSceneBuilder;

	private MainSceneBuilder mainSceneBuilder;

	private UserRegistredSucessfullySceneBuilder userRegistredSucessfullySceneBuilder;

	public Scene createSceneBuilder(Stage primaryWindow, CustomerDto customerDtoToUpdate, boolean isUserBeingEdited) {
		BorderPane rootNodeOfRegisterNewUserScene = new BorderPane();

		TilePane mainLayout = new TilePane();
		mainLayout.setOrientation(Orientation.VERTICAL);
		mainLayout.setHgap(25);
		mainLayout.setVgap(25);

		TextField firstNameTextField = new TextField();
		if (isUserBeingEdited) {
			firstNameTextField.setText(customerDtoToUpdate.getFirstName());
		} else {
			firstNameTextField.setPromptText("First name");
		}

		TextField lastNameTextField = new TextField();
		if (isUserBeingEdited) {
			lastNameTextField.setText(customerDtoToUpdate.getLastName());
		} else {
			lastNameTextField.setPromptText("Last name");
		}

		TextField userNameTextField = new TextField();
		if (isUserBeingEdited) {
			userNameTextField.setText(customerDtoToUpdate.getUserName());
			userNameTextField.setDisable(true);
		} else {
			userNameTextField.setPromptText("Customer name");
		}

		Button registerOrEditUserButton = new Button();
		if (isUserBeingEdited) {
			registerOrEditUserButton.setText("Edit user");
		} else {
			registerOrEditUserButton.setText("Save");
		}
		registerOrEditUserButton.setMinSize(270, 60);

		Label tickLabel = new Label();
		tickLabel.setMinSize(270, 60);

		ImageView imageViewRegisterUserButton = new ImageView("Save.png");
		imageViewRegisterUserButton.setFitHeight(40);
		imageViewRegisterUserButton.setFitWidth(40);

		registerOrEditUserButton.setGraphic(imageViewRegisterUserButton);

		registerOrEditUserButton.setOnAction(e -> {
			String firstName = firstNameTextField.getText();
			String lastName = lastNameTextField.getText();
			String userName = userNameTextField.getText();
			if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || userName == null
					|| userName.isEmpty()) {
				String messageError = "";
				if (firstName == null || firstName.isEmpty()) {
					messageError = "Type the first name of the customerDto!!\n";
				}
				if (lastName == null || lastName.isEmpty()) {
					messageError += "Type the last name of the customerDto!!\n";
				}
				if (userName == null || userName.isEmpty()) {
					messageError += "Type the customerDto name of the customerDto!!";
				}
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				alertSceneBuilder.popUpAlertWindowWhenRegsiteringUserUserIsInValid(messageError, primaryWindow);
				return;
			}

			CustomerDto customerDto = new CustomerDto();
			customerDto.setFirstName(firstName);
			customerDto.setLastName(lastName);
			customerDto.setUserName(userName);
			if (isUserBeingEdited) {
				customerDto.setPoints(customerDtoToUpdate.getPoints());
			}

			if (isUserBeingEdited) {
				Map<Integer, Object> updateUserRequestResult = UserRequestsStrategy.updateUserRequest(customerDto);
				if (updateUserRequestResult.containsKey(new Integer(200))) {
					CustomerDto updatedCustomerDto = (CustomerDto) updateUserRequestResult.get(new Integer(200));
					if (userRegistredSucessfullySceneBuilder == null) {
						userRegistredSucessfullySceneBuilder = new UserRegistredSucessfullySceneBuilder();
					}
					primaryWindow.setScene(
							userRegistredSucessfullySceneBuilder.createScene(primaryWindow, updatedCustomerDto.getFirstName(),
									updatedCustomerDto.getLastName(), updatedCustomerDto.getUserName(), updatedCustomerDto.getPoints()));
				} else if (updateUserRequestResult.containsKey(new Integer(404))) {
					if (alertSceneBuilder == null) {
						alertSceneBuilder = new AlertSceneBuilder();
					}
					ExceptionDetails exception = (ExceptionDetails) updateUserRequestResult.get(new Integer(404));
					alertSceneBuilder.popUpAlertWindowWhenSearchUserNotFound(primaryWindow, exception);
				} else if (updateUserRequestResult.containsKey(new Integer(400))) {
					if (alertSceneBuilder == null) {
						alertSceneBuilder = new AlertSceneBuilder();
					}
					ExceptionDetails exception = (ExceptionDetails) updateUserRequestResult.get(new Integer(400));
					alertSceneBuilder.popUpWindowUserNameNotUnique(exception, primaryWindow);
				} else if (updateUserRequestResult.containsKey(new Integer(409))) {
					ValidationErrorDto error = (ValidationErrorDto) updateUserRequestResult.get(new Integer(409));
					if (alertSceneBuilder == null) {
						alertSceneBuilder = new AlertSceneBuilder();
					}
					alertSceneBuilder.popUpAlertWindowInvalidData(error, primaryWindow);
				} else {
					Iterator<java.util.Map.Entry<Integer, Object>> iterator = updateUserRequestResult.entrySet()
							.iterator();
					while (iterator.hasNext()) {
						java.util.Map.Entry<Integer, Object> response = iterator.next();
						ExceptionDetails exceptionDetails = (ExceptionDetails) response.getValue();
						alertSceneBuilder.popUpWindowUserNameNotUnique(exceptionDetails, primaryWindow);
						break;
					}
				}
			} else {

				Map<Integer, Object> createClientRequestResult = UserRequestsStrategy.registerUser(customerDto);

				if (userRegistredSucessfullySceneBuilder == null) {
					userRegistredSucessfullySceneBuilder = new UserRegistredSucessfullySceneBuilder();
				}

				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}

				// Everything OK! Get the body of the response!
				if (createClientRequestResult.containsKey(new Integer(201))) {

					CustomerDto registredCustomerDto = (CustomerDto) createClientRequestResult.get(new Integer(201));

					primaryWindow.setScene(userRegistredSucessfullySceneBuilder.createScene(primaryWindow,
							registredCustomerDto.getFirstName(), registredCustomerDto.getLastName(), registredCustomerDto.getUserName(),
							registredCustomerDto.getPoints()));
				} else if (createClientRequestResult.containsKey(new Integer(409))) {

					ValidationErrorDto errorMessage = (ValidationErrorDto) createClientRequestResult
							.get(new Integer(409));
					alertSceneBuilder.popUpAlertWindowInvalidData(errorMessage, primaryWindow);
				} else if (createClientRequestResult.containsKey(new Integer(400))) {
					ExceptionDetails exceptionDetails = (ExceptionDetails) createClientRequestResult
							.get(new Integer(400));
					alertSceneBuilder.popUpWindowUserNameNotUnique(exceptionDetails, primaryWindow);
				} else {
					Iterator<java.util.Map.Entry<Integer, Object>> iterator = createClientRequestResult.entrySet()
							.iterator();
					while (iterator.hasNext()) {
						java.util.Map.Entry<Integer, Object> response = iterator.next();
						ExceptionDetails exceptionDetails = (ExceptionDetails) response.getValue();
						alertSceneBuilder.popUpWindowUserNameNotUnique(exceptionDetails, primaryWindow);
						break;
					}
				}
			}
		});
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

		mainLayout.getChildren().add(firstNameTextField);
		mainLayout.getChildren().add(lastNameTextField);
		mainLayout.getChildren().add(userNameTextField);
		if (isUserBeingEdited) {
			TextField points = new TextField();
			points.setText("Points: " + customerDtoToUpdate.getPoints().toString());
			points.setDisable(true);
			mainLayout.getChildren().add(points);

		}
		mainLayout.getChildren().add(registerOrEditUserButton);
		mainLayout.getChildren().add(tickLabel);

		BorderPane.setAlignment(mainLayout, Pos.CENTER);
		rootNodeOfRegisterNewUserScene.setCenter(mainLayout);

		FlowPane leftLayout = new FlowPane();

		Label dummyLabel = new Label();
		dummyLabel.setMinSize(270, 60);
		leftLayout.getChildren().add(dummyLabel);
		leftLayout.getChildren().add(backButton);

		BorderPane.setAlignment(leftLayout, Pos.BOTTOM_CENTER);
		rootNodeOfRegisterNewUserScene.setLeft(leftLayout);

		Label dummyLabel1 = new Label();
		dummyLabel1.setMinSize(270, 60);

		BorderPane.setAlignment(dummyLabel1, Pos.BOTTOM_CENTER);
		rootNodeOfRegisterNewUserScene.setTop(dummyLabel1);

		Scene scene = new Scene(rootNodeOfRegisterNewUserScene, getWindowsWidth(), getWindowsHeight());
		scene.getStylesheets().add("DarkTheme.css");
		rootNodeOfRegisterNewUserScene.requestFocus();
		return scene;
	}

}
