package tttrandom;

import tttrandom.*;

public final class Constants {
    private Constants() {}
    static int NXN = 3;   //最大でも五目並べまで
    final static int WHITE = 10;
    final static int BLACK = -10;
    final static int MARU = WHITE;
    final static int BATSU = BLACK;
    final static int MAX = WHITE;
    final static int MIN = BLACK;
    final static int EMPTY = Integer.MIN_VALUE;

    static int WIDTH = 320;
    final static int HEIGHT = WIDTH;
    final static int TTLBAR = 82;
    final static int Nx = NXN;
    final static int Ny = NXN;
    
    final static int NEXT = 200;
    final static int DRAW = 100;

    final static int NETWORK = 1005;
    final static int PROMPT = 1000;
    final static int RANDOM = 1010;
    final static int MINIMAX = 1001;
    final static int ALPHABETA = 1002;
    final static int MONTECARLO = 1003;
    final static int GUI = 1004;

    static Game game=null;
    static Screen screen=null;
    static Server server=null;
    static Client client=null;
}
