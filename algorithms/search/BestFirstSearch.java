package algorithms.search;

import java.util.PriorityQueue;

/**
 * this class is more or less like BreadthFirstSearch
 * the only differences are its name, and that it uses a Priority queue instead of a list
 */
public class BestFirstSearch extends BreadthFirstSearch {

    public BestFirstSearch(){
        stateQueue = new PriorityQueue<>();
    }

    public String getName(){
        return "Best First Search";
    }


}
