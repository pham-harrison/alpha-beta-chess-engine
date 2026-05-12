package com.harrisonandtimmy.alphabetachessengine.model.piece;

public class Knight {
    public Knight(Color color) {
        super(color, PieceType.KNIGHT);
    }
    @Override
    public List<Move> getValidMoves(Board board, Square from) {
        List<Move> moves = new ArrayList<>();
        int[][] jumps = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        for (int[] jump : jumps) {
            int row = from.getRow() + jump[0];
            int col = from.getCol() + jump[1];
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
