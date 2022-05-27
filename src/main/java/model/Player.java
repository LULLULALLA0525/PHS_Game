package model;

public class Player {
    public final int playerIndex;
    private int playerScore;
    private boolean playable;

    private boolean chanceToRoll = false;
    private boolean chanceToGo = false;

    public Player(int playerIndex) {
        this.playerIndex = playerIndex;
        this.playerScore = 0;
        this.playable = true;
    }

    public boolean isPlayable() { return this.playable; }
    public boolean isChanceToRoll() { return this.chanceToRoll; }
    public boolean isChanceToGo() { return this.chanceToGo; }
    public boolean isTurnOver() { return this.chanceToRoll || this.chanceToGo; }

    public int getPlayerScore() { return this.playerScore; }

    public void giveTurn() { this.chanceToRoll = true; }
    public void finishRoll() {
        this.chanceToRoll = false;
        this.chanceToGo = true;
    }
    public void finishGo() { this.chanceToGo = false; }
    public void giveScore(int score) { this.playerScore += score; }

    public void quitPlay() { this.playable = false; }
}
