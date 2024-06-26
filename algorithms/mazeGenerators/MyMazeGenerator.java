package algorithms.mazeGenerators;
import java.util.LinkedList;
import java.util.Random;
public class MyMazeGenerator extends AMazeGenerator
{
    @Override
    public Maze generate(int rows, int cols)
    {
        if (rows<2)
            rows=2;
        if (cols<2)
            cols=2;
        Maze myMaze = new Maze(rows, cols);
        if (rows <= 3 || cols <= 3)
        {
            for (int i=0; i<rows; i++)
            {
                for (int j=0; j<cols; j++)
                    myMaze.getMaze()[i][j] = '0';
            }
        }
        else
        {
            for (int i=0; i<rows; i++)
            {
                for (int j=0; j<cols; j++)
                    myMaze.getMaze()[i][j] = '1';
            }

            LinkedList<Position[]> neighbours = new LinkedList<>();
            Random random = new Random();
            int x;
            int y;
            int startX = 0;
            int startY = 0;
            int endX = rows-1;
            int endY = cols-1;
            Position start = new Position(startX,startY);
            Position end = new Position(endX,endY);
            neighbours.add(new Position[]{start,end});
            //myMaze.getMaze()[1][0] = 0;
            while (!neighbours.isEmpty())
            {
                Position[] curr = neighbours.remove(random.nextInt(neighbours.size()));
                x = curr[1].getRowIndex();
                y = curr[1].getColumnIndex();

                if(myMaze.getMaze()[x][y] == '1')
                {
                    myMaze.getMaze()[curr[0].getRowIndex()][curr[0].getColumnIndex()] = '0';
                    myMaze.getMaze()[x][y] = '0';

                    if (myMaze.getMaze()[0][1] == '0' || myMaze.getMaze()[1][0] != '0')
                    {
                        neighbours.add(new Position[]{new Position(1,0),new Position(0,1)}); //otherwise the maze will always stop
                    }
                    if (x>=2 && myMaze.getMaze()[x-2][y] == '1')
                        neighbours.add(new Position[]{new Position(x-1,y),new Position(x-2,y)});

                    if (y>=2 && myMaze.getMaze()[x][y-2] == '1')
                        neighbours.add(new Position[]{new Position(x,y-1),new Position(x,y-2)});

                    if (x < rows-2 && myMaze.getMaze()[x+2][y] == '1')
                        neighbours.add(new Position[]{new Position(x+1,y),new Position(x+2,y)});

                    if (y < cols-2 && myMaze.getMaze()[x][y+2] == '1')
                        neighbours.add(new Position[]{new Position(x,y+1),new Position(x,y+2)});
                }

            }
        }
        setStartAndGoal(myMaze);
        return myMaze;
    }
}
