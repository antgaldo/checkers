package model;

import game.Move;
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
        initPieces();
        setupBoard();
    }

    public Piece[][] getBoard(){
        return board;
    }

    public void printBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                System.out.print(p == null ? "--- " : (p.getColor() == Color.WHITE ? "W " : "B "));
            }
            System.out.println();
        }
    }

    public int getRow(){
        return board.length;
    }

    public int getColumn(){
        return board[0].length;
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
        for(int i=0; i<8;i++){
            for(int j=0; j<3;j++){
                if((i+j)%2==0){
                    Piece w = white.get(index--);
                    w.setRow(i);
                    w.setCol(j);
                    board[i][j] = w;
                }
            }
        }
        index=11;
        for(int i=0; i<8;i++){
            for(int j=5; j<8;j++){
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

    public void movePiece(Move move,Piece piece){
        board[move.getOldRow()][move.getOldCol()] = null;
        board[move.getRow()][move.getCol()] = piece;
        piece.setRow(move.getRow());
        piece.setCol(move.getCol());
    }
}
