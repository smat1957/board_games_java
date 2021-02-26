package nmoku55;

import static nmoku55.Constants.*;

public class Player extends Strategy {

    private String Name = null;
    private int Senryaku = 0;
    private int Color = EMPTY;
    private String coord = null;

    Player(int i, String n, int s) {
        Color = i;
        Name = n;
        Senryaku = s;
        coord = null;
    }

    void putStone(Board board) {
        Stone s = null;
        do {
            if ((Senryaku == NETWORK) || (Senryaku == GUI)) {
                while (getCoord() == null) {
                    //System.out.println("どこに石を置きます？");
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            int te = Te(board);

            if (!(0 <= te && te < WIDTH * HEIGHT)) {
                coord = null;
                //System.out.println("te="+te);
                continue;
            }

            s = new Stone(te, Color);
            if (board.canPut(s)) {
                break;
            }
        } while (true);
        board.storeStone(s);
    }

    boolean getHuman() {
        //return true;

        if (Color == BATSU) {
            return true;
        }
        return false;

    }

    void setCoord(String cd) {
        coord = cd;
    }

    String getCoord() {
        return coord;
    }

    int getSenryaku() {
        return Senryaku;
    }

    private int Te(Board board) {
        int v = 0;
        switch (Senryaku) {
            case PROMPT:
                v = prompt(Name);
                break;
            case RANDOM:
                v = random(Name);
                break;
            case MINIMAX:
                screen.setFrameTitle("コンピュータ思考中。。。ちょっと待って。。。");
                v = bestMoveMM(board, this);
                screen.setFrameTitle(null);
                break;
            case ALPHABETA:
                screen.setFrameTitle("コンピュータ思考中。。。ちょっと待って。。。");
                v = bestMoveAB(board, this);
                screen.setFrameTitle(null);
                break;
            case MONTECARLO:
                break;
            case GUI:
                //screen.setFrameTitle("あなたの番です");
                v = gui(coord);
                coord = null;
                //screen.setFrameTitle(null);
                break;
            case NETWORK:
                v = network(coord);
                coord = null;
                break;
        }
        return v;
    }

    String toStringCoord(int xy) {
        int y = xy / WIDTH;
        int x = xy % WIDTH;
        return toStringCoord(x, y);
    }

    String toStringCoord(int nx, int ny) {
        char cx, cy;
        if (nx < 10) {
            cx = (char) ('1' + (nx - 1));
        } else {
            cx = (char) ('a' + (nx - 10));
        }
        if (ny < 10) {
            cy = (char) ('1' + (ny - 1));
        } else {
            cy = (char) ('a' + (ny - 10));
        }
        return String.valueOf(cx) + String.valueOf(cy);
    }
    int getColor(){
        return Color;
    }
    String getName(){
        return Name;
    }
}
