package gui;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static javafx.scene.paint.Color.WHITE;

public class ViewKing extends ViewPiece  {

    private static final double RADIUS = 15;

    public ViewKing(Color baseColor) {
        super(baseColor);
        // Disco inferiore
        Circle base = new Circle(RADIUS);
        base.setFill(baseColor.darker());
        base.setStroke(Color.BLACK);
        base.setStrokeWidth(2);

        // Disco superiore (più piccolo)
        Circle top = new Circle(RADIUS * 0.7);
        top.setFill(baseColor);
        top.setStroke(baseColor == WHITE ? Color.BLACK : Color.WHITE);
        top.setStrokeWidth(2);

        // Ombra per effetto 3D
        DropShadow shadow = new DropShadow();
        shadow.setRadius(8);
        shadow.setOffsetY(4);
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        base.setEffect(shadow);

        getChildren().addAll(base, top);
    }
}

