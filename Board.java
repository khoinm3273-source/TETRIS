import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Board extends JPanel {
    private int width = 10, height = 20;
    private Tetrominos[][] grid;
    private Shape currentPiece;
    private int currentX, currentY;

    public void clearBoard() {          //tạo board trống
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = Tetrominos.NoShape;
            }
        }
    }

    public Board() {
        grid = new Tetrominos[height][width];
        clearBoard();
    }

    public void spawnPiece() {      //spawn block
    currentPiece = new Shape();
    currentPiece.setRandomShape();
    currentX = width / 2 - 1;
    currentY = 0;
}
}
