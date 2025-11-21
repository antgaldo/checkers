import game.Game;
import gui.ViewBoard;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Frame extends Application {
    private Game game;
    private Scene scene;

    @Override
    public void start(Stage primaryStage){
        this.game= new Game();

        VBox vbox = new VBox(10);  // 10px di distanza tra i GridPane
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(game.getViewBoard(), game.getViewtable());  // Aggiungi entrambi i GridPane

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
