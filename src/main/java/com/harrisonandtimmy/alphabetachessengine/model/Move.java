package com.harrisonandtimmy.alphabetachessengine.model;

import com.harrisonandtimmy.alphabetachessengine.model.piece.Piece;

public class Move {
  private final Square from;
  private final Square to;
  private final Piece piece;
  private final Piece capturedPiece;
  private final boolean isPromotion;
  private final boolean isCastling;
  private final boolean isEnPassant;
  private final boolean pieceHadMoved;

  // Full constructor
  public Move(Square from, Square to, Piece piece, Piece capturedPiece,
              boolean isPromotion, boolean isCastling, boolean isEnPassant,
              boolean pieceHadMoved) {
    this.from = from;
    this.to = to;
    this.piece = piece;
    this.capturedPiece = capturedPiece;
    this.isPromotion = isPromotion;
    this.isCastling = isCastling;
    this.isEnPassant = isEnPassant;
    this.pieceHadMoved = pieceHadMoved;
  }

  // Convenience constructor — used by most pieces
  public Move(Square from, Square to, Piece piece, Piece capturedPiece) {
    this(from, to, piece, capturedPiece, false, false, false, false);
  }

  // Promotion constructor — used by Pawn
  public Move(Square from, Square to, Piece piece, Piece capturedPiece, boolean isPromotion) {
    this(from, to, piece, capturedPiece, isPromotion, false, false, false);
  }

  // En passant constructor — used by Pawn
  public Move(Square from, Square to, Piece piece, Piece capturedPiece,
              boolean isPromotion, boolean isCastling, boolean isEnPassant) {
    this(from, to, piece, capturedPiece, isPromotion, isCastling, isEnPassant, false);
  }

  public Square getFrom() { return this.from; }
  public Square getTo() { return this.to; }
  public Piece getPiece() { return this.piece; }
  public Piece getCapturedPiece() { return this.capturedPiece; }
  public boolean isPromotion() { return this.isPromotion; }
  public boolean isCastling() { return this.isCastling; }
  public boolean isEnPassant() { return this.isEnPassant; }
  public boolean pieceHadMoved() { return this.pieceHadMoved; }

  @Override
  public String toString() {
    return piece + ": " + this.from + "->" + this.to;
  }
}