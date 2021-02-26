package othello88;

import othello88.Screen;
import othello88.Client;
import othello88.Game;
import othello88.Server;
import static othello88.Constants.*;

public class Othello {

    //public Othello(String[] args) {
    public static void main(String[] args) {
        boolean reverse = false;
        if (args.length > 0) {
            if (args[0].equals("-r")) {
                reverse = true;
            }
            //int bsize=Integer.parseInt(args[1]);
            //if(bsize>0)
            //    Constants.WIDTH = bsize;
            //int nmoku=Integer.parseInt(args[2]);
            //if(nmoku>0)
            //    Constants.NMOKU = nmoku;
        }
        game = new Game(reverse);
        screen = new Screen();
        client = new Client();
        game.start();
        server = new Server();
    }

}
