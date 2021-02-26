package nmoku55;

import static nmoku55.Constants.*;

public class Board {

    private static Stone[] stones = new Stone[WIDTH * WIDTH];
    private static Stone recentStone = null;
    private static Board board = new Board();
    
    private Board() {
        clearBoard();
    }

    void clearBoard() {
        for (int i = 0; i < WIDTH * WIDTH; i++) {
            stones[i] = new Stone(i, EMPTY);
        }
    }

    void setBoard(Stone s) {
        recentStone = s;
        stones[s.getLocate()] = s;
    }
    
    static Board getBoard(){
        return board;
    }
    
    void setEmpty(int xy){
        stones[xy]=new Stone(xy, EMPTY);
    }

    void storeStone(Stone s) {
        recentStone = s;
        stones[s.getLocate()] = s;
    }

    Stone getStone(int n) {
        return stones[n];
    }

    boolean canPut(Stone s) {
        if (stones[s.getLocate()].getColor() != EMPTY) {
            return false;
        }
        return true;
    }

    void print() {
        do {
            if (screen != null) {
                Print();
                screen.repaint();
                break;
            }
        } while (true);
    }

    void Print() {
        final String[] str = {".", "●", "○"};

        System.out.printf("\n    ");
        for (int x = 0; x < WIDTH; x++) {
            if (x < 10) {
                System.out.printf("%2d", x);
            } else {
                System.out.printf("%2c", (char) ('a' - 10 + x));
            }
        }
        System.out.printf("\n");
        for (int y = 0; y < WIDTH; y++) {
            if (y < 10) {
                System.out.printf("%2d: ", y);
            } else {
                System.out.printf("%2c: ", (char) ('a' - 10 + y));
            }
            for (int x = 0; x < WIDTH; x++) {
                int cl = stones[getz(x, y)].getColor();
                int num=0;
                if(cl==WHITE)num=2;
                if(cl==BLACK)num=1;
                System.out.printf("%2s", str[num]);
                //System.out.printf("%2s", str[board[getz(x, y)].getColor()]);
            }
            System.out.printf("\n");
        }
    }

    private int getz(int x, int y) {
        return y * WIDTH + x;	// 0<= x <WIDTH, 0<= y <WIDTH
    }

    private int boardScan(int x, int y) {
        //盤面の調査（N個並んだかの調査）
        int[] n = new int[4];     //8方向（直線4本分）に並んだ数
        //[＼]方向
        n[0] = boardScanSub(x, y, 1, 1);
        //[│]方向
        n[1] = boardScanSub(x, y, 0, 1);
        //[─]方向
        n[2] = boardScanSub(x, y, 1, 0);
        //[／]方向
        n[3] = boardScanSub(x, y, -1, 1);
        for (int i = 0; i < 4; i++) {
            if (n[i] == NMOKU) {
                return recentStone.getColor();
            }
        }
        return NEXT;
    }

    private int boardScanSub(int xx, int yy, int move_x, int move_y) {
        int n = 1;          //置いた場所の1個分で初期化
        int i;
        for (i = 1; i < NMOKU; i++) {
            int x = xx + (move_x * i);
            int y = yy + (move_y * i);
            if((WIDTH<=x)||(WIDTH<=y)||(x<0)||(y<0))break;
            int z = getz(x, y);
            //if (!(0 <= z && z < WIDTH * WIDTH)) {
            //    continue;
            //}
            if (stones[z].getColor() == recentStone.getColor()) {
                //if(move_x==1 && move_y==0)
                //    if(z/WIDTH != recentStone.getLocate()/WIDTH)n=0;
                n += 1;
            } else {
                break;
            }
        }
        for (i = 1; i < NMOKU; i++) {
            int x = xx + (-1 * move_x * i);
            int y = yy + (-1 * move_y * i);
            if((WIDTH<=x)||(WIDTH<=y)||(x<0)||(y<0))break;
            int z = getz(x, y);
            //if (!(0 <= z && z < WIDTH * WIDTH)) {
            //    continue;
            //}
            if (stones[z].getColor() == recentStone.getColor()) {
                //if(move_x==1 && move_y==0)
                //    if(z/WIDTH != recentStone.getLocate()/WIDTH)n=0;
                n += 1;
            } else {
                break;
            }
        }
        return n;
    }

    int check() {
        return boardScan(recentStone.getLocate() % WIDTH, recentStone.getLocate() / WIDTH);
    }
    
    boolean isWin(){
        if(check()!=NEXT)return true;
        return false;
    }
    
    boolean isDraw(){
        return false;
    }
    
    float evaluate(Player player, int turn){
        if(isWin()){
            if(turn==player.getColor())return (float)(-1.0);
            else if(turn!=player.getColor())return (float)(1.0);
        }
        return (float)0.0;
    }

}
