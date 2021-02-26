package igo99;

import static igo99.Constants.*;

public class Player extends Strategy {

    private int color = 0;
    private String name = null;
    private int senryaku = 0;
    private int recentTe = 0;
    private String coord = null;

    public Player(int color, String name, int senryaku) {
        this.color = color;
        this.name = name;
        this.senryaku = senryaku;
        if (senryaku == UCT) {
            for (int i = 0; i < NODE_MAX; i++) {
                node[i] = new Node();
            }
        }
        coord = null;
    }

    int getSenryaku() {
        return senryaku;
    }

    int getRecent() {
        return recentTe;
    }

    int move(Board board, int fill_eye_err) {
        recentTe = 0;
        int errCode = 0;
        do{
            if ((senryaku == NETWORK) || (senryaku == GUI)) {
                while (getCoord() == null) {
                    //System.out.println("どこに石を置きます？");
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            int tz = te(board, fill_eye_err);
            //if (!(0 <= tz && tz < B_SIZE * B_SIZE)) {
            if (!(0 <= tz && tz <= (B_SIZE * WIDTH + B_SIZE))) {
                coord = null;
                //System.out.println("te="+te);
                continue;
            }
            errCode = move(board, tz, color, fill_eye_err);
            //System.out.println("errCode="+errCode);
            String msg;
            switch (errCode) {
                    case 1:
                        System.out.println("自殺手");
                        msg = "自殺手";
                        break;
                    case 2:
                        System.out.println("コウ");
                        msg = "コウ";
                        break;
                    case 3:
                        System.out.println("眼(ルール違反ではない)");
                        msg = "眼(ルール違反ではない)";
                        break;
                    case 4:
                        System.out.println("既に石がある");
                        msg = "既に石がある";
                        break;
                    case 0:
                    default:
                        msg = "富山県立高岡工芸高校コンピュータ研究部";
                        break;
            }
            Screen.frame.setTitle(msg);
            if(errCode==0)break;
        } while(true);
        
        return errCode;
    }

    // 石を置く。エラーの時は0以外が返る。playoutではfill_eye_err = 1
    int move(Board board, int tz, int color, int fill_eye_err) {
        if (tz == 0) {
            ko_z = 0;
            return 0;
        }	// パスの場合

        int[][] around = new int[4][3];	// 4方向のダメ数、石数、色
        int un_col = flip_color(color);	// 相手の石の色

        // 4方向の石のダメと石数を調べる
        int space = 0;			// 4方向の空白の数
        int kabe = 0;			// 4方向の盤外の数
        int mikata_safe = 0;	// ダメ2以上で安全な味方の数
        int take_sum = 0;		// 取れる石の合計
        int ko_kamo = 0;		// コウになるかもしれない場所
        for (int i = 0; i < 4; i++) {
            around[i][0] = 0;
            around[i][1] = 0;
            around[i][2] = 0;
            int z = tz + DIR4[i];
            int c = board.getBoard(z);	// 石の色
            if (c == 0) {
                space++;
            }
            if (c == 3) {
                kabe++;
            }
            if (c == 0 || c == 3) {
                continue;
            }
            int dame = 0;	// ダメの数
            int ishi = 0;	// 石の数
            int[] p = new int[2];
            p[0] = dame;
            p[1] = ishi;
            //DameIshi di=new DameIshi();
            board.count_dame(z, p);
            dame = p[0];
            ishi = p[1];
            around[i][0] = dame;
            around[i][1] = ishi;
            around[i][2] = c;
            if (c == un_col && dame == 1) {
                take_sum += ishi;
                ko_kamo = z;
            }
            if (c == color && dame >= 2) {
                mikata_safe++;
            }
        }

        if (take_sum == 0 && space == 0 && mikata_safe == 0) {
            return 1; // 自殺手
        }
        if (tz == ko_z) {
            return 2; // コウ
        }
        if (((kabe + mikata_safe) == 4) && (fill_eye_err != 0)) {
            return 3; // 眼(ルール違反ではない)
        }
        if (board.getBoard(tz) != 0) {
            return 4; // 既に石がある
        }
        for (int i = 0; i < 4; i++) {
            int d = around[i][0];
            int n = around[i][1];
            int c = around[i][2];
            if (c == un_col && d == 1 && (board.getBoard(tz + DIR4[i]) != 0)) {	// 石が取れる
                board.kesu(tz + DIR4[i], un_col);
                hama[color - 1] += n;
            }
        }
        recentTe = tz;
        board.setBoard(tz, color);	// 石を置く

        int dame = 0, ishi = 0;
        int[] p = new int[2];
        p[0] = dame;
        p[1] = ishi;
        board.count_dame(tz, p);
        dame = p[0];
        ishi = p[1];
        if (take_sum == 1 && ishi == 1 && dame == 1) {
            ko_z = ko_kamo;	// コウになる
        } else {
            ko_z = 0;
        }
        return 0;
    }

    int te(Board board, int fill_eye_err) {
        int z = 0;
        switch (senryaku) {
            case MONTECARLO:
                z = select_best_move(color, board, this, fill_eye_err);	// 原始モンテカルロ
                break;
            case UCT:
                z = select_best_uct(color, board, this, fill_eye_err);
                break;
            case PROMPT:
                z = prompt(name);
                break;
            case GUI:
                z = gui(coord);
                coord = null;
                break;
            case NETWORK:
                z = network(coord);
                coord = null;
                break;
            default:
        }
        return z;
    }
    
    void setCoord(String ss){
        coord = ss;
    }
    
    String getCoord(){
        return coord;
    }
    
    String toStringCoord(int xy) {
        int y = xy / Ny;
        int x = xy % Nx;
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
