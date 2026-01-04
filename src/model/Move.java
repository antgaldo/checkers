package model;

import java.util.ArrayList;

public class Move {
    private Piece piece;
    private boolean isCapture;
    private int row;
    private int col;
    private int startRow;
    private int startCol;
    private ArrayList<Piece> capturedPieces = new ArrayList<>();
    //ci occorre per poter tornare indietro
    private boolean promoted = false;

    //Per Utente
    public Move( int row,int col){
        this.row=row;
        this.col=col;
    }

    //Per AI
    public Move(Piece piece,boolean isCapture, int row,int col){
        this.piece=piece;
        this.isCapture=isCapture;
        this.row=row;
        this.col=col;
        //occorre per undoMove e makeMove, altrimenti non possiamo tornare indietro
        this.startRow = piece.getRow();
        this.startCol = piece.getCol();
    }
    // Nuovo costruttore per catture multiple e simulazioni precise
    public Move(Piece piece, boolean isCapture, int row, int col, int startRow, int startCol) {
        this.piece = piece;
        this.isCapture = isCapture;
        this.row = row;
        this.col = col;
        this.startRow = startRow;
        this.startCol = startCol;
    }

    public void addCapturedPiece(Piece p) {
        this.capturedPieces.add(p);
    }

    //Lista dei nemici eliminati
    public ArrayList<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public int getStartRow() { return startRow; }
    public int getStartCol() { return startCol; }
    public Piece getPiece(){return piece; }
    public boolean getIsCapture(){
        return isCapture;
    }
    public boolean getPromoted(){return promoted;}
    public void setPromoted(boolean promoted){
        this.promoted=promoted;
    }

    @Override
    public String toString(){
        return "Piece:" + piece + " " + "row" + row + " "  + "col" + col + " "  + "startrow" + startRow + "startCol:" + startCol + "getIsCapture:" + isCapture;
    }
}
