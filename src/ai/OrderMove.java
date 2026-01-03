package ai;

import model.Board;
import model.Move;
import model.Piece;

import java.util.ArrayList;

import static javafx.scene.paint.Color.WHITE;

public class OrderMove {

    public OrderMove(){}

    //Ordiniamo le mosse in base all'euristica in modo tale da velocizzare l'ai
    public ArrayList<Move> sortMove(ArrayList<Move> moves){
        ArrayList<Move> ordermoves= new ArrayList<Move>(moves);
        ordermoves.sort((m1, m2) -> Integer.compare(heuristic(m2), heuristic(m1))
        );
        return ordermoves;
    }

    //funzione per valutare la mossa
    private int heuristic(Move move){
        Piece piece= move.getPiece();
        int direction = piece.getColor()==WHITE ? 1:-1;
        int score=0;
            //se diventa dama (solo per le pedine)
            if(!piece.getisKing()) {
                if (direction == 1 && move.getRow() == 7) score += 300;
                if (direction == -1 && move.getRow() == 0) score += 300;
            }
            //Per ogni pezzo catturato aggiungi 100 punti
            score += move.getCapturedPieces().size() * 100;
        return score;
    }

}
