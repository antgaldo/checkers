package gui;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ViewTable extends GridPane {
    private Text text;
    public ViewTable(){
        this.text= new Text("Turno: ");
        text.setFill(Color.BLACK);
        this.add(text, 0, 0, 8, 1);
    }

    public void setTextTurn(String string){
        this.text.setText("Turno: " + string);
    }
}
