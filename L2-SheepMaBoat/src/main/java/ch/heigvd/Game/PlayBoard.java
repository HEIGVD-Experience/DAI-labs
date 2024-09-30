package ch.heigvd.Game;

import java.util.HashMap;
import java.util.Map;

public class PlayBoard {
    public static char minColumn = 'A', maxColumn = 'E', minRow = '1', maxRow = '5';
    private static final Map<Character, Integer> mapPositions = new HashMap<>();
    private Boat[] boats = new Boat[3];
    private final String[] playBoard = {
            "     A   B   C   D   E",
            "   ╔═══╦═══╦═══╦═══╦═══╗ ",
            " 1 ║   ║   ║   ║   ║   ║ ",
            "   ╠═══╬═══╬═══╬═══╬═══╣ ",
            " 2 ║   ║   ║   ║   ║   ║ ",
            "   ╠═══╬═══╬═══╬═══╬═══╣ ",
            " 3 ║   ║   ║   ║   ║   ║ ",
            "   ╠═══╬═══╬═══╬═══╬═══╣ ",
            " 4 ║   ║   ║   ║   ║   ║ ",
            "   ╠═══╬═══╬═══╬═══╬═══╣ ",
            " 5 ║   ║   ║   ║   ║   ║ ",
            "   ╚═══╩═══╩═══╩═══╩═══╝ ",
    };

    public PlayBoard(boolean bat){
        if(bat){
            boats = Boat.creatBoats();
            assert boats != null;
            for(Boat boat : boats){
                for(Position position : boat.getPositions()){
                    insert(position, boat.getSymbol());
                }
            }
        }
    }

    public String[] getPlayBoard() {
        return playBoard;
    }
    public Boat[] getBoats() {
        return boats;
    }
    public void insert(Position position, char obj){
        mapPositions.put('A', 1);
        mapPositions.put('B', 2);
        mapPositions.put('C', 3);
        mapPositions.put('D', 4);
        mapPositions.put('E', 5);

        int x = mapPositions.get(position.getColumn());
        x = x * 4 + 1;

        int y = position.getRow();
        y += y;

        if (y >= 0 && y < getPlayBoard().length && x >= 0 && x < getPlayBoard()[0].length()) {
            char[] ligneArray = getPlayBoard()[y].toCharArray();
            ligneArray[x] = obj;
            getPlayBoard()[y] = new String(ligneArray);

        } else {
            System.out.println("Error");
        }
    }
    public String insert(Position position, PlayBoard otherPlayBoard){
        mapPositions.put('A', 1);
        mapPositions.put('B', 2);
        mapPositions.put('C', 3);
        mapPositions.put('D', 4);
        mapPositions.put('E', 5);

        String res = "";
        boolean touch = false;

        int x = mapPositions.get(position.getColumn());
        x = x * 4 + 1;

        int y = position.getRow();
        y += y;

        if (y >= 0 && y < getPlayBoard().length && x >= 0 && x < getPlayBoard()[0].length()) {
            char[] ligneArray = getPlayBoard()[y].toCharArray();
            ligneArray[x] = 'X';
            getPlayBoard()[y] = new String(ligneArray);

        } else {
            System.out.println("Error");
        }

        for(Boat boat : boats){
            for(Position pos : boat.getPositions()) {
                if (pos.getColumn() == position.getColumn() && pos.getRow() == position.getRow()) {
                    touch = true;
                    res = "touched(0)";
                    insert(pos, '0');
                    otherPlayBoard.insert(pos, '0');
                    boat.deletePos(new Position('A', 0), boat.getPositions().indexOf(pos));
                    int count = 0;
                    for (Position pos2 : boat.getPositions()) {
                        if (pos2.getColumn() == 'A' && pos2.getRow() == 0) {
                            ++count;
                        }
                    }
                    if (count == boat.getSize()) {
                        res = "sinked(0)";
                        boat.setDrowned(true);
                    }
                }
            }
        }
        if(!touch){
            res = "missed(X)";
            otherPlayBoard.insert(position,'X');
        }
        return res;
    }
    public String display(){
        String result = "";
        for (String ligne : this.getPlayBoard()) {
            result += ligne + "_";
        }
        return result;
    }
}
