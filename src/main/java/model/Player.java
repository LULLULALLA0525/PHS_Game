package model;

public class Player {
    public final int playerIndex;
    private boolean playable;

    private int playerScore;
    private int bridgeCards;

    private boolean chanceToRoll = false;
    private boolean chanceToGo = false;

    private int x;
    private int y;

    public Player(int playerIndex, int x, int y) {
        this.playerIndex = playerIndex;
        this.playable = true;

        this.playerScore = 0;
        this.bridgeCards = 0;

        this.x = x;
        this.y = y;
    }

    public boolean isPlayable() { return this.playable; }
    public boolean isChanceToRoll() { return this.chanceToRoll; }
    public boolean isChanceToGo() { return this.chanceToGo; }
    public boolean isTurnOver() { return this.chanceToRoll || this.chanceToGo; }

    public int getPlayerScore() { return this.playerScore; }
    public int getBridgeCards() { return this.bridgeCards; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public void giveTurn() { this.chanceToRoll = true; }
    public void finishRoll() {
        this.chanceToRoll = false;
        this.chanceToGo = true;
    }
    public void finishGo() { this.chanceToGo = false; }
    public void giveScore(int score) { this.playerScore += score; }
    public void giveBridgeCard(int amount) { this.bridgeCards += amount; }
    public void takeBridgeCard() { this.bridgeCards--; }
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void quitPlay() { this.playable = false; }
}
