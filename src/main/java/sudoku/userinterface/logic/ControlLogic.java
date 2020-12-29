package sudoku.userinterface.logic;

//Represents a controller. It manages the interactions between the user and
//user interface and the backend of the application.

import sudoku.computationlogic.GameLogic;
import sudoku.constants.GameState;
import sudoku.constants.Messages;
import sudoku.problemdomain.IStorage;
import sudoku.problemdomain.SudokuGame;
import sudoku.userinterface.IUserInterfaceContract;

import java.io.IOException;

public class ControlLogic implements IUserInterfaceContract.EventListener {

    /*In this project, the communication with the backend and frontend is
    made through interfaces. This is done to help to design the application
    upfront without worrying about the specific implementation. For example,
    if it is necessary to change the storage implementation, it is possible
    to do so quickly and easily without causing issues in this ControlLogic
    class.*/

    //Back end
    private IStorage storage;

    //Front end
    private IUserInterfaceContract.View view;

    public ControlLogic(IStorage storage, IUserInterfaceContract.View view) {
        this.storage = storage;
        this.view = view;
    }

    /*
    - When the user inputs a number or deletes a number, that is going
      to be written to the game storage.

    - The game storage is the source of truth of the game data.

    - The GameState is designed to be immutable (it can't be changed), so
      for the control logic to change the state of the game, it has to create
      a new state from the old state.
    */
    @Override
    public void onSudokuInput(int x, int y, int input) {
        try {
            SudokuGame gameData = storage.getGameData();
            int[][] newGridState = gameData.getCopyOfGridState();
            newGridState[x][y] = input;

            gameData = new SudokuGame(
                    GameLogic.checkForCompletion(newGridState),
                    newGridState
            );

            //Updates the data in the backend
            storage.updateGameData(gameData);

            //Updates the view in the frontend
            view.updateSquare(x, y, input);

            //Shows a dialog when the user wins the game
            if (gameData.getGameState() == GameState.COMPLETE) {
                view.showDialog(Messages.GAME_COMPLETE);
            }
        } catch (IOException e){
            e.printStackTrace();
            //If for some reason something went wrong, shows that to the user
            view.showError(Messages.ERROR);
        }
    }

    //When the user clicks "OK" on the dialog to start a new game.
    @Override
    public void onDialogClick() {
        try {
            storage.updateGameData(
                    GameLogic.getNewGame()
            );

            view.updateBoard(storage.getGameData());
        } catch (IOException e){
            view.showError(Messages.ERROR);
        }
    }
}
