package sudoku.computationlogic;

import sudoku.problemdomain.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static sudoku.problemdomain.SudokuGame.GRID_BOUNDARY;

public class GameGenerator {
    public static int[][] getNewGameGrid() {
        return unsolveGame(getSolvedGame());
    }

    //Will create a game that can be solved from a solved game by basically
    //removing numbers from the already solved game and checking if it is
    //possible to solve the game, until 40 numbers were removed without
    //making an unsolvable game.
    private static int[][] unsolveGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());

        boolean solvable = false;
        int[][] solvableArray = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        while (solvable == false) {
            SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);

            int index = 0;

            while (index < 40) {
                int xCoordinate = random.nextInt(GRID_BOUNDARY);
                int yCoordinate = random.nextInt(GRID_BOUNDARY);

                if (solvableArray[xCoordinate][yCoordinate] != 0) {
                    solvableArray[xCoordinate][yCoordinate] = 0;
                    index++;
                }
            }

            //After removing a number, the puzzle needs to be solved to check
            //if it is solvable or impossible to solve.
            int[][] toBeSolved = new int[GRID_BOUNDARY][GRID_BOUNDARY];
            SudokuUtilities.copySudokuArrayValues(solvableArray, toBeSolved);

            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);
        }

        return solvableArray;
    }

    //This method will generate a solved sudoku game.
    //The algorithm implemented here is not optimal, a better way
    //would be to use graph coloring.
    private static int[][] getSolvedGame() {
        Random random = new Random(System.currentTimeMillis());
        int[][] newGrid = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        for (int value = 1; value <= GRID_BOUNDARY; value++) {
            int allocations = 0;
            int interrupt = 0;

            List<Coordinates> allocTracker = new ArrayList<>();

            int attempts = 0;

            while (allocations < GRID_BOUNDARY) {
                //If more than 200 attempts to allocate a number were
                //made, it backtracks, trying again.
                if (interrupt > 200) {
                    allocTracker.forEach(coord -> {
                        newGrid[coord.getX()][coord.getY()] = 0;
                    });

                    interrupt = 0;
                    allocations = 0;
                    allocTracker.clear();
                    attempts++;
                }

                //Only the interrupts aren't enough, because this algorithm
                //could stuck on an infinite loop, so if more than 500
                //attempts were made, the whole array is cleared. This makes
                //this algorithm a two stage backtracking algorithm.
                if (attempts > 500) {
                    clearArray(newGrid);
                    attempts = 0;
                    value = 1;
                }


                //Each time it is about to allocate a number, a random square is
                //selected.
                int xCoordinate = random.nextInt(GRID_BOUNDARY);
                int yCoordinate = random.nextInt(GRID_BOUNDARY);

                //If that particular grid location is equal to zero, then it will
                //be allocated with a value.
                if (newGrid[xCoordinate][yCoordinate] == 0) {
                    newGrid[xCoordinate][yCoordinate] = value;

                    //As soon as the value is allocated, it is tested to see if
                    //it is valid by the sudoku rules.
                    if (GameLogic.sudokuIsInvalid(newGrid)) {
                        //If it is invalid, the value is erased.
                        newGrid[xCoordinate][yCoordinate] = 0;
                        interrupt++;
                    } else {
                        allocTracker.add(new Coordinates(xCoordinate, yCoordinate));
                        allocations++;
                    }
                }
            }
        }
        return newGrid;
    }

    //Helper function to clear the array
    private static void clearArray(int[][] newGrid) {
        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                newGrid[xIndex][yIndex] = 0;
            }
        }
    }
}
