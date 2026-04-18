import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
public class PlayManager {
    public final int CELL_SIZE = 30;
    public final int COLS = 10;
    public final int ROWS = 20;
    public final int WIDTH = COLS * CELL_SIZE;
    public final int HEIGHT = ROWS * CELL_SIZE;
    public int left_x, top_y;
    Board board;
    Shape currentShape;
    int x, y;

    //kiểm soát tốc độ rơi
    int dropCounter = 0;
    int dropInterval = 30;
    int softDropCounter = 0;
    int softDropInterval = 5;
    int moveCounter = 0;
    int moveInterval = 10; 

    // xoá delay
    boolean leftHeld = false;
    boolean rightHeld = false;
    int dasDelay = 10;   // delay trước khi auto
    int arrSpeed = 3;    // tốc độ lặp sau delay

    //kiểm soát xoay khối
    boolean rotateHeld = false;

    KeyHandler keyH;

    public PlayManager(KeyHandler keyH) {
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2);
        top_y = 50;
        this.keyH = keyH;
        board = new Board();
        spawnShape();
    }

    public void spawnShape() {
        currentShape = new Shape();
        currentShape.setRandomShape();
        x = 4;
        y = 0;
        if (!board.isValidPosition(currentShape.getCoords(), x, y)) {
            System.out.println("GAME OVER");
        }
    }

    public void update() {
        if (keyH.leftPressed || keyH.aPressed) {
            moveCounter++;
            if (moveCounter >= moveInterval) {
                if (board.isValidPosition(currentShape.getCoords(), x - 1, y)) {
                    x--;
                }
                moveCounter = 0;
            }
        }
        else if (keyH.rightPressed || keyH.dPressed) {
            moveCounter++;
            if (moveCounter >= moveInterval) {
                if (board.isValidPosition(currentShape.getCoords(), x + 1, y)) {
                    x++;
                }
                moveCounter = 0;
            }
        }
        else {
            moveCounter = 0; // reset khi không bấm
        }

        //xoá delay khi bấm sang phải
        if (keyH.rightPressed) {        
            if (!rightHeld) {
                if (board.isValidPosition(currentShape.getCoords(), x + 1, y)) {
                    x++;
                }
                rightHeld = true;
                moveCounter = 0;
            } else {
                moveCounter++;

                if (moveCounter > dasDelay) {
                    if (moveCounter % arrSpeed == 0) {
                        if (board.isValidPosition(currentShape.getCoords(), x + 1, y)) {
                            x++;
                        }
                    }
                }
            }

        } else {
            rightHeld = false;
        }
    
        //xoá delay khi bấm qua trái
        if (keyH.leftPressed) {
        if (!leftHeld) {
            if (board.isValidPosition(currentShape.getCoords(), x - 1, y)) {
                x--;
            }
            leftHeld = true;
            moveCounter = 0;
        } else {
            moveCounter++;

            if (moveCounter > dasDelay) {
                if (moveCounter % arrSpeed == 0) {
                    if (board.isValidPosition(currentShape.getCoords(), x - 1, y)) {
                        x--;
                    }
                }
            }
        }

    } else {
        leftHeld = false;
    }
    
    //xoay khối bằng phím mũi tên lên / phím W
    if ((keyH.upPressed || keyH.wPressed) && !rotateHeld) {
    currentShape.rotate();
    // nếu xoay xong mà invalid thì rollback
    if (!board.isValidPosition(currentShape.getCoords(), x, y)) {
        for (int i = 0; i < 3; i++) {
            currentShape.rotate(); // quay lại trạng thái cũ
        }
    }
    rotateHeld = true;
}
if (!keyH.upPressed && !keyH.wPressed) {
    rotateHeld = false;
}
    // SOFT DROP
    if (keyH.downPressed) {
        softDropCounter++;
        if (softDropCounter >= softDropInterval) {
            if (board.isValidPosition(currentShape.getCoords(), x, y + 1)) {
                y++;
            }
            softDropCounter = 0;
        }
        return; // chặn gravity
    }

    // GRAVITY
    dropCounter++;
    if (dropCounter >= dropInterval) {
        if (board.isValidPosition(currentShape.getCoords(), x, y + 1)) {
            y++;
        } else {
            board.placeShape(currentShape.getCoords(), x, y, currentShape.getPieceShape());
            board.clearLines();
            spawnShape();
        }
        dropCounter = 0;
    }
}

    public void draw(Graphics2D g2) {
        // vẽ khung
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x, top_y, WIDTH, HEIGHT);

        // vẽ block đã đặt
        board.draw(g2, left_x, top_y, CELL_SIZE);

        // vẽ block đang rơi
        drawCurrentShape(g2);
    }

    private void drawCurrentShape(Graphics2D g2) {
        int[][] shape = currentShape.getCoords();
        g2.setColor(currentShape.getColor());
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    g2.fillRect(
                        left_x + (x + c) * CELL_SIZE,
                        top_y + (y + r) * CELL_SIZE,
                        CELL_SIZE, CELL_SIZE
                    );
                }
            }
        }
    }
}