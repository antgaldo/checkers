package model;

import javafx.scene.paint.Color;

public class Piece implements Cloneable{
    private int row;
    private int col;
    private Color color;
    private boolean isking;

    public Piece(int row, int col, Color color){
        this.row=row;
        this.col=col;
        this.color=color;
        this.isking=false;
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

    public boolean getisKing(){
        return isking;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setIsKing(boolean value) {
        this.isking = value;
    }

    public Color getColor(){
        return color;
    }

    @Override
    public String toString() {
        return (color == Color.WHITE ? "W" : "B") + " row:" + row + "col" + col + "king" + isking;
    }

    @Override
    public Piece clone() {
        try {
            return (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
