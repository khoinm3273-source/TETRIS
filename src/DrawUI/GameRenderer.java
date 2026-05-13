package src.DrawUI;
import java.awt.*;
import src.Board;
import src.Shape;
import src.Manager.GameManager;
import src.Manager.ScoreManager;

public class GameRenderer {
    private GameManager game;
    private Board board;
    private int leftX, topY, cellSize, width, height;

    public GameRenderer(GameManager game, Board board, int leftX, int topY, int cellSize, int cols) {
        this.game = game;
        this.board = board;
        this.leftX = leftX;
        this.topY = topY;
        this.cellSize = cellSize;
        this.width = cols * cellSize;
        this.height = 20 * cellSize; // Mặc định 20 hàng
    }

    public void draw(Graphics2D g2) {
        // Vẽ Gradient nền game
        GradientPaint bg = new GradientPaint(0, 0, new Color(20, 20, 60), 0, GamePanel.HEIGHT, new Color(60, 20, 100));
        g2.setPaint(bg);
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        // Vẽ khung Board
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(leftX, topY, width, height);

        board.draw(g2, leftX, topY, cellSize); 
        drawCurrentShape(g2); 
        drawSidePanel(g2);

        if (game.isClearing()) drawClearAnimation(g2); 
        if (game.getLevelUpTimer() > 0) drawLevelUp(g2); 
    }

    private void drawCurrentShape(Graphics2D g2) {
        Shape shapeObj = game.getCurrentShape(); 
        int[][] coords = shapeObj.getCoords();
        g2.setColor(shapeObj.getColor());
        for (int r = 0; r < coords.length; r++) {
            for (int c = 0; c < coords[r].length; c++) {
                if (coords[r][c] != 0) {
                    g2.fill3DRect(leftX + (game.getX() + c) * cellSize + 2, 
                                  topY + (game.getY() + r) * cellSize + 2, 
                                  cellSize - 4, cellSize - 4, true);
                }
            }
        }
    }

    private void drawSidePanel(Graphics2D g2) {
        int panelX = leftX + width + 40;
        int panelY = topY;

        // Shadow (Đổ bóng cho panel)
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(panelX + 6, panelY + 6, 220, 600, 20, 20);

        // Nền glass tối
        g2.setColor(new Color(20, 20, 40, 200));
        g2.fillRoundRect(panelX, panelY, 220, 600, 20, 20);

        drawNext(g2, panelX);
        drawStats(g2, panelX);
    }

    private void drawNext(Graphics2D g2, int panelX) {
        Shape nextShape = game.getNextShape(); 
        int previewCell = cellSize - 6;
        int panelWidth = 220;
        int boxSize = 120;
        int boxX = panelX + (panelWidth - boxSize) / 2;
        int boxY = topY + 60;
       
        // Vẽ tiêu đề "NEXT" với hiệu ứng Glow
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        String title = "NEXT";
        FontMetrics fm = g2.getFontMetrics();
        int tx = panelX + (panelWidth - fm.stringWidth(title)) / 2;
        int ty = topY + 40;

        g2.setColor(new Color(0, 255, 255, 80));
        g2.drawString(title, tx + 1, ty + 1);
        g2.setColor(new Color(220, 240, 255));
        g2.drawString(title, tx, ty);

        // Box chứa khối tiếp theo
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(boxX + 4, boxY + 4, boxSize, boxSize, 20, 20);
        g2.setColor(new Color(20, 20, 40, 200));
        g2.fillRoundRect(boxX, boxY, boxSize, boxSize, 20, 20);
        g2.setColor(new Color(180, 220, 255));
        g2.drawRoundRect(boxX, boxY, boxSize, boxSize, 20, 20);

        // Vẽ khối tiếp theo căn giữa box
        int[][] shape = nextShape.getCoords();
        g2.setColor(nextShape.getColor());
        int padding = 10;
        int margin = 2;
        int innerSize = boxSize - padding * 2;
        int offsetX = boxX + padding + (innerSize - shape[0].length * previewCell) / 2;
        int offsetY = boxY + padding + (innerSize - shape.length * previewCell) / 2;

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    g2.fill3DRect(offsetX + c * previewCell + margin,
                                  offsetY + r * previewCell + margin,
                                  previewCell - (2 * margin),
                                  previewCell - (2 * margin), true);
                }
            }
        }
    }

    private void drawStats(Graphics2D g2, int panelX) {
        ScoreManager sm = game.getScoreManager(); 
        int centerX = panelX + 110;
        int baseY = topY + 240;
        drawStatBlock(g2, "SCORE", String.valueOf(sm.getScore()), centerX, baseY);
        drawStatBlock(g2, "LINES", String.valueOf(sm.getLines()), centerX, baseY + 100);
        drawStatBlock(g2, "LEVEL", String.valueOf(sm.getLevel()), centerX, baseY + 200);
    }

    private void drawStatBlock(Graphics2D g2, String label, String value, int centerX, int y) {
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm1 = g2.getFontMetrics();
        int lx = centerX - fm1.stringWidth(label) / 2;
        g2.setColor(new Color(0, 255, 255, 60));
        g2.drawString(label, lx + 1, y + 1);
        g2.setColor(new Color(200, 220, 255));
        g2.drawString(label, lx, y);

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm2 = g2.getFontMetrics();
        int vx = centerX - fm2.stringWidth(value) / 2;
        g2.setColor(new Color(0, 255, 255, 80));
        g2.drawString(value, vx + 1, y + 31);
        g2.setColor(Color.WHITE);
        g2.drawString(value, vx, y + 30);
    }

    private void drawClearAnimation(Graphics2D g2) {
        float progress = (float) game.getClearAnimTimer() / game.getClearAnimDuration();
        int[] rows = game.getClearingRows();
        for (int i = 0; i < game.getClearingCount(); i++) {
            int row = rows[i];
            g2.setColor(new Color(0, 255, 255, (int)(200 * (1 - progress))));
            g2.fillRect(leftX, topY + row * cellSize, width, cellSize);
        }
    }

    private void drawLevelUp(Graphics2D g2) {
        int alpha = (int)(255 * (game.getLevelUpTimer() / (float)60));

        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.setColor(new Color(255, 215, 0, alpha)); 

        String text = "LEVEL UP!";
        FontMetrics fm = g2.getFontMetrics();

        int x = leftX + width - fm.stringWidth(text) / 2;
        int y = topY + height / 2;

        g2.drawString(text, x, y);
    }
}