package com.harrisonandtimmy.alphabetachessengine.model;

import com.harrisonandtimmy.alphabetachessengine.model.piece.Piece;

public class Board {
  private Piece[][] grid;

  public Board() {
    grid = new Piece[8][8];
    initializeBoard();
  }

  public Piece getPiece(Square square) {
    return grid[square.getRow()][square.getCol()];
  }

  /*
  This is what Claude recommends for this class

  Board() — constructor, creates the grid and calls initializeBoard()
  initializeBoard() — places all pieces in their starting positions on the grid
  getPiece(Square) — returns whatever piece is at that square, or null if empty
  ~   setPiece(Square, Piece) — places a piece at a square
  ~   removePiece(Square) — sets a square to null
  applyMove(Move) — moves a piece from one square to another, handles auto-promotion
  undoMove(Move) — reverses a move, restores captured piece if there was one
  getAllValidMoves(Color) — loops over every square, finds all pieces of that color, collects all their valid moves into one list
  isInCheck(Color) — checks if the king of that color is currently being attacked by any enemy piece
  isLegalMove(Move) — applies a move, checks if own king is in check afterward, undoes the move, returns true or false. This is how you filter out moves that would leave your king exposed
  getLegalMoves(Color) — calls getAllValidMoves() then filters each one through isLegalMove(), returns only truly legal moves
  findKing(Color) — scans the grid to find the king of a given color, returns its square. Used by isInCheck()
  toString() — prints the board to the terminal
  */
}
public void setPiece(Square square, Piece piece) {
  if (square.isOnBoard()) {
    grid[square.getRow()][square.getCol()] = piece;
    // If we are placing an actual piece (not null),
    // tell the piece its new location.
    if (piece != null) {
      piece.setSquare(square);
    }
  }
}

public void removePiece(Square square) {
  if (square.isOnBoard()) {
    grid[square.getRow()][square.getCol()] = null;
  }
}