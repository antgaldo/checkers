package game;

import model.Board;

public class Move {
    private int row;
    private int col;
    private int oldrow;
    private int oldcol;

    public Move(int oldrow, int oldcol, int row,int col){
        this.oldrow=oldrow;
        this.oldcol=oldcol;
        this.row=row;
        this.col=col;
    }

    public int getOldRow(){
        return oldrow;
    }
    public int getOldCol(){
        return oldcol;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }

}
