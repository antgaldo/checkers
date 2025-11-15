package game;

import model.Board;

public class Move {
    private Board board;
    private int currentplayer=1;
    private int row;
    private int col;
    private int oldrow;
    private int oldcol;

    public Move(int oldrow, int oldcol, int row,int col, Board board){
        this.oldrow=oldrow;
        this.oldcol=oldcol;
        this.row=row;
        this.col=col;
        this.board=board;
    }

    public int getOldRow(){
        return oldrow;
    }
    public int getOldCol(){
        return oldcol;
    }
    public int getRow(){
        return oldrow;
    }
    public int getCol(){
        return oldcol;
    }

    public void switchPlayer(){
        currentplayer= currentplayer==1 ? 2:1;
    }
}
