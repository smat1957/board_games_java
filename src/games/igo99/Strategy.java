package igo99;

import static igo99.Constants.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import java.util.Random;

public class Strategy {

    int select_best_move(int color, Board board, Player player, int fill_eye_err) {
        int try_num = 30; // playoutを繰り返す回数
        int best_z = 0;
        double best_value = -100;

        int[] board_copy = new int[BOARD_MAX];	// 現局面を保存
        board_copy = Arrays.copyOf(board.getBoard(), board.getBoard().length);
        int ko_z_copy = ko_z;

        // すべての空点を着手候補に
        for (int y = 0; y < B_SIZE; y++) {
            for (int x = 0; x < B_SIZE; x++) {
                int z = get_z(x, y);
                if (board.getBoard(z) != 0) {
                    continue;
                }

                int err = player.move(board, z, color, FILL_EYE_ERR);	// 打ってみる
                if (err != 0) {
                    continue;	// エラー
                }
                int win_sum = 0;
                for (int i = 0; i < try_num; i++) {
                    int[] board_copy2 = new int[BOARD_MAX];
                    board_copy2 = Arrays.copyOf(board.getBoard(), board.getBoard().length);
                    int ko_z_copy2 = ko_z;

                    int win = -playout(flip_color(color), board, player, fill_eye_err);
                    win_sum += win;
                    //			print_board();
                    //			printf("win=%d,%d\n",win,win_sum);

                    board.setBoard(board_copy2);
                    ko_z = ko_z_copy2;
                }
                double win_rate = (double) win_sum / try_num;
                //		print_board();
                //		printf("z=%d,win=%5.3f\n",get81(z),win_rate);

                if (win_rate > best_value) {
                    best_value = win_rate;
                    best_z = z;
                    System.out.printf("best_z=%d,v=%5.3f,try_num=%d\n", get81(best_z), best_value, try_num);
                }
                board.setBoard(board_copy);
                ko_z = ko_z_copy;
            }
        }

        return best_z;
    }

    int playout(int turn_color, Board board, Player player, int fill_eye_err) {
        all_playouts++;
        int color = turn_color;
        int before_z = 0;	// 1手前の手
        int loop_max = B_SIZE * B_SIZE + 200;	// 最大でも300手程度まで。3コウ対策
        for (int loop = 0; loop < loop_max; loop++) {
            // すべての空点を着手候補にする
            int[] kouho = new int[BOARD_MAX];
            int kouho_num = 0;
            for (int y = 0; y < B_SIZE; y++) {
                for (int x = 0; x < B_SIZE; x++) {
                    int z = get_z(x, y);
                    if (board.getBoard(z) != 0) {
                        continue;
                    }
                    kouho[kouho_num] = z;
                    kouho_num++;
                }
            }
            int z, r = 0;
            Random rnd = new Random();

            for (;;) {
                if (kouho_num == 0) {
                    z = 0;
                } else {
                    r = rnd.nextInt(RAND_MAX) % kouho_num;		// 乱数で1手選ぶ
                    z = kouho[r];
                }
                int err = player.move(board, z, color, FILL_EYE_ERR);
                if (err == 0) {
                    break;
                }
                kouho[r] = kouho[kouho_num - 1];	// エラーなので削除
                kouho_num--;
            }
            if (z == 0 && before_z == 0) {
                break;	// 連続パス
            }
            before_z = z;
            //		print_board();
            //		printf("loop=%d,z=%d,c=%d,kouho_num=%d,ko_z=%d\n",loop,get81(z),color,kouho_num,get81(ko_z));
            color = flip_color(color);
        }
        return board.count_score(turn_color);
    }

    int select_best_uct(int color, Board board, Player player, int fill_eye_err) {
        node_num = 0;
        int next = create_node(board);

        for (int i = 0; i < uct_loop; i++) {
            int[] board_copy = new int[BOARD_MAX];                // 局面を保存
            board_copy = Arrays.copyOf(board.getBoard(), board.getBoard().length);
            int ko_z_copy = ko_z;

            search_uct(color, next, board, player, fill_eye_err);

            board.setBoard(board_copy);
            ko_z = ko_z_copy;
        }
        int best_i = -1;
        int max = -999;
        Node pN = node[next];
        for (int i = 0; i < pN.child_num; i++) {
            Child c = pN.child[i];
            if (c.getGames() > max) {
                best_i = i;
                max = c.getGames();
            }
            //  printf("%3d:z=%2d,games=%5d,rate=%.4f\n",i,get81(c->z),c->games,c->rate);
        }
        int ret_z = pN.child[best_i].getZ();
        System.out.printf("z=%2d,rate=%.4f,games=%d,playouts=%d,nodes=%d\n", get81(ret_z), pN.child[best_i].getRate(), max, all_playouts, node_num);
        return ret_z;
    }

    void add_child(Node pN, int z) {
        int n = pN.child_num;
        pN.child[n].setZ(z);
        pN.child[n].setGames(0);
        pN.child[n].setRate(0.);
        pN.child[n].setNext(NODE_EMPTY);
        pN.child_num++;
    }

