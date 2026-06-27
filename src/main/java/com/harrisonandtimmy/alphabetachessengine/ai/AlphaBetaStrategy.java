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
    Board board = gameState.getBoard();
    Color color = gameState.getCurrentTurn();
    List<Move> legalMoves = board.getLegalMoves(color);

    Move bestMove = null;
    int bestScore = Integer.MIN_VALUE;
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;

    for (Move move : legalMoves) {
      board.applyMove(move);
      int score = alphaBetaMinimax(board, DEPTH - 1, alpha, beta, false, color);
      board.undoMove(move);

      if (score > bestScore) {
        bestScore = score;
        bestMove = move;
      }
      alpha = Math.max(alpha, bestScore);
    }

    return bestMove;
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
      if (board.isInCheck(currentColor)) {
        // Checkmate
        return isMaxPlayer ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE - 1;
      }
      // Stalemate
      return 0;
    }

    if (isMaxPlayer) {
      int maxScore = Integer.MIN_VALUE;
      for (Move move : legalMoves) {
        board.applyMove(move);
        int score = alphaBetaMinimax(board, depth - 1, alpha, beta, false, aiColor);
        board.undoMove(move);
        maxScore = Math.max(maxScore, score);
        alpha = Math.max(alpha, score);
        if (beta <= alpha) {
          break;
        }
      }
      return maxScore;
    } else {
      int minScore = Integer.MAX_VALUE;
      for (Move move : legalMoves) {
        board.applyMove(move);
        int score = alphaBetaMinimax(board, depth - 1, alpha, beta, true, aiColor);
        board.undoMove(move);
        minScore = Math.min(minScore, score);
        beta = Math.min(beta, score);
        if (beta <= alpha) {
          break;
        }
      }
      return minScore;
    }
  }
}
