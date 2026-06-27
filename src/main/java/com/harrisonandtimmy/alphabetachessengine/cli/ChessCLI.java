package com.harrisonandtimmy.alphabetachessengine.cli;

import com.harrisonandtimmy.alphabetachessengine.ai.AIStrategy;
import com.harrisonandtimmy.alphabetachessengine.ai.AlphaBetaStrategy;
import com.harrisonandtimmy.alphabetachessengine.model.Color;
import com.harrisonandtimmy.alphabetachessengine.model.GameState;
import com.harrisonandtimmy.alphabetachessengine.model.Square;
import com.harrisonandtimmy.alphabetachessengine.model.Move;

import java.util.Scanner;

public class ChessCLI {

    public static void main(String[] args) {
        GameState gameState = new GameState();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Alpha-Beta Chess Engine!\n");

        // Ask for game mode
        System.out.println("Select game mode:");
        System.out.println("1. Two players");
        System.out.println("2. Play against AI");
        System.out.println("Enter 1 or 2: ");
        String modelInput = scanner.nextLine().trim();

        boolean vsAI = modelInput.equals("2");
        Color aiColor = null;
        AIStrategy ai = null;

        if (vsAI) {
            System.out.println("What do you want to play as? (white/black):");

            while (aiColor == null) {
                String colorInput = scanner.nextLine().trim().toLowerCase();
                if (colorInput.equals("white") || colorInput.equals("w")) {
                    aiColor = Color.BLACK;
                } else if (colorInput.equals("black") || colorInput.equals("b")) {
                    aiColor = Color.WHITE;
                } else {
                    System.out.println("Invalid input. Enter 'white' or 'black':");
                }
            }

            ai = new AlphaBetaStrategy();
            System.out.println("AI will play as " + aiColor + "\n");
        }

        System.out.println("Enter moves as: e2 e4");
        System.out.println("Type 'quit' to exit\n");

        while (!gameState.isGameOver()) {

            // Print the board
            System.out.println(gameState.getBoard());

            // Print turn and check status
            Color turn = gameState.getCurrentTurn();
            System.out.print(turn + "'s turn");
            if (gameState.isCheck()) {
                System.out.print(" — CHECK!");
            }
            System.out.println();

            if (vsAI && gameState.getCurrentTurn() == aiColor) {
                System.out.println("AI is thinking...");
                Move move = ai.getNextMove(gameState);
                gameState.applyMove(move.getFrom(), move.getTo());
                continue;
            }

            // Read input
            System.out.print("Enter move: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            }

            // Validate format
            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid format. Use: e2 e4\n");
                continue;
            }

            // Parse squares
            Square from = parseSquare(parts[0]);
            Square to   = parseSquare(parts[1]);

            if (from == null || to == null) {
                System.out.println("Invalid square. Columns a-h, rows 1-8.\n");
                continue;
            }

            // Apply the move
            boolean success = gameState.applyMove(from, to);
            if (!success) {
                System.out.println("Illegal move. Try again.\n");
                continue;
            }
        }

        // Game over
        if (gameState.isCheckmate()) {
            Color winner = gameState.getCurrentTurn().getOpposite();
            System.out.println(gameState.getBoard());
            System.out.println("Checkmate! " + winner + " wins!");
        } else if (gameState.isStalemate()) {
            System.out.println(gameState.getBoard());
            System.out.println("Stalemate! It's a draw.");
        }

        scanner.close();
    }

    public static Square parseSquare(String s) {
        if (s == null || s.length() != 2) {
            return null;
        }

        char colChar = s.charAt(0);
        char rowChar = s.charAt(1);

        if (colChar < 'a' || colChar > 'h') return null;
        if (rowChar < '1' || rowChar > '8') return null;

        int col = colChar - 'a';   // 'a'→0, 'b'→1 ... 'h'→7
        int row = '8' - rowChar;   // '8'→0, '7'→1 ... '1'→7

        return new Square(row, col);
    }
}