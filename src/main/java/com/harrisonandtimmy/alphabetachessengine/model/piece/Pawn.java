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
        //if white then moves up else black moves down

        //forward move case
        Square forward = new Square(from.getRow() + direction, from.getCol());
        if (forward.isOnBoard() && board.getPiece(forward) == null) {
            moves.add(new Move(from, forward, this, null));

            // double move from starting rank
            boolean isStartingRank = (getColor() == Color.WHITE && from.getRow() == 6) ||
                    (getColor() == Color.BLACK && from.getRow() == 1);
            if (isStartingRank) {
                Square doubleForward = new Square(from.getRow() + (2 * direction), from.getCol());
                if (board.getPiece(doubleForward) == null) {
                    moves.add(new Move(from, doubleForward, this, null));
                }
            }
        }

        // capture
        int[] captureCols = {from.getCol() - 1, from.getCol() + 1};
        for (int col : captureCols) {
            Square target = new Square(from.getRow() + direction, col);
            if (target.isOnBoard()) {
                Piece occupant = board.getPiece(target);
                if (occupant != null && occupant.getColor() != this.getColor()) {
                    moves.add(new Move(from, target, this, occupant));
                }
            }
        }
        return moves;
    }
}