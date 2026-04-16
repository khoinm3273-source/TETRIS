import java.awt.Color;
import java.util.Random;

public enum Tetrominos {
    // shape[rotation][row][col]
    // gọi từng object enum cho mỗi loại khối
    NoShape (new int[][][] {
        {
            {0}             //tượng trưng cho mỗi ô trống
        },
    },Color.BLACK),

    I (new int[][][] {
        {
            {1,1,1,1}
        },
        {
            {1},
            {1},
            {1},
            {1}
        }
    },Color.RED),

    J (new int[][][] {
        {
            {0,1},
            {0,1},
            {1,1}
        },
        {
            {1,0,0},
            {1,1,1}
        },
        {
            {1,1},
            {1,0},
            {1,0}
        },
        {
            {1,1,1},
            {0,0,1}
        }
    },Color.YELLOW),

    L (new int[][][] {
        {
            {1,0},
            {1,0},
            {1,1}
        },
        {
            {1,1,1},
            {1,0,0}
        },
        {
            {1,1},
            {0,1},
            {0,1}
        },
        {
            {0,0,1},
            {1,1,1}
        }
    },Color.GREEN),

    O (new int[][][] {
        {
            {1,1},
            {1,1}
        }
    },Color.ORANGE),

    S (new int[][][] {
        {
            {0,1,1},
            {1,1,0}
        },
        {
            {1,0},
            {1,1},
            {0,1}
        }
    },Color.BLUE),

    T (new int[][][] {      //4 góc quay
        {
            {1,1,1},
            {0,1,0}
        },
        {
            {1,0},
            {1,1},
            {1,0}
        },
        {
            {0,1,0},
            {1,1,1}
        },
        {
            {0,1},
            {1,1},
            {0,1}
        }
    },Color.MAGENTA),

    Z (new int[][][] {
        {
            {1,1,0},        //góc quay thứ 1 (2 rows, 3cols)
            {0,1,1}
        },
        {
            {0,1},      //góc quay thứ 2 (3 rows, 2cols)
            {1,1},
            {1,0}
        }
    },Color.PINK);

    public final int[][][] shape;
    public final Color color;
    //gọi constructor để truyền dữ liệu của 7 loại khối
    Tetrominos (int[][][] shape, Color color) { 
        this.shape = shape;
        this.color = color;
    }

    // Trả về số lượng trạng thái xoay của khối đó
    public int getRotationCount() {
        return this.shape.length; 
    }
    
    // lấy random 1 loại khối để rơi, sau này dùng trong class
    // Shape và Board
    private static final Random random = new Random();
    public static Tetrominos getRandomShape() {
        Tetrominos[] values = Tetrominos.values();
        return values[random.nextInt(values.length - 1) + 1];
    }
    
    public int[][] getShape(int rotation) {
        return shape[rotation];
    }

    public Color getColor() {
        return color;
    }
}
