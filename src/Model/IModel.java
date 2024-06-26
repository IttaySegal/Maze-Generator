package Model;

import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {
    void generateMaze(int width, int height, String level);
    void solveMaze(Maze maze);
    void moveCharacter(KeyCode movement);

    Maze getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    boolean SaveMaze(File file);
    boolean OpenMaze(File file);
}
