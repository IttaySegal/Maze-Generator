package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

public class SearchableMaze implements ISearchable {

    private Maze m_maze;

    public SearchableMaze(Maze maze) {
        if (maze!=null){
            m_maze=maze;
        }else {
            m_maze = new Maze(0,0);
        }
    }

    /**
     * get Start state base on the start position of the given maze
     * @return start state of the problem
     */
    @Override
    public AState getStartState() {

        int col = m_maze.getStartPosition().getColumnIndex();
        int row = m_maze.getStartPosition().getRowIndex();
        AState StartState = new MazeState(null,row,col,false);
        return StartState;
    }

    /**
     * @return Goal state of the problem
     */
    @Override
    public AState getGoalState() {
        int col = m_maze.getGoalPosition().getColumnIndex();
        int row = m_maze.getGoalPosition().getRowIndex();
        AState goalState = new MazeState(null,row,col, false);
        return goalState;
    }

    /**
     * @param s - a state
     * @return - all possible pathes from the given state
     */
    @Override
    public ArrayList<AState> getAllPossibleStates(AState s) {

        ArrayList<AState> allPossibleState = new ArrayList<>();
        if(s==null){
            return allPossibleState;
        }
        //get state position
        int [] statePosition = getCoordinates(s);
        int x = statePosition[0];
        int y = statePosition[1];

        //set bool vars for cells check
        boolean upIsPossible = false;
        boolean downIsPossible=false;
        boolean rightIsPossible=false;
        boolean leftIsPossible = false;

        //check up
        if(isPossibleState(x-1,y)){
            allPossibleState.add(new MazeState((MazeState) s,x-1,y, false));
            upIsPossible = true;
        }
        //check right
        if (isPossibleState(x,y+1)){
            allPossibleState.add(new MazeState((MazeState) s,x,y+1, false));
            rightIsPossible =true;
        }
        //check down
        if(isPossibleState(x+1,y)){
            allPossibleState.add(new MazeState((MazeState) s,x+1,y, false));
            downIsPossible=true;
        }
        //check left
        if(isPossibleState(x,y-1)){
            allPossibleState.add(new MazeState((MazeState) s,x,y-1, false));
            leftIsPossible=true;
        }
        //check up-right
        if(upIsPossible||rightIsPossible){
            if(isPossibleState(x-1,y+1)) {
                allPossibleState.add(new MazeState((MazeState) s, x -1, y+1, true));
            }
        }
        //check down - right
        if (rightIsPossible||downIsPossible){
            if(isPossibleState(x+1,y+1)){
                allPossibleState.add(new MazeState((MazeState) s, x +1, y+1, true));
            }
        }
        //check down - left
        if (downIsPossible||leftIsPossible){
            if(isPossibleState(x+1,y-1)){
                allPossibleState.add(new MazeState((MazeState) s, x +1, y-1, true));
            }
        }
        //check up -left
        if (leftIsPossible||upIsPossible){
            if(isPossibleState(x-1,y-1)){
                allPossibleState.add(new MazeState((MazeState) s, x-1, y-1, true));
            }
        }
        return allPossibleState;
    }

    /**
     * @param s - AState to get coordinate from it.
     * @return String Array contains: [0] - x coordinate [1] - y coordinate
     */
    public int[] getCoordinates(AState s){
        String state = s.getState();
        state = state.substring(1,state.length()-1);
        String [] r = state.split(",");
        int [] res = new int[2];
        int x = Integer.parseInt(r[0]);
        int y = Integer.parseInt(r[1]);
        res[0]=x;
        res[1]=y;
        return res;
    }

    /**
     * A method the checks if a gives cell is valid
     * @param row - rom position in the maze
     * @param col - col postion in the maze
     * @return boolean - if its a valid cell - '0' and it the range of the maze
     */
    private boolean isPossibleState(int row,int col){
        if (row>=0 && row<m_maze.getRows()){
            if (col>=0 && col<m_maze.getCols()){
                if(m_maze.isPass(row,col)){
                    return true;
                }
            }
        }
        return false;
    }
}
