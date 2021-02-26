package tttminimax;

import static tttminimax.Constants.*;

public class Player extends Strategy {

    private String Name = null;
    private int Senryaku = 0;
    private int Color = EMPTY;
    private static String coord = null;

    Player(int i, String n, int s) {
        Color = i;
        Name = n;
        Senryaku = s;
        coord = null;
    }

    void putStone(Board board) {
        Stone s = null;
        do {
            if ((Senryaku == GUI) || (Senryaku == NETWORK)) {
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
            s = new Stone(te, Color);
            if (board.canPut(s)) {
                break;
            }
        } while (true);
        
        if (Senryaku == NETWORK) {
            int x = coord.charAt(0) - 'a';
            int y = coord.charAt(1) - '1';
            client.out.println("slot " + String.valueOf(y * NXN + x));
            client.out.println("message " + "あなたの番です");
            client.out.flush();
        }
        
        board.setStones(s);
    }

    boolean getHuman() {
        if (Color == BATSU) {
            return true;
        }
        return false;
    }

    int getSenryaku() {
        return Senryaku;
    }

    void setCoord(String cd) {
        coord = cd;
    }

    String getCoord() {
        return coord;
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
                v = bestMove(board, this);
                break;
            case ALPHABETA:
                break;
            case MONTECARLO:
                break;
            case GUI:
                v = gui(coord);
                coord = null;
                break;
            case NETWORK:
                v = network(coord);
                coord = null;
                break;
        }
        return v;
    }
    int getColor(){
        return Color;
    }
}
