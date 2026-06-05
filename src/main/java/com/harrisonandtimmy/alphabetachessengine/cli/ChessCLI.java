package com.harrisonandtimmy.alphabetachessengine.cli;

public class ChessCLI {
}

/*

The terminal UI. Ties everything together for two players on the same keyboard.
This is temporary — it gets replaced by React in Phase 3. Has no chess logic of its own,
just reads input and delegates to GameState.

main(String[] args) — creates a GameState, loops until game over: prints board, prints turn/check status, reads input, validates format, calls applyMove, announces result when game ends
parseSquare(String s) — converts chess notation like "e2" into a Square object, returns null if input is invalid
 */