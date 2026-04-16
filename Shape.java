import java.awt.Color;

public class Shapes {
    private Tetrominos pieceShape;
    private int currentRotation;

    public Shapes() {
        setShape(Tetrominos.NoShape);
    }

    public void setShape(Tetrominos shape) {
        this.pieceShape = shape;
        this.currentRotation = 0;
    }

    public void setRandomShape() {
        setShape(Tetrominos.getRandomShape());
    }

    public void rotate() {
        currentRotation = (currentRotation + 1) % pieceShape.getRotationCount();
    }
    
    public int[][] getCoords() {
        return pieceShape.shape[currentRotation];
    }

    public Color getColor() {
        return pieceShape.color;
    }

    public int getWidth() {
        return getCoords()[0].length;
    }

    public int getHeight() {
        return getCoords().length;
    }

    public Tetrominos pieceShape() {
        return pieceShape;
    }
}