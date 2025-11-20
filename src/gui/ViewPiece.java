package gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ViewPiece extends StackPane {
    private Circle piece;

    public ViewPiece(Color color){
        this.piece= new Circle(15,color);
        this.getChildren().addAll(piece);
        this.setPrefSize(30, 30);
    }
}
