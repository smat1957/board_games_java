package othello1212;

import java.util.ArrayList;
import java.util.List;
import static othello1212.Constants.*;

public class Board {
        //石の位置による評価値
    private static int[] tableVal88 = {
        120, -20,  20,   5,   5,  20, -20, 120,
        -20, -40,  -5,  -5,  -5,  -5, -40, -20,
         20,  -5,  15,   3,   3,  15,  -5,  20,
          5,  -5,   3,   3,   3,   3,  -5,   5,
          5,  -5,   3,   3,   3,   3,  -5,   5,
         20,  -5,  15,   3,   3,  15,  -5,  20,
        -20, -40,  -5,  -5,  -5,  -5, -40, -20,
        120, -20,  20,   5,   5,  20, -20, 120
    };

    private static Stone[] stones = new Stone[NxN];
    private static Board board = new Board();
    private int nKoma[] = new int[9];   // 指定された位置の各方向に対する反転できるコマの数
    //    [0] : 上方向
    //    [1] : 斜め右上方向
    //    [2] : 右方向
    //    [3] : 斜め右下方向
    //    [4] : 下方向
    //    [5] : 斜め左下方向
    //    [6] : 左方向
    //    [7] : 斜め左上方向
    //    [8] : 全体

    private Board() {
        initBoard();        
    }
    
    void initBoard(){
        for (int i = 0; i < NxN; i++) {
            stones[i] = new Stone(i, EMPTY);
        }
        int nrnd = (int) (Math.random() * 2);
        int n = WIDTH / 2 - 1;
        int m = n + 1;
        if (nrnd == 1) {
            stones[WIDTH * n + n].setColor(WHITE);
            stones[WIDTH * n + m].setColor(BLACK);
            stones[WIDTH * m + n].setColor(BLACK);
            stones[WIDTH * m + m].setColor(WHITE);
        } else {
            stones[WIDTH * n + n].setColor(BLACK);
            stones[WIDTH * n + m].setColor(WHITE);
            stones[WIDTH * m + n].setColor(WHITE);
            stones[WIDTH * m + m].setColor(BLACK);
        }        
    }
    
    static Board getBoard(){
        return board;
    }
    
    void print() {
        final String[] str = {".", "●", "○"};

        System.out.printf("\n     ");
        for (int x = 0; x < WIDTH; x++) {
            System.out.printf("%d ", x);
        }
        System.out.printf("\n");
        for (int y = 0; y < WIDTH; y++) {
            System.out.printf("%2d: ", y);
            for (int x = 0; x < WIDTH; x++) {
                int cl = stones[getz(x, y)].getColor();
                int num=0;
                if(cl==WHITE)num=2;
                if(cl==BLACK)num=1;
                System.out.printf("%2s", str[num]);
            }
            System.out.printf("\n");
        }
    }

    Stone getStone(int z) {
        return stones[z];
    }

    List<Integer> setBoard(Stone k) {
        int y = k.getLocate() / WIDTH;
        int x = k.getLocate() % WIDTH;
        return flip(x, y, k.getColor());
    }
    
    void setAgain(List<Integer> flipped, int turn){
        if(flipped.size()<=0)return;
        for(int i=0; i<flipped.size(); i++){
            int z = flipped.get(i);
            if(i==(flipped.size()-1))
                stones[z] = new Stone(z, EMPTY);
            else
                stones[z] = new Stone(z, turn);
        }
    }
    
    boolean canPut(Stone k) {
        int y = k.getLocate() / WIDTH;
        int x = k.getLocate() % WIDTH;
        if (checkFlip(x, y, k.getColor()) != 0)
            return true;
        return false;
    }
    
    boolean noEmpty(){
        boolean flag=true;
        for(int i=0; i<NxN; i++){
            if(stones[i].getColor()==EMPTY){
                flag=false;
                break;
            }
        }
        return flag;
    }
    
