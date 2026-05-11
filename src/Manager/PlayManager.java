package src.Manager;
import java.awt.*;

import src.Board;
import src.GameState;
import src.KeyHandler;
import src.DrawUI.GamePanel;
import src.DrawUI.GameRenderer;
import src.DrawUI.MenuUI;
import src.DrawUI.OverlayUI;

public class PlayManager {
    public final int CELL_SIZE = 30;
    public final int COLS = 10;
    public final int ROWS = 20;
    public final int WIDTH = COLS * CELL_SIZE;
    public final int HEIGHT = ROWS * CELL_SIZE;
    public int left_x, top_y;

    // giảm tốc độ soft drop
    private int softDropCounter = 0;
    private final int SOFT_DROP_INTERVAL = 3; // càng lớn càng chậm

    private GameState currentState;
    private GameManager game;
    private Board board;
    private KeyHandler keyH;

    private GameRenderer renderer;
    private MenuUI menu;
    private OverlayUI overlayUI;

    private int leftHold = 0;
    private int rightHold = 0;

    //kiểm soát tốc độ rơi
    private int spawnDelay = 0;
    private final int SPAWN_DELAY_MAX = 10; 

    // DAS: delayed auto shift
    // ARR: auto repeat rate
    private final int DAS = 6; // delay trước khi auto move
    private final int ARR = 2;  // tốc độ lặp

    public PlayManager(KeyHandler keyH, Board board, OverlayUI overlayUI) {
        this.keyH = keyH;
        this.board = board;

        this.left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2) - 100;
        this.top_y = 50;

        this.game = new GameManager(board);
        this.renderer = new GameRenderer(game, board, left_x, top_y, CELL_SIZE, COLS);
        this.menu = new MenuUI();
        this.overlayUI = overlayUI;

        this.currentState = GameState.START_MENU;
    }

    public void resetGame() {
        board.clearBoard();
        game = new GameManager(board);
        renderer = new GameRenderer(game, board, left_x, top_y, CELL_SIZE, COLS);
        currentState = GameState.PLAYING;

        leftHold = 0;
        rightHold = 0;
    }

    public void update() {
        overlayUI.update(currentState);
        if (spawnDelay > 0) spawnDelay--;   //giảm delay
        // start menu
        if (currentState == GameState.START_MENU) {
            if (keyH.enterPressed) {
                resetGame();
                keyH.enterPressed = false;
            }
            return;
        }

        // game over
        if (currentState == GameState.GAME_OVER) {

            if (keyH.rPressed) {
                startGame();
                keyH.rPressed = false;
                return;
            }

            if (keyH.escPressed) {
                System.exit(0);
            }

            if (keyH.enterPressed) {
                currentState = GameState.START_MENU;
                keyH.enterPressed = false;
            }
            return;
        }

        // playing
        if (currentState == GameState.PLAYING) {
            handleInput();
            game.update();

            if (game.justSpawnedPiece()) {
                spawnDelay = SPAWN_DELAY_MAX;
            }

            if (game.isGameOver()) {
                currentState = GameState.GAME_OVER;
                overlayUI.resetGameOver();
            }
        }
    }

    private void handleInput() {
        // qua trái
        if ((keyH.leftPressed || keyH.aPressed) && !(keyH.rightPressed || keyH.dPressed)) {
            leftHold++;
            if (leftHold == 1) {
                game.moveLeft();
            } else if (leftHold > DAS) {
                if (ARR == 0 || (leftHold - DAS) % ARR == 0) {
                    game.moveLeft();
                }
            }
        } else {
            leftHold = 0;
        }

        // qua phải
        if ((keyH.rightPressed || keyH.dPressed) && !(keyH.leftPressed || keyH.aPressed)) {
            rightHold++;
            if (rightHold == 1) {
                game.moveRight();
            } else if (rightHold > DAS) {
                if (ARR == 0 || (rightHold - DAS) % ARR == 0) {
                    game.moveRight();
                }
            }
        } else {
            rightHold = 0;
        }

        // xoay khối
        if (keyH.upPressed || keyH.wPressed) {
            game.rotate();
            keyH.upPressed = false;
            keyH.wPressed = false;
        }

        // soft drop
        if ((keyH.downPressed || keyH.sPressed) && spawnDelay == 0) {
            softDropCounter++;
            if (softDropCounter >= SOFT_DROP_INTERVAL) {
                game.softDrop();
                softDropCounter = 0;
            }
        } else {
            softDropCounter = 0;
        }

        // hard drop
        if (keyH.spacePressed) {
            game.hardDrop();
            keyH.spacePressed = false;
        }
    }

    public void draw(Graphics2D g2) {
        switch (currentState) {
            case START_MENU:
                menu.draw(g2);
                break;

            case PLAYING:
                renderer.draw(g2);
                break;

            case HOW_TO_PLAY:
                overlayUI.drawGuide(g2);
                break;

            case GAME_OVER:
                renderer.draw(g2);
                overlayUI.drawGameOver(g2, game.getScoreManager().getScore());
                break;
        }
    }

    public void handleMouseMove(int mx, int my) {
        if (currentState == GameState.START_MENU) {
            menu.setHover(mx, my);
        } else {
            overlayUI.updateHover(mx, my, currentState);
        }
    }

    public void handleMouseClick(int mx, int my) {
        if (currentState == GameState.START_MENU) {
            int index = menu.getButtonIndex(mx, my);
            if (index == 0) {
                //Bật nhạc khi ấn Play
                src.Manager.SoundManager.getInstance().playBGM("src/resources/sounds/game.wav");
                startGame();}
            else if (index == 1) currentState = GameState.HOW_TO_PLAY;
            else if (index == 2) System.exit(0);
        } 
        else if (currentState == GameState.HOW_TO_PLAY) {
            if (overlayUI.backBtn.contains(mx, my)) currentState = GameState.START_MENU;
        }
        else if (currentState == GameState.GAME_OVER) {
            if (overlayUI.playAgainBtn.contains(mx, my)){
                src.SoundManager.getInstance().playBGM("src/resources/sounds/game.wav"); startGame();}
            if (overlayUI.quitBtn.contains(mx, my)) System.exit(0);
        }
    }

    private void startGame() {
        board.clearBoard();
        game = new GameManager(board);
        renderer = new GameRenderer(game, board, left_x, top_y, CELL_SIZE, COLS);
        currentState = GameState.PLAYING;
        leftHold = 0;
        rightHold = 0;
    }
    public GameState getState() { return currentState; }
    public void setState(GameState state) { this.currentState = state; }
}
