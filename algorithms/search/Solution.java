package algorithms.search;

import java.util.ArrayList;
import java.util.Stack;
import java.io.Serializable;


public class Solution implements Serializable{

    private final AState goalState;

    public Solution(AState aState) {
        if (aState == null){
            System.out.println("no solution!");
            goalState = null;
        }
        else
            goalState = aState;
    }

    /**
     * Retrieves the solution path as a list of states from the start state to the goal state.
     * @return The solution path as a list of states.
     */
    public ArrayList<AState> getSolutionPath(){
        ArrayList<AState> result = new ArrayList<>();
        Stack<AState> tempResult = new Stack<>();
        //add goal state to stack
        tempResult.push(goalState);
        AState tempState = null;
        //add father of goal state to stack
        if (goalState.getParent()!=null){
            tempState = goalState.getParent();
        }
        //add all state of the solution to stack
        while(tempState.getParent()!=null){
            tempResult.push(tempState);
            tempState=tempState.getParent();
        }
        tempResult.push(tempState);
        //reverse the path by pop from stack and add to list
        while(!tempResult.empty()){
            result.add(tempResult.pop());
        }
        return result;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "goalState=" + goalState +
                '}';
    }
}
