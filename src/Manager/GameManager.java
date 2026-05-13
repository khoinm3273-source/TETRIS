package src.Manager;

import src.Board;
import src.Shape;

public class GameManager {
    private Board board;
    private ScoreManager scoreManager;
    private Shape currentShape;
    private Shape nextShape;

    private int x, y;

    private int dropCounter = 0;

    private boolean isClearing = false;
    private int clearAnimTimer = 0;
    private int clearAnimDuration = 30;

    private int[] clearingRows = new int[4];
    private int clearingCount = 0;

    private boolean gameOver = false;

    // hiệu ứng level up
    private int levelUpTimer = 0;
    private final int levelUpDuration = 60;

    public GameManager(Board board) {
        this.board = board;
        this.scoreManager = new ScoreManager();

        nextShape = new Shape();
        nextShape.setRandomShape();

        spawnShape();
    }

    public void update() {
        if (gameOver) return;

        // level up
        if (scoreManager.isLevelUp()) {
            levelUpTimer = levelUpDuration;
        }

        if (levelUpTimer > 0) {
            levelUpTimer--;
            scoreManager.resetLevelUp();
        }

        // hiệu ứng clear khi full lines
        if (isClearing) {
            clearAnimTimer++;

            if (clearAnimTimer >= clearAnimDuration) {
                board.removeLines(clearingRows, clearingCount);
                scoreManager.addLines(clearingCount);

                isClearing = false;
                spawnShape();
            }
            return;
        }

        // gravity
        dropCounter++;
        if (dropCounter >= getDropInterval()) {
            if (board.isValidPosition(currentShape.getCoords(), x, y + 1)) {
                y++;
            } else {
                lockShape();
            }
            dropCounter = 0;
        }
    }

    // kiểm soát tốc độ rơi
    private int getDropInterval() {
        int level = scoreManager.getLevel();
        return Math.max(15, 60 - (level - 1) * 3);
    }

    private void lockShape() {
        board.placeShape(currentShape.getCoords(), x, y, currentShape.getPieceShape());

        int cleared = board.checkFullLines(clearingRows);

        if (cleared > 0) {
            isClearing = true;
            clearingCount = cleared;
            clearAnimTimer = 0;
        } else {
            spawnShape();
        }
    }

    public void spawnShape() {
        currentShape = nextShape;

        nextShape = new Shape();
        nextShape.setRandomShape();

        x = 4;
        y = 0;

        dropCounter = 0; 

        if (!board.isValidPosition(currentShape.getCoords(), x, y)) {
            gameOver = true;
        }
    }

    public void moveLeft() {
        if (board.isValidPosition(currentShape.getCoords(), x - 1, y)) x--;
    }

    public void moveRight() {
        if (board.isValidPosition(currentShape.getCoords(), x + 1, y)) x++;
    }

    public void rotate() {
        currentShape.rotate();
        if (!board.isValidPosition(currentShape.getCoords(), x, y)) {
            for (int i = 0; i < 3; i++) currentShape.rotate();
        }
    }

    public void softDrop() {
        if (board.isValidPosition(currentShape.getCoords(), x, y + 1)) y++;
    }

    public void hardDrop() {
        while (board.isValidPosition(currentShape.getCoords(), x, y + 1)) {
            y++;
        }
        lockShape();
    }

    private boolean justSpawned = false;
    public boolean justSpawnedPiece() {
        boolean temp = justSpawned;
        justSpawned = false;
        return temp;
    }

    // các getter
    public Shape getCurrentShape() { return currentShape; }
    public Shape getNextShape() { return nextShape; }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isClearing() { return isClearing; }
    public int getClearAnimTimer() { return clearAnimTimer; }
    public int getClearAnimDuration() { return clearAnimDuration; }

    public int[] getClearingRows() { return clearingRows; }
    public int getClearingCount() { return clearingCount; }

    public ScoreManager getScoreManager() { return scoreManager; }

    public boolean isGameOver() { return gameOver; }

    public int getLevelUpTimer() { return levelUpTimer; }
}