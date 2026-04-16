import java.awt.Color;

public class Shape {
    private Tetrominos pieceShape;
    private int currentRotation;

    public Shape() {
        setShape(Tetrominos.NoShape);
    }

    // Đặt hình cho khối, sau khi một khối bất kỳ chạm đáy thì nó sẽ reset về ban đầu và tạo nên một khối bất kì khác
    public void setShape(Tetrominos shape) {
        this.pieceShape = shape;
        this.currentRotation = 0;
    }

    // Tự lấy ngẫu nhiên một khối để nó rơi xuống
    public void setRandomShape() {
        setShape(Tetrominos.getRandomShape());
    }

    // Xoay hình (sẽ trở về ban đầu khi một khối bất kỳ đạt đủ số xoay của nó)
    public void rotate() {
        currentRotation = (currentRotation + 1) % pieceShape.getRotationCount();
    }
    
    public int[][] getCoords() {
        return pieceShape.getShape(currentRotation);
    }

    public Color getColor() {
        return pieceShape.getColor();
    }

    /* Về chiều rộng (nó sẽ đếm số ô của khối đó bắt đầu ở trên cùng đếm sang ngang)
       Lấy ví dụ là khối T thì số 0 có nghĩa là bắt đầu từ hàng đầu tiên đếm sang ngang
       Thì hàng đầu tiên của khối T là 0 và có 3 ô thì nó sẽ trả về là 3*/
    public int getWidth() {
        return getCoords()[0].length;
    }

    // Về chiều cao (nó sẽ đếm số ô của khối đó bắt đầu ở trên cùng đếm xuống dưới)
    public int getHeight() {
        return getCoords().length;
    }

    public Tetrominos getPieceShape() {
        return pieceShape;
    }
}