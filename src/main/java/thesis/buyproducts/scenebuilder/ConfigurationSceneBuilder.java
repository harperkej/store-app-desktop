package thesis.buyproducts.scenebuilder;

import static thesis.buyproducts.constant.Constants.getWindowsHeight;
import static thesis.buyproducts.constant.Constants.getWindowsWidth;

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
import thesis.buyproducts.restcall.model.PointMappingDto;
import thesis.buyproducts.restcall.model.ValidationErrorDto;
import thesis.buyproducts.restcall.strategy.ConfigurationRequestsStrategy;

public class ConfigurationSceneBuilder {

	private MainSceneBuilder mainSceneBuilder;
	private AlertSceneBuilder alertSceneBuilder;

	public Scene createScene(Stage primaryWindow, String message) {

		BorderPane rootNode = new BorderPane();

		TilePane mainLayout = new TilePane();
		mainLayout.setOrientation(Orientation.VERTICAL);

		mainLayout.setHgap(25);
		mainLayout.setVgap(25);

		Label currentPointToAmountMapper = new Label();
		currentPointToAmountMapper.setText(message);

		TextField amountToUpdate = new TextField();
		amountToUpdate.setPromptText("Set the point mapper");

		Button updatePointMapperButton = new Button("Update point mapper");
		updatePointMapperButton.setMinSize(270, 60);

		ImageView imageViewUpdatePointMapperButton = new ImageView("Update.png");
		updatePointMapperButton.setGraphic(imageViewUpdatePointMapperButton);

		updatePointMapperButton.setOnAction(e -> {
			boolean isPointMapperValid = true;
			String points = amountToUpdate.getText();
			try {
				Double.parseDouble(points);
			} catch (Exception ex) {
				isPointMapperValid = false;
			}
			if (!isPointMapperValid) {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				alertSceneBuilder.popUpAlertWindowWhenUpdatinConfigurationIsInvalid("Please type an integer!",
						primaryWindow);
				return;
			}
			PointMappingDto pointMappingDto = new PointMappingDto();
			pointMappingDto.setValue(Double.parseDouble(points));
			Map<Integer, Object> response = ConfigurationRequestsStrategy.updatePointMapping(pointMappingDto);
			if (response.containsKey(new Integer(200))) {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				Boolean retRes = (Boolean) response.get(new Integer(200));
				if (retRes == true) {
					alertSceneBuilder.popUpAlertWindowUpdatePoingMappingOK(primaryWindow);
				}
			} else if (response.containsKey(new Integer(409))) {
				ValidationErrorDto error = (ValidationErrorDto) response.get(new Integer(409));
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				alertSceneBuilder.popUpAlertWindowInvalidData(error, primaryWindow);
			} else {
				if (alertSceneBuilder == null) {
					alertSceneBuilder = new AlertSceneBuilder();
				}
				ExceptionDetails exception = (ExceptionDetails) response.get(new Integer(500));
				alertSceneBuilder.popUpAlertWindowWhenSearchUserNotFound(primaryWindow, exception);
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

		mainLayout.getChildren().addAll(currentPointToAmountMapper, amountToUpdate, updatePointMapperButton);

		BorderPane.setAlignment(mainLayout, Pos.CENTER);
		rootNode.setCenter(mainLayout);

		FlowPane leftLayout = new FlowPane();

		Label dummyLabel = new Label();
		dummyLabel.setMinSize(270, 60);

		leftLayout.getChildren().add(dummyLabel);

		leftLayout.getChildren().add(backButton);

		BorderPane.setAlignment(leftLayout, Pos.BASELINE_CENTER);
		rootNode.setLeft(leftLayout);

		Label dummyLabel1 = new Label();
		dummyLabel1.setMinSize(270, 80);

		BorderPane.setAlignment(dummyLabel1, Pos.BOTTOM_CENTER);
		rootNode.setTop(dummyLabel1);

		Scene scene = new Scene(rootNode, getWindowsWidth(), getWindowsHeight());
		scene.getStylesheets().add("DarkTheme.css");
		rootNode.requestFocus();
		return scene;
	}

}
