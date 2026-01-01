package model;

public class Move {
    private Piece piece;
    private boolean isCapture;
    private int row;
    private int col;

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
    }

    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
}
