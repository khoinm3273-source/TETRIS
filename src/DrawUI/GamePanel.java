package src.DrawUI;
import javax.swing.JPanel;
import java.awt.*;
import src.Board;
import src.KeyHandler;
import src.Manager.PlayManager;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    final int FPS = 60;
    Thread gameThread;

    KeyHandler keyH = new KeyHandler();
    Board board;
    OverlayUI overlayUI;
    PlayManager playManager;

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(keyH); 
        board = new Board();
        overlayUI = new OverlayUI();
        playManager = new PlayManager(keyH, board, overlayUI);
        
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) { 
                playManager.handleMouseClick(e.getX(), e.getY());
            }
        });
        
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                playManager.handleMouseMove(e.getX(), e.getY());
            }
        });
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
        requestFocusInWindow();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        playManager.update(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        playManager.draw(g2);
    }
}