package tttminimax;

import static tttminimax.Constants.*;

public class TicTacToe {

    //public TicTacToe(String[] args) {
    public static void main(String[] args) {
        boolean reverse = false;
        if (args.length > 0) {
            if (args[0].equals("-r")) {
                reverse = true;
            }
            //int bsize=Integer.parseInt(args[1]);
            //if(bsize>0)
            //    Constants.NXN = bsize;
            //int nmoku = Integer.parseInt(args[2]);
            //if (nmoku > 0) {
            //    Constants.NXN = nmoku;
            //}
            //System.out.println("nmoku="+nmoku);
        }
        //System.out.println("NXN="+Constants.NXN);
        game = new Game(reverse);
        client = new Client();
        screen = new Screen();
        game.start();
        server = new Server();
    }

}
