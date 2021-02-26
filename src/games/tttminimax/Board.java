package tttminimax;

import static tttminimax.Constants.*;

public class Board {

    private static Stone[] stones = new Stone[NXN * NXN];
    private static Board board = new Board();
    
    private Board() {
        clearBoard();
    }
    
    void clearBoard(){
        for (int i = 0; i < NXN * NXN; i++) {
            stones[i] = new Stone(i, EMPTY);
        }        
    }
    
    static Board getBoard(){
        return board;
    }
    
    void setEmpty(int i){
        stones[i]=new Stone(i, EMPTY);
    }
    
    void setBoard(Stone s) {
        stones[s.getLocate()] = s;
    }
    
    void setStones(Stone s) {
        stones[s.getLocate()] = s;
    }
    
    Stone getStones(int i){
        return stones[i];
    }
    
    boolean canPut(Stone s) {
        if (stones[s.getLocate()].getColor() != EMPTY) {
            return false;
        }
        return true;
    }
    
    public void print(){
        if(screen!=null)
            screen.repaint();
    }
    
    void Print() {
        char[] bd0 = new char[NXN * NXN];
        char[] bd1 = new char[NXN * NXN];
        for (int i = 0; i < NXN * NXN; i++) {
            if (stones[i].getColor() == MARU) {
                bd1[i] = ' ';
                bd0[i] = 'O';
            } else if (stones[i].getColor() == BATSU) {
                bd1[i] = ' ';
                bd0[i] = 'X';
            } else {
                if (i < 10) {
                    bd1[i] = '0';
                    //bd0[i] = (char) ('0' + i);
                } else if (i < 20) {
                    bd1[i] = '1';
                    //bd0[i] = (char) ('A' - 10 + i);
                } else if (i < 30) {
                    bd1[i] = '2';
                    //bd0[i] = (char) ('A' - 10 + i);                    
                }
                bd0[i] = (char) ('0' + i % 10);
            }
        }
        String fmt = "\n/";
        for (int i = 0; i < NXN - 1; i++) {
            fmt += "----|";
        }
        fmt += "----\\";
        System.out.println(fmt);
        //System.out.println("\n/---|---|---\\");
        for (int i = 0; i < NXN; i++) {
            System.out.print("|");
            for (int j = 0; j < NXN; j++) {
                String f = String.format(" %c%c |", bd1[i * NXN + j], bd0[i * NXN + j]);
                System.out.print(f);
            }
            System.out.println();
            if (i < (NXN - 1)) {
                fmt = "|";
                for (int j = 0; j < NXN; j++) {
                    fmt += "----|";
                }
                System.out.println(fmt);
            }
        }
        /*
        fmt = String.format("| %c | %c | %c |", bd[0], bd[1], bd[2]);
        System.out.println(fmt);
        System.out.println("|---|---|---|");
        fmt = String.format("| %c | %c | %c |", bd[3], bd[4], bd[5]);
        System.out.println(fmt);
        System.out.println("|---|---|---|");
        fmt = String.format("| %c | %c | %c |", bd[6], bd[7], bd[8]);
        System.out.println(fmt);
         */
        fmt = "\\";
        for (int i = 0; i < NXN - 1; i++) {
            fmt += "----|";
        }
        fmt += "----/";
        System.out.println(fmt);
        //System.out.println("\\---|---|---/");
    }

    private int lineSum(int n1, int n2, int n3) {
        return stones[n1].getColor() + stones[n2].getColor() + stones[n3].getColor();
    }

    private int lineSum(int[] idx) {
        int sum = 0;
        for (int i = 0; i < NXN; i++) {
            sum += stones[idx[i]].getColor();
        }
        return sum;
    }

    int check() {
        int i, l = 0;
        for (i = 0; i < (NXN + NXN + 2); i++) {
            int[] idx = new int[NXN];
            if (i < NXN) {
                for (int j = 0; j < NXN; j++) {
                    idx[j] = i * NXN + j;
                }
            } else if (i < NXN * 2) {
                for (int j = 0; j < NXN; j++) {
                    idx[j] = (i - NXN) + j * NXN;
                }
            } else {
                if (i == (NXN + NXN)) {
                    for (int j = 0; j < NXN; j++) {
                        if (j == 0) {
                            idx[j] = 0;
                        } else {
                            idx[j] = idx[j - 1] + NXN + 1;
                        }
                    }
                } else {
                    for (int j = 0; j < NXN; j++) {
                        if (j == 0) {
                            idx[j] = NXN - 1;
                        } else {
                            idx[j] = idx[j - 1] + NXN - 1;
                        }
                    }
                }
            }
            l = lineSum(idx);
            if (l == NXN * MARU) {
                return MARU;
            } else if (l == NXN * BATSU) {
                return BATSU;
            }
        }
        for (i = 0; i < NXN * NXN; i++) {
            if (stones[i].getColor() == EMPTY) {
                return NEXT;
            }
        }
        return DRAW;
    }
    
    private boolean line(int n1, int n2, int n3){
        return (( stones[n1].getColor()!=EMPTY ) &&
                ( stones[n1].getColor()==stones[n2].getColor() ) &&
                ( stones[n1].getColor()==stones[n3].getColor() ));
    }
    
    boolean isWin(){
        return (line(0,1,2)||
                line(3,4,5)||
                line(6,7,8)||
                line(0,3,6)||
                line(1,4,7)||
                line(2,5,8)||
                line(0,4,8)||
                line(2,4,6));
    }
    
    boolean isDraw(){
        int nEmpty=0;
        for(int i=0; i<NXN*NXN; i++){
            if(stones[i].getColor()==EMPTY)nEmpty++;
        }
        if(!isWin() && nEmpty==0)return true;
        return false;
    }

    float evaluate(Player p, int turn){
        if(isWin()){
            if(turn==p.getColor())return (float)(-1.0);
            else if(turn!=p.getColor())return (float)(1.0);
        }
        return (float)0.0;
    }    
}
