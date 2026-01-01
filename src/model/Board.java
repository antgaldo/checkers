package model;

import javafx.scene.paint.Color;

import java.util.ArrayList;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Board {
    private Piece[][] board;
    private ArrayList<Piece> black;
    private ArrayList<Piece> white;
    private boolean hasCapture;

    public Board(){
        this.board= new Piece[8][8];
        this.black= new ArrayList<Piece>();
        this.white= new ArrayList<Piece>();
        this.hasCapture= false;
        initPieces();
        setupBoard();
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

    public boolean getHasCapture(){
        return hasCapture;
    }

    private void initPieces() {
        for (int i = 0; i < 12; i++) white.add(new Piece(WHITE));
        for (int i = 0; i < 12; i++) black.add(new Piece(BLACK));
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

    private void setBoxNull(int row, int col){
        board[row][col]=null;
    }

    public boolean movePiece(Move move,Piece piece,int turn,boolean mustcapture) {
        boolean ok=true;
        hasCapture=false;
        if(this.isMoveLegal(move,piece,turn,mustcapture)){
            //se è una mossa cattura allora esegui
            if ((Math.abs(move.getRow() - piece.getRow()) == 2) && (Math.abs(move.getCol() - piece.getCol()) == 2)){
                //se è una pedina normale valuta la direzione , se è damone no
                int enemyRow= piece.getisKing() ? (piece.getRow() + move.getRow()) / 2 : piece.getRow() + ((turn == 0) ? 1 : -1);
                int enemyCol= (move.getCol() + piece.getCol()) / 2;
                removePiece( turn ==0 ? black : white ,enemyRow,enemyCol);
                setBoxNull(enemyRow, enemyCol);
                hasCapture=true;
            }
            //sposta la pedina
            board[piece.getRow()][piece.getCol()] = null;
            board[move.getRow()][move.getCol()] = piece;
            piece.setRow(move.getRow());
            piece.setCol(move.getCol());
            //verifica se la pedina è diventata Dama
            this.promoteToKing(piece,turn);
        } else ok=false;
        return ok;
    }

    public boolean isMoveLegal(Move move, Piece piece, int turn,boolean mustcapture) {
        //se può catturare allora verifica se la mossa va bene
        if (mustcapture) {
            return isCaptureMove(move, piece, turn);
        }
        return isLegalMove(piece, move, turn);
    }

    //verifica se è una mossa cattura
    private boolean isCaptureMove(Move move, Piece piece, int turn) {
        int direction = (turn == 0) ? 1 : -1;
        if (Math.abs(move.getRow() - piece.getRow()) != 2) return false;
        if (Math.abs(move.getCol() - piece.getCol()) != 2) return false;
        int enemyRow= piece.getisKing() ? (piece.getRow() + move.getRow()) / 2 : piece.getRow() + direction;
        int enemyCol = (move.getCol() + piece.getCol()) / 2;
        return checkCaptureDirection(piece, enemyRow, enemyCol, move.getRow(), move.getCol());
    }

    //verifica se una cattura è possibile
    private boolean checkCaptureDirection(Piece piece, int enemyRow, int enemyCol, int landingRow, int landingCol) {
        //controlla limiti della scacchiera
        if (!isInside(enemyRow) || !isInside(enemyCol)) return false;
        if (!isInside(landingRow) || !isInside(landingCol)) return false;
        //verifica se è un nemico
        Piece enemy = getPiece(enemyRow, enemyCol);
        if (enemy == null || enemy.getColor() == piece.getColor()) return false;
        //verifica se c'è spazio di atterraggio
        return getPiece(landingRow, landingCol) == null;
    }

    //controlla se ci sono pedine catturabili partendo dall'ultima inserita nella lista
    public boolean checkisCapturable(int turn){
        ArrayList<Piece> array= turn==0 ? white: black;
        for (int i = array.size() - 1; i >= 0; i--) {
            if(isCapturable(array.get(i),turn)) {
                return true;
            }
        }
        return false;
    }

    //verifica se il nemico puo catturare
    private boolean canEnemyCapture(Piece piece, int enemyRow, int enemyCol, int landingRow, int landingCol, int turn) {
        // se il nemico è una dama e c'è spazio di atterraggio allora sotto attacco
        if (getPiece(enemyRow,enemyCol).getisKing() && getPiece(landingRow,landingCol) == null) return true; // la dama attacca sempre
        // altrimenti è una pedina normale, controlla la direzione
        if(!getPiece(enemyRow,enemyCol).getisKing()) {
            if(piece.getisKing()) return false;
            int direction = (turn == 0) ? 1 : -1;
            //verifica direzione, spazio di atterraggio e se è una dama normale
            return ((enemyRow == piece.getRow() + direction) && getPiece(landingRow, landingCol) == null);
        }
        return false;
    }

    //controlla se c'è una pedina sotto attacco
    public boolean isCapturable(Piece piece,int turn){
            int[] dirRow={1,-1};
            int[] dirCol={1,-1};
            int enemyRow;
            int enemyCol;
            int landingRow;
            int landingCol;
            //se è dama controlliamo tutte e 4 le direzioni
            for (int dirrow : dirRow) {
                enemyRow = piece.getRow() + dirrow;
                if (!isInside(enemyRow)) continue;
                for (int dircol : dirCol) {
                    enemyCol = piece.getCol() + dircol;
                    if (!isInside(enemyCol)) continue;
                    //se esiste un nemico
                    if((getPiece(enemyRow,enemyCol) == null)) continue;
                    if((getPiece(enemyRow,enemyCol).getColor() == piece.getColor())) continue;
                    landingRow = piece.getRow() - dirrow;
                    landingCol = piece.getCol() - dircol;
                    if (!isInside(landingRow) || !isInside(landingCol)) continue;
                    //System.out.println(enemyRow + " " + enemyCol+ " " + landingRow + " " + landingCol);
                    if(canEnemyCapture(piece, enemyRow, enemyCol, landingRow, landingCol,turn)) return true;
                }
            }
        return false;
    }

    //verifica se una pedina è diventata Damone
    private void promoteToKing(Piece piece,int turn){
        if(turn==0){
            if(piece.getRow() == 7) piece.setIsKing();
        }
        if(turn==1){
            if(piece.getRow() == 0) piece.setIsKing();
        }
    }

    //verifica se una pedina è dentro la scacchiera
    private boolean isInside(int number){
        boolean valid=false;
        if(number >= 0 && number < 8)
            valid=true;
        return valid;
    }

    //verifica se la pedina puo catturare
    private boolean canPieceCapture(Piece piece, int enemyRow, int enemyCol, int landingRow, int landingCol, int turn) {
        //se piece è una dama puo mangiare tutto
        if (piece.getisKing() && getPiece(landingRow,landingCol) == null) return true;
        // altrimenti è una pedina normale, controlla la direzione
        if(!piece.getisKing() && !getPiece(enemyRow,enemyCol).getisKing()) {
            int direction = (turn == 0) ? 1 : -1;
            //verifica direzione, spazio di atterraggio e se è una dama normale
            return ((enemyRow == piece.getRow() + direction) && getPiece(landingRow, landingCol) == null);
        }
        return false;
    }

    //verifica se c'è possibilità di cattura
    public boolean canCapture(Piece piece,int turn) {
        int[] dirRow = {2, -2};
        int[] dirCol = {2, -2};
        int landingRow;
        int landingCol;
        int enemyRow;
        int enemyCol;
        for (int dirrow : dirRow) {
            landingRow = piece.getRow() + dirrow;
            if (!isInside(landingRow)) continue;
            enemyRow = piece.getRow() + dirrow / 2;
            for (int dircol : dirCol) {
                landingCol = piece.getCol() + dircol;
                if (!isInside(landingCol)) continue;
                enemyCol = piece.getCol() + dircol / 2;
                if ((getPiece(landingRow, landingCol) != null)) continue;
                if ((getPiece(enemyRow, enemyCol) == null)) continue;
                if ((getPiece(enemyRow, enemyCol).getColor() == piece.getColor())) continue;
                if (canPieceCapture(piece, enemyRow, enemyCol, landingRow, landingCol, turn)) return true;
            }
        }
        return false;
    }

    //verifica se la mossa è lecita
    private boolean isLegalMove(Piece piece, Move move, int turn) {
        int direction = (turn == 0) ? 1 : -1;
        boolean correctRow;
        if(piece.getisKing()){
            correctRow = Math.abs(move.getRow() - piece.getRow()) == 1;
        } else {
            correctRow = (move.getRow() - piece.getRow()) == direction;
        }
        boolean correctCol = Math.abs(move.getCol() - piece.getCol()) == 1;
        return correctRow && correctCol;
    }

    //manca: catture per il damone e che una pedina non puo mangiare un damone
    private void removePiece(ArrayList<Piece> list,int row,int col){
        for (int i = 0; i < list.size(); i++) {
            Piece p = list.get(i);
            if (p.getRow() == row && p.getCol() == col) {
                list.remove(i);
                break;
            }
        }
    }
}
