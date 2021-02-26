package othello1414;

import static othello1414.Constants.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Strategy {
    private boolean numP(char x) {
        if (('0' <= x) && (x <= '9')) {
            return true;
        }
        return false;
    }

    private int fromA(char x) {
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
                int nx = serialNum(cx);
                char cy = instr.trim().charAt(1);
                int ny = serialNum(cy);
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
    /*
    int prompt(String name) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int num = -1;
        String fmt = null, instr = null;
        do {
            fmt = String.format("\n石を置く場所 xy を指定（ %s の番です ）：", name);
            System.out.print(fmt);
            try {
                instr = br.readLine();
                int nx = Integer.parseInt(instr.trim().substring(0, 1));
                int ny = Integer.parseInt(instr.trim().substring(1, 2));
                num = ny * WIDTH + nx;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!(0 <= num && num < NxN)) {
                System.out.println("やり直し！ 場所を xy で指定して ）");
                continue;
            }
            break;
        } while (true);
        return num;
    }
    */
    int random(String name) {
        //System.out.printf("。。。　%s 思考中　。。。", name);
        return (int) (Math.random() * NxN);
    }

    int computer(Board board, Player player) {
        int k[] = new int[2];
        int max = 0;
        for (int i1 = 0; i1 < WIDTH; i1++) {
            for (int i2 = 0; i2 < WIDTH; i2++) {
                int mx = board.checkFlip(i1, i2, player.getColor());
                if (mx > max) {
                    max = mx;
                    k[0] = i1;
                    k[1] = i2;
                }
            }
        }
        //return board.getz(k[0], k[1]);
        return k[1] * WIDTH + k[0];
    }
    
    int gui(String coord) {
        if(coord==null)return -1;
        char cx = coord.charAt(0);
        char cy = coord.charAt(1);
        int nx = serialNum(cx);
        int ny = serialNum(cy);
        //int x = coord.charAt(0) - 'a';
        //int y = coord.charAt(1) - '1';
        return ny * WIDTH + nx;
    }

    
    private float minimax(Board board, int depth, int turn, Player player) {
        
        float best, score;
        if (board.isWin() || board.isDraw() || board.passP(turn) || depth == 0) {
            score = board.evaluate(player, turn);
            return score;
        }
        
        if (turn == MAX) {
            best = (float) Integer.MIN_VALUE;
        } else {
            best = (float) Integer.MAX_VALUE;
        }        
        for (int i = 0; i < NxN; i++) {
            List<Integer> flipped = new ArrayList<Integer>();
            Stone s = new Stone(i, turn);
            if (board.canPut(s)) {
                flipped = board.setBoard(s);
                score = minimax(board, depth - 1, -turn, player);
                board.setAgain(flipped, -turn);
                if (turn == MAX) {
                    if (score > best) {
                        best = score;
                    } 
                } else {
                    if (score < best) {
                        best = score;
                    }
                }
            }
        }        
        return best;
    }

    int bestMoveMM(Board board, Player player) {
        float bestEval = (float) Integer.MIN_VALUE;
        int bestMove = -1;
        int turn = player.getColor();
        for (int i = 0; i < NxN; i++) {
            List<Integer> flipped = new ArrayList<Integer>();
            Stone s = new Stone(i, turn);
            if (board.canPut(s)) {
                flipped = board.setBoard(s);  //BLACKに打たせるときは、-player.getColor()の負号をとり、
                float eval = minimax(board, 8, -turn, player);
                board.setAgain(flipped, -turn);  //Board.evaluate で -1 と 1 を入れ替える
                if (eval > bestEval) {
                    bestEval = eval;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }
    
    private float alphabeta(Board board, int depth, int turn, Player player, float alpha, float beta) {
        if (board.isWin() || board.isDraw() || board.noEmpty() || board.passP(turn) || depth == 0) {
            float score = board.evaluate(player, turn);
            return score;
        }
                
        for (int i = 0; i < NxN; i++) {
            List<Integer> flipped = new ArrayList<Integer>();
            Stone s = new Stone(i, turn);
            if (board.canPut(s)) {
                flipped = board.setBoard(s);
                float score = alphabeta(board, depth - 1, -turn, player, alpha, beta);
                board.setAgain(flipped, -turn);
                if (turn == MAX) {
                    alpha = Float.max(score, alpha);
                    if( beta <= alpha )break;
                } else {
                    beta = Float.min(score, beta);
                    if( beta <= alpha )break;
                }
            }
        }
        if(turn==MAX)return alpha;
        return beta;
    }

    int bestMoveAB(Board board, Player player) {
        float bestEval = (float) Integer.MIN_VALUE;
        float alpha = (float) Integer.MIN_VALUE;
        float beta = (float) Integer.MAX_VALUE;
        int bestMove = -1;
        int turn = player.getColor();
        for (int i = 0; i < NxN; i++) {
            List<Integer> flipped = new ArrayList<Integer>();            
            Stone s = new Stone(i, turn);
            if (board.canPut(s)) {
                flipped = board.setBoard(s);  //BLACKに打たせるときは、-player.getColor()の負号をとり、
                float eval = alphabeta(board, 4, -turn, player, alpha, beta);
                board.setAgain(flipped, -turn);  //Board.evaluate で -1 と 1 を入れ替える
                if (eval > bestEval) {
                    bestEval = eval;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    int network(String coord){
        return gui(coord);
    }
    
}
