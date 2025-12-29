package game;

import gui.ViewBoard;
import gui.ViewPoint;
import gui.ViewTable;
import model.Board;
import model.Move;
import model.Piece;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Game implements Listener{
    private Board board;
    private ViewBoard viewboard;
    private ViewTable viewtable;
    private ViewPoint viewpoint;
    private int turn;
    private Piece selectpiece;

    public Game(){
        this.viewboard= new ViewBoard();
        this.board= new Board();
        this.viewtable= new ViewTable();
        this.viewpoint= new ViewPoint();
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

    public ViewPoint getViewPoint(){
        return viewpoint;
    }

    public void initGame(){
        viewboard.viewstart(board);
        viewtable.setTextTurn((turn == 0 ? "Bianco" : "Nero"));
        viewpoint.setCountPoint("Numero di Bianchi: " + board.getWhite().size() + "\n\nNumero di Neri: " + board.getBlack().size());
    }

    public void onPieceClick(Piece piece){
        this.selectpiece = piece;
    }

    public void onBoxClick(int row, int col){
        if(selectpiece!=null) {
            Move move= new Move( row, col);
            if(board.movePiece(move, selectpiece,turn)) {
                viewboard.viewstart(board);
                selectpiece = null;
                viewpoint.setCountPoint("Numero di Bianchi: " + board.getWhite().size() + "\n\nNumero di Neri: " + board.getBlack().size());
                switchTurn();
            } else viewboard.showAlert("","","Mossa non valida");
        }
    }

    private void switchTurn(){
        if(turn==0){
            turn=1;
        } else turn = 0;
        viewtable.setTextTurn(turn == 0 ? "Bianco" : "Nero");
    }
}
