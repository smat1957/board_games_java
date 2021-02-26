package othello1212;

import static othello1212.Constants.*;

public class Stone {

    private int locate = 0;
    private int color = EMPTY;

    public Stone(int n, int i) {
        locate = n;
        color = i;
    }

    int getColor() {
        return color;
    }

    void setColor(int i) {
        color = i;
    }

    int getLocate() {
        return locate;
    }

    int flip(int col) {
        return 3 - col;	// 石の色を反転させる
    }

}
