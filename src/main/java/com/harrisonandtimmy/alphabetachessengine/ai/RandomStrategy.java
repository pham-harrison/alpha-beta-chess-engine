package com.harrisonandtimmy.alphabetachessengine.ai;

import com.harrisonandtimmy.alphabetachessengine.model.GameState;
import com.harrisonandtimmy.alphabetachessengine.model.Move;

import java.util.List;
import java.util.Random;


public class RandomStrategy implements AIStrategy {

  private final Random random = new Random();

  @Override
  public Move getNextMove(GameState gameState) {
    List<Move> legalMoves = gameState.getBoard().getLegalMoves(gameState.getCurrentTurn());

    if (legalMoves.isEmpty()) return null;

    return legalMoves.get(random.nextInt(legalMoves.size()));
  }
}
