package othello66;

import static othello66.Constants.*;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
    
    public Client() {
    }
    
    //login処理関連のオブジェクト
    //int sx = 100;
    //int sy = 100;
    //TextField host, tf_name;
    //Dialog d;

    //loginメソッド
    //loginウィンドウを表示し必要な情報を得る
    //実際のlogin処理はrealLoginメソッドで行う
    void login() {
        //ウィンドウの表示とデータの入力
        Dialog d = new Dialog(screen.frame, true);
        TextField host = new TextField(10);
        TextField tf_name = new TextField(10);
        d.setLayout(new GridLayout(3, 2));
        d.add(new Label("相手のIP:"));
        d.add(host);
        d.add(new Label("あなたの名前:"));
        d.add(tf_name);
        Button button = new Button("Ok");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //入力が完了したらrealLoginメソッドを使ってサーバにloginする
                realLogin(host.getText(), tf_name.getText());
                d.setVisible(false);
                d.dispose();
            }
        });
        d.add(button);
        d.setResizable(true);
        d.setSize(255, 150);
        d.setVisible(true);
        //
        //screen.winalt.popUp("あなたは黒石、スタート！");
        //
        (new Thread(this)).start();
    }

    //realLogin関連のオブジェクト
    Socket server;          //ゲームサーバとの接続ソケット
    int port = 10000;       //接続ポート
    //int port = DEFAULT_PORT;
    BufferedReader in;      //入力ストリーム
    PrintWriter out;        //出力ストリーム
    String name;            //ゲーム参加者の名前

    //realLoginメソッド
    //サーバへのlogin処理
    void realLogin(String host, String name) {
        try {
            this.name = name;
            this.server = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
            out = new PrintWriter(this.server.getOutputStream());
            //loginコマンド送付
            out.println("login " + name);
            screen.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    void restart() {
        out.println("restart " + name);
    }

    //logoutメソッド
    //logout処理
    void logout() {
        try {
            //logoutコマンド送付
            out.println("logout " + name);
            out.flush();
            this.server.close();
        } catch (Exception e) {
            ;
        }
        System.exit(0);
    }

    //public String getUserName(){
    //    return name;
    //}
}
