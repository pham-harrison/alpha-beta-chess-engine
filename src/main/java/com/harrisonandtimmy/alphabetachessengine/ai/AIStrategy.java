package com.harrisonandtimmy.alphabetachessengine.ai;

import com.harrisonandtimmy.alphabetachessengine.model.GameState;
import com.harrisonandtimmy.alphabetachessengine.model.Move;

public interface AIStrategy {
  Move getNextMove(GameState gameState);
}
