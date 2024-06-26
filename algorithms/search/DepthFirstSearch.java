package algorithms.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class DepthFirstSearch extends ASearchingAlgorithm {

    public String getName() {
        return "Depth First Search";
    }

    @Override
    /**
     * solve a problem using DFS Algorithm
     * @param searchable is the problem that the algorithm solves
     * @return solution for the problevm
     */
    public Solution solve(ISearchable searchable) {
        if (searchable == null){
            return null;
        }

        Stack<AState> stateStack = new Stack<>();
        HashMap<String, AState> visitedStates = new HashMap<>();

        //add the start state to open list
        AState goalState = searchable.getGoalState();
        AState startState = searchable.getStartState();

        /* Edge cases*/
        if(goalState == null || startState == null){
            return null;
        }
        if(startState.equals(goalState)){
            return new Solution(goalState);
        }
        stateStack.push(startState);
        AState temp;
        AState temp2;
        ArrayList<AState> possibleNeighbours;
        //while there is a cell in the open list
        while (!stateStack.empty()) {
            //remove the first cell in the list and add it to close list
            temp = stateStack.pop();
            visitedStates.put(temp.getState(),temp);
            visitNodes++;

            if (temp.getState().equals(goalState.getState())) {
                return new Solution(temp);
            }
            possibleNeighbours = searchable.getAllPossibleStates(temp);

            if(possibleNeighbours == null){
                return null;
            }

            for (int i = 0; i < possibleNeighbours.size(); i++) {
                temp2 = possibleNeighbours.get(i);
                if (!(visitedStates.containsKey(temp2.getState()))) {
                    stateStack.push(temp2);
                }
            }
        }
        return null;
    }


}
