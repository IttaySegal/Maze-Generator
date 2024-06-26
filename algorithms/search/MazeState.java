package algorithms.search;
import java.io.Serializable;


/**
 * constructor for maze state
 */
public class MazeState extends AState implements Serializable {
    public MazeState (MazeState ms, int row, int col, boolean diagonal){
        if(row >= 0 && col >= 0){
            state = "(" +row + "," + col + ")";
        }
        parent = ms;
        if (ms != null) {
            cost = ms.cost + 1;
            if(diagonal){
                cost += 0.5;
            }
        }
        else{
            cost = 0;
        }
    }

    public MazeState(String currentState, double cost, AState parentState) {
        super(currentState, cost, parentState);
    }
}
