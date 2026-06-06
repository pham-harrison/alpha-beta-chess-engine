package com.harrisonandtimmy.alphabetachessengine.model.piece;

import com.harrisonandtimmy.alphabetachessengine.model.Board;
import com.harrisonandtimmy.alphabetachessengine.model.Color;
import com.harrisonandtimmy.alphabetachessengine.model.Move;
import com.harrisonandtimmy.alphabetachessengine.model.PieceType;
import com.harrisonandtimmy.alphabetachessengine.model.Square;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private boolean hasMoved = false;

    public King(Color color) {
        super(color, PieceType.KING);
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

        // Normal king moves
        int[][] directions = {
                {1, 1}, {1, -1}, {-1, -1}, {-1, 1},
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };
        for (int[] d : directions) {
            int row = from.getRow() + d[0];
            int col = from.getCol() + d[1];
            Square target = new Square(row, col);
            if (target.isOnBoard()) {
                Piece occupant = board.getPiece(target);
                if (occupant == null || occupant.getColor() != this.getColor()) {
                    moves.add(new Move(from, target, this, occupant,
                            false, false, false,
                            this.hasMoved()));  // snapshot king's current hasMoved
                }
            }
        }

        // Castling — only if king hasn't moved and isn't in check
        if (!hasMoved && !board.isInCheck(getColor())) {
            int row = from.getRow();

            // Kingside
            Piece kingsideRook = board.getPiece(new Square(row, 7));
            if (kingsideRook instanceof Rook && !((Rook) kingsideRook).hasMoved()) {
                if (board.getPiece(new Square(row, 5)) == null &&
                        board.getPiece(new Square(row, 6)) == null &&
                        !board.isSquareAttacked(new Square(row, 5), getColor()) &&
                        !board.isSquareAttacked(new Square(row, 6), getColor())) {
                    moves.add(new Move(from, new Square(row, 6), this, null,
                            false, true, false,
                            this.hasMoved()));  // always false, castling requires it
                }
            }

            // Queenside
            Piece queensideRook = board.getPiece(new Square(row, 0));
            if (queensideRook instanceof Rook && !((Rook) queensideRook).hasMoved()) {
                if (board.getPiece(new Square(row, 1)) == null &&
                        board.getPiece(new Square(row, 2)) == null &&
                        board.getPiece(new Square(row, 3)) == null &&
                        !board.isSquareAttacked(new Square(row, 2), getColor()) &&
                        !board.isSquareAttacked(new Square(row, 3), getColor())) {
                    moves.add(new Move(from, new Square(row, 2), this, null,
                            false, true, false,
                            this.hasMoved()));  // always false, castling requires it
                }
            }
        }

        return moves;
    }
}