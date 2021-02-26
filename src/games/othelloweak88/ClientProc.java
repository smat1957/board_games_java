package othelloweak88;

import static othelloweak88.Constants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientProc implements Runnable {

    Socket s;
    BufferedReader in;
    PrintWriter out;
    String name = null;

    public ClientProc(Socket s) throws IOException {
        this.s = s;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {    // logoutを受信するまで繰り返す
                String line = in.readLine();  // クライアントからの入力を読み取ります
                StringTokenizer st = new StringTokenizer(line);
                String cmd = st.nextToken();
                if (name == null) { // nameが空の場合loginコマンドのみ受け付ける
                    if ("login".equalsIgnoreCase(cmd)) {
                        name = st.nextToken();
                        server.loginUser(name);
                    } else {
                        //loginコマンド以外は全て無視する
                    }
                } else {  // nameが空でないなら、既にlogin済みなので、各種コマンドを受け付ける
                    if ("logout".equalsIgnoreCase(cmd)) {  //logoutで繰り返しを抜けます
                        name = st.nextToken();
                        server.logoutUser(name);
                        break;
                    } else if ("slot".equalsIgnoreCase(cmd)) {
                        String num = st.nextToken();
                        server.setStone(num);
                    } else if ("message".equalsIgnoreCase(cmd)) {
                        String msg = st.nextToken();
                        server.setMessage(msg);
                    } else if ("restart".equalsIgnoreCase(cmd)) {
                        //String name=st.nextToken();
                        server.logoutUser(name);
                        server.loginUser(name);
                    }
                }
            }
            //登録情報を削除し、接続を切ります
            server.deleteConnection(s);
            s.close();
        } catch (IOException e) {
            try {
                s.close();
            } catch (IOException e2) {
            }
        }
    }
}
