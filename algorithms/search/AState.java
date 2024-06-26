package algorithms.search;


import java.io.Serializable;

public abstract class AState implements Comparable<AState>, Serializable {
    protected String state;
    protected double cost;
    protected AState parent;


    public AState() {
    this.state = null;
    this.cost = 0;
    this.parent = null;
    }

    public AState(String currentState, double cost, AState parentState)
    {
        this.state = currentState;
        this.cost = cost;
        this.parent = parentState;
    }

    public String getState() {
        return state;
    }

    public double getCost() {
        return cost;
    }

    public AState getParent() {
        return parent;
    }

    public int compareTo(AState aState){
        if (aState==null){
            return 0;
        }
        return Double.compare(cost, aState.getCost());
    }

    @Override
    public String toString() {
        return state;
    }

    public boolean equals(AState as) {
        return state.equals(as.getState());
    }
}

