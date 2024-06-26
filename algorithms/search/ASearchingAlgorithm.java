package algorithms.search;


import java.util.ArrayList;
import java.util.Collections;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {

    protected int visitNodes;

    public ASearchingAlgorithm(){
        visitNodes=0;
    }

    public int getNumberOfNodesEvaluated() {
        return visitNodes;
    }
}

