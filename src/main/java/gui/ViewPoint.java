package gui;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ViewPoint extends GridPane {
    private Text text;
    public ViewPoint(){
        this.text= new Text();
        text.setFill(Color.BLACK);
        this.add(text, 1, 1, 1, 1);
    }

    public void setCountPoint(String string){
        this.text.setText(string);
    }
}
