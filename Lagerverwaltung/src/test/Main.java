package test;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main class to start the application.
 * 
 * @author Collin Kempkes
 */
public class Main extends Application {
	
	final HBox hb = new HBox();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lagerverwaltung");
        final Label label = new Label("Lagerverwaltung");
		label.setFont(new Font("Arial", 20));
		
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Table.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            hb.getChildren().addAll(page);
            
            final VBox vbox = new VBox();
    		vbox.setSpacing(5);
    		vbox.setPadding(new Insets(10, 0, 0, 10));
    		vbox.getChildren().addAll(label, hb);
    		
    		Scene scene = new Scene(new Group());
            ((Group) scene.getRoot()).getChildren().addAll(vbox);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}