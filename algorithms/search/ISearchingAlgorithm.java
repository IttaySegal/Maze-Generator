package algorithms.search;

public interface ISearchingAlgorithm {
    /**
     *recives a maze and solves it
     * @param searchable something (maze in this casse) that you can search through
     * @return Solution
     */
    Solution solve (ISearchable searchable);

    int getNumberOfNodesEvaluated();

    String getName();
}
