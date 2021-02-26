package othello66;

import static othello66.Constants.*;

import java.util.ArrayList;
import java.util.List;

public class Player extends Strategy {

    private String Name = null;
    private int Senryaku = 0;
    private int Color = EMPTY;
    private String coord = null;

    public Player(int c, String n, int s) {
        Color = c;
        Name = n;
        Senryaku = s;
        coord=null;
    }

    int getColor() {
        return Color;
    }

    String getName() {
        return Name;
    }
    
    boolean getHuman(){
        if(Color==BLACK)return true;
        return false;
    }
    
    void setCoord(String cd){
        coord = cd;        
    }

    String getCoord() {
        return coord;
    }
    
    int getSenryaku(){
        return Senryaku;
    }
    
    int putStone(Board board) {
        List<Integer> teList = new ArrayList<Integer>();
        int[] bw = new int[2];
        board.countBW(bw);
        int nres = NxN - bw[0] - bw[1];
        Stone s = null;
        do {
            if ((Senryaku==NETWORK) || (Senryaku == GUI)) {
                while (getCoord() == null) {
                    //System.out.println("どこに石を置きます？");
                                        
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                }
            }
            int te = Te(board);

            if( !(0<=te && te<WIDTH*WIDTH) ){
                coord=null;
                System.out.println("te="+te);
                continue;
            }

            s = new Stone(te, Color);
            if (board.canPut(s)) {
                break;
            } else {
                if (board.getStone(te).getColor() == EMPTY) {
                    int i = 0;
                    for (i = 0; i < teList.size(); i++) {
                        if (teList.get(i) == te) {
                            break;
                        }
                    }
                    if (i >= teList.size()) {
                        teList.add(te);
                        nres--;
                    }
                }
                if (nres <= 0) {
                    return PASS;
                }
            }
        } while (true);
        board.setBoard(s);
        return NEXT;
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
                v = bestMoveMM(board, this);
                break;
            case ALPHABETA:
                v = bestMoveAB(board, this);
                break;
            case MONTECARLO:
                break;
            case COMPUTER:
                v = computer(board, this);
                break;
             case GUI:
                v = gui(coord);
                coord = null;
                break;
             case NETWORK:
                v = network(coord);
                coord = null;
                break;
           default:
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

}
