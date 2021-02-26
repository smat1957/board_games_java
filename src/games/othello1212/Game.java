package othello1212;

import static othello1212.Constants.*;

public class Game {

    private static Player[] player = null;
    private static Board board = null;
    private static Player currPlayer = null;
    private static int current_player = 1;

    public Game(boolean reverse) {
        player = new Player[2];
        if (reverse) {
            player[0] = new Player(WHITE, "コンピュータ", ALPHABETA);
            player[1] = new Player(BLACK, "あなた", GUI);
        } else {
            player[0] = new Player(BLACK, "あなた", GUI);
            player[1] = new Player(WHITE, "コンピュータ", ALPHABETA);
        }
        currPlayer = player[0];
        board = Board.getBoard();
    }

    void start() {
        boolean passflag = false;
        int pre = 0, kyoku = 0;
        int winner = NEXT;
        System.out.println("スタート！ [Othello " + WIDTH + "x" + WIDTH + "]");
        do {
            kyoku++;
            board.print();
            current_player = ++current_player % 2;
            currPlayer = player[current_player];
            winner = board.whoWin();
            if (winner == NEXT) {
                currPlayer.putStone(board);
            }
            
            else if (!passflag) {
                pre = kyoku;
                passflag = true;
            } else {
                if (pre == kyoku - 1) {
                    break;
                } else {
                    passflag = false;
                }
            }
            
        } while (winner == NEXT || winner == PASS);
        //以下、対戦結果の表示
        int[] bw = new int[2];
        board.countBW(bw);
        if(bw[0]>bw[1])winner=BLACK;
        else if(bw[0]<bw[1])winner=WHITE;
        else winner=DRAW;
        String title="黒 " + bw[0] + " 個， 白 " + bw[1] + " 個";
        System.out.println("\n"+title);
        screen.frame.setTitle(title);
        result(winner);
    }

    private void result(int winner) {
        System.out.println("");
        switch (winner) {
            case DRAW:
                System.out.print("引き分け\t");
                break;
            case WHITE:
                System.out.print("'白' の勝ち\t");
                break;
            case BLACK:
                System.out.print("'黒' の勝ち\t");
                break;
        }
        System.out.println("またね！");
        screen.repaint();
        screen.winalt.popUp(this, winner);
    }
    
    void restartGame(){
        resetGame();
        screen.frame.setTitle("高岡工芸高校コンピュータ研究部");
        start();        
    }
    
    void resetGame(){
        board.initBoard();
        current_player = 1;        
    }
    
    Player getPlayer(){
        return currPlayer;
    }
    
    Board getBoard(){
        return board;
    }
        
    void setTurn(int n) {
        current_player = n;
    }
    
    protected Player getCurrPlayer(){
        return currPlayer;
    }
}
