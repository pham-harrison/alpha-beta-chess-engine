package com.harrisonandtimmy.alphabetachessengine.ai;

import com.harrisonandtimmy.alphabetachessengine.model.Board;
import com.harrisonandtimmy.alphabetachessengine.model.Color;
import com.harrisonandtimmy.alphabetachessengine.model.GameState;
import com.harrisonandtimmy.alphabetachessengine.model.Move;
import com.harrisonandtimmy.alphabetachessengine.model.Square;
import com.harrisonandtimmy.alphabetachessengine.model.piece.Piece;

import java.util.List;

public class AlphaBetaStrategy implements AIStrategy {
  private static final int DEPTH = 3;

  @Override
  public Move getNextMove(GameState gameState) {
    return null;
  }

  private int evaluateState(Board board, Color aiColor) {
    int score = 0;
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Square square = new Square(row, col);
        Piece piece = board.getPiece(square);
        if (piece == null) continue;

        int value = piece.getType().getValue();
        if (piece.getColor() == aiColor) {
          score += value;
        } else {
          score -= value;
        }
      }
    }
    return score;
  }

  private int alphaBetaMinimax(Board board, int depth, int alpha, int beta, boolean isMaxPlayer, Color aiColor) {
    if (depth == 0) {
      return evaluateState(board, aiColor);
    }

    Color currentColor = isMaxPlayer ? aiColor : aiColor.getOpposite();
    List<Move> legalMoves = board.getLegalMoves(currentColor);

    if (legalMoves.isEmpty()) {
      if (board.isInCheck(currentColor))
    }
  }
}
