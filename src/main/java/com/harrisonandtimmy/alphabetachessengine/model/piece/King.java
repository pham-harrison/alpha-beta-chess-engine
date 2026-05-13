package com.harrisonandtimmy.alphabetachessengine.model.piece;

import com.harrisonandtimmy.alphabetachessengine.model.Board;
import com.harrisonandtimmy.alphabetachessengine.model.Color;
import com.harrisonandtimmy.alphabetachessengine.model.Move;
import com.harrisonandtimmy.alphabetachessengine.model.PieceType;
import com.harrisonandtimmy.alphabetachessengine.model.Square;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Color color) {
        super(color, PieceType.KING);
    }

    @Override
    public List<Move> getValidMoves(Board board, Square from) {
        List<Move> moves = new ArrayList<>();
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
                    moves.add(new Move(from, target, this, occupant));
                }
            }
        }
        return moves;
    }
}