package game;

import gui.ViewBoard;
import gui.ViewTable;
import model.Board;
import model.Piece;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Game implements Listener{
    private Board board;
    private ViewBoard viewboard;
    private ViewTable viewtable;
    private int turn;
    private Piece selectpiece;

    public Game(){
        this.viewboard= new ViewBoard();
        this.board= new Board();
        this.viewtable= new ViewTable();
        this.turn=0;
        viewboard.setListener(this);
        initGame();
    }

    public ViewBoard getViewBoard(){
        return viewboard;
    }

    public ViewTable getViewtable(){
        return viewtable;
    }

    public void initGame(){
        viewboard.viewstart(board);
        viewtable.setTextTurn((turn == 0 ? "Bianco" : "Nero"));
    }

    public void onPieceClick(Piece piece){
        if(turn==0 && piece.getColor()==WHITE) {
            this.selectpiece = piece;
        }
        if(turn==1 && piece.getColor()==BLACK) {
            this.selectpiece = piece;
        }
    }

    public void onBoxClick(int row, int col){
        if(selectpiece!=null) {
            Move move= new Move( row, col);
            if(board.isMoveLegal(move,selectpiece)) {
                board.movePiece(move, selectpiece);
                viewboard.viewstart(board);
                selectpiece = null;
                switchTurn();
            } else
                viewboard.showAlert("","","Mossa non valida");
        }
    }

    private void switchTurn(){
        if(turn==0){
            turn=1;
        } else turn = 0;
        viewtable.setTextTurn(turn == 0 ? "Bianco" : "Nero");
    }
}
