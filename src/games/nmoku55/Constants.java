package nmoku55;

public final class Constants {
    
    private Constants() {}
    
    static int GWIDTH = 640;
    final static int GHEIGHT = GWIDTH;
    final static int TTLBAR = 12;

    static int WIDTH = 10;
    final static int HEIGHT = WIDTH;
    final static int Nx = WIDTH;
    final static int Ny = Nx;
    static int NMOKU = 5;
    final static int NxN = NMOKU * NMOKU;
    final static int WHITE = 1;
    final static int BLACK = -1;
    final static int MARU = WHITE;
    final static int BATSU = BLACK;
    final static int MAX = WHITE;
    final static int MIN = BLACK;
    final static int EMPTY = 0;

    final static int NEXT = 200;
    final static int DRAW = 100;
    final static int PROMPT = 1000;
    final static int RANDOM = 1010;
    final static int MINIMAX = 1001;
    final static int ALPHABETA = 1002;
    final static int MONTECARLO = 1003;
    final static int GUI = 1004;
    final static int NETWORK = 1005;

    static Game game=null;
    static Screen screen=null;
    static Server server=null;
    static Client client=null;

}
