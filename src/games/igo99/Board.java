package igo99;

import static igo99.Constants.*;

public class Board {

    private static int[] board = {
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
        3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
        3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
        3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
        3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
        3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
        3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
        3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
        3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
        3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3
    };
    static int[] check_board = new int[BOARD_MAX];	// 検索済みフラグ用

    public Board() {
        bInit();
    }

    int[] getBoard() {
        return board;
    }

    int getBoard(int n) {
        return board[n];
    }
    
    int getStoneColor(int n){
        return getBoard(n);
    }
    
    void setBoard(int[] bd) {
        for (int i = 0; i < bd.length; i++) {
            this.board[i] = bd[i];
        }
    }

    void setBoard(int n, int color) {
        board[n] = color;
    }

    void bInit() {
        // 盤面初期化
        for (int i = 0; i < BOARD_MAX; i++) {
            board[i] = 3;
        }
        for (int y = 0; y < B_SIZE; y++) {
            for (int x = 0; x < B_SIZE; x++) {
                board[get_z(x, y)] = 0;
            }
        }
    }

    void print() {
        final String[] str = {".", "●", "○"};

        System.out.printf("     ");
        for (int x = 0; x < B_SIZE; x++) {
            System.out.printf("%d ", x + 1);
        }
        System.out.printf("\n");
        for (int y = 0; y < B_SIZE; y++) {
            System.out.printf("%2d: ", y + 1);
            for (int x = 0; x < B_SIZE; x++) {
                System.out.printf("%2s", str[board[get_z(x, y)]]);
            }
            System.out.printf("\n");
        }
    }

    // 石を消す
    void kesu(int tz, int color) {
        board[tz] = 0;
        for (int i = 0; i < 4; i++) {
            int z = tz + DIR4[i];
            if (board[z] == color) {
                kesu(z, color);
            }
        }
    }

    // ダメと石数を数える再帰関数
    // 4方向を調べて、空白だったら+1、自分の石なら再帰で。相手の石、壁ならそのまま。
    void count_dame_sub(int tz, int color, int[] p) {
        check_board[tz] = 1;     // この位置(石)は検索済み
        p[1]++;                 // 石の数
        for (int i = 0; i < 4; i++) {
            int z = tz + DIR4[i];      // 4方向を調べる
            if (check_board[z] != 0) {
                continue;
            }
            if (board[z] == 0) {
                check_board[z] = 1;  // この位置(空点)はカウント済みに
                p[0]++;             // ダメの数
            }
            if (board[z] == color) {
                count_dame_sub(z, color, p);  // 未探索の自分の石
            }
        }
    }

    // 位置 tz におけるダメの数と石の数を計算。
    void count_dame(int tz, int[] p) {
        p[0] = 0;
        p[1] = 0;
        for (int i = 0; i < BOARD_MAX; i++) {
            check_board[i] = 0;
        }
        count_dame_sub(tz, board[tz], p);
    }

    // 地を数えて勝ちか負けかを返す
    int count_score(int turn_color) {
        int score = 0;
        int[] kind = new int[3];  // 盤上に残ってる石数
        kind[0] = kind[1] = kind[2] = 0;
        for (int y = 0; y < B_SIZE; y++) {
            for (int x = 0; x < B_SIZE; x++) {
                int z = get_z(x, y);
                int c = getBoard(z);
                kind[c]++;
                if (c != 0) {
                    continue;
                }
                int[] mk = new int[4];	// 空点は4方向の石を種類別に数える
                mk[1] = mk[2] = 0;
                for (int i = 0; i < 4; i++) {
                    mk[getBoard(z + DIR4[i])]++;
                }
                if ((mk[1] != 0) && mk[2] == 0) {
                    score++; // 同色だけに囲まれていれば地
                }
                if ((mk[2] != 0) && mk[1] == 0) {
                    score--;
                }
            }
        }
        score += kind[1] - kind[2];

        double final_score = score - komi;
        int win = 0;
        if (final_score > 0) {
            win = 1;
        }
        //  printf("win=%d,score=%d\n",win,score);
        //  win = score;
        if (turn_color == 2) {
            win = -win;
        }
        return win;
    }
}
