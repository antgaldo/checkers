package model;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Board {
    private Piece[][] board;
    private ArrayList<Piece> black;
    private ArrayList<Piece> white;
    private int countcapture;

    public Board(){
        this.board= new Piece[8][8];
        this.black= new ArrayList<Piece>();
        this.white= new ArrayList<Piece>();
        this.countcapture= 0;
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

    public int getCountcapture(){
        return countcapture;
    }

    private void initPieces() {
        for (int i = 0; i < 12; i++) white.add(new Piece(Color.WHITE));
        for (int i = 0; i < 12; i++) black.add(new Piece(Color.BLACK));
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

    public boolean movePiece(Move move,Piece piece,int turn) {
        boolean ok=true;
        if(this.isMoveLegal(move,piece,turn)){
            //se è una mossa cattura allora esegui
            if ((Math.abs(move.getRow() - piece.getRow()) == 2) && (Math.abs(move.getCol() - piece.getCol()) == 2)){
                int enemyRow= piece.getRow() + ((turn == 0) ? 1 : -1);
                int enemyCol= (move.getCol() + piece.getCol()) / 2;
                removePiece( turn ==0 ? black : white ,enemyRow,enemyCol);
                setBoxNull(enemyRow, enemyCol);
            }
            //sposta la pedina
            board[piece.getRow()][piece.getCol()] = null;
            board[move.getRow()][move.getCol()] = piece;
            piece.setRow(move.getRow());
            piece.setCol(move.getCol());
        } else ok=false;
        return ok;
    }

    public boolean isMoveLegal(Move move, Piece piece, int turn) {
        //conta catture possibili
        boolean canCapture = countCapture(piece, turn);
        //se può catturare allora verifica se la mossa va bene
        if (canCapture) {
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


    public boolean countCapture(Piece piece, int turn) {
        countcapture = 0;
        boolean canCapture = false;
        int direction = (turn == 0) ? 1 : -1;
        // diagonale sinistra
        if (checkCaptureDirection(
                piece,
                piece.getRow() + direction,
                piece.getCol() - 1,
                piece.getRow() + 2 * direction,
                piece.getCol() - 2)) {
            countcapture++;
            canCapture = true;
        }
        // diagonale destra
        if (checkCaptureDirection(
                piece,
                piece.getRow() + direction,
                piece.getCol() + 1,
                piece.getRow() + 2 * direction,
                piece.getCol() + 2)) {
            countcapture++;
            canCapture = true;
        }
        return canCapture;
    }

    private boolean checkCaptureDirection(Piece piece, int enemyRow, int enemyCol, int landingRow, int landingCol) {
        if (!isInside(enemyRow) || !isInside(enemyCol)) return false;
        if (!isInside(landingRow) || !isInside(landingCol)) return false;
        Piece enemy = getPiece(enemyRow, enemyCol);
        if (enemy == null || enemy.getColor() == piece.getColor()) return false;
        return getPiece(landingRow, landingCol) == null;
    }

    private boolean isInside(int number){
        boolean valid=false;
        if(number > 0 && number < 8)
            valid=true;
        return valid;
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
