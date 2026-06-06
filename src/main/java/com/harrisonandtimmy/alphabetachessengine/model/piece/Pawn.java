package com.harrisonandtimmy.alphabetachessengine.model.piece;

import com.harrisonandtimmy.alphabetachessengine.model.Board;
import com.harrisonandtimmy.alphabetachessengine.model.Color;
import com.harrisonandtimmy.alphabetachessengine.model.Move;
import com.harrisonandtimmy.alphabetachessengine.model.PieceType;
import com.harrisonandtimmy.alphabetachessengine.model.Square;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public List<Move> getValidMoves(Board board, Square from) {
        List<Move> moves = new ArrayList<>();
        int direction = (getColor() == Color.WHITE) ? -1 : 1;

        // Forward move
        Square forward = new Square(from.getRow() + direction, from.getCol());
        if (forward.isOnBoard() && board.getPiece(forward) == null) {
            boolean promotion = (forward.getRow() == 0 || forward.getRow() == 7);
            moves.add(new Move(from, forward, this, null, promotion));

            // Double push from starting rank
            boolean isStartingRank = (getColor() == Color.WHITE && from.getRow() == 6) ||
                    (getColor() == Color.BLACK && from.getRow() == 1);
            if (isStartingRank) {
                Square doubleForward = new Square(from.getRow() + (2 * direction), from.getCol());
                if (board.getPiece(doubleForward) == null) {
                    moves.add(new Move(from, doubleForward, this, null));
                }
            }
        }

        // Normal captures
        int[] captureCols = {from.getCol() - 1, from.getCol() + 1};
        for (int col : captureCols) {
            Square target = new Square(from.getRow() + direction, col);
            if (target.isOnBoard()) {
                Piece occupant = board.getPiece(target);
                if (occupant != null && occupant.getColor() != this.getColor()) {
                    boolean promotion = (target.getRow() == 0 || target.getRow() == 7);
                    moves.add(new Move(from, target, this, occupant, promotion));
                }
            }
        }

        // En passant
        Move lastMove = board.getLastMove();
        if (lastMove != null) {
            Piece lastPiece = lastMove.getPiece();
            boolean wasDoublePush = lastPiece.getType() == PieceType.PAWN &&
                    Math.abs(lastMove.getFrom().getRow() - lastMove.getTo().getRow()) == 2;

            if (wasDoublePush) {
                // The enemy pawn that just double pushed
                Square enemyPawnSquare = lastMove.getTo();

                // Is it on the same row as us and right beside us?
                boolean sameRow    = enemyPawnSquare.getRow() == from.getRow();
                boolean adjacent   = Math.abs(enemyPawnSquare.getCol() - from.getCol()) == 1;

                if (sameRow && adjacent) {
                    // We move diagonally forward to the square the enemy pawn passed through
                    Square enPassantTarget = new Square(from.getRow() + direction, enemyPawnSquare.getCol());
                    moves.add(new Move(from, enPassantTarget, this, lastPiece, false, false, true));
                }
            }
        }

        return moves;
    }
}