package gui;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

    public class ViewMoveAi extends GridPane {
        private Text text;
        public ViewMoveAi(){
            this.text= new Text();
            text.setFill(Color.BLACK);
            this.add(text, 1, 1, 1, 1);
        }

        public void setText(String string){
            this.text.setText(string);
        }
        public void appendText(String string) {
            String current = this.text.getText();
            this.text.setText(current + string);
        }
}
