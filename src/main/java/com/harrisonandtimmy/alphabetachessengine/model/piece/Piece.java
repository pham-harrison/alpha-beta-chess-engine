package com.harrisonandtimmy.alphabetachessengine.model.piece;

import com.harrisonandtimmy.alphabetachessengine.model.Board;
import com.harrisonandtimmy.alphabetachessengine.model.Color;
import com.harrisonandtimmy.alphabetachessengine.model.Move;
import com.harrisonandtimmy.alphabetachessengine.model.PieceType;
import com.harrisonandtimmy.alphabetachessengine.model.Square;

import java.util.List;

public abstract class Piece {
  private final Color color;
  private final PieceType type;

  public Piece(Color color, PieceType type) {
    this.color = color;
    this.type = type;
  }

  public Color getColor() {
    return this.color;
  }

  public PieceType getType() {
    return this.type;
  }

  public abstract List<Move> getValidMoves(Board board, Square from);
}
