package ch.heigvd.Game;

import java.util.LinkedList;
import java.util.Random;

public class Boat {
    private final LinkedList<Position> positions = new LinkedList<>();
    private final int size;
    private final char symbol;
    private boolean drowned = false;

    public Boat(int size, char symbol){
        this.size = size;
        this.symbol = symbol;
        this.creatBoat(size);
    }

    private void creatBoat(int size) {
        Random random = new Random();
        boolean horizontal = random.nextBoolean();

        int x = random.nextInt(5) + 1;
        int y = random.nextInt(5) + 1;

        if (horizontal && x + size - 1 > 5) {
            x = 5 - size + 1;
        } else if (!horizontal && y + size - 1 > 5) {
            y = 5 - size + 1;
        }

        for (int i = 0; i < size; i++) {
            if (horizontal) {
                positions.add(new Position((char)((x + i) + 64), y));
            } else {
                positions.add(new Position((char)(x + 64), (y + i)));
            }
        }
    }
    public static Boat[] creatBoats() {
        boolean check = false;

        while(!check){
            Boat[] boats = new Boat[3];
            boats[0] = new Boat(2,'A');
            boats[1] = new Boat(3,'B');
            boats[2] = new Boat(2,'C');

            check = true;

            for(int i = 0; i < boats.length; i++){
                for(int j = 0; j < boats.length; j++){
                    if(i != j){
                        for(Position pos : boats[i].getPositions()){
                            for(Position pos2 : boats[j].getPositions()){
                                if(pos.getColumn() == pos2.getColumn() && pos.getRow() == pos2.getRow()){
                                    check = false;
                                }
                            }
                        }
                    }
                }
            }
            if(check){
                return boats;
            }
        }

        return null;
    }
    public LinkedList<Position> getPositions() {
        return positions;
    }
    public void deletePos(Position position, int index){
        positions.set(index, position);
    }
    public char getSymbol() {
        return symbol;
    }
    public int getSize() {
        return size;
    }
    public boolean isDrowned() {
        return drowned;
    }
    public void setDrowned(boolean coule) {
        this.drowned = coule;
    }
}
