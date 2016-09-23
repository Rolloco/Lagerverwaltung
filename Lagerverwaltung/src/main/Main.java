package main;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Main extends Application {

	private Button button;
	private FlowPane pane;

	@Override
	public void start(Stage stage) throws Exception {

		button = new Button("Hello");

		pane = new FlowPane();
		pane.getChildren().addAll(button);
		
		addEventHandler();
		
		Scene scene = new Scene(pane, 300, 300);
		scene.getStylesheets().add("style.css");

//		stage.setMaximized(true);
		stage.setScene(scene);
		stage.setTitle("Test");
		stage.show();

	}

	private void addEventHandler() {
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Parent root;
		        Stage stage = new Stage();
				stage.setTitle("My New Stage Title");
				stage.show();

				//hide this current window (if this is what you want
				((Node)(event.getSource())).getScene().getWindow().hide();
			}
		});
	}

	public static void main(String[] args) {
		TableViewSample.launch(args);
		Application.launch(args);

	}
}
