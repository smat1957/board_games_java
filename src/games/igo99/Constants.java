package igo99;

public final class Constants {
    private Constants() {}
    
    final static double komi = 6.5;
    final static int B_SIZE = 9;		// 碁盤の大きさ
    final static int WIDTH = B_SIZE + 2;	// 枠を含めた横幅
    final static int BOARD_MAX = WIDTH * WIDTH;
    final static int BLACK = 1;
    final static int WHITE = 2;
    final static int PROMPT = 100;
    final static int MONTECARLO = 101;
    final static int NETWORK = 102;
    final static int UCT = 103;
    final static int GUI = 104;
    final static int FILL_EYE_ERR = 1;
    final static int FILL_EYE_OK = 0;
    final static int[] DIR4 = {+1, -1, +WIDTH, -WIDTH};	// 右、左、下、上への移動量
    final static int RAND_MAX = 0x7fffffff;

    final static int CHILD_MAX = B_SIZE * B_SIZE + 1;  // +1はPASS用
    final static int NODE_MAX = 10000;
    final static int NODE_EMPTY = -1; // 次のノードが存在しない場合
    static Node[] node = new Node[NODE_MAX];
    static int node_num = 0;          // 登録ノード数
    static int uct_loop = 1000;  // uctでplayoutを行う回数
    final static int ILLEGAL_Z = -1; // ルール違反の手
            
    static int[] hama = new int[2];
    static int ko_z;
    static int all_playouts = 0;

    final static int GWIDTH = 480;
    final static int GHEIGHT = GWIDTH;
    final static int Nx = B_SIZE;
    final static int Ny = Nx;
    final static int MARU = WHITE;
    final static int BATSU = BLACK;
    
    static Game game;
    static Screen screen;
    static Client client;
    static Server server;

    static int flip_color(int col) {
        return 3 - col;	// 石の色を反転させる
    }
    
    static int get_z(int x, int y) {
        return (y + 1) * WIDTH + (x + 1);	// 0<= x <=8, 0<= y <=8
    }
    
    static int get81(int z) {
        if (z == 0) {
            return 0;
        }
        int y = z / WIDTH;	 	// 座標をx*10+yに変換。表示用。
        int x = z - y * WIDTH;	// 106 = 9*11 + 7 = (x,y)=(7,9) -> 79
        return x * 10 + y;        // 19路ではx*100+y
    }

}
