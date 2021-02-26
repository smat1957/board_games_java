package othello88;

//import shobi.othello88.Game;
import java.awt.Color;
import static othello88.Constants.*;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class WindowClosing extends WindowAdapter {
    private static Frame frame = null;
    
    public WindowClosing(Frame frame){
        this.frame = frame;
    }
    
    public void windowClosing(WindowEvent e) {
        int ans = JOptionPane.showConfirmDialog(frame, "終了しますか?");
        if (ans == JOptionPane.YES_OPTION) {
            System.out.println("プログラムによる終了処理の実行");
            System.exit(0);
        }else if(ans == JOptionPane.NO_OPTION){
            //game.restartGame();
        }else if(ans == JOptionPane.CANCEL_OPTION){
        }else if(ans == JOptionPane.OK_OPTION){
            //game.restartGame();
        }
        //game.restartGame();
    }
    
    void popUp(Game game, int winner){
        String txt="引き分けでした";
        if(winner==WHITE){
            txt = "あなたの負けです";
        }else if(winner==BLACK){
            txt = "あなたの勝ちです";
        }else{
            txt = "引き分けでした";
        }
        JLabel label = new JLabel(txt+"\nもう一度対戦します？");
        label.setForeground(Color.RED);
        JOptionPane.showMessageDialog(frame, label);
        game.restartGame();
    }

    void popUp(String s){
        JLabel label = new JLabel(s);
        label.setForeground(Color.RED);
        JOptionPane.showMessageDialog(frame, label);        
    }
    
}
