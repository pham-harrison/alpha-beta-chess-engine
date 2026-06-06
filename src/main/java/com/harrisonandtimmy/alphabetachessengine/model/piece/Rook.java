package com.harrisonandtimmy.alphabetachessengine.model.piece;

import com.harrisonandtimmy.alphabetachessengine.model.Board;
import com.harrisonandtimmy.alphabetachessengine.model.Color;
import com.harrisonandtimmy.alphabetachessengine.model.Move;
import com.harrisonandtimmy.alphabetachessengine.model.PieceType;
import com.harrisonandtimmy.alphabetachessengine.model.Square;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
  private boolean hasMoved = false;

  public Rook(Color color) {
    super(color, PieceType.ROOK);
  }

  public boolean hasMoved() {
    return hasMoved;
  }

  public void setHasMoved(boolean hasMoved) {
    this.hasMoved = hasMoved;
  }

  @Override
  public List<Move> getValidMoves(Board board, Square from) {
    List<Move> moves = new ArrayList<>();

    int[][] directions = {
            {0, 1},
            {0, -1},
            {1, 0},
            {-1, 0}
    };

    for (int[] direction : directions) {
      int row = from.getRow() + direction[0];
      int col = from.getCol() + direction[1];

      Square nextSquare = new Square(row, col);
      while (nextSquare.isOnBoard()) {
        Piece occupant = board.getPiece(nextSquare);

        if (occupant == null) {
          moves.add(new Move(from, nextSquare, this, null,
                  false, false, false,
                  this.hasMoved()));  // snapshot rook's current hasMoved
        } else if (occupant.getColor() != this.getColor()) {
          moves.add(new Move(from, nextSquare, this, occupant,
                  false, false, false,
                  this.hasMoved()));  // snapshot rook's current hasMoved
          break;
        } else {
          break;
        }

        row += direction[0];
        col += direction[1];
        nextSquare = new Square(row, col);
      }
    }

    return moves;
  }
}