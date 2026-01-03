package model;

import javafx.scene.paint.Color;

import java.util.ArrayList;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Board implements Cloneable{
    private Piece[][] board;
    private ArrayList<Piece> black;
    private ArrayList<Piece> white;
    private boolean hasCapture;

    public Board() {
        this.board = new Piece[8][8];
        this.black = new ArrayList<Piece>();
        this.white = new ArrayList<Piece>();
        this.hasCapture = false;
        initPieces();
        setupBoard();
    }

    public Piece[][] getBoard() {
        return board;
    }

    public int getRow() {
        return board.length;
    }

    public int getColumn() {
        return board[0].length;
    }

    public boolean getHasCapture() {
        return hasCapture;
    }

    private void initPieces() {
        for (int i = 0; i < 12; i++) white.add(new Piece(WHITE));
        for (int i = 0; i < 12; i++) black.add(new Piece(BLACK));
    }

    public ArrayList<Piece> getWhite() {
        return white;
    }

    public ArrayList<Piece> getBlack() {
        return black;
    }

    public void setupBoard() {
        int index = 11;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    Piece w = white.get(index--);
                    w.setRow(i);
                    w.setCol(j);
                    board[i][j] = w;
                }
            }
        }
        index = 11;
        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    Piece b = black.get(index--);
                    b.setRow(i);
                    b.setCol(j);
                    board[i][j] = b;
                }
            }
        }
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    private void setBoxNull(int row, int col) {
        board[row][col] = null;
    }

    public boolean movePiece(Move move, Piece piece, int turn, boolean mustcapture) {
        boolean ok = true;
        hasCapture = false;
        if (this.isMoveLegal(move, piece, turn, mustcapture)) {
            //se è una mossa cattura allora esegui
            if ((Math.abs(move.getRow() - piece.getRow()) == 2) && (Math.abs(move.getCol() - piece.getCol()) == 2)) {
                //se è una pedina normale valuta la direzione , se è damone no
                int enemyRow = piece.getisKing() ? (piece.getRow() + move.getRow()) / 2 : piece.getRow() + ((turn == 0) ? 1 : -1);
                int enemyCol = (move.getCol() + piece.getCol()) / 2;
                removePiece(turn == 0 ? black : white, enemyRow, enemyCol);
                setBoxNull(enemyRow, enemyCol);
                hasCapture = true;
            }
            //sposta la pedina
            board[piece.getRow()][piece.getCol()] = null;
            board[move.getRow()][move.getCol()] = piece;
            piece.setRow(move.getRow());
            piece.setCol(move.getCol());
            //verifica se la pedina è diventata Dama
            this.promoteToKing(piece, turn);
        } else ok = false;
        return ok;
    }

    public boolean isMoveLegal(Move move, Piece piece, int turn, boolean mustcapture) {
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
        int enemyRow = piece.getisKing() ? (piece.getRow() + move.getRow()) / 2 : piece.getRow() + direction;
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
    public boolean checkisCapturable(int turn) {
        ArrayList<Piece> array = turn == 0 ? white : black;
        for (int i = array.size() - 1; i >= 0; i--) {
            if (isCapturable(array.get(i), turn)) {
                return true;
            }
        }
        return false;
    }

    //verifica se il nemico puo catturare
    private boolean canEnemyCapture(Piece piece, int enemyRow, int enemyCol, int landingRow, int landingCol, int turn) {
        // se il nemico è una dama e c'è spazio di atterraggio allora sotto attacco
        if (getPiece(enemyRow, enemyCol).getisKing() && getPiece(landingRow, landingCol) == null)
            return true; // la dama attacca sempre
        // altrimenti è una pedina normale, controlla la direzione
        if (!getPiece(enemyRow, enemyCol).getisKing()) {
            if (piece.getisKing()) return false;
            int direction = (turn == 0) ? 1 : -1;
            //verifica direzione, spazio di atterraggio e se è una dama normale
            return ((enemyRow == piece.getRow() + direction) && getPiece(landingRow, landingCol) == null);
        }
        return false;
    }

    //controlla se c'è una pedina sotto attacco
    public boolean isCapturable(Piece piece, int turn) {
        int[] dirRow = {1, -1};
        int[] dirCol = {1, -1};
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
                if ((getPiece(enemyRow, enemyCol) == null)) continue;
                if ((getPiece(enemyRow, enemyCol).getColor() == piece.getColor())) continue;
                landingRow = piece.getRow() - dirrow;
                landingCol = piece.getCol() - dircol;
                if (!isInside(landingRow) || !isInside(landingCol)) continue;
                //System.out.println(enemyRow + " " + enemyCol+ " " + landingRow + " " + landingCol);
                if (canEnemyCapture(piece, enemyRow, enemyCol, landingRow, landingCol, turn)) return true;
            }
        }
        return false;
    }

    //verifica se una pedina è diventata Damone
    private void promoteToKing(Piece piece, int turn) {
        if (turn == 0) {
            if (piece.getRow() == 7) piece.setIsKing(true);
        }
        if (turn == 1) {
            if (piece.getRow() == 0) piece.setIsKing(true);
        }
    }

    //verifica se una pedina è dentro la scacchiera
    private boolean isInside(int number) {
        boolean valid = false;
        if (number >= 0 && number < 8)
            valid = true;
        return valid;
    }

    //funzione di supporto per canCapture
    private boolean canPieceCapture(Piece piece, int enemyRow, int enemyCol, int landingRow, int landingCol, int turn) {
        //se piece è una dama puo mangiare tutto
        if (piece.getisKing() && getPiece(landingRow, landingCol) == null) return true;
        // altrimenti è una pedina normale, controlla la direzione
        if (!piece.getisKing() && !getPiece(enemyRow, enemyCol).getisKing()) {
            int direction = (turn == 0) ? 1 : -1;
            //verifica direzione, spazio di atterraggio e se è una dama normale
            return ((enemyRow == piece.getRow() + direction) && getPiece(landingRow, landingCol) == null);
        }
        return false;
    }

    //verifica se c'è possibilità di cattura
    public boolean canCapture(Piece piece, int turn) {
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
        if (piece.getisKing()) {
            correctRow = Math.abs(move.getRow() - piece.getRow()) == 1;
        } else {
            correctRow = (move.getRow() - piece.getRow()) == direction;
        }
        boolean correctCol = Math.abs(move.getCol() - piece.getCol()) == 1;
        return correctRow && correctCol;
    }

    private void removePiece(ArrayList<Piece> list, int row, int col) {
        for (int i = 0; i < list.size(); i++) {
            Piece p = list.get(i);
            if (p.getRow() == row && p.getCol() == col) {
                list.remove(i);
                break;
            }
        }
    }

    //FUNZIONI PER L'AI; manca gestione delle catture multiple
    //Crea una lista con tutte le mosse possibili per il giocatore
    public ArrayList<Move> generateMoves(int turn) {
        ArrayList<Move> listmove = new ArrayList<>();
        ArrayList<Piece> pieces = (turn == 0) ? white : black;
        boolean captureExists = false;

        //verifica se mossa cattura
        for (Piece piece : pieces) {
            if (canCapture(piece, turn)) {
                captureExists = true;
                break;
            }
        }

        //se mossa cattura aggiungi alla lista
        //(le mosse di cattura multiple sono atomiche quindi la funzione getMoveCapture restituisce solo il move finale
        for (Piece piece : pieces) {
            if (captureExists) {
                ArrayList<Move> pieceMoves = getMoveCapture(piece, turn);
                listmove.addAll(pieceMoves);
            } else {
                ArrayList<Move> pieceMoves = getSimpleMoves(piece, turn);
                listmove.addAll(pieceMoves);
            }
        }

        return listmove;
    }

    //restituisci una mossa semplice
    public ArrayList<Move> getSimpleMoves(Piece piece, int turn) {
        ArrayList coordinate = new ArrayList<Move>();
        int[] dirRow = {1, -1};
        int[] dirCol = {1, -1};
        int landingRow;
        int landingCol;
        for (int dirrow : dirRow) {
            landingRow = piece.getRow() + dirrow;
            if (!isInside(landingRow)) continue;
            for (int dircol : dirCol) {
                landingCol = piece.getCol() + dircol;
                if (!isInside(landingCol)) continue;
                if ((getPiece(landingRow, landingCol) != null)) continue;
                if (canPieceMove(piece, landingRow, landingCol, turn)) {
                    coordinate.add(new Move(piece, false, landingRow, landingCol, piece.getRow(), piece.getCol()));
                }
            }
        }
        return coordinate;
    }

    //verifica se lecita la mossa semplice
    private boolean canPieceMove(Piece piece, int landingRow, int landingCol, int turn) {
        int direction = (turn == 0) ? 1 : -1;
        if (piece.getisKing()) return true;
        if ((piece.getColor() == WHITE) && landingRow > piece.getRow()) return true;
        if ((piece.getColor() == BLACK) && landingRow < piece.getRow()) return true;
        return false;
    }

    //restituisci le coordinate di cattura
    public ArrayList<Move> getMoveCapture(Piece piece, int turn) {
        ArrayList coordinate = new ArrayList<Move>();
        int[] dirRow = {2, -2};
        int[] dirCol = {2, -2};
        int startrow = piece.getRow();
        int startcol = piece.getCol();
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
                if (canPieceCapture(piece, enemyRow, enemyCol, landingRow, landingCol, turn)) {
                    //Simulazione per verificare se ci sono altre possibili mangiate dopo la prima
                    //rimozione del nemico mangiato e sposto la pedina in avanti
                    Piece enemy = getPiece(enemyRow, enemyCol);
                    board[enemyRow][enemyCol] = null;
                    piece.setRow(landingRow);
                    piece.setCol(landingCol);
                    //Chiamata ricorsiva per vedere se ci sono altre possibili catture
                    //Controlliamo se da questa nuova posizione può mangiare ancora
                    ArrayList<Move> nextMoves = getMoveCapture(piece, turn);

                    //Se la lista è vuota vuol dire che non può più mangiare
                    if (nextMoves.isEmpty()) {
                        // Aggiungo questa casella come destinazione finale.
                        Move finalMove = new Move(piece, true, landingRow, landingCol,startrow,startcol);
                        finalMove.addCapturedPiece(enemy);
                        coordinate.add(finalMove);
                    } else {
                        // BIVIO O CONTINUAZIONE: la ricorsione ha trovato la fine della strada
                        for (Move m : nextMoves) {
                            // Creiamo la mossa atomica che arriva alla destinazione finale
                            Move atomicMove = new Move(piece, true, m.getRow(), m.getCol(), startrow, startcol);
                            // Copiamo tutti i pezzi mangiati nei salti successivi futuri
                            atomicMove.getCapturedPieces().addAll(m.getCapturedPieces());
                            // Aggiungiamo il nemico mangiato in QUESTO salto specifico
                            atomicMove.addCapturedPiece(enemy);
                            coordinate.add(atomicMove);
                        }
                    }
                    //Ripristino della posizione iniziale della pedina e del nemico
                    piece.setRow(startrow);
                    piece.setCol(startcol);
                    board[enemyRow][enemyCol] = enemy;
                }
            }
        }
        return coordinate;
    }

    //Funzioni utili per non dover ricreare board quando viene chiamata generateMovies
    //Con queste funzioni andiamo avanti e indietro su board reale, molto più veloce (Backtracking)
    //Per andare avanti
    public void makeMove(Move move) {
        Piece piece = move.getPiece();
        //Spostamento sulla scacchiera
        board[move.getStartRow()][move.getStartCol()] = null;
        board[move.getRow()][move.getCol()] = piece;
        //Aggiorna le coordinate interne dell'oggetto Piece
        piece.setRow(move.getRow());
        piece.setCol(move.getCol());
        //Rimuovi i pezzi catturati (se presenti)
        if (move.getIsCapture()) {
            for (Piece enemy : move.getCapturedPieces()) {
                board[enemy.getRow()][enemy.getCol()] = null;
                //Rimuoviamo il pezzo dalle liste attive
                if (enemy.getColor() == WHITE) white.remove(enemy);
                else black.remove(enemy);
            }
        }
        //Gestione Promozione
        if (!piece.getisKing()) {
            if ((piece.getColor() == WHITE && piece.getRow() == 7) || (piece.getColor() == BLACK && piece.getRow() == 0)) {
                piece.setIsKing(true);
                //Per poter tornare indietro
                move.setPromoted(true);
            }
        }
    }

    //Per poter tornare indietro
    public void undoMove(Move move) {
        Piece piece = move.getPiece();
        // Svuotiamo la casella dove il pezzo era arrivato (endRow, endCol)
        board[move.getRow()][move.getCol()] = null;
        // Rimettiamo il pezzo nella casella da cui era partito (startRow, startCol)
        board[move.getStartRow()][move.getStartCol()] = piece;
        // Riportiamo il pezzo al suo stato originale
        piece.setRow(move.getStartRow());
        piece.setCol(move.getStartCol());
        // Se la mossa aveva trasformato la pedina in dama, ora deve tornare pedina
        if (move.getPromoted()) {
            piece.setIsKing(false); // Assicurati che Piece abbia setIsKing(boolean)
        }
        // Se la mossa era una cattura, resuscitiamo i nemici
        if (!move.getCapturedPieces().isEmpty()) {
            for (Piece enemy : move.getCapturedPieces()) {
                // Rimettiamo il nemico fisicamente sulla matrice
                board[enemy.getRow()][enemy.getCol()] = enemy;
                // Re-inseriamo il nemico nelle liste usate dall'evaluator
                // Se il colore del nemico è bianco (0) va in white, altrimenti in black
                if (enemy.getColor() == WHITE) {
                    if (!white.contains(enemy)) white.add(enemy);
                } else {
                    if (!black.contains(enemy)) black.add(enemy);
                }
            }
        }
    }

    //Necessario per poterlo usare con l'ai altrimenti modifica la board originale
    @Override
    public Board clone() {
        Board clonedBoard = new Board(); // Crea una nuova scacchiera vuota
        // 1. Svuota le liste create dal costruttore (perché vogliamo riempirle noi)
        clonedBoard.getWhite().clear();
        clonedBoard.getBlack().clear();

        // 2. Svuota la matrice
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                clonedBoard.getBoard()[i][j] = null;
            }
        }

        for (Piece p : this.white) {
            Piece newP = new Piece(p.getColor()); // Nuovo Oggetto!
            newP.setRow(p.getRow());
            newP.setCol(p.getCol());
            newP.setIsKing(p.getisKing());

            // Aggiungi alla nuova scacchiera
            clonedBoard.getWhite().add(newP);
            clonedBoard.getBoard()[newP.getRow()][newP.getCol()] = newP;
        }
        for (Piece p : this.black) {
            Piece newP = new Piece(p.getColor());
            newP.setRow(p.getRow());
            newP.setCol(p.getCol());
            newP.setIsKing(p.getisKing());

            clonedBoard.getBlack().add(newP);
            clonedBoard.getBoard()[newP.getRow()][newP.getCol()] = newP;
        }

        return clonedBoard;
    }
}
