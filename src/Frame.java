import game.Game;
import gui.ViewBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Frame extends Application {
    private Game game;
    private Scene scene;

    @Override
    public void start(Stage primaryStage){
        this.game= new Game();
        this.scene = new Scene(game.getViewBoard(), 500, 500);
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
        game.initGame();
    }

    public static void main(String[] args){
        launch(args);
    }
}
