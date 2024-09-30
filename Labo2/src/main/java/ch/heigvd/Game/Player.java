package ch.heigvd.Game;


public class Player {
    private final String username;
    private final PlayBoard terrainBoats = new PlayBoard(true);
    private final PlayBoard terrainShoots = new PlayBoard(false);

    public Player(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    public PlayBoard getPlayBoardBoats() {
        return terrainBoats;
    }
    public PlayBoard getPlayBoardShoots() {
        return terrainShoots;
    }
    public boolean isLosing(){
        boolean win = false;

        for(Boat boat : getPlayBoardBoats().getBoats()){
            if(boat.isDrowned()){
                win = true;
            } else {
                win = false;
                break;
            }
        }

        return win;
    }
}
