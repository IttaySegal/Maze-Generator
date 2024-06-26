package View;

import Server.Configurations;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *this class MazeDisplayer extends canvas
 * Contain- maze(int[][]), mazeRreal, characterPositionRow, characterPositionColumn, sol, ShowSOL, Showhint
 */

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private Maze mazeReal;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private Solution sol;
    private boolean ShowSOl=false;
    private boolean Showhint=false;

    /**
     * these functions are setters
     */
    public void setMaze(Maze maze) {
        this.mazeReal=maze;
        this.maze = maze.getMaze();
        redraw();
    }
    public void setSol(Solution sol){
        this.sol=sol;
        redraw();
    }
    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public void setShowSOl(Boolean show){
        ShowSOl=show;
    }
    public void setShowhint(Boolean show){
        Showhint=show;
    }

    /**
     * these functions getters
     */
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    /**
     * this function redraw - draw the app
     */
    public void redraw() {
        if (maze != null) { //check if maze is null
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();

            //calc the size of the cells
            double cellHeight = canvasHeight/ (maze.length+2);
            double cellWidth = canvasWidth/ (maze[0].length+2);

            try {
                //updated the images
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNamePlayer1.get()));
                Image goalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));
                Image solImage = new Image(new FileInputStream(ImageFileNameSol.get()));

                String temp = Configurations.getProperty("Player");
                if (temp != null) {
                    if (temp.equals("Ash")) {
                        //update the images
                        goalImage = new Image(new FileInputStream(ImageFileNameGoal1.get()));
                        characterImage = new Image(new FileInputStream(ImageFileNamePlayer.get()));
                    }
                }

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight()); //empty the canvas

                //Draw Maze
                for (int i = 0; i < maze.length+2; i++) {
                    for (int j = 0; j < maze[0].length+2; j++) {
                        if(i==0 || i==maze.length+1 || j==0 || j==maze[0].length+1){ //draw the frame
                            gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                        else {
                            if (maze[i-1][j-1] == 1) { //draw thw walls
                                gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                            } else if (i == mazeReal.getGoalPosition().getRowIndex()+1 && j == mazeReal.getGoalPosition().getColumnIndex()+1) { //draw the goal
                                gc.drawImage(goalImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                            }
                        }
                    }
                }

                //Draw Sol & hint
                if(sol!=null && Showhint==true){ //draw hint
                    ArrayList<AState> mazeSolutionSteps = sol.getSolutionPath();
                    if(mazeSolutionSteps.size()>2) {
                        MazeState State = (MazeState) mazeSolutionSteps.get(1);
                        gc.drawImage(solImage, (State.getPosition().getColumnIndex() + 1) * cellWidth, (State.getPosition().getRowIndex() + 1) * cellHeight, cellWidth, cellHeight);
                    }
                }
                if(sol!=null && ShowSOl==true) { //draw sol
                    ArrayList<AState> mazeSolutionSteps = sol.getSolutionPath();
                    for (int i = 1; i < mazeSolutionSteps.size()-1; i++) {
                        MazeState State = (MazeState) mazeSolutionSteps.get(i);
                        gc.drawImage(solImage, (State.getPosition().getColumnIndex()+1) * cellWidth, (State.getPosition().getRowIndex()+1) * cellHeight, cellWidth, cellHeight);
                    }
                }

                //Draw Character
                gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);
}
            catch (FileNotFoundException e) {
                System.out.println("error 8");
            }
        }
    }

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNamePlayer = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();
    private StringProperty ImageFileNameSol = new SimpleStringProperty();
    private StringProperty ImageFileNamePlayer1 = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal1 = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }
    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }
    public String getImageFileNameSol() {
        return ImageFileNameSol.get();
    }
    public String getImageFileNameCharacter() {
        return ImageFileNamePlayer.get();
    }
    public String getImageFileNameCharacter1() {
        return ImageFileNamePlayer1.get();
    }
    public String getImageFileNameGoal1() {
        return ImageFileNameGoal1.get();
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.ImageFileNameGoal.set(imageFileNameGoal);
    }
    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }
    public void setImageFileNameSol(String imageFileNameSol) {
        this.ImageFileNameSol.set(imageFileNameSol);
    }
    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNamePlayer.set(imageFileNameCharacter);
    }
    public void setImageFileNameCharacter1(String imageFileNameCharacter1) {
        this.ImageFileNamePlayer1.set(imageFileNameCharacter1);
    }
    public void setImageFileNameGoal1(String imageFileNameGoal1) {
        this.ImageFileNameGoal1.set(imageFileNameGoal1);
    }
}
