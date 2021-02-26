package tttrandom;

import static tttrandom.Constants.*;

public class Stone {

    private int locate = 0;
    private int Color = BATSU;

    Stone(int n, int i) {
        locate = n;
        Color = i;
    }

    int getColor() {
        return Color;
    }

    void setColor(int i) {
        Color = i;
    }

    int getLocate() {
        return locate;
    }
    
}
