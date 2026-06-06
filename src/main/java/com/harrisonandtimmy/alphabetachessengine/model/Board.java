package com.harrisonandtimmy.alphabetachessengine.model;

import com.harrisonandtimmy.alphabetachessengine.model.piece.Piece;
import com.harrisonandtimmy.alphabetachessengine.model.piece.Bishop;
import com.harrisonandtimmy.alphabetachessengine.model.piece.King;
import com.harrisonandtimmy.alphabetachessengine.model.piece.Knight;
import com.harrisonandtimmy.alphabetachessengine.model.piece.Pawn;
import com.harrisonandtimmy.alphabetachessengine.model.piece.Queen;
import com.harrisonandtimmy.alphabetachessengine.model.piece.Rook;

import java.util.ArrayList;
import java.util.List;

public class Board {
  private Piece[][] grid;
  private Move lastMove;   // tracked for en passant

  public Board() {
    grid = new Piece[8][8];
    lastMove = null;
    initializeBoard();
  }

  // -------------------------------------------------------------------------
  // Board setup
  // -------------------------------------------------------------------------

  private void initializeBoard() {
    // --- Black back rank (row 0) ---
    setPiece(new Square(0, 0), new Rook(Color.BLACK));
    setPiece(new Square(0, 1), new Knight(Color.BLACK));
    setPiece(new Square(0, 2), new Bishop(Color.BLACK));
    setPiece(new Square(0, 3), new Queen(Color.BLACK));
    setPiece(new Square(0, 4), new King(Color.BLACK));
    setPiece(new Square(0, 5), new Bishop(Color.BLACK));
    setPiece(new Square(0, 6), new Knight(Color.BLACK));
    setPiece(new Square(0, 7), new Rook(Color.BLACK));

    // --- Black pawns (row 1) ---
    for (int col = 0; col < 8; col++) {
      setPiece(new Square(1, col), new Pawn(Color.BLACK));
    }

    // --- White pawns (row 6) ---
    for (int col = 0; col < 8; col++) {
      setPiece(new Square(6, col), new Pawn(Color.WHITE));
    }

    // --- White back rank (row 7) ---
    setPiece(new Square(7, 0), new Rook(Color.WHITE));
    setPiece(new Square(7, 1), new Knight(Color.WHITE));
    setPiece(new Square(7, 2), new Bishop(Color.WHITE));
    setPiece(new Square(7, 3), new Queen(Color.WHITE));
    setPiece(new Square(7, 4), new King(Color.WHITE));
    setPiece(new Square(7, 5), new Bishop(Color.WHITE));
    setPiece(new Square(7, 6), new Knight(Color.WHITE));
    setPiece(new Square(7, 7), new Rook(Color.WHITE));
  }

  // -------------------------------------------------------------------------
  // Basic grid access
  // -------------------------------------------------------------------------

  public Piece getPiece(Square square) {
    return grid[square.getRow()][square.getCol()];
  }

  public void setPiece(Square square, Piece piece) {
    if (square.isOnBoard()) {
      grid[square.getRow()][square.getCol()] = piece;
    }
  }

  public void removePiece(Square square) {
    if (square.isOnBoard()) {
      grid[square.getRow()][square.getCol()] = null;
    }
  }

  public Move getLastMove() {
    return lastMove;
  }

  // -------------------------------------------------------------------------
  // Move application / undo
  // -------------------------------------------------------------------------

  public void applyMove(Move move) {
    Piece piece = move.getPiece();
    removePiece(move.getFrom());

    if (move.isPromotion()) {
      setPiece(move.getTo(), new Queen(piece.getColor()));
    } else {
      setPiece(move.getTo(), piece);
    }

    // Set hasMoved flags
    if (piece instanceof King) {
      ((King) piece).setHasMoved(true);
    }
    if (piece instanceof Rook) {
      ((Rook) piece).setHasMoved(true);
    }

    // Move the rook too if castling
    if (move.isCastling()) {
      int row = move.getTo().getRow();
      boolean kingside = move.getTo().getCol() == 6;
      if (kingside) {
        Piece rook = getPiece(new Square(row, 7));
        removePiece(new Square(row, 7));
        setPiece(new Square(row, 5), rook);
      } else {
        Piece rook = getPiece(new Square(row, 0));
        removePiece(new Square(row, 0));
        setPiece(new Square(row, 3), rook);
      }
    }

    // Remove the captured pawn if en passant
    // The captured pawn is beside the landing square, not on it
    if (move.isEnPassant()) {
      int capturedPawnRow = move.getFrom().getRow();         // same row as the capturing pawn
      int capturedPawnCol = move.getTo().getCol();           // same col as where we land
      removePiece(new Square(capturedPawnRow, capturedPawnCol));
    }

    lastMove = move;
  }

