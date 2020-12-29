package sudoku.persistence;

import sudoku.buildlogic.SudokuBuildLogic;
import sudoku.problemdomain.IStorage;
import sudoku.problemdomain.SudokuGame;

import java.io.*;

//Deals with storage of data (persistence). It does so by writing to a file
//in the local system.
public class LocalStorageImpl implements IStorage {

    //File that holds the game data
    private static File GAME_DATA = new File(
            System.getProperty("user.home"),
            "sudokuGameData.txt"
    );

    //Writes the SudokuGame object to a file
    @Override
    public void updateGameData(SudokuGame game) throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(GAME_DATA);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new IOException("Unable to access Game Data");
        }
    }

    //Retrieves the SudokuGame object from a file
    @Override
    public SudokuGame getGameData() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(GAME_DATA);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        try {
            SudokuGame gameState = (SudokuGame) objectInputStream.readObject();
            objectInputStream.close();
            return gameState;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException("File not found");
        }
    }
}
