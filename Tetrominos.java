import java.awt.Color;
import java.util.Random;

public enum Tetrominos {
    // shape[rotation][row][col]
    // gọi từng object enum cho mỗi loại khối
    NoShape (new int[][][] {
        {
            {0}             //tượng trưng cho mỗi ô trống
        },
    },Color.CYAN),

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

    // Trả về số lượng trạng thái xoay của khối đó
    public int getRotationCount() {
        return this.shape.length; 
    }
    public final int[][][] shape;
    public final Color color;
    //gọi constructor để truyền dữ liệu của 7 loại khối
    Tetrominos (int[][][] shape, Color color) { 
        this.shape = shape;
        this.color = color;
    }
    // lấy random 1 loại khối để rơi, sau này dùng trong class
    // Shape và Board
    public static Tetrominos getRandomShape() {
    Random random = new Random();
    Tetrominos[] values = Tetrominos.values();
    // values[0] thường là NoShape, nên lấy từ index 1 đến hết
    return values[random.nextInt(values.length - 1) + 1];
    }
}
