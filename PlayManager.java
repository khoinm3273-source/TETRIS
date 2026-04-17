import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
public class PlayManager {
    public final int WIDTH = 360, HEIGHT = 600;
    public final int CELL_SIZE = 30;
    public int left_x, top_y;
    Board board;
    Shape currentShape;
    int x, y;
    int dropCounter = 0;
    int dropInterval = 30;

    public PlayManager() {
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2);
        top_y = 50;
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
        dropCounter++;
        if (dropCounter >= dropInterval) {
            if (board.isValidPosition(currentShape.getCoords(), x, y + 1)) {
                y++;
            } else {
                board.placeShape(
                    currentShape.getCoords(),
                    x, y,
                    currentShape.getPieceShape()
                );
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