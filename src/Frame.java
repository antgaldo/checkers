import game.Game;
import gui.ViewBoard;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Frame extends Application {
    private Game game;
    private Scene scene;

    @Override
    public void start(Stage primaryStage){
        this.game= new Game();

        HBox hbox= new HBox(10);
        VBox vbox = new VBox(10);  // 10px di distanza tra i GridPane
        vbox.setPadding(new Insets(20));
        hbox.getChildren().addAll(game.getViewBoard(),game.getViewPoint());
        vbox.getChildren().addAll(game.getViewtable());  // Aggiungi entrambi i GridPane

        VBox root = new VBox(20); // Root container per includere HBox e VBox
        root.getChildren().addAll(hbox, vbox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
