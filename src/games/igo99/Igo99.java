package igo99;

import static igo99.Constants.*;

public class Igo99 {

    //public Igo99(String[] args) {
    public static void main(String[] args) {
        boolean reverse = false;
        if (args.length > 0) {
            if (args[0].equals("-r")) {
                reverse = true;
            }
            //int bsize=Integer.parseInt(args[1]);
            //if(bsize>0)
            //    Constants.B_SIZE = bsize;
            //int nmoku=Integer.parseInt(args[2]);
            //if(nmoku>0)
            //    Constants.NMOKU = nmoku;
        }
        game = new Game();
        screen = new Screen();
        client = new Client();
        game.start();
        server = new Server();
    }
    
}
