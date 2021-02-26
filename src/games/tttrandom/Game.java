package tttrandom;

import static tttrandom.Constants.*;

public class Game {

    private static Player currPlayer;
    private static Player[] player = null;
    private static Board board = null;
    private static int current_player = 0;

    public Game(boolean reverse) {
        player = new Player[2];
        if (reverse) {
            player[0] = new Player(MARU, "PC", RANDOM);
            player[1] = new Player(BATSU, "You", GUI);
        } else {
            player[0] = new Player(BATSU, "You", GUI);
            player[1] = new Player(MARU, "PC", RANDOM);
        }
        board = Board.getBoard();
    }

    void start(){
        resetGame();
        int winner = NEXT;
        do {
            board.print();
            currPlayer = player[current_player];
            if( currPlayer.getHuman() ){
                screen.frame.setTitle("あなたの番です");
            }else{
                screen.frame.setTitle("PC思考中。。Wait。。");
            }            
            currPlayer.putStone(board);
            winner = board.check();
            current_player = ++current_player % 2;
        } while (winner == NEXT);
        board.print();
        screen.winalt.popUp(this, winner);
    }
    
    void Start() {
        resetGame();
        int winner = NEXT;
        System.out.println("スタート！ [Tic Tac Toe]");
        do {
            board.print();
            Player currentP = player[current_player];
            currentP.putStone(board);
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
    }

    int nextTurn() {
        current_player++;
        current_player %= 2;
        return current_player;
    }

    void setTurn(int n) {
        current_player = n;
    }

    int getTurn() {
        return current_player;
    }

    void resetGame() {
        board.clearBoard();
        current_player = 0;
    }
    
    void reStartGame(){
        start();
    }
    
    Board getBoard() {
        return board;
    }
    
    Player getPlayer(){
        return currPlayer;
    }
}
