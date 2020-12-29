package sudoku.problemdomain;

import sudoku.computationlogic.SudokuUtilities;
import sudoku.constants.GameState;

import java.io.Serializable;

//The GameState is designed to be immutable (it can't be changed).

//Implements Serializable (which turns the object in a format that is
//easy to store) so that it can be stored in a file in the class
//LocalStorageImpl.
public class SudokuGame implements Serializable {
    private final GameState gameState;
    private final int[][] gridState;

    public static final int GRID_BOUNDARY = 9;

    public SudokuGame(GameState gameState, int[][] gridState) {
        this.gameState = gameState;
        this.gridState = gridState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int[][] getCopyOfGridState() {
        //Returns a copy of the object, to prevent the SudokuGame
        //from being changed.
        return SudokuUtilities.copyToNewArray(gridState);
    }
}
