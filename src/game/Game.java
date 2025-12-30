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
    private boolean mustcapture;
    private Piece selectpiece;

    public Game(){
        this.viewboard= new ViewBoard();
        this.board= new Board();
        this.viewtable= new ViewTable();
        this.viewpoint= new ViewPoint();
        this.turn=0;
        viewboard.setListener(this);
        this.mustcapture=false;
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
        if((turn ==0 && piece.getColor()== WHITE) || (turn ==1 && piece.getColor()== BLACK)){
            this.selectpiece = piece;
        } else viewboard.showAlert("","","Sbagliato turno");
    }

    public void onBoxClick(int row, int col){
        if(selectpiece!=null) {
            Move move= new Move( row, col);
            if(board.movePiece(move, selectpiece,turn,mustcapture)) {
                viewboard.viewstart(board);
                viewpoint.setCountPoint("Numero di Bianchi: " + board.getWhite().size() + "\n\nNumero di Neri: " + board.getBlack().size());
                mustcapture=false;
                //se dopo la mossa la pedina spostata è sotto attacco allora aggiorna lo stato mustcapture
                //per obbligare l'avversario a mangiare
                if(board.isCapturable(selectpiece,turn)){
                    mustcapture=true;
                }
                System.out.println(mustcapture);
                selectpiece = null;
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
