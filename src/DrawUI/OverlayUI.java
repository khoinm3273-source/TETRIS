package src.DrawUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import src.GameState;
import src.Manager.SoundManager;

import java.io.IOException;

public class OverlayUI {
    public Rectangle playAgainBtn = new Rectangle(515, 360, 250, 60);
    public Rectangle quitBtn = new Rectangle(515, 440, 250, 60);
    public Rectangle backBtn = new Rectangle(540, 520, 200, 60);

    private boolean playAgainHover, quitHover, backHover;
    private BufferedImage guidesImage;
    private float overlayAlpha = 0f;

    public OverlayUI() {
        try {
            guidesImage = ImageIO.read(getClass().getResource("/src/assets/guides_bg.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(GameState state) {
        if (state == GameState.GAME_OVER) {
            //Phát nhạc Game Over
            if (!gameOverSoundPlayed) {
                SoundManager.getInstance().stopBGM(); // Tắt nhạc nền
                SoundManager.getInstance().playSFX("src/resources/sounds/game-over.wav"); // Bật tiếng thua
                gameOverSoundPlayed = true; //
            }

            overlayAlpha += 0.02f;
            if (overlayAlpha > 1f) overlayAlpha = 1f;
        }
    }

    public void resetGameOver() {   //reset khi vừa thua
        overlayAlpha = 0f;
    }

    public void drawGameOver(Graphics2D g2, int score) {
        int alpha = (int)(220 * overlayAlpha);
        g2.setColor(new Color(0, 0, 0, alpha));
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        int pw = 520, ph = 460;
        int px = (GamePanel.WIDTH - pw) / 2;
        int py = (GamePanel.HEIGHT - ph) / 2;

        // panel glass
        g2.setColor(new Color(20, 20, 30, 200));
        g2.fillRoundRect(px, py, pw, ph, 35, 35);

        g2.setColor(new Color(255, 255, 255, 60));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(px, py, pw, ph, 35, 35);

        // game over
        g2.setFont(new Font("Arial", Font.BOLD, 64));
        String text = "GAME OVER";
        FontMetrics fm = g2.getFontMetrics();

        int tx = (GamePanel.WIDTH - fm.stringWidth(text)) / 2;
        int ty = py + 110;

        // glow
        g2.setColor(new Color(255, 0, 0, 80));
        g2.drawString(text, tx+2, ty+2);

        // gradient
        GradientPaint gp = new GradientPaint(
            tx, ty-50, new Color(255, 100, 100),
            tx, ty+20, new Color(180, 0, 0)
        );
        g2.setPaint(gp);
        g2.drawString(text, tx, ty);

        // score
        g2.setFont(new Font("Arial", Font.BOLD, 32));
        String scoreText = "SCORE: " + score;

        int sx = (GamePanel.WIDTH - g2.getFontMetrics().stringWidth(scoreText)) / 2;
        int sy = ty + 70;

        // shadow
        g2.setColor(new Color(0,0,0,150));
        g2.drawString(scoreText, sx+2, sy+2);

        // main
        g2.setColor(new Color(0, 255, 200));
        g2.drawString(scoreText, sx, sy);

        // hint
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        String hint = "Press R to Restart • ESC to Quit";

        FontMetrics fmHint = g2.getFontMetrics();
        int hx = (GamePanel.WIDTH - fmHint.stringWidth(hint)) / 2;

        // đặt dưới nút QUIT
        int hy = quitBtn.y + quitBtn.height + 40;
        g2.setColor(new Color(200, 200, 200, 160));
        g2.drawString(hint, hx, hy);

        // button
        drawStyledButton(g2, playAgainBtn, "PLAY AGAIN", playAgainHover);
        drawStyledButton(g2, quitBtn, "QUIT", quitHover);
    }

    public void drawGuide(Graphics2D g2) {
        if (guidesImage != null) {
            g2.drawImage(guidesImage, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
        }
        g2.setColor(new Color(0, 0, 50, 150));
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        int pw = 600, ph = 500;
        int px = (GamePanel.WIDTH - pw) / 2;
        int py = (GamePanel.HEIGHT - ph) / 2;

        g2.setColor(new Color(40, 80, 200, 200));
        g2.fillRoundRect(px, py, pw, ph, 30, 30);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(px, py, pw, ph, 30, 30);
        
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        String title = "HOW TO PLAY";
        FontMetrics fmTitle = g2.getFontMetrics();
        g2.drawString(title, (GamePanel.WIDTH - fmTitle.stringWidth(title)) / 2, py + 70);
        
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        FontMetrics fmText = g2.getFontMetrics();
        String[] instructions = {
            "← →  : Move",
            "↑ / W : Rotate",
            "↓ / S : Soft Drop",
            "SPACE : Hard Drop"
        };
        int startY = py + 160;
        for (int i = 0; i < instructions.length; i++) {
            int tx = (GamePanel.WIDTH - fmText.stringWidth(instructions[i])) / 2;
            g2.drawString(instructions[i], tx, startY + (i * 60));
        }
        drawStyledButton(g2, backBtn, "GO BACK", backHover);
    }

    private void drawStyledButton(Graphics2D g2, Rectangle r, 
                                    String text, boolean hover) {
        // glow hover
        if (hover) {
            g2.setColor(new Color(0, 255, 255, 80));
            g2.fillRoundRect(r.x - 8, r.y - 8, r.width + 16, r.height + 16, 25, 25);
        }

        // shadow
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(r.x + 4, r.y + 4, r.width, r.height, 20, 20);

        // gradient chính
        GradientPaint gp = new GradientPaint(
            r.x, r.y, new Color(100, 150, 255),
            r.x, r.y + r.height, new Color(140, 80, 255)
        );
        g2.setPaint(gp);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 20, 20);

        // viền sáng
        g2.setColor(new Color(255,255,255,120));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, 20, 20);

        // text
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();

        int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
        int ty = r.y + (r.height + fm.getAscent()) / 2 - 4;

        // shadow text
        g2.setColor(new Color(0,0,0,150));
        g2.drawString(text, tx+1, ty+1);

        g2.setColor(Color.WHITE);
        g2.drawString(text, tx, ty);
    }

    public void updateHover(int mx, int my, GameState state) {
        playAgainHover = playAgainBtn.contains(mx, my);
        quitHover = quitBtn.contains(mx, my);
        backHover = backBtn.contains(mx, my);
    }
}
