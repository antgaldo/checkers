package model;

import game.Move;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

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

    public void movePiece(Move move,Piece piece) {
        if(this.isMoveLegal(move,piece)){
            board[piece.getRow()][piece.getCol()] = null;
            board[move.getRow()][move.getCol()] = piece;
            piece.setRow(move.getRow());
            piece.setCol(move.getCol());
        }
    }

    public boolean isMoveLegal(Move move,Piece piece) {
        boolean valid=true;
        if(piece.getColor() == WHITE){
            valid= isLegalWhite(piece,move);
        }
        if(piece.getColor() == BLACK){
            valid= isLegalBlack(piece,move);
        }
        //regole comuni tra white e black
        if(piece.getCol()==move.getCol()) valid= false;
        if(piece.getRow()==move.getRow()) valid=false;
        return valid;
    }

    public boolean isLegalWhite(Piece piece,Move move){
        boolean valid=true;
        // evita il ritorno indietro
        if(move.getRow() <= piece.getRow()) valid=false;
        // evita di spostarsi piu di una casella
        if((move.getRow()-piece.getRow())>=2) valid=false;
        return valid;
    }
    public boolean isLegalBlack(Piece piece,Move move){
        boolean valid=true;
        // evita il ritorno indietro
        if(move.getRow() >= piece.getRow()) valid=false;
        // evita di spostarsi piu di una casella
        if((piece.getRow()-move.getRow())>=2) valid=false;
        return valid;
    }
}
