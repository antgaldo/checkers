package gui;

import model.Board;
import model.Piece;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ViewBoard extends GridPane{

    public ViewBoard(){
        this.setHgap(3); // 2 px tra le colonne
        this.setVgap(3); // 2 px tra le righe
        this.setPadding(new Insets(10)); // 10px margine su tutti i lati
        this.setPadding(new Insets(10, 20, 10, 20)); // top, right, bottom, left
        this.setAlignment(Pos.CENTER);   // centro
        this.setAlignment(Pos.TOP_LEFT); // in alto a sinistra
        //this.setGridLinesVisible(true);
        this.setPrefSize(800, 800);
    }

    public void viewstart(Board board){
        for(int i=0; i< 8;i++){
            for(int j=0; j< 8; j++){
                Rectangle rectangle = new Rectangle(50, 50);
                if ((i + j) % 2 == 0) rectangle.setFill(Color.BURLYWOOD);
                else rectangle.setFill(Color.ANTIQUEWHITE);
                this.add(rectangle, i, j);
                Piece piece= board.getBoard()[i][j];
                if(piece!=null){
                    ViewPiece viewpiece= new ViewPiece(piece.getColor(),i,j);
                    this.add(viewpiece, i,j);
                }
            }
        }
    }

}
