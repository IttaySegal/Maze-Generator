package ViewModel;

import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class MyViewModel extends Observable implements Observer {
    private MyModel model;
    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    //private static final Logger logger = LogManager.getLogger(Model);

    public MyViewModel(MyModel model) {
        this.model = model;
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            characterPositionRowIndex = model.getCharacterPositionRow();
            //characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            //characterPositionColumn.set(characterPositionColumnIndex + "");
//            chaserPositionRowIndex = model.getChaserPositionRow();
//            chaserPositionColumnIndex = model.getChaserPositionColumn();
            setChanged();
            notifyObservers();
        }
    }
    //functions that connect between the model and the view-
    public Maze getMaze(){
        return model.getMaze();
    }
    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }
//    public int getChaserPositionRow() {
//        return chaserPositionRowIndex;
//    }
//    public int getChaserPositionColumn() {
//        return chaserPositionColumnIndex;
//    }
    public void generateMaze(int width, int height,String level){ model.generateMaze(width, height,level); }
    public void solveMaze(Maze maze){model.solveMaze(maze);}
    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }
    public void SameMaze(){model.SameMaze();}
    public Solution getSol(){return model.getSol();}
    public boolean SaveMaze(File file){return model.SaveMaze(file);}
    public boolean OpenMaze(File file){return model.OpenMaze(file);}
    public String GetProp(String s){return model.GetProp(s);}
//    public void setCanMove(boolean bool){model.setCanMove(bool);}

}
