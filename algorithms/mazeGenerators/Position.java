package algorithms.mazeGenerators;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {

   private final int row;
   private final int column;

    public int getRowIndex() {
        return row;
    }
    public int getColumnIndex() {return column;}
    public Position(int row, int column) {
        boolean legalRow=true;
        boolean legalCol = true;
        if (row<0){
            legalRow=false;
            System.out.println("invalid input, row position can't be equale or less than 0");
        }
        if (column<0){
            legalCol =false;
            System.out.println("invalid input, column position can't be equale or less than 0");
        }

        if (legalCol && legalRow){
            this.row = row;
            this.column = column;
        }
        else if (!legalCol && legalRow ){
            this.row = row;
            this.column = 1;
        }else if (legalCol && !legalRow){
            this.row = 1;
            this.column = column;
        }else {
            this.row = 1;
            this.column = 1;
        }
    }
    public Position(String s) {
        if(s.equals("-2")){
            this.row = -2;
            this.column = -2;
        }
        else{
            this.row = 0;
            this.column = 0;
        }
    }
    public boolean isNeighbor(Position other){
        if (other==null){
            return false;
        }
        if(Math.abs( this.row - other.row)<=1 && Math.abs( this.column - other.column)<=1){
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        return "{"+row+","+ column+"}";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                column == position.column;
    }
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}

