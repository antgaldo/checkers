package ai;

import model.Board;
import model.Move;

import java.util.ArrayList;

public class MinMax {
    private MoveEvaluator moveevaluator;
    private OrderMove objectorder;

    public MinMax(Board board){
        this.moveevaluator=new MoveEvaluator(board);
        this.objectorder= new OrderMove();
    }

    //Serve a valutare la scelta
    private int minimax(Board board, int depth, int alpha, int beta, int turn) {
        //Se abbiamo raggiunto la profondità massima o la partita è finita
        if (depth == 0) {
            return moveevaluator.evaluator(board);
        }
        //genera lista delle mosse possibili in base al turno
        ArrayList<Move> moves = board.generateMoves(turn);
        //Se non ci sono mosse, il giocatore ha perso
        if (moves.isEmpty()) {
            return (turn == 0) ? -1000000 : 1000000;
        }
        //ordina le mosse per tagliare in maniera più efficente
        ArrayList<Move>  ordermove= objectorder.sortMove(moves);

        //Chiamate per MAX
        if (turn == 0) {
            //impostiamo una variabile a -infinito
            int maxEval = Integer.MIN_VALUE;
            //cicliamo ogni mossa ordinata in precedenza
            for (Move move : ordermove) {
                //eseguiamo la mossa sulla scacchiera
                board.makeMove(move);
                //chiamata ricorsiva per scendere in profondità; ritorna la valutazione fatta da evaluator (Utility)
                int utility = minimax(board, depth - 1, alpha, beta, 1);
                //dopo esser sceso in profondità riposizioniamo i pezzi come erano prima
                board.undoMove(move);
                //-infinito adesso avrà il valore più alto tra -infinito e il risultato della chiamata ricorsiva
                maxEval = Math.max(maxEval, utility);
                //prendiamo il valore massimo tra alpha e utility
                alpha = Math.max(alpha, utility);
                if (beta <= alpha) break; // Potatura Beta
            }
            return maxEval;
        }
        //Chiamate per MIN
        else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : ordermove) {
                board.makeMove(move);
                int utility = minimax(board, depth - 1, alpha, beta, 0);
                board.undoMove(move);
                minEval = Math.min(minEval, utility);
                beta = Math.min(beta, utility);
                if (beta <= alpha) break; // Potatura Alpha
            }
            return minEval;
        }
    }

    //Sceglie la miglior mossa in base al valore di minmax
    public Move getBestMove(Board board, int depth, int turn) {
        ArrayList<Move> moves = board.generateMoves(turn);
        moves = objectorder.sortMove(moves);

        Move bestMove = null;
        int bestScore = (turn == 0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Move move : moves) {
            //Simula la mossa
            board.makeMove(move);
            // Nota: scendiamo di 1 profondità e cambiamo turno (1 - turn)
            int currentScore = minimax(board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, 1 - turn);
            //Annulla la mossa
            board.undoMove(move);
            //Se questa mossa è la migliore trovata finora, salvala
            if (turn == 0) { // AI è Bianco (MAX)
                if (currentScore > bestScore) {
                    bestScore = currentScore;
                    bestMove = move;
                }
            } else { // AI è Nero (MIN)
                if (currentScore < bestScore) {
                    bestScore = currentScore;
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }
}
