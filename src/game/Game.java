package game;

import gui.ViewBoard;
import javafx.scene.paint.Color;
import model.Board;
import model.Piece;

public class Game {
    private Board board;
    private ViewBoard viewboard;

    public Game(){
        this.viewboard= new ViewBoard();
        this.board= new Board();
        initPieces();
    }

    public void initPieces(){
        for(int i=0; i<board.getRow();i++){
            for(int j=0; j<3;j++){
                if((i+j)%2==0){
                    Piece p = new Piece(i, j, Color.WHITE);
                    board.setPiece(p, i, j);
                }
            }
        }
        for(int i=0; i<board.getColumn();i++){
            for(int j=5; j<board.getRow();j++){
                if((i+j)%2==0){
                    Piece p = new Piece(i,j,Color.BLACK);
                    board.setPiece(p,i,j);
                }
            }
        }
    }

    public ViewBoard getViewBoard(){
        return viewboard;
    }

    public void viewstart(){
        viewboard.viewstart(board);
    }
}
