package ch.heigvd.Game;

import ch.heigvd.GameClient.GameClient;

import java.util.Scanner;

public class Game {
    private final Player[] players = new Player[2];
    private boolean hasWinner = false;

    public String printGame(Player player){
        return  "Player : " + player.getUsername() + "_" +
                "Boats playboard _" +
                player.getPlayBoardBoats().display()+ "_" +
                "Shoots playboard _" +
                player.getPlayBoardShoots().display();
    }
    public void setPlayer(Player j) {
        if(players[0] == null)
            players[0] = j;
        else
            players[1] = j;
    }
    public boolean isHasWinner() {
        return hasWinner;
    }
    public String playRound(Player play, Player wait, char column, int row){
        Position shoot = new Position(column, row);
        String result = "";

        result = wait.getPlayBoardBoats().insert(shoot, play.getPlayBoardShoots());

        if(wait.isLosing()){
            result = play.getUsername() + " won !";
            hasWinner = true;
        }

        return result;
    }
}