  public void undoMove(Move move) {
    Piece piece = move.getPiece();

    // Put piece back, restore whatever was on destination
    setPiece(move.getFrom(), piece);
    setPiece(move.getTo(), move.getCapturedPiece());

    // Restore hasMoved to what it was before this move
    if (piece instanceof King) {
      ((King) piece).setHasMoved(move.pieceHadMoved());
    }
    if (piece instanceof Rook) {
      ((Rook) piece).setHasMoved(move.pieceHadMoved());
    }

    // Move rook back if castling, rook was always false before castling
    if (move.isCastling()) {
      int row = move.getTo().getRow();
      boolean kingside = move.getTo().getCol() == 6;
      if (kingside) {
        Piece rook = getPiece(new Square(row, 5));
        removePiece(new Square(row, 5));
        setPiece(new Square(row, 7), rook);
        ((Rook) rook).setHasMoved(false);
      } else {
        Piece rook = getPiece(new Square(row, 3));
        removePiece(new Square(row, 3));
        setPiece(new Square(row, 0), rook);
        ((Rook) rook).setHasMoved(false);
      }
    }

    // Restore captured pawn if en passant
    if (move.isEnPassant()) {
      int capturedPawnRow = move.getFrom().getRow();
      int capturedPawnCol = move.getTo().getCol();
      setPiece(new Square(capturedPawnRow, capturedPawnCol), move.getCapturedPiece());
    }

    lastMove = null;
  }

  // -------------------------------------------------------------------------
  // Move generation
  // -------------------------------------------------------------------------

  public List<Move> getAllValidMoves(Color color) {
    List<Move> moves = new ArrayList<>();
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Piece piece = grid[row][col];
        if (piece != null && piece.getColor() == color) {
          Square square = new Square(row, col);
          moves.addAll(piece.getValidMoves(this, square));
        }
      }
    }
    return moves;
  }

  public List<Move> getLegalMoves(Color color) {
    List<Move> allMoves   = getAllValidMoves(color);
    List<Move> legalMoves = new ArrayList<>();
    for (Move move : allMoves) {
      if (isLegalMove(move)) {
        legalMoves.add(move);
      }
    }
    return legalMoves;
  }

  public boolean isCheckmate(Color color) {
    return isInCheck(color) && getLegalMoves(color).isEmpty();
  }

  public boolean isStalemate(Color color) {
    return !isInCheck(color) && getLegalMoves(color).isEmpty();
  }

  public boolean isLegalMove(Move move) {
    applyMove(move);
    boolean leavesKingInCheck = isInCheck(move.getPiece().getColor());
    undoMove(move);
    return !leavesKingInCheck;
  }

  // -------------------------------------------------------------------------
  // King safety
  // -------------------------------------------------------------------------

  public boolean isInCheck(Color color) {
    Square kingSquare = findKing(color);
    if (kingSquare == null) {
      return false;
    }
    Color opponent = color.getOpposite();
    List<Move> opponentMoves = getAllValidMoves(opponent);
    for (Move move : opponentMoves) {
      if (move.getTo().equals(kingSquare)) {
        return true;
      }
    }
    return false;
  }

  public boolean isSquareAttacked(Square square, Color friendlyColor) {
    Color opponent = friendlyColor.getOpposite();
    List<Move> opponentMoves = getAllValidMoves(opponent);
    for (Move move : opponentMoves) {
      if (move.getTo().equals(square)) {
        return true;
      }
    }
    return false;
  }

  public Square findKing(Color color) {
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Piece piece = grid[row][col];
        if (piece != null
                && piece.getColor() == color
                && piece.getType() == PieceType.KING) {
          return new Square(row, col);
        }
      }
    }
    return null;
  }

  // -------------------------------------------------------------------------
  // Debug / display
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("  a b c d e f g h\n");
    for (int row = 0; row < 8; row++) {
      sb.append(8 - row).append(" ");
      for (int col = 0; col < 8; col++) {
        Piece piece = grid[row][col];
        if (piece == null) {
          sb.append(".");
        } else {
          sb.append(pieceSymbol(piece));
        }
        sb.append(" ");
      }
      sb.append(8 - row).append("\n");
    }
    sb.append("  a b c d e f g h\n");
    return sb.toString();
  }

  private char pieceSymbol(Piece piece) {
    char symbol = switch (piece.getType()) {
      case KING   -> 'K';
      case QUEEN  -> 'Q';
      case ROOK   -> 'R';
      case BISHOP -> 'B';
      case KNIGHT -> 'N';
      case PAWN   -> 'P';
    };
    return piece.getColor() == Color.BLACK ? Character.toLowerCase(symbol) : symbol;
  }
}