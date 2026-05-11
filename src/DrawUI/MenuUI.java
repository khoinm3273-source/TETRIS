package src.DrawUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import src.Manager.SoundManager;

import java.io.IOException;

public class MenuUI {
    private BufferedImage bgImage;
    private String[] menuItems = {"PLAY", "GUIDES", "QUIT"};
    private Rectangle[] buttons;
    private int selectedIndex = -1;

    public MenuUI() {
        buttons = new Rectangle[menuItems.length];
        int startY = 360;
        for (int i = 0; i < menuItems.length; i++) {
            buttons[i] = new Rectangle(540, startY + i * 80, 200, 60);
        }

        try {
            bgImage = ImageIO.read(getClass().getResource("/src/assets/tetris_bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SoundManager.getInstance().playBGM("src/resources/sounds/menu.wav");
    }

    public void draw(Graphics2D g2) {
        // Vẽ ảnh nền menu
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
        }
        
        // vẽ lớp phủ tối nhẹ
        g2.setColor(new Color(0, 0, 0, 10));
        g2.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        for (int i = 0; i < buttons.length; i++) {
            Rectangle r = buttons[i];
            if (i == selectedIndex) {
                g2.setColor(new Color(0, 255, 255, 150));
                g2.fillRoundRect(r.x - 6, r.y - 6, r.width + 12, r.height + 12, 20, 20);
            }

            // vẽ đổ bóng và gradient cho nút bấm
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(r.x + 4, r.y + 4, r.width, r.height, 20, 20);

            GradientPaint gp = new GradientPaint(
                r.x, r.y, new Color(60, 120, 255),
                r.x, r.y + r.height, new Color(120, 60, 255)
            );
            g2.setPaint(gp);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 20, 20);

            g2.setColor(new Color(180, 220, 255));
            g2.drawRoundRect(r.x, r.y, r.width, r.height, 20, 20);

            // chữ
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            FontMetrics fm = g2.getFontMetrics();
            int textX = r.x + (r.width - fm.stringWidth(menuItems[i])) / 2;
            int textY = r.y + (r.height + fm.getAscent()) / 2 - 4;

            g2.setColor(new Color(0, 255, 255, 80));
            g2.drawString(menuItems[i], textX + 1, textY + 1);
            g2.setColor(Color.WHITE);
            g2.drawString(menuItems[i], textX, textY);
        }
    }

    public int getButtonIndex(int mx, int my) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].contains(mx, my)) return i;
        }
        return -1;
    }

    public void setHover(int mx, int my) {
        selectedIndex = getButtonIndex(mx, my);
    }
}
