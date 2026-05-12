package com.harrisonandtimmy.alphabetachessengine.model.piece;

import com.harrisonandtimmy.alphabetachessengine.model.Color;
import com.harrisonandtimmy.alphabetachessengine.model.Move;
import com.harrisonandtimmy.alphabetachessengine.model.PieceType;
import com.harrisonandtimmy.alphabetachessengine.model.Square;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{


    public Queen(Color color) {
        super(color, PieceType.QUEEN);
    }

    @Override
    public List<Move> getValidMoves(Board board, Square from) {
        List<Move> moves = new ArrayList<>();

        int[][] directions = {
                {1, 1},
                {1, -1},
                {-1, -1},
                {-1, 1},
                {1,0},
                {-1,0},
                {0, 1},
                {0, -1}
        };

        for (int[] direction : directions) {
            int row = from.getRow() + direction[0];
            int col = from.getCol() + direction[1];

            Square nextSquare = new Square(row, col);
            while (nextSquare.isOnBoard()) {
                Piece occupant = board.getPiece(nextSquare);

                Move move;
                if (occupant == null) {
                    move = new Move(from, nextSquare, this, null);
                    moves.add(move);
                } else if (occupant.getColor() != this.getColor()) {
                    move = new Move(from, nextSquare, this, occupant);
                    moves.add(move);
                    break;
                } else {
                    break;
                }

                row += direction[0];
                col += direction[1];
                nextSquare = new Square(row, col);
            }
        }

        return moves;
    }
}
