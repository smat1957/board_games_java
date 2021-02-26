package nmoku55;

import static nmoku55.Constants.*;

class Game {

    private static Player[] player = null;
    private static Board board = null;
    private static int current_player = 0;
    private Player currPlayer = null;
    
    public Game(boolean reverse) {
        player = new Player[2];
        if (reverse) {
            player[0] = new Player(MARU, "コンピュータ", ALPHABETA);
            player[1] = new Player(BATSU, "あなた", GUI);
        } else {
            player[0] = new Player(BATSU, "あなた", GUI);
            player[1] = new Player(MARU, "コンピュータ", ALPHABETA);
        }
        currPlayer = player[0];
        board = Board.getBoard();
    }

    void start() {
        resetGame();
        int winner = NEXT;
        System.out.println("スタート！ [ " + NMOKU + "目並べ ]");
        do {
            screen.setRepaint(true);
            board.print();
            screen.setRepaint(false);
            currPlayer = player[current_player];
            currPlayer.putStone(board);
            winner = board.check();
            current_player = ++current_player % 2;
        } while (winner == NEXT);
        board.print();
        result(winner);
    }

    private void result(int winner) {
        System.out.println("");
        switch (winner) {
            case DRAW:
                System.out.print("引き分け\t");
                break;
            case MARU:
                System.out.print("'O' の勝ち\t");
                break;
            case BATSU:
                System.out.print("'X' の勝ち\t");
                break;
        }
        System.out.println("またね！");
        screen.winalt.popUp(winner);
    }

    void resetGame(){
        board.clearBoard();
        current_player = 0;        
    }
    
    void reStartGame() {
        resetGame();
        start();
    }

    Board getBoard(){
        return board;
    }
    
    Player getPlayer(){
        return currPlayer;
    }
    
    void setTurn(int n) {
        current_player = n;
    }

}
