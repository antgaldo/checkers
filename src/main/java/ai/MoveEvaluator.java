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
        //Se l'AI ha più scelte del nero aggiungiamo 5 punti per ogni scelta in più
        whiteScore= 5* (board.generateMoves(0).size()-board.generateMoves(1).size());
        //Se l'AI ha poche mosse di scelta verrà penalizzata
        if (board.generateMoves(0).size() < 3) whiteScore-= 150;

        //Diamo un valore maggiore alla dama nelle fasi finali del gioco
        int totalPieces = board.getWhite().size() + board.getBlack().size();
        boolean isEndGame = totalPieces < 8;
        int kingValue = isEndGame ? 500 : 350;

        for(Piece piece: board.getWhite()){
            //Per ogni dama assegniamo un valore di 300 punti
            if(piece.getisKing()) whiteScore+=kingValue;
            //Per ogni pedina normale assegniamo 100
            if(!piece.getisKing()) whiteScore+=100;
            //Per ogni pedina che resta in difesa 70
            if(piece.getRow()==0) whiteScore+=70;
            //Se la pedina sta proteggendo una dama 30 punti altrimenti 15 punti
            //Se invece sto proteggendo una pedina di spalle da una dama allora 5 punti (quasi impossibile ma non si sa mai)
            whiteScore+= isProtectingSomeone(piece,board,0);
            //Calcoliamo la distanza per diventare dama ed assegniamo 10 punti per ogni riga avanzata
            whiteScore+=getPromotionBonus(piece);
            //Per ogni pedina sul bordo
            if (piece.getCol() == 0 || piece.getCol() == 7) {whiteScore += 15;}
            //per ogni pedina che si trova in posizione centrale
            if (isCentral(piece)) {whiteScore += 20;}
        }

        blackScore= 5*(board.generateMoves(1).size()-board.generateMoves(0).size());
        if (board.generateMoves(1).size() < 3) blackScore-= 150;

        for(Piece piece: board.getBlack()){
            if(piece.getisKing()) blackScore+=kingValue;
            if(!piece.getisKing()) blackScore+=100;
            if(piece.getRow()==7) blackScore+=70;
            blackScore+= isProtectingSomeone(piece,board,1);
            blackScore+=getPromotionBonus(piece);
            if (piece.getCol() == 0 || piece.getCol() == 7) {blackScore += 15;}
            if (isCentral(piece)) {blackScore += 20;}
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
                        count += friend.getisKing() ? 50 : 30;
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

    //verifica se è in posizioni centrali
    private boolean isCentral(Piece p) {
        int r = p.getRow();
        int c = p.getCol();
        return (r >= 3 && r <= 4 && c >= 2 && c <= 5);
    }

    //verifica se una pedina è dentro la scacchiera
    private boolean isInside(int number){
        boolean valid=false;
        if(number >= 0 && number < 8)
            valid=true;
        return valid;
    }
}
