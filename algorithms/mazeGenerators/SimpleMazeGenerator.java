package algorithms.mazeGenerators;

import java.util.Random;


public class SimpleMazeGenerator extends AMazeGenerator {
    public Maze generate(int rows, int col) {
        Maze maze = new Maze(rows, col);
        setMazeOnesAndZeros(maze, '0');
        Random rand = new Random();
        Position prev = new Position("-2");
        for (int r = 0; r < rows; r++) {
            int c = rand.nextInt(col);
            Position random = new Position(r, c);
            while (random.isNeighbor(prev)) { // making sure that the maze have solution
                c = rand.nextInt(rows);
                random = new Position(r, c);
            }
            maze.setArrayCellValue(r, c, '1');
            prev = random;
        }
        setStartAndGoal(maze); // Amaze method
        return maze;
    }
}


