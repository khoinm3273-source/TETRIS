package src;
import java.awt.Graphics2D;

public class Board {
    private int width = 10, height = 20;
    private Tetrominos[][] grid;
    private src.SoundManager soundManager;
    
    public Board() {
        grid = new Tetrominos[height][width];
        //Âm thanh
        soundManager =  SoundManager.getInstance();
        clearBoard();
    }

    // reset board
    public void clearBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = Tetrominos.NoShape;
            }
        }
    }

    // check va chạm
    public boolean isValidPosition(int[][] shape, int x, int y) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 0) continue;
                int newX = x + c;
                int newY = y + r;

                // check biên
                if (newX < 0 || newX >= width || newY >= height) {
                    return false;
                }

                // cho phép spawn trên top
                if (newY >= 0 && grid[newY][newX] != Tetrominos.NoShape) {
                    return false;
                }
            }
        }
        return true;
    }

    // đặt block
    public void placeShape(int[][] shape, int x, int y, Tetrominos type) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    grid[y + r][x + c] = type;
                }
            }
        }
    }

    // xoá hàng
    public int clearLines() {
        int linesCleared = 0;
        for (int row = height - 1; row >= 0; row--) {
            boolean isFull = true;
            for (int col = 0; col < width; col++) {
                if (grid[row][col] == Tetrominos.NoShape) {
                    isFull = false;
                    break;
                }
            }

            if (isFull) {
                for (int r = row; r > 0; r--) {
                    for (int c = 0; c < width; c++) {
                        grid[r][c] = grid[r - 1][c];
                    }
                }

                for (int c = 0; c < width; c++) {
                    grid[0][c] = Tetrominos.NoShape;
                }

                linesCleared++;
                row++;
            }
        }
        return linesCleared;
    }

    // kiểm tra hàng đầy nhưng không xoá
    public int checkFullLines(int[] rows) {
        int count = 0;
        for (int r = 0; r < height; r++) {
            boolean full = true;
            for (int c = 0; c < width; c++) {
                if (grid[r][c] == Tetrominos.NoShape) {
                    full = false;
                    break;
                }
            }
            if (full) {
                rows[count++] = r;
            }
        }
        if (count > 0) {
            soundManager.playSFX("src/resources/sounds/score.wav");
        }
        return count;
    }

    // xoá hàng sau animation
    public void removeLines(int[] rows, int count) {
        for (int i = 0; i < count; i++) {
            int row = rows[i];

            for (int r = row; r > 0; r--) {
                System.arraycopy(grid[r - 1], 0, grid[r], 0, width);
            }

            // clear dòng trên cùng
            for (int c = 0; c < width; c++) {
                grid[0][c] = Tetrominos.NoShape;
            }
        }
    }

    // vẽ block đã đặt
    public void draw(Graphics2D g2, int offsetX, int offsetY, int cellSize) {
        int margin = 2;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (grid[r][c] != Tetrominos.NoShape) {
                    g2.setColor(grid[r][c].getColor());
                    g2.fill3DRect(
                        offsetX + c * cellSize + margin,
                        offsetY + r * cellSize + margin,
                        cellSize - (2*margin),
                        cellSize - (2*margin),
                        true
                    );
                }
            }
        }
    }
}
