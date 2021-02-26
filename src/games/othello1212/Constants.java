package othello1212;

public final class Constants {
    private Constants(){}
    
    static public int GWIDTH = 600;    // 画面上の大きさ
    static public int GHEIGHT = GWIDTH;
    
    static public int WIDTH = 12;    // 盤の大きさ
    final static int NxN = WIDTH * WIDTH;

    final static public int WHITE = 1; //黒が1で白が2
    final static public int BLACK = -1; //黒が1で白が2
    final static public int MAX = WHITE; //黒が1で白が2
    final static public int MIN = BLACK; //黒が1で白が2
    final static public int EMPTY = 0;

    final static public int NEXT = 10;
    final static public int DRAW = 11;
    final static public int PASS = 12;

    final static public int RANDOM = 20;
    final static public int PROMPT = 21;
    final static public int MINIMAX = 22;
    final static public int ALPHABETA = 23;
    final static public int MONTECARLO = 24;
    final static public int COMPUTER = 25;
    final static public int GUI = 26;
    final static public int NETWORK = 27;

    static Game game=null;
    static Screen screen=null;
    static Server server=null;
    static Client client=null;
}
