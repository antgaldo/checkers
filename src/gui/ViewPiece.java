package gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ViewPiece extends StackPane {

    private static final double RADIUS = 15;

    public ViewPiece(Color baseColor) {

        // Corpo principale
        Circle body = new Circle(RADIUS);
        body.setFill(baseColor);
        body.setStroke(Color.BLACK);
        body.setStrokeWidth(2);

        // Effetto ombra (effetto 3D)
        DropShadow shadow = new DropShadow();
        shadow.setRadius(6);
        shadow.setOffsetY(3);
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        body.setEffect(shadow);

        getChildren().add(body);
    }
}

