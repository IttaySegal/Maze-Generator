package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Configurations;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;


/**
 *this class MyModel using Imodel interface and extends Observable
 * Contain- Maze, Solution, characterPositionRow, characterPositionColumn, chaserPositionRow, chaserPositionColumn, numofsteps, CanMove
 * function - generateMaze, getMaze, getSol, getCharacterPositionRow, getCharacterPositionColumn, getChaserPositionRow, getChaserPositionColumn,
 * moveCharacter, moveChaser, solveMaze, solveMazeSever, SaveMaze, OpenMaze, SameMaze, GetProp.
 */

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private Solution tempSolution;
    private Solution solution;
    private int characterPositionRow;
    private int characterPositionColumn;
//    private int chaserPositionRow=-2;
//    private int chaserPositionColumn=-2;
    private int numofsteps=0;
//    private int stepsofChase=0;
    private boolean CanMove=true;

    /**
     * this function generateMaze -using part 2 of the project
     * @param width -width of the new maze
     * @param height - height of the new maze
     * @param level - which generate algorithm to use - empty ,simple ,myMaze(default)
     */
    @Override
    public void generateMaze(int width, int height, String level) {
        if(level.equals("Empty Maze")) //change the config file according to the level
            Configurations.setProperty("MazeGenerator","Empty");
        else if(level.equals("Simple Maze"))
            Configurations.setProperty("MazeGenerator","Simple");
        else if(level.equals("My Maze"))
            Configurations.setProperty("MazeGenerator","MyMaze");
        Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        mazeGeneratingServer.start();
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{height, width};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[height*width+12]; //allocating byte[] for the decompressed maze
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze1 = new Maze(decompressedMaze);
                        maze = maze1; //set maze to be the new maze
                        tempSolution=null; //make sure the solution is null when new maze is generated
                        solution=null;
                        characterPositionRow=maze.getStartPosition().getRowIndex(); //the character start at the StartPosition
                        characterPositionColumn=maze.getStartPosition().getColumnIndex();
                        //chaserPositionRow=-2; //the chaser position
                        //chaserPositionColumn=-2;
                        numofsteps=0; //number of steps that the character did
//                        stepsofChase=0; //number of steps the chaser did
                    } catch (Exception e) {
                        System.out.println("error1");
                        System.out.println(e.getMessage());
                    }
                }
            });
            client.communicateWithServer();
            setChanged();
            notifyObservers();
        } catch (UnknownHostException e) {
            System.out.println("error2");
        }
        mazeGeneratingServer.stop();

    }
    /**
     * this function solveMaze - update the solution
     * @param maze -the maze we want to get his solution
     */
    @Override
    public void solveMaze(Maze maze) {
        Maze TempMaze = new Maze(maze.toByteArray()); //using tempMaze for changing the start position as the character move
        TempMaze.setStartPosition(characterPositionRow,characterPositionColumn);
        solveMazeSever(TempMaze);
        Collections.reverse(tempSolution.getSolutionPath());
        solution= new Solution(tempSolution.getSolutionPath()); //set the solution
        setChanged();
        notifyObservers();

    }
    /**
     * this function solveMazeSever -using part 2 of the project
     * @param maze -the maze we want to get his solution
     */
    public void solveMazeSever(Maze maze){
        Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject();
                        tempSolution=mazeSolution;
                    } catch (Exception e) {
                        System.out.println("error3");
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            System.out.println("error4");
        }
        solveSearchProblemServer.stop();
    }

    /**
     * this function moveCharacter - handling the move of the character based on numpad
     * @param movement - based on this we change the character position
     */
    @Override
    public void moveCharacter(KeyCode movement) {
        if(maze!=null) {
            switch (movement) {
                case NUMPAD8: //up
                    if (characterPositionRow > 0 && maze.getMaze()[characterPositionRow - 1][characterPositionColumn] == 0){
                        characterPositionRow--;
                        numofsteps++;
                    }
                    break;
                case NUMPAD2: //down
                    if (characterPositionRow < maze.getMaze().length - 1 && maze.getMaze()[characterPositionRow + 1][characterPositionColumn] == 0) {
                        characterPositionRow++;
                        numofsteps++;
                    }
                    break;
                case NUMPAD6: //right
                    if (characterPositionColumn < maze.getMaze()[0].length - 1 && maze.getMaze()[characterPositionRow][characterPositionColumn + 1] == 0) {
                        characterPositionColumn++;
                        numofsteps++;
                    }
                    break;
                case NUMPAD4: //left
                    if (characterPositionColumn > 0 && maze.getMaze()[characterPositionRow][characterPositionColumn - 1] == 0) {
                        characterPositionColumn--;
                        numofsteps++;
                    }
                    break;
                case NUMPAD1: //Diagonal Down-Left
                    if (characterPositionColumn > 0 && characterPositionRow < maze.getMaze().length - 1) {
                        if (maze.getMaze()[characterPositionRow + 1][characterPositionColumn - 1] == 0 && (maze.getMaze()[characterPositionRow][characterPositionColumn - 1] == 0 || maze.getMaze()[characterPositionRow + 1][characterPositionColumn] == 0)) {
                            characterPositionColumn--;
                            characterPositionRow++;
                            numofsteps++;
                        }
                    }
                    break;
                case NUMPAD7: //Diagonal Up-Left
                    if (characterPositionColumn > 0 && characterPositionRow > 0) {
                        if (maze.getMaze()[characterPositionRow - 1][characterPositionColumn - 1] == 0 && (maze.getMaze()[characterPositionRow][characterPositionColumn - 1] == 0 || maze.getMaze()[characterPositionRow - 1][characterPositionColumn] == 0)) {
                            characterPositionColumn--;
                            characterPositionRow--;
                            numofsteps++;
                        }
                    }
                    break;
                case NUMPAD9: //Diagonal Up-Left
                    if (characterPositionColumn < maze.getMaze()[0].length - 1 && characterPositionRow > 0) {
                        if (maze.getMaze()[characterPositionRow - 1][characterPositionColumn + 1] == 0 && (maze.getMaze()[characterPositionRow][characterPositionColumn + 1] == 0 || maze.getMaze()[characterPositionRow - 1][characterPositionColumn] == 0)) {
                            characterPositionColumn++;
                            characterPositionRow--;
                            numofsteps++;
                        }
                    }
                    break;
                case NUMPAD3: //Diagonal Down-Right
                    if (characterPositionColumn < maze.getMaze()[0].length - 1 && characterPositionRow < maze.getMaze().length - 1) {
                        if (maze.getMaze()[characterPositionRow + 1][characterPositionColumn + 1] == 0 && (maze.getMaze()[characterPositionRow][characterPositionColumn + 1] == 0 || maze.getMaze()[characterPositionRow + 1][characterPositionColumn] == 0)) {
                            characterPositionColumn++;
                            characterPositionRow++;
                            numofsteps++;
                        }
                    }
                    break;

            }
            //moveChaser(); //after the character move the chaser need to move too
            CanMove=true;
            setChanged();
            notifyObservers();
        }
    }

    /**
     * this function movechaser
     * move the chaser based on the character position - using solving algorithm when the character is the goal
     */
