package com.harrisonandtimmy.alphabetachessengine.model;

public enum PieceType {
  KING(0),
  QUEEN(9),
  ROOK(5),
  BISHOP(3),
  KNIGHT(3),
  PAWN(1);

  private final int value;

  PieceType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
