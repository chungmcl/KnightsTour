package com.chungmcl;

import java.lang.Math;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;


public class Main {
    // The size of the chess board. (Default chess boards are 8 x 8)
    private static final int boardSize = 5;

    // Possible "moves" a knight can make represented by deltaX/deltaY
    private static final int[][] possibleMoves = new int[][] {
        {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -1}, {2, -1}
    };

    static ExecutorService executor = Executors.newCachedThreadPool();
    
    public static void main(String[] args)
    {
        // A 2D array of numbers to represent a chessboard
        int[][] board = new int[boardSize][boardSize];

        // Set all places on a board
        InitializeBoard(board);

        // Start at the top left corner of board (count as already taking a move - Move 1)
        board[0][0] = 0;

        // Returns false if a knight's tour could not be generated
        if (!RunKnightsTour(board, 1, 0, 0))
        {
            System.out.println("A knights tour could not be generated.");
        }
    }

    // Initialize all board squares to -1 (-1 representing not having touched the square)
    private static void InitializeBoard(int[][] board)
    {
        for (int x = 0; x < boardSize; x++)
        {
            for (int y = 0; y < boardSize; y++)
            {
                board[x][y] = -1;
            }
        }
    }

    // Recursive function to generate a knights tour
    private static boolean RunKnightsTour(int[][] board, int moveCount, int currentX, int currentY)
    {
        // System.out.println(moveCount);
        if (moveCount == Math.pow(boardSize, 2)) // If Knight touches all squares on chessboard
        {
            // Print out the chess board with the move number on each square
            for (int x = 0; x < boardSize; x++)
            {
                for (int y = 0; y < boardSize; y++)
                {
                    System.out.print(board[x][y] + "\t");
                }
                System.out.println();
            }
            return true;
        }
        else // If Knight hasn't touched all squares yet
        {
            List<Callable<Boolean>> callables = new ArrayList<Callable<Boolean>>();
            for (int i = 0; i < possibleMoves.length; i++)
            {
                int newX = currentX + possibleMoves[i][0];
                int newY = currentY + possibleMoves[i][1];
                if (ValidateMove(newX, newY, board))
                {
                    final int[][] boardCopy = CopyBoard(board);
                    boardCopy[newX][newY] = moveCount;
                    
                    callables.add(new Callable<Boolean>() {
                        public Boolean call() {
                            // get parent 
                            return RunKnightsTour(board, moveCount, currentX, currentY);
                        }
                    });
                }
            }

            List<Future<Boolean>> results;
            try
            {
                results = executor.invokeAll(callables);
                System.out.println();
            }
            catch (Exception e)
            {
                System.out.println("EXCEPTION THROWN: " + e);
                return false;
            }

            // Join the boi
            for (Future<Boolean> x : results)
            {
                try 
                {
                    if (x.get())
                        return true;
                } catch (Exception e) {
                    System.out.println("EXCEPTION THROWN: " + e);
                    return false;
                }
            }
            return false;
        }
    }

    // Check if the move is valid
    // (Doesn't leave the board, doesn't go to a square that the Knight has already been to)
    private static boolean ValidateMove(int newX, int newY, int[][] board)
    {
        return (
                (newX >= 0 && newX < boardSize)
                && (newY >= 0 && newY < boardSize)
                && (board[newX][newY] == -1)
                );
    }

    // Return a reference to a new copy of a board
    private static int[][] CopyBoard(int[][] board)
    {
        int[][] newBoard = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++)
        {
            newBoard[i] = board[i].clone();
        }
        return newBoard;
    }
}