//    public void moveChaser(){
//        if(numofsteps==1 && (stepsofChase==0 || (CanMove==false && stepsofChase==1))){ //if the character did on step the chaser go to the start postion
//            chaserPositionRow=maze.getStartPosition().getRowIndex();
//            chaserPositionColumn=maze.getStartPosition().getColumnIndex();
//            stepsofChase++;
//        }
//        else if(numofsteps>1 && stepsofChase+1==numofsteps && CanMove==true){ //when the character did more than on step
//            stepsofChase++;
//            Position Start=new Position(chaserPositionRow,chaserPositionColumn); //change the start to be the chaser position
//            Position Goal=new Position(characterPositionRow,characterPositionColumn); //change the goal to be the character position
//            Maze tempMaze = new Maze(Start,Goal,maze.getMaze());
//            solveMazeSever(tempMaze);
//            Collections.reverse(tempSolution.getSolutionPath());
//            Solution mazeSolution= new Solution(tempSolution.getSolutionPath()); //update the solution
//            ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
//            if(mazeSolution.getSolutionPath().size()>1) { //update the chaser position
//                MazeState State = (MazeState) mazeSolutionSteps.get(1);
//                chaserPositionRow = State.getPosition().getRowIndex();
//                chaserPositionColumn = State.getPosition().getColumnIndex();
//            }
//        }
//        setChanged();
//        notifyObservers();
//    }

    /**
     * this function SaveMaze - save maze in a directory
     * @param file - the path for the file we want to save
     * @return status if true - success
     */
    @Override
    public boolean SaveMaze(File file) {
        if (file != null) {
            try {
                ObjectOutputStream toDirectory = new ObjectOutputStream(new FileOutputStream(file));
                toDirectory.writeObject(getMaze());
                toDirectory.flush();
                return true;
            }
            catch (Exception e){
                return false;
            }
        }
        return true;
    }

    /**
     * this function OpenMaze - open maze from directory
     * @param file - the path of the file we want to open
     * @return status if true - success
     */
    @Override
    public boolean OpenMaze(File file){
        if (file != null) {
            try {
                ObjectInputStream fromDirectory = new ObjectInputStream(new FileInputStream(file));
                Maze tempMaze = (Maze) fromDirectory.readObject();
                characterPositionRow=tempMaze.getStartPosition().getRowIndex(); //update the character position based on the new maze
                characterPositionColumn=tempMaze.getStartPosition().getColumnIndex();
                //chaserPositionRow=-2; //restart the chaser position
                //chaserPositionColumn=-2;
                numofsteps=0;
//                stepsofChase=0;
                maze=tempMaze;
                solution=null;
                tempSolution=null;
                setChanged();
                notifyObservers();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * this function SameMaze - open restart the maze that is already saved
     */
    public void SameMaze(){
        characterPositionRow=maze.getStartPosition().getRowIndex();
        characterPositionColumn=maze.getStartPosition().getColumnIndex();
        //chaserPositionRow=-2;
        //chaserPositionColumn=-2;
        numofsteps=0;
//        stepsofChase=0;
        solution=null;
        tempSolution=null;
        setChanged();
        notifyObservers();
    }

    /**
     * this function GetProp
     * @param s - string that is the name of the property
     * @return String - the value of the property
     */
    public String GetProp(String s){
        String res = Configurations.getProperty(s); //get from the config file
        switch (s) {
            case "MazeGenerator":
                if (res == null) //default
                    return "MyMaze";
                break;
            case "SearchingAlgorithm":
                if(res == null ) //default
                    return "Best First Search Algorithm";
                break;
            case "Player":
                if(res==null)//default
                    return "Ash";
        }
        return res;
    }

    /**
     * this function getMaze
     * @return the Maze
     */
    @Override
    public Maze getMaze() {
        return maze;
    }

    /**
     * this function getSol
     * @return the Solution of the maze
     */
    public Solution getSol(){
        return solution;
    }

    /**
     * this function getCharacterPositionRow
     * @return the character position row
     */
    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow+1;
    }

    /**
     * this function getCharacterPositionColumn
     * @return the character poistion column
     */
    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn+1;
    }
    /**
     * this function getChaserPositionRow
     * @return the chaser poistion row
     */
//    public int getChaserPositionRow() {
//        return chaserPositionRow+1;
//    }

    /**
     * this function getChaserPositionColumn
     * @return the character poistion column
     */
//    public int getChaserPositionColumn() {
//        return chaserPositionColumn+1;
//    }

    /**
     * this function setCanMove
     * @param bool - boolean that declare if the chaser can move
     */
//    public void setCanMove(boolean bool) {
//        CanMove=bool;
//        stepsofChase++;
//    }

}