    //
    // x 行 y 列に黒または白（ b_w ）のコマを置いた場合，反転できるコマを探す
    //
    int checkFlip(int x, int y, int c) {
        int d[][] = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
        nKoma[8] = 0;
        if (stones[getz(x, y)].getColor() == EMPTY) {
            for (int i1 = 0; i1 < 8; i1++) {
                int m1 = x, m2 = y;
                nKoma[i1] = 0;
                int s = 0;   // 0:開始，1:カウント，2:カウント終了，3:反転不可能
                int ct = 0;
                while (s < 2) {
                    m1 += d[i1][0];
                    m2 += d[i1][1];
                    if (m1 >= 0 && m1 < WIDTH && m2 >= 0 && m2 < WIDTH) {
                        if (stones[getz(m1, m2)].getColor() == EMPTY) {
                            s = 3;
                        } else if (stones[getz(m1, m2)].getColor() == c) {
                            if (s == 1) {
                                s = 2;
                            } else {
                                s = 3;
                            }
                        } else {
                            s = 1;
                            ct++;
                        }
                    } else {
                        s = 3;
                    }
                }
                if (s == 2) {
                    nKoma[8] += ct;
                    nKoma[i1] = ct;
                }
            }
        }
        return nKoma[8];
    }

    // x 行 y 列に黒または白（ b_w ）のコマを置いた場合におけるコマの反転
    //
    List<Integer> flip(int x, int y, int c) {
        List<Integer> flipped = new ArrayList<Integer>();
        int d[][] = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
        for (int i1 = 0; i1 < 8; i1++) {
            int m1 = x, m2 = y;
            for (int i2 = 0; i2 < nKoma[i1]; i2++) {
                m1 += d[i1][0];
                m2 += d[i1][1];
                stones[getz(m1, m2)].setColor(c);
                flipped.add(getz(m1, m2));
            }
        }
        stones[getz(x, y)].setColor(c);
        flipped.add(getz(x, y));
        return flipped;
    }

    boolean passP(int turn) {
        // パスのチェック
        boolean sw = false;
        for (int i1 = 0; i1 < WIDTH && !sw; i1++) {
            for (int i2 = 0; i2 < WIDTH && !sw; i2++) {
                if (checkFlip(i1, i2, turn) != 0) {
                    sw = true;
                }
            }
        }
        return !sw;
    }
    
    boolean passP() {
        // パスのチェック
        boolean sw = false;
        for (int i1 = 0; i1 < WIDTH && !sw; i1++) {
            for (int i2 = 0; i2 < WIDTH && !sw; i2++) {
                if (checkFlip(i1, i2, game.getCurrPlayer().getColor()) != 0) {
                    sw = true;
                }
            }
        }
        return !sw;
    }
    
    int countBW(int[] bw) {
        // コマの数を数える
        int b = 0, w = 0, total = 0;
        for (int i1 = 0; i1 < WIDTH; i1++) {
            for (int i2 = 0; i2 < WIDTH; i2++) {
                int c = stones[getz(i1, i2)].getColor();
                if (c != EMPTY) {
                    total++;
                    if (c == BLACK) {
                        b++;
                    } else {
                        w++;
                    }
                }
            }
        }
        bw[0] = b;
        bw[1] = w;
        return total;
    }

    int whoWin() {
        // コマの数を数える
        int[] bw = new int[2];
        int total = countBW(bw);
        int b = bw[0];
        int w = bw[1];
        // 勝敗決定
        if (total == NxN) {
            //System.out.println("黒 " + b + " 個， 白 " + w + " 個");
            if (b > w) {
                return BLACK;
                //System.out.println("黒の勝ちです．");
            } else if (b == w) {
                return DRAW;
                //System.out.println("引き分けです．\n");
            } else {
                return WHITE;
                //System.out.println("白の勝ちです．\n");
            }
        } else {            
            if(passP()){
                System.out.println("\n" + game.getCurrPlayer().getName() + "、PASSですね．");
                return PASS;
            }
        }
        return NEXT;
    }

    int getz(int x, int y) {
        return y * WIDTH + x;	// 0<= x <WIDTH, 0<= y <WIDTH
    }
    
    boolean isWin(){
        int ww = whoWin();
        if(ww!=NEXT)
            if(ww!=PASS)return true;
        return false;
    }
    
    boolean isDraw(){
        if(whoWin()==DRAW)return true;
        return false;
    }
    
    float evaluate(Player player, int turn){
        int[] bw={0,0};
        int total=countBW(bw);
        return turn * (bw[0] - bw[1]);        
    }

}
