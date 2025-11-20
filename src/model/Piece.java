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
    public Piece(Color color){
        this.color=color;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Color getColor(){
        return color;
    }

    @Override
    public String toString() {
        return color == Color.WHITE ? "W" : "B";
    }
}
