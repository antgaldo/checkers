package ai;

import model.Board;
import model.Piece;

import static javafx.scene.paint.Color.WHITE;

public class MoveEvaluator {

    private Board board;

    public MoveEvaluator(Board board){
        this.board=board;
    }

    //funzione per stimare la bontà di uno stato (UTILITY)
    public int evaluator(Board board){
        //Assegniamo un valore alto di vittoria in modo tale da avere priorità assoluta sul resto
        if (board.getWhite().isEmpty()) return -1000000;
        if (board.getBlack().isEmpty()) return 1000000;
        int whiteScore=0;
        int blackScore=0;

        for(Piece piece: board.getWhite()){
            //Per ogni dama assegniamo un valore di 300 punti
            if(piece.getisKing()) whiteScore+=300;
            //Per ogni pedina normale assegniamo 100
            if(!piece.getisKing()) whiteScore+=100;
            //Per ogni pedina che resta in difesa 30
            if(piece.getRow()==0) whiteScore+=30;
            //Se la pedina sta proteggendo una dama 30 punti altrimenti 15 punti
            //Se invece sto proteggendo una pedina di spalle da una dama allora 5 punti (quasi impossibile ma non si sa mai)
            whiteScore+= isProtectingSomeone(piece,board,0);
            //Calcoliamo la distanza per diventare dama ed assegniamo 10 punti per ogni riga avanzata
            whiteScore+=getPromotionBonus(piece);
        }
        for(Piece piece: board.getBlack()){
            if(piece.getisKing()) blackScore+=300;
            if(!piece.getisKing()) blackScore+=100;
            if(piece.getRow()==7) blackScore+=30;
            blackScore+= isProtectingSomeone(piece,board,1);
            blackScore+=getPromotionBonus(piece);
        }

        return whiteScore-blackScore;
    }

    //funzione che ci occorre per verificare una pedina quanti pezzi protegge, utile per valutare
    private int isProtectingSomeone(Piece piece,Board board, int turn) {
        int count = 0;
        // Direzioni in cui una pedina può proteggere (solo davanti)
        int direction = (turn == 0) ? 1 : -1;
        // Se è una Dama, controlla tutte e 4 le direzioni.
        // Se è una pedina, controlla solo le 2 direzioni davanti.
        int[] dirRow = {1, -1};
        int[] dirCol = {1, -1};
        int friendRow;
        int friendCol;
        for (int dirrow : dirRow) {
            friendRow = piece.getRow() + dirrow;
            if (!isInside(friendRow)) continue;
            for (int dircol : dirCol) {
                friendCol = piece.getCol() + dircol;
                if (!isInside(friendCol)) continue;
                Piece friend= board.getPiece(friendRow,friendCol);
                if(friend != null && friend.getColor()==piece.getColor()){
                    //se la pedina che copre è una dama assegna 30 punti altrimenti 15 punti
                    if (dirrow == direction) {
                        count += friend.getisKing() ? 30 : 15;
                    }
                    //se invece mi piazzo avanti sto proteggendo lui (protezione di spalle contro una possibile dama)
                    else {
                        count += 5;
                    }
                }
            }
        }
        return count;
    }

    //calcolo distanza fino alla riga opposta per assegnare un punteggio
    private int getPromotionBonus(Piece piece) {
        if (piece.getisKing()) return 0;
        int row = piece.getRow();
        int pointsPerRow = 10;
        int bonus = 0;
        if (piece.getColor() == WHITE) {
            bonus = row * pointsPerRow;
        } else {
            bonus = (7 - row) * pointsPerRow;
        }
        return bonus;
    }

    //verifica se una pedina è dentro la scacchiera
    private boolean isInside(int number){
        boolean valid=false;
        if(number >= 0 && number < 8)
            valid=true;
        return valid;
    }
}
