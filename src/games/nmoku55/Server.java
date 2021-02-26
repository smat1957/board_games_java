package nmoku55;

import static nmoku55.Constants.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    static final int DEFAULT_PORT = 10000;
    static ServerSocket serverSocket;
    static List connections = null;
    static ConcurrentHashMap userTable = null;
    //static Game game;

    public Server() {    //コンストラクタ
        //this.game = game;
        try {    //サーバソケットの作成
            serverSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            System.err.println("can't create server socket.");
            System.exit(1);
        }
        // ソケットの受付と、クライアント処理プログラムの開始処理を行います
        while (true) {    //無限ループ
            try {
                Socket cs = serverSocket.accept();
                addConnection(cs);      //コネクションを登録します
                //クライアント処理スレッドを作成します
                Thread ct = new Thread(new ClientProc(cs));
                ct.start();
            } catch (IOException e) {
                System.err.println("client socket or accept error.");
            }
        }
    }

    public void addConnection(Socket s) {
        if (connections == null) {
            // スレッドセーフなLinkedListを生成。
            connections = Collections.synchronizedList(new LinkedList());
        }
        connections.add(s);
    }

    public void deleteConnection(Socket s) {
        if (connections != null) {
            connections.remove(s);
        }
    }

    public void loginUser(String name) {
        if (userTable == null) {
            userTable = new ConcurrentHashMap();
        }
        userTable.put("name", name);
        //System.out.println("login:"+name);
        System.out.flush();
        game.setTurn(1);
    }

    public void logoutUser(String name) {
        //System.out.println("logout:"+name);
        System.out.flush();
        userTable.remove("name");
    }

    public void setStone(String num) {
        //System.out.println("getStone:" + num );
        System.out.flush();
        if (game.getPlayer().getSenryaku() == NETWORK) {
            int xy = Integer.parseInt(num);
            String ss = game.getPlayer().toStringCoord(xy);
            /*
            int y = xy / WIDTH;
            int x = xy % WIDTH;
            char cy = (char) ('a' + y);
            char cx = (char) ('1' + x);
            
            String ss = String.valueOf(cx) + String.valueOf(cy);
            */
            game.getPlayer().setCoord(ss);
        }
        /*
        Stone stn = new Stone(Integer.parseInt(num), MARU);
        if (game.getBoard().canPut(stn)) {
            game.getBoard().setStones(stn);
            game.screen.frame.setTitle("相手の手を待っています。。。");
            game.nextTurn();
        }
         */
    }

    public void setMessage(String msg) {
        //System.out.println("getMessage:" + msg );
        System.out.flush();
        screen.frame.setTitle(msg);
    }

    //public String getUserName(){
    //    return (String) userTable.get("name");
    //}
}
