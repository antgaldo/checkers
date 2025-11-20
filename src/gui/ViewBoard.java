package gui;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Board;
import model.Piece;
import game.Listener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ViewBoard extends GridPane{

    private Listener listener;

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

    public void setListener(Listener listener){
        this.listener=listener;
    }

    public void viewstart(Board board){
        this.getChildren().clear();
        for(int i=0; i< board.getRow();i++){
            for(int j=0; j< board.getColumn(); j++){
                Rectangle rectangle = new Rectangle(50, 50);
                if ((i + j) % 2 == 0) rectangle.setFill(Color.BURLYWOOD);
                else rectangle.setFill(Color.ANTIQUEWHITE);
                this.add(rectangle, i, j);
                Piece piece= board.getPiece(i,j);
                if(piece!=null){
                    ViewPiece viewpiece= new ViewPiece(piece.getColor());
                    this.add(viewpiece, i,j);
                    if(listener!=null){
                        viewpiece.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                listener.onPieceClick(piece);
                                event.consume();
                            }
                        });
                    }
                }
                if(rectangle!=null){
                    if(listener!=null){
                        final int row=i;
                        final int col=j;
                        rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
                            if (event.getButton() == MouseButton.PRIMARY) {
                                listener.onBoxClick(row,col);
                                event.consume();
                            }
                        });
                    }
                }
            }
        }
    }
}
