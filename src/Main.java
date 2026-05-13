package src;
import javax.swing.JFrame;
import src.DrawUI.GamePanel;

public class Main extends JFrame {
    public Main() {
        this.setTitle("Tetris");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        GamePanel gp = new GamePanel();
        this.add(gp);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        gp.launchGame(); 
    }
    public static void main(String[] args) {
        new Main();
    }
}

