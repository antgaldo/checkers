package game;

import ai.MinMax;
import gui.ViewBoard;
import gui.ViewMoveAi;
import gui.ViewPoint;
import gui.ViewTable;
import model.Board;
import model.Move;
import model.Piece;
import static javafx.scene.paint.Color.BLACK;

public class Game implements Listener{
    private Board board;
    private ViewBoard viewboard;
    private ViewTable viewtable;
    private ViewPoint viewpoint;
    private ViewMoveAi viewmoveai;
    private Move moveiview;
    private int turn;
    private boolean mustcapture;
    private Piece selectpiece;

    public Game(){
        this.viewboard= new ViewBoard();
        this.board= new Board();
        this.viewtable= new ViewTable();
        this.viewpoint= new ViewPoint();
        this.viewmoveai= new ViewMoveAi();
        this.turn=0;
        viewboard.setListener(this);
        this.mustcapture=false;
        initGame();
        playAIMove();
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
    public ViewMoveAi getViewMoveAi(){
        return viewmoveai;
    }

    public void initGame(){
        viewboard.viewstart(board);
        viewtable.setTextTurn((turn == 0 ? "Bianco" : "Nero"));
        viewpoint.setCountPoint("Numero di Bianchi: " + board.getWhite().size() + "\n\nNumero di Neri: " + board.getBlack().size());
        viewmoveai.setText("Mosse dell'AI\n");
    }

    //Serve per l'umano
    public void onPieceClick(Piece piece){
        if((turn ==1 && piece.getColor()== BLACK)){
            this.selectpiece = piece;
        } else viewboard.showAlert("","","Sbagliato turno");
    }

    public void onBoxClick(int row, int col){
        if(selectpiece!=null && turn==1) {
            Move move= new Move( row, col);
            if(board.movePiece(move, selectpiece,turn,mustcapture)) {
                viewboard.viewstart(board);
                viewpoint.setCountPoint("Numero di Bianchi: " + board.getWhite().size() + "\n\nNumero di Neri: " + board.getBlack().size());
                mustcapture=false;
                //se dopo la mossa la pedina spostata è sotto attacco allora aggiorna lo stato mustcapture
                //per obbligare l'avversario a mangiare
                if(board.canCapture(selectpiece,turn) && board.getHasCapture()){
                    mustcapture = true;
                } else {
                    selectpiece = null;
                    mustcapture = board.checkisCapturable(turn);
                    switchTurn();
                }
            } else viewboard.showAlert("","","Mossa non valida");
        }
    }

    //Funzioni per l'ai
    private void playAIMove() {
        Board shadowBoard = this.board.clone();
        MinMax solver = new MinMax(shadowBoard);
        Move moveAI = solver.getBestMove(shadowBoard, 6, turn);

        if (moveAI != null) {
            this.selectpiece = board.getPiece(moveAI.getStartRow(), moveAI.getStartCol());
            if (this.selectpiece == null) {
                return;
            }

            if(board.movePiece(moveAI, selectpiece,turn,mustcapture)) {
                moveiview = moveAI;
                viewboard.viewstart(board);
                viewpoint.setCountPoint("Numero di Bianchi: " + board.getWhite().size() + "\n\nNumero di Neri: " + board.getBlack().size());
                viewmoveai.appendText("\nRiga: "+ moveiview.getRow() + " - Colonna: " + moveiview.getCol());
                if(moveAI.getCapturedPieces().size()>0){
                    viewmoveai.appendText("\nPezzi mangiati dall'AI: "+ moveAI.getCapturedPieces().size() );
                }
                selectpiece = null;
                mustcapture = board.checkisCapturable(turn);
                switchTurn();
            } else viewboard.showAlert("","","Mossa non valida");
        }
    }

    private void switchTurn(){
        if(turn==0){
            turn=1;
        } else {
            turn = 0;
            javafx.application.Platform.runLater(() -> {
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(4000));
                pause.setOnFinished(e -> playAIMove());
                pause.play();
            });
        }
        viewtable.setTextTurn(turn == 0 ? "Bianco" : "Nero");
    }
}