    // ノードを作成する。作成したノード番号を返す
    int create_node(Board board) {
        if (node_num == NODE_MAX) {
            System.out.printf("node over Err\n");
            System.exit(0);
        }
        Node pN = node[node_num];
        pN.child_num = 0;
        pN.child_games_sum = 0;
        for (int y = 0; y < B_SIZE; y++) {
            for (int x = 0; x < B_SIZE; x++) {
                int z = get_z(x, y);
                if (board.getBoard(z) != 0) {
                    continue;
                }
                add_child(pN, z);
            }
        }
        add_child(pN, 0);  // PASSも追加

        node[node_num] = pN;

        node_num++;
        return node_num - 1;
    }

    // UCBが一番高い手を選ぶ
    int select_best_ucb(int node_n) {
        Node pN = node[node_n];
        int select = -1;
        double max_ucb = -999;
        Random rnd = new Random();
        for (int i = 0; i < pN.child_num; i++) {
            Child c = pN.child[i];
            if (c.getZ() == ILLEGAL_Z) {
                continue;
            }
            double ucb = 0;
            if (c.getGames() == 0) {
                ucb = 10000 + (rnd.nextInt(RAND_MAX) & 0x7fff);  // 未展開
            } else {
                final double C = 0.31;
                ucb = c.getRate() + C * Math.sqrt(Math.log(pN.child_games_sum) / c.getGames());
            }
            if (ucb > max_ucb) {
                max_ucb = ucb;
                select = i;
            }
        }
        if (select == -1) {
            System.out.printf("Err! select\n");
            System.exit(0);
        }
        return select;
    }

    int search_uct(int color, int node_n, Board board, Player player, int fill_eye_err) {
        Node pN = node[node_n];
        Child c = null;
        int select;
        for (;;) {
            select = select_best_ucb(node_n);
            c = pN.child[select];
            int z = c.getZ();
            int err = player.move(board, z, color, FILL_EYE_ERR);	// 打ってみる
            if (err == 0) {
                break;
            }
            c.setZ(ILLEGAL_Z);     // 別の手を選ぶ
            pN.child[select].setZ(ILLEGAL_Z);
        }

        int win;
        if (c.getGames() <= 0) {  // 最初の1回目はplayout. <= 10 でノード数を減らせる
            win = -playout(flip_color(color), board, player, fill_eye_err);
        } else {
            if (c.getNext() == NODE_EMPTY) {
                c.setNext( create_node(board) );
            }
            win = -search_uct(flip_color(color), c.getNext(), board, player, fill_eye_err);
        }

        // 勝率を更新
        c.setRate( (c.getRate() * c.getGames() + win) / (c.getGames() + 1) );
        c.setGames( c.getGames()+1 );		      // この手の回数を更新
        pN.child_games_sum++;  // 合計回数も更新

        pN.child[select] = c;
        node[node_n] = pN;
        return win;
    }

    int prompt(String name) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int num = -1;
        String fmt = null, instr = null;
        do {
            fmt = String.format("\n石を置く場所 xy を指定（ %s の番です ）：", name);
            System.out.print(fmt);
            try {
                instr = br.readLine();
                char cx = instr.trim().charAt(0);
                int nx;
                if (numP(cx)) {
                    nx = Integer.parseInt(String.valueOf(cx));
                } else {
                    nx = fromA(cx) + 10;
                }
                char cy = instr.trim().charAt(1);
                int ny;
                if (numP(cy)) {
                    ny = Integer.parseInt(String.valueOf(cy));
                } else {
                    ny = fromA(cy) + 10;
                }
                num = ny * WIDTH + nx;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!(0 <= num && num < WIDTH * WIDTH)) {
                System.out.println("やり直し！ 場所を xy で指定して ）");
                continue;
            }
            break;
        } while (true);
        return num;
    }

    private static boolean numP(char x) {
        if (('0' <= x) && (x <= '9')) {
            return true;
        }
        return false;
    }

    private static int fromA(char x) {
        return (int) (x - 'a');
    }
    
    private int serialNum(char cx) {
        int nx;
        if (numP(cx)) {
            nx = Integer.parseInt(String.valueOf(cx));
        } else {
            nx = fromA(cx) + 10;
        }
        return nx;
    }

    int network(String coord){
        return gui(coord);
    }

    int gui(String coord){
        if(coord==null)return -1;
        char cx = coord.charAt(0);
        char cy = coord.charAt(1);
        int nx = serialNum(cx);
        int ny = serialNum(cy);
        //int x = coord.charAt(0) - 'a';
        //int y = coord.charAt(1) - '1';
        /*
        System.out.println("cood="+coord);
        System.out.println("cx="+cx);
        System.out.println("cy="+cy);
        System.out.println("nx="+nx);
        System.out.println("ny="+ny);
        System.out.println("z="+get_z(nx,ny));
        */
        return get_z(nx, ny);
    }
}
