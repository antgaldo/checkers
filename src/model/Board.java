package model;

import javafx.scene.paint.Color;

import java.util.ArrayList;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Board {
    private Piece[][] board;
    private ArrayList<Piece> black;
    private ArrayList<Piece> white;
    private boolean hasCapture;

    public Board(){
        this.board= new Piece[8][8];
        this.black= new ArrayList<Piece>();
        this.white= new ArrayList<Piece>();
        this.hasCapture= false;
        initPieces();
        setupBoard();
    }

    public Piece[][] getBoard(){
        return board;
    }

    public int getRow(){
        return board.length;
    }

    public int getColumn(){
        return board[0].length;
    }

    public boolean getHasCapture(){
        return hasCapture;
    }

    private void initPieces() {
        for (int i = 0; i < 12; i++) white.add(new Piece(WHITE));
        for (int i = 0; i < 12; i++) black.add(new Piece(BLACK));
    }

    public ArrayList<Piece> getWhite(){
        return white;
    }
    public ArrayList<Piece> getBlack(){
        return black;
    }

    public void setupBoard(){
        int index=11;
        for(int i=0; i<3;i++){
            for(int j=0; j<8;j++){
                if((i+j)%2==0){
                    Piece w = white.get(index--);
                    w.setRow(i);
                    w.setCol(j);
                    board[i][j] = w;
                }
            }
        }
        index=11;
        for(int i=5; i<8;i++){
            for(int j=0; j<8;j++){
                if((i+j)%2==0){
                    Piece b = black.get(index--);
                        b.setRow(i);
                        b.setCol(j);
                        board[i][j] = b;
                }
            }
        }
    }

    public Piece getPiece(int row, int col){
        return board[row][col];
    }

    private void setBoxNull(int row, int col){
        board[row][col]=null;
    }

    public boolean movePiece(Move move,Piece piece,int turn,boolean mustcapture) {
        boolean ok=true;
        hasCapture=false;
        if(this.isMoveLegal(move,piece,turn,mustcapture)){
            //se è una mossa cattura allora esegui
            if ((Math.abs(move.getRow() - piece.getRow()) == 2) && (Math.abs(move.getCol() - piece.getCol()) == 2)){
                int enemyRow= piece.getRow() + ((turn == 0) ? 1 : -1);
                int enemyCol= (move.getCol() + piece.getCol()) / 2;
                removePiece( turn ==0 ? black : white ,enemyRow,enemyCol);
                setBoxNull(enemyRow, enemyCol);
                hasCapture=true;
            }
            //sposta la pedina
            board[piece.getRow()][piece.getCol()] = null;
            board[move.getRow()][move.getCol()] = piece;
            piece.setRow(move.getRow());
            piece.setCol(move.getCol());
        } else ok=false;
        return ok;
    }

    public boolean isMoveLegal(Move move, Piece piece, int turn,boolean mustcapture) {
        //se può catturare allora verifica se la mossa va bene
        if (mustcapture) {
            return isCaptureMove(move, piece, turn);
        }
        return isLegalMove(piece, move, turn);
    }

    private boolean isCaptureMove(Move move, Piece piece, int turn) {
        int direction = (turn == 0) ? 1 : -1;
        if (Math.abs(move.getRow() - piece.getRow()) != 2) return false;
        if (Math.abs(move.getCol() - piece.getCol()) != 2) return false;
        int enemyRow = piece.getRow() + direction;
        int enemyCol = (move.getCol() + piece.getCol()) / 2;
        return checkCaptureDirection(piece, enemyRow, enemyCol, move.getRow(), move.getCol());
    }

    private boolean checkCaptureDirection(Piece piece, int enemyRow, int enemyCol, int landingRow, int landingCol) {
        if (!isInside(enemyRow) || !isInside(enemyCol)) return false;
        if (!isInside(landingRow) || !isInside(landingCol)) return false;
        Piece enemy = getPiece(enemyRow, enemyCol);
        if (enemy == null || enemy.getColor() == piece.getColor()) return false;
        return getPiece(landingRow, landingCol) == null;
    }

    //controlla se ci sono pedine catturabili partendo dall'ultima inserita
    public boolean checkisCapturable(int turn){
        ArrayList<Piece> array= turn==0 ? white: black;
        for (int i = array.size() - 1; i >= 0; i--) {
            if(isCapturable(array.get(i),turn)) {
                return true;
            }
        }
        return false;
    }

    //controlla se c'è una pedina sotto attacco
    private boolean isCapturable(Piece piece,int turn){
        int direction= turn==0 ? 1 : -1;
            if(!isInside(piece.getRow() +direction)) return false;
            int enemyRow = piece.getRow() +direction;
            if(isInside(piece.getCol() -1) && isInside(piece.getCol() +1) && isInside(piece.getRow() - 1) && isInside(piece.getRow() + 1)){
                int enemyLeftCol = piece.getCol() -1;
                int enemyRigthCol= piece.getCol() +1;
                Piece enemyLeft= getPiece(enemyRow, enemyLeftCol);
                Piece enemyRigth= getPiece(enemyRow, enemyRigthCol);
                if(turn==0) {
                    if ((enemyLeft != null && enemyLeft.getColor() == BLACK) && (getPiece(piece.getRow() - 1, piece.getCol() + 1) == null)) {
                        return true;
                    }
                    if ((enemyRigth != null && enemyRigth.getColor() == BLACK) && (getPiece(piece.getRow() - 1, piece.getCol() - 1) == null)) {
                        return true;
                    }
                }
                if(turn==1) {
                    if ((enemyLeft != null && enemyLeft.getColor() == WHITE) && (getPiece(piece.getRow() + 1, piece.getCol() + 1) == null)) {
                        return true;
                    }
                    if ((enemyRigth != null && enemyRigth.getColor() == WHITE) && (getPiece(piece.getRow() + 1, piece.getCol() - 1) == null)) {
                        return true;
                    }
                }
            }
        return false;
    }

    public void promoteToKing(Piece piece,int turn){
        if(turn==0){
            if(piece.getRow() == 7) piece.setIsKing();
        }
        if(turn==1){
            if(piece.getRow() == 0) piece.setIsKing();
        }
    }

    private boolean isInside(int number){
        boolean valid=false;
        if(number >= 0 && number < 8)
            valid=true;
        return valid;
    }

    public boolean canCapture(Piece piece,int turn){
        int direction = (turn==0) ?1 : -1;
        if(turn==0){
            if(!isInside(piece.getRow()+direction+1)) return false;
            //se a destra c'è spazio di atterraggio e il nemico è nero
            if(isInside(piece.getCol()+2)){
                Piece enemyrigth= getPiece(piece.getRow()+direction,piece.getCol()+1);
                if(enemyrigth != null && enemyrigth.getColor() == BLACK && (getPiece(piece.getRow()+2,piece.getCol()+2)==null)) {
                    return true;
                }
            }
            //se a sinistra c'è spazio di atterraggio e il nemico è nero
            if(isInside(piece.getCol()-2)){
                Piece enemyleft= getPiece(piece.getRow()+direction,piece.getCol()-1);
                if(enemyleft != null && enemyleft.getColor() == BLACK && (getPiece(piece.getRow()+2,piece.getCol()-2)==null)) {
                    return true;
                }
            }
        }
        //non si vede il damone, da correggere
        if(turn==1){
            if(!isInside(piece.getRow()+direction-1)) return false;
            //se a destra c'è spazio di atterraggio e il nemico è bianco
            if(isInside(piece.getCol()+2)){
                Piece enemyrigth= getPiece(piece.getRow()+direction,piece.getCol()+1);
                if(enemyrigth != null && enemyrigth.getColor() == WHITE && (getPiece(piece.getRow()-2,piece.getCol()+2)==null)) {
                    return true;
                }
            }
            //se a sinistra c'è spazio di atterraggio e il nemico è bianco
            if(isInside(piece.getCol()-2)){
                Piece enemyrigth= getPiece(piece.getRow()+direction,piece.getCol()-1);
                if(enemyrigth != null && enemyrigth.getColor() == WHITE && getPiece(piece.getRow()-2,piece.getCol()-2)==null ){
                    return true;
                    }
            }
        }
        return false;
    }

    private boolean isLegalMove(Piece piece, Move move, int turn) {
        int direction = (turn == 0) ? 1 : -1;
        boolean correctRow = (move.getRow() - piece.getRow()) == direction;
        boolean correctCol = Math.abs(move.getCol() - piece.getCol()) == 1;
        return correctRow && correctCol;
    }

    private void removePiece(ArrayList<Piece> list,int row,int col){
        for (int i = 0; i < list.size(); i++) {
            Piece p = list.get(i);
            if (p.getRow() == row && p.getCol() == col) {
                list.remove(i);
                break;
            }
        }
    }
}
