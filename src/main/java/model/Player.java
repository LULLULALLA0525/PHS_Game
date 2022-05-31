package model;

import javafx.scene.paint.Paint;

public class Player {
    public final int playerIndex;
    private final Paint playerColor;
    private boolean playable;
    private int playerScore;
    private int bridgeCards;
    private int x;
    private int y;

    private boolean chanceToRoll = false;
    private boolean chanceToGo = false;

    public Player(int playerIndex, Paint playerColor, boolean playable, int playerScore, int bridgeCards, int x, int y) {
        this.playerIndex = playerIndex;
        this.playerColor = playerColor;
        this.playable = playable;
        this.playerScore = playerScore;
        this.bridgeCards = bridgeCards;
        this.x = x;
        this.y = y;
    }

    public boolean isPlayable() { return playable; }
    public boolean isChanceToRoll() { return chanceToRoll; }
    public boolean isChanceToGo() { return chanceToGo; }
    public boolean isTurnOver() { return chanceToRoll || chanceToGo; }

    public Paint getPlayerColor() { return playerColor; }
    public int getPlayerScore() { return playerScore; }
    public int getBridgeCards() { return bridgeCards; }
    public int getX() { return x; }
    public int getY() { return y; }
    public String getPlayerStatus() {
        if (playable)
            return String.format("%d-true-%d-%d-%d-%d\n", playerIndex, playerScore, bridgeCards, x, y);
        else
            return String.format("%d-false-%d-%d-%d-%d\n", playerIndex, playerScore, bridgeCards, x, y);
    }

    public void giveTurn() { chanceToRoll = true; }
    public void finishRoll() {
        chanceToRoll = false;
        chanceToGo = true;
    }
    public void finishGo() { chanceToGo = false; }
    public void giveScore(int score) { playerScore += score; }
    public void giveBridgeCard(int amount) { bridgeCards += amount; }
    public void takeBridgeCard() { bridgeCards--; }
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void quitPlay() { playable = false; }
}
