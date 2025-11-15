package model;

import javafx.scene.paint.Color;

public class Piece {
    private int row;
    private int col;
    private Color color;

    public Piece(int row, int col, Color color){
        this.row=row;
        this.col=col;
        this.color=color;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public Color getColor(){
        return color;
    }
}
