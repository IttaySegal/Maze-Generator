package algorithms.mazeGenerators;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Maze implements Serializable {
    private final int rows;
    private final int columns;
    private final char [][] cMaze;
    private Position startPosition;
    private Position goalPosition;


    public Maze(int rows, int columns) {
        if (rows < 4 || columns < 4) { // too small, thus we change its size to 20 X 20
            rows = 5;
            columns = 5;
        }
        this.rows = rows;
        this.columns = columns;
        cMaze = new char[rows][columns];
    }

    /**
     * Constructs a maze object from a one-dimensional byte array representation.
     * @param bMaze1D The one-dimensional byte array representing the maze.
     */
    public Maze(byte[] bMaze1D){
        rows = decompress(bMaze1D,0);
        columns = decompress(bMaze1D,3);
        int startRow = decompress(bMaze1D,6);
        int startCol = decompress(bMaze1D,9);
        Position start = new Position(startRow,startCol);
        startPosition = start;
        int goalRow = decompress(bMaze1D,12);
        int goalCol = decompress(bMaze1D,15);
        Position goal = new Position(goalRow, goalCol);
        goalPosition = goal;
        int position = 18;
        cMaze = new char[rows][columns];
        for(int i=0;i<rows;i++){
            for (int j=0;j<columns;j++,position++){
                if (bMaze1D[position]==0){
                    cMaze[i][j]='0';
                }else {
                    cMaze[i][j] = '1';
                }
            }
        }
    }

    /**
     * Compares the current maze object with the specified object for equality.
     * @param o The object to compare with the current maze.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maze maze = (Maze) o;
        boolean ans =true;
        if (rows != maze.getRows()){return false;}
        if(columns != maze.getCols()){return false;}
        for(int i = 0; i < cMaze.length; i++){
            for (int j = 0; j < cMaze[0].length; j++){
                if(cMaze[i][j] != maze.getMaze()[i][j]){
                    return false;
                }
            }
        }
        if (!startPosition.equals(maze.getStartPosition())) {
            return false;
        }
        if(!goalPosition.equals(maze.getGoalPosition())){
            return false;
        }
        return ans;
    }

    /**
     * Computes the hash code for the maze object.
     * @return The hash code value for the maze.
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(rows, columns, startPosition, goalPosition);
        result = 31 * result + Arrays.hashCode(cMaze);
        return result;
    }
    public void setArrayCellValue(int row,int col ,char ch) {
        this.cMaze[row][col]=ch;
    }
    public byte[] toByteArray(){
        byte[] unCompressMaze = new byte[columns*rows+18];
        addMazeSettingsToByteArray(unCompressMaze);

        int position=18;
        for (int i=0;i<rows;i++){
            for (int j=0;j<columns;j++,position++){
                unCompressMaze[position]= (byte)Character.getNumericValue(cMaze[i][j]);
            }
        }
        return unCompressMaze;
    }

    /**
     * Adds maze settings to the byte array representation of the maze.
     * @param unCompressMaze The byte array representing the maze.
     */
    private void addMazeSettingsToByteArray(byte[] unCompressMaze){
        byte[] temp = compress(rows);

        int position=0;
        //add rows to 0-2 cells
        position= pointerSwitch(unCompressMaze, temp, position);
        //add columns to 3-5 cells
        temp = compress(columns);
        position= pointerSwitch(unCompressMaze, temp, position);
        //add startPosition to 6-11 cells
        temp = compress(startPosition.getRowIndex());
        position= pointerSwitch(unCompressMaze, temp, position);
        temp=compress(startPosition.getColumnIndex());
        position= pointerSwitch(unCompressMaze, temp, position);
        //add GoalPosition to 12-17 cells
        temp = compress(goalPosition.getRowIndex());
        position= pointerSwitch(unCompressMaze, temp, position);
        temp = compress(goalPosition.getColumnIndex());
        pointerSwitch(unCompressMaze, temp, position);
    }

    /**
     * Copies the values from the 'other' byte array to the 'unCompressMaze' byte array starting from the specified position.
     * @param unCompressMaze The byte array to copy values into.
     * @param other The byte array containing values to copy.
     * @param position The starting position in the 'unCompressMaze' byte array.
     * @return The updated position after copying the values.
     */
    private int pointerSwitch(byte[]unCompressMaze,byte[] other,int position ) {
        int max = position+3;
        for (int j = 0; position < max; position++, j++) {
            unCompressMaze[position] = other[j];
        }
        return position;
    }

    /**
     * Prints the maze to the console.
     */
//    public void print() {
//        System.out.println("Maze Runner");
//        setArrayCellValue(startPosition.getRowIndex(), startPosition.getColumnIndex(), 'S');
//        setArrayCellValue(goalPosition.getRowIndex(), goalPosition.getColumnIndex(), 'E');
//
//
//        for (int i=0;i<columns+2;i++){
//            System.out.print('\u2588');
//        }
//        System.out.print('\n');
//
//        for (int i = 0; i < rows; i++) {
//            System.out.print('\u2588');
//            for (int j = 0; j < columns; j++) {
//                if (cMaze[i][j] == '0') {
//                    System.out.print(' ');
//                } else if(cMaze[i][j] == '1') {
//                    System.out.print('\u2588') ;
//                } else {
//                    System.out.print(cMaze[i][j]);
//                }
//            }
//            System.out.print('\u2588');
//            System.out.print('\n');
//        }
//        for (int i=0;i<columns+2;i++){
//            System.out.print('\u2588');
//        }
//        System.out.print('\n');
//        setArrayCellValue(startPosition.getRowIndex(), startPosition.getColumnIndex(), '0');
//        setArrayCellValue(goalPosition.getRowIndex(), goalPosition.getColumnIndex(), '0');
//    }

    public void print() {
        System.out.println("Maze Runner");
        setArrayCellValue(startPosition.getRowIndex(), startPosition.getColumnIndex(), 'S');
        setArrayCellValue(goalPosition.getRowIndex(), goalPosition.getColumnIndex(), 'E');


//        for (int i=0;i<columns+2;i++){
//            System.out.print('1');
//        }
//        System.out.print('\n');

        for (int i = 0; i < rows; i++) {
//            System.out.print('1');
            for (int j = 0; j < columns; j++) {
                if (cMaze[i][j] == '0') {
                    System.out.print('0');
                } else if(cMaze[i][j] == '1') {
                    System.out.print('1') ;
                } else {
                    System.out.print(cMaze[i][j]);
                }
            }
//            System.out.print('1');
            System.out.print('\n');
        }
//        for (int i=0;i<columns+2;i++){
//            System.out.print('1');
//        }
        System.out.print('\n');
        setArrayCellValue(startPosition.getRowIndex(), startPosition.getColumnIndex(), '0');
        setArrayCellValue(goalPosition.getRowIndex(), goalPosition.getColumnIndex(), '0');
    }

    /**
     * Compresses the given number into a byte array representation.
     * @param numToCompress The number to compress.
     * @return The byte array representing the compressed number.
     */
    private byte[] compress(int numToCompress){
        byte[]  res = new byte[3];
        int first =  (numToCompress%255);
        res[0] = (byte)first;
        numToCompress-=first;
        if (numToCompress==0){
            res[1]=0;
            res[2]=0;
        }else {
            int third = (numToCompress/255);
            res[1]= (byte) 255;
            res[2] = (byte)third;
        }

        return res;
    }

    /**
     * Decompresses the number from the specified position in the byte array.
     * @param compress The byte array containing the compressed number.
     * @param position The position in the byte array to start decompressing from.
     * @return The decompressed number.
     */
    private int decompress(byte[] compress,int position){
        return Byte.toUnsignedInt(compress[position+1])*Byte.toUnsignedInt(compress[position+2])+Byte.toUnsignedInt(compress[position]);
    }
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return columns;
    }
    public Position getStartPosition() {
        return startPosition;
    }
    public char[][] getMaze(){return cMaze;}
    public Position getGoalPosition() {
        return goalPosition;
    }
    public void setStartPosition(Position startPosition) {
        if (startPosition == null){
            return;
        }
        this.startPosition = startPosition;
    }

    public void setGoalPosition(Position goalPosition) {
        if (goalPosition == null){
            return;
        }
        this.goalPosition = goalPosition;
    }
    public boolean isPass(int row, int col) { return cMaze[row][col] == '0'; }
}