package com.chungmcl;

public class Main {

    // The size of the chess board. (Default chess boards are 8 x 8)
    static final int boardSize = 8;

    // Possible "moves" a knight can make represented by deltaX/deltaY
    static final int[][] possibleMoves = new int[][] {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1 }, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };


    public static void main(String[] args)
    {
        // A 2D array of numbers to represent a chessboard
        int[][] board = new int[boardSize][boardSize];

        // Set all places on a board
        InitializeBoard(board);

        // Start at the top left corner of board (count as already taking a move - Move 1)
        board[0][0] = 1;

        // Returns false if a knight's tour could not be generated
        if (!KnightsTour(board, 1, 0, 0))
        {
            System.out.println("A knights tour could not be generated.");
        }
    }

    // Initialize all board squares to -1 (-1 representing not having touched the square)
    public static void InitializeBoard(int[][] board)
    {
        for (int a = 0; a < boardSize; a++)
        {
            for (int b = 0; b < boardSize; b++)
            {
                board[a][b] = -1;
            }
        }
    }

    // Recursive function to generate a knights tour
    public static boolean KnightsTour(int[][] board, int moveCount, int currentX, int currentY)
    {
        System.out.println(moveCount);
        if (moveCount == Math.pow(boardSize, 2)) // If Knight touches all squares on chessboard
        {
            // Print out the chess board with the move number on each square
            for (int x = 0; x < boardSize; x++)
            {
                for (int y = 0; y < boardSize; y++)
                {
                    System.out.print(board[x][y] + " ");
                }
                System.out.println();
            }
            return true;
        }
        else // If Knight hasn't touched all squares yet
        {
            for (int i = 0; i < boardSize; i++)
            {
                if (ValidateMove(currentX, currentY, possibleMoves[i][0], possibleMoves[i][1], board))
                {
                    int[][] boardCopy = CopyBoard(board);
                    boardCopy[currentX + possibleMoves[i][0]][currentY + possibleMoves[i][1]] = moveCount;

                    // Recursively call KnightsTour
                    if (KnightsTour(boardCopy,
                            moveCount + 1,
                            currentX + possibleMoves[i][0],
                            currentY + possibleMoves[i][1]))
                        return true;
                }
            }
            return false;
        }
    }

    // Check if the move is valid
    // (Doesn't leave the board, doesn't go to a square that the Knight has already been to)
    public static boolean ValidateMove(int x, int y, int deltaX, int deltaY, int[][] board)
    {
        return (
                (x + deltaX >= 0 && x + deltaX < boardSize)
                && (y + deltaY >= 0 && y + deltaY < boardSize)
                && (board[x + deltaX][y + deltaY] == -1)
                );
    }

    // Return a reference to a new copy of a board
    public static int[][] CopyBoard(int[][] board)
    {
        int[][] newBoard = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++)
        {
            newBoard[i] = board[i].clone();
        }
        return newBoard;
    }
}
