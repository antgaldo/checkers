package model;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Board {
    private Piece[][] board;
    private ArrayList<Piece> black;
    private ArrayList<Piece> white;

    public Board(){
        this.board= new Piece[8][8];
        this.black= new ArrayList<Piece>();
        this.white= new ArrayList<Piece>();
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

    public void setPiece(Piece p, int row, int col) {
        board[row][col] = p;
        if (p.getColor() == Color.WHITE)
            white.add(p);
        else black.add(p);
    }
}
