package com.harrisonandtimmy.alphabetachessengine.model;

import java.util.ArrayList;
import java.util.List;
import com.harrisonandtimmy.alphabetachessengine.model.piece.Piece;

public class GameState {

    private final Board board;
    private Color currentTurn;
    private final List<Move> moveHistory;
    private boolean isCheck;
    private boolean isCheckmate;
    private boolean isStalemate;

    public GameState() {
        board = new Board();
        currentTurn = Color.WHITE;
        moveHistory = new ArrayList<>();
        isCheck = false;
        isCheckmate = false;
        isStalemate = false;
    }

    public boolean applyMove(Square from, Square to) {
        Piece piece = board.getPiece(from);

        if (piece == null || piece.getColor() != currentTurn) {
            return false;
        }

        List<Move> legalMoves = board.getLegalMoves(currentTurn);
        Move matchedMove = null;
        for (Move move : legalMoves) {
            if (move.getFrom().equals(from) && move.getTo().equals(to)) {
                matchedMove = move;
                break;
            }
        }

        if (matchedMove == null) {
            return false;
        }

        board.applyMove(matchedMove);
        moveHistory.add(matchedMove);
        currentTurn = currentTurn.getOpposite();
        updateStatus();
        return true;
    }

    private void updateStatus() {
        isCheck     = board.isInCheck(currentTurn);
        isCheckmate = board.isCheckmate(currentTurn);
        isStalemate = board.isStalemate(currentTurn);
    }

    public boolean isGameOver() {
        return isCheckmate || isStalemate;
    }

    public Board getBoard() {
        return board;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public boolean isStalemate() {
        return isStalemate;
    }
}