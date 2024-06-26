package algorithms.search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Queue;

public class BreadthFirstSearch extends ASearchingAlgorithm {
    protected Queue<AState> stateQueue;
    protected HashMap<String, AState> openStateMap;
    protected HashMap<String, AState> closedStateMap;

    /**
     * Empty Constructor
     */
    public BreadthFirstSearch(){
        stateQueue = new LinkedList<>();
        openStateMap = new HashMap<>();
        closedStateMap = new HashMap<>();
    }

    /**
     *
     * @return String name of the search algorithm
     */
    public String getName(){
        return "Breadth First Search";
    }

    /**
     * This method gets a search problem and solve it
     * @param searchable the search problem
     * @return Solution
     */
    @Override
    public Solution solve(ISearchable searchable) {
        if (searchable == null){
            return null;
        }
        AState current;
        AState aState;
        ArrayList<AState> neighbours;
        stateQueue.clear();
        openStateMap.clear();
        closedStateMap.clear();
        visitNodes = 0;

        AState startState = searchable.getStartState();
        AState goalState = searchable.getGoalState();

        if(startState == null || goalState == null){
            return null;
        }
        if(startState.equals(goalState)){
            Solution solution = new Solution(goalState);
            return solution;
        }
        stateQueue.add(startState);
        openStateMap.put(startState.getState(), startState);

        while(!stateQueue.isEmpty()){
            current = stateQueue.poll();
            closedStateMap.put(current.getState(), current);

            if(current.equals(goalState) ){ /*means: we finished*/
                visitNodes = closedStateMap.size();
                Solution S = new Solution(current);
                return S;
            }

            neighbours = searchable.getAllPossibleStates(current);
            if(neighbours == null){
                return null;
            }
            while(!neighbours.isEmpty()){
                aState = neighbours.remove(0);
                if (!closedStateMap.containsKey(aState.getState()) && !openStateMap.containsKey(aState.getState())){
                    stateQueue.add(aState);
                    openStateMap.put(aState.getState(), aState);
                }
            }
        }
        return null;
    }
}
