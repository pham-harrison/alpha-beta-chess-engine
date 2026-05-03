package com.harrisonandtimmy.alphabetachessengine.model;

public class Move {
  private final Square from;
  private final Square to;
  private final Piece piece;
  private final Piece capturedPiece;

  public Move(Square from, Square to, Piece piece, Piece capturedPiece) {
    this.from = from;
    this.to = to;
    this.piece = piece;
    this.capturedPiece = capturedPiece;
  }

  public Square getFrom() {
    return this.from;
  }

  public Square getTo() {
    return this.to;
  }

  public Piece getPiece() {
    return this.piece;
  }

  public Piece getCapturedPiece() {
    return this.capturedPiece;
  }

  @Override
  public String toString() {
    return piece + ": " + this.from + "->" + this.to;
  }
}
