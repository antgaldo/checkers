package game;

import model.Piece;

public interface Listener {
    void onPieceClick(Piece piece);
    void onBoxClick(int row,int col);
}
