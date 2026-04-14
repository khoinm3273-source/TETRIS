import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class Tetris {
    public static void main (String[] args) {
            // tạo 1 cửa sổ
        JFrame frame = new JFrame("TETRIS");
        frame.setSize(550, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);// tô màu nền cho cửa sổ

        JButton button = new JButton("I'm gay!");
        panel.add(button);

        // thêm panel vào frame rồi cho hiển thị
        frame.add(panel);
        frame.setVisible(true);
    }
}
