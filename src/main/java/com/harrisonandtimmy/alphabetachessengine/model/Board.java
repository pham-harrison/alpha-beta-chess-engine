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
  //Constructor
  public Board() {
    grid = new Piece[8][8];
    initializeBoard();
  }

  // -------------------------------------------------------------------------
  // Board setup
  // -------------------------------------------------------------------------

  /**
   * Places all 32 pieces in their standard chess starting positions.
   * Row 0 = rank 8 (Black back rank)
   * Row 7 = rank 1 (White back rank)
   */
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

    // --- Empty rows 2–5 ---
    // grid is already null-initialized, nothing to do.

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

  /** Returns the piece at the given square, or null if the square is empty. */
  public Piece getPiece(Square square) {
    return grid[square.getRow()][square.getCol()];
  }

  /** Places a piece (or null) at the given square. Updates the piece's stored square. */
  public void setPiece(Square square, Piece piece) {
    if (square.isOnBoard()) {
      grid[square.getRow()][square.getCol()] = piece;
    }
  }

  /** Clears the given square (sets it to null). */
  public void removePiece(Square square) {
    if (square.isOnBoard()) {
      grid[square.getRow()][square.getCol()] = null;
    }
  }

  // -------------------------------------------------------------------------
  // Move application / undo
  // -------------------------------------------------------------------------


  public void applyMove(Move move) {
    removePiece(move.getFrom());
    if (move.isPromotion()) {
      setPiece(move.getTo(), new Queen(move.getPiece().getColor()));
    } else {
      setPiece(move.getTo(), move.getPiece());
    }
  }

  /**
   * Reverses a move, restoring any captured piece to its original square.
   */
  public void undoMove(Move move) {
    Square from          = move.getFrom();
    Square to            = move.getTo();
    Piece  piece         = move.getPiece();
    Piece  capturedPiece = move.getCapturedPiece();

    setPiece(from, piece);
    // Restore the captured piece (or null if the square was empty).
    setPiece(to, capturedPiece);
  }

  // -------------------------------------------------------------------------
  // Move generation
  // -------------------------------------------------------------------------

  /**
   * Collects every pseudo-legal move for the given color by asking each
   * piece for its valid moves. Does NOT filter for legality (king safety).
   */
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

  /**
   * Returns only the truly legal moves for the given color:
   * every pseudo-legal move that does NOT leave the friendly king in check.
   */
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
  /**
   * Applies the move temporarily, checks whether the moving side's king
   * is in check, then undoes the move. Returns true if the position is safe.
   */
  public boolean isLegalMove(Move move) {
    applyMove(move);
    boolean leavesKingInCheck = isInCheck(move.getPiece().getColor());
    undoMove(move);
    return !leavesKingInCheck;
  }

  // -------------------------------------------------------------------------
  // King safety
  // -------------------------------------------------------------------------

  /**
   * Returns true if the king of the given color is currently attacked by
   * any enemy piece.
   */
  public boolean isInCheck(Color color) {
    Square kingSquare = findKing(color);
    if (kingSquare == null) {
      // Should never happen in a legal game, but guard anyway.
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

  /**
   * Scans the board and returns the square occupied by the king of the
   * given color. Returns null if the king is not found.
   */
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

  /**
   * Prints an 8×8 ASCII representation of the board to stdout.
   * Upper-case letters = White, lower-case = Black, '.' = empty.
   */
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

  /** Maps a piece to a single display character (upper = White, lower = Black). */
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