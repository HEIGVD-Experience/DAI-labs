package ch.heigvd.Game;

public class Position {
    private final char column; // A B C D E
    private final int row;     // 1 2 3 4 5

    public Position(char column, int row){
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }
}
