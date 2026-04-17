import javax.swing.JPanel;
import java.awt.Graphics2D;
public class Board extends JPanel {
    private int width = 10, height = 20;
    private Tetrominos[][] grid;

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

    public boolean isValidPosition(int[][] shape, int x, int y) {       //check va chạm
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 0) continue;
                int newX = x + c;
                int newY = y + r;
                if (newX < 0 || newX >= width || newY < 0 || newY >= height) {  //check biên
                    return false;
                }
                if (grid[newY][newX] != Tetrominos.NoShape) {       //check đè khối
                    return false;
                }
            }
        }
        return true;
    }

    //đặt block vào grid
    public void placeShape(int[][] shape, int x, int y, Tetrominos type) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    grid[y + r][x + c] = type;
                }
            }
        }
    }

    public void clearLines() {          //xoá hàng đầy
        for (int row = height - 1; row >= 0; row--) {
            boolean isFull = true;
            for (int col = 0; col < width; col++) {
                if (grid[row][col] == Tetrominos.NoShape) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {       //kéo xuống sau khi xoá hàng đầy
                for (int r = row; r > 0; r--) {
                    for (int c = 0; c < width; c++) {
                        grid[r][c] = grid[r - 1][c];
                    }
                }
                // clear top row
                for (int c = 0; c < width; c++) {
                    grid[0][c] = Tetrominos.NoShape;
                }
                row++; // kiểm tra lại dòng này
            }
        }
    }

    public void draw(Graphics2D g2, int offsetX, int offsetY, int cellSize) {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (grid[r][c] != Tetrominos.NoShape) {
                    g2.setColor(grid[r][c].getColor());
                    g2.fillRect(offsetX + c * cellSize,
                                offsetY + r * cellSize,
                                cellSize, cellSize);
                }
            }
        }
    }
}

