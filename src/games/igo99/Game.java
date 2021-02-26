package igo99;

import static igo99.Constants.*;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Board board = null;
    private Player[] player = new Player[2];
    private Player currPlayer = null;

    public Game() {
        board = new Board();
        player[0] = new Player(BLACK, "あなた", GUI);
        player[1] = new Player(WHITE, "コンピュータ", UCT);
        currPlayer = player[0];
    }

    void start() {
        List<Integer> kifu = new ArrayList<Integer>();
        int color = 1, tesuu;
        board.print();
        while (true) {
            long start = System.currentTimeMillis();
            //if (currPlayer.getSenryaku() == UCT)
            //    uct_loop = all_playouts/3+1;	// playout数を単純モンテカルロに合わせる
            all_playouts = 0;	// playout回数を初期化
            currPlayer = player[color - 1];
            int err = currPlayer.move(board, FILL_EYE_OK);
            /*
            if (currPlayer.getSenryaku() == PROMPT) {
                switch (err) {
                    case 1:
                        System.out.println("自殺手");
                        break;
                    case 2:
                        System.out.println("コウ");
                        break;
                    case 3:
                        System.out.println("眼(ルール違反ではない)");
                        break;
                    case 4:
                        System.out.println("既に石がある");
                        break;
                    default:
                }
            }
            */
            int z = currPlayer.getRecent();
            kifu.add(z);
            tesuu = kifu.size();
            board.print();
            System.out.printf("play_z = %d,手数=%d,色=%d,all_playouts=%d\n", get81(z), tesuu, color, all_playouts);
            long end = System.currentTimeMillis();
            double t = (end - start) / 1000.0;
            System.out.printf("%.1f 秒, %.0f playout/秒\n", t, all_playouts / t);
            if ((z == 0) && (tesuu > 1) && (kifu.get(tesuu - 2) == 0)) {
                break;
            }
            if (tesuu > 300) {
                break;  // 3コウでのループ対策
            }
            color = flip_color(color);
        }
        /*
	// 自己対戦のテスト用
	int score = board.count_score(1);
	score_sum += score;
	loop_count++;
	FILE *fp = fopen("out.txt","a+");
	fprintf(fp,"Last Score=%d,%d,%d,tesuu=%d\n",score,score_sum,loop_count,tesuu);
	fclose(fp);
	printf("Last Score=%d,%d,%d,tesuu=%d\n",score,score_sum,loop_count,tesuu); color = 1; tesuu = 0; goto loop;
        */
        // SGFで棋譜を
        System.out.printf("(;GM[1]SZ[%d]KM[%.1f]\r\n", B_SIZE, komi);
        for (int i = 0; i < tesuu; i++) {
            int z = kifu.get(i);
            int y = z / WIDTH;
            int x = z - y * WIDTH;
            final String[] sStone = {"B", "W"};
            System.out.printf(";%s", sStone[i & 1]);
            if (z == 0) {
                System.out.printf("[]");
            } else {
                System.out.printf("[%c%c]", x + 'a' - 1, y + 'a' - 1);
            }
            if (((i + 1) % 10) == 0) {
                System.out.printf("\r\n");
            }
        }
        System.out.printf("\r\n)\r\n");

    }
    
    Board getBoard(){
        return board;
    }
    
    Player getPlayer(){
        return currPlayer;
    }
    
    void restartGame(){
        board.bInit();
        currPlayer = player[0];
        Screen.frame.setTitle("富山県立高岡工芸高校コンピュータ研究部");
        //start();
    }
    
    void setTurn(int n){
        
    }
}
