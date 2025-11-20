package game;

import gui.ViewBoard;
import model.Board;
import model.Piece;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Game implements Listener{
    private Board board;
    private ViewBoard viewboard;
    private int turn;
    private Piece selectpiece;
    private int piecerow;
    private int piececol;

    public Game(){
        this.viewboard= new ViewBoard();
        this.board= new Board();
        this.turn=0;
        viewboard.setListener(this);
    }

    public ViewBoard getViewBoard(){
        return viewboard;
    }
    public void initGame(){
        viewboard.viewstart(board);
    }

    public void onPieceClick(Piece piece){
        if(turn==0 && piece.getColor()==WHITE) {
            this.selectpiece = piece;
            this.piecerow = piece.getRow();
            this.piececol = piece.getCol();
        }
        if(turn==1 && piece.getColor()==BLACK) {
            this.selectpiece = piece;
            this.piecerow = piece.getRow();
            this.piececol = piece.getCol();
        }
    }

    public void onBoxClick(int row, int col){
        if(selectpiece!=null) {
            board.movePiece(new Move(selectpiece.getRow(),selectpiece.getCol(),row,col),selectpiece);
            selectpiece = null;
            piecerow = 0;
            piececol = 0;
            viewboard.viewstart(board);
            switchTurn();
        }
    }

    private void switchTurn(){
        if(turn==0){
            turn=1;
        } else turn = 0;
    }
}
