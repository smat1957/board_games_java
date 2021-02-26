package nmoku55;

import static nmoku55.Constants.*;

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
}
