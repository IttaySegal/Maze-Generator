package algorithms.search;

import java.util.ArrayList;

public interface ISearchable {

    /**
     * @return the start state
     */
    AState getStartState();

    /**
     * @return the goal state
     */
    AState getGoalState();

    /**
     * @param state
     * @return arrayList of the possible paths to this state
     */
    ArrayList<AState> getAllPossibleStates(AState state);
}
