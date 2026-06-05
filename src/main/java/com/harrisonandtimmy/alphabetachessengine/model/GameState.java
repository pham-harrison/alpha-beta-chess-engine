package com.harrisonandtimmy.alphabetachessengine.model;

public class GameState {
}

/*
 The referee of the game. Wraps the board and enforces all the rules:
 whose turn it is, whether a move is legal in the context of the game,
 and whether the game is over.

GameState() — constructor, creates a new Board, sets currentTurn to WHITE, initializes empty move history, sets check/checkmate/stalemate to false
applyMove(Square from, Square to) — validates the piece belongs to the current player, checks the move is legal, applies it to the board, records it in history, switches turns, calls updateStatus, returns true if successful false if not
updateStatus() — called after every move, checks if the new current player is in check, checks if they have any legal moves, sets isCheck/isCheckmate/isStalemate accordingly
isGameOver() — returns true if isCheckmate or isStalemate
getBoard() — returns the board
getCurrentTurn() — returns whose turn it is
getMoveHistory() — returns the list of moves made
isCheck() — returns whether the current player is in check
isCheckmate() — returns whether the game is over by checkmate
isStalemate() — returns whether the game is over by stalemate

 */