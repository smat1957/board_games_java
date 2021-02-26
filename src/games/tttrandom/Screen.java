package tttrandom;

import static tttrandom.Constants.*;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.geom.Ellipse2D;

public class Screen extends Thread implements MouseListener {

    protected Frame frame;
    private Panel panel;
    private Canvas canvas;
    protected WindowClosing winalt;
    //private Game game;
    private boolean flag = true;
    //private Network net = null;

    public Screen() {    //コンストラクタ
        //this.game = game;
        frame = new Frame();
        panel = new Panel();
        panel.setLayout(new BorderLayout());
        canvas = new Canvas();
        canvas.setSize(WIDTH, HEIGHT);  //大きさの設定
        canvas.addMouseListener(this);  //マウスのイベントリスナをこのクラス内に        
        //フレームに必要な部品を取り付けます
        panel.add(canvas, BorderLayout.CENTER);  //キャンバスをパネルに
        //フレームfにloginボタンを取り付ける
        /*
        Button button = new Button("Login");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //loginボタンが押されたときの処理
                //サーバがセットされていなければlogin処理を行う
                if (server == null) {
                    client.login();
                }
            }
        });
        panel.add(button, BorderLayout.NORTH);
        */
        frame.add(panel);   //パネルをフレームに
        //フレームfを表示
        //frame.setSize(WIDTH, HEIGHT);
        frame.pack();
        frame.setTitle("コンピュータ研究部(Random)");

        winalt = new WindowClosing(frame);
        frame.addWindowListener((WindowListener) winalt);

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // Determine the new location of the window
        int x = (dim.width - WIDTH) / 2;
        int y = (dim.height - HEIGHT) / 2;
        frame.setLocation(x, y);

        frame.setVisible(true);

        (new Thread(this)).start();

        winalt.popUp("スタート！[Tic Tac Toe] あなたは黒石");
        //game.start();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            repaint();
        }
    }

    public void repaint() {
        // 盤面の描画
        try {
            Graphics g = canvas.getGraphics();
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            int dely = HEIGHT / Ny;
            for (int j = 0; j < Ny; j++) {
                int y = j * dely;
                //int y = j * dely + dely/2;
                g.drawLine(0, y, WIDTH - 1, y);
            }
            int delx = WIDTH / Nx;
            for (int i = 0; i < Nx; i++) {
                int x = i * delx;
                //int x = i * delx + delx/2;
                g.drawLine(x, 0, x, HEIGHT - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        boardState();
    }

    private void boardState() {
        //盤面の状態を表示
        for (int y = 0; y < Ny; y++) {
            for (int x = 0; x < Nx; x++) {
                switch (game.getBoard().getStones(y * Ny + x).getColor()) {
                    case BATSU:
                        putDisc(x, y, BATSU);
                        break;
                    case MARU:
                        putDisc(x, y, MARU);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void putDisc(int nx, int ny, int nc) {
        int x = nx * (WIDTH / Nx);
        int y = ny * (HEIGHT / Ny);
        int diameter = (WIDTH / Nx) * 2 / 5;
        Graphics2D g2d = (Graphics2D) canvas.getGraphics();
        Ellipse2D.Double circle = new Ellipse2D.Double(x + (WIDTH / Nx - diameter) / 2, y + (HEIGHT / Ny - diameter) / 2, diameter, diameter);
        Color cl = Color.WHITE;
        if (nc == BATSU) {
            cl = Color.BLACK;
        }
        if (nc == MARU) {
            cl = Color.WHITE;
        }
        g2d.setColor(cl);
        g2d.fill(circle);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (game.getTurn() == 1) {
            return;
        }
        java.awt.Point point = e.getPoint();
        int xstep = WIDTH / Nx;
        int ystep = HEIGHT / Ny;
        int nx = -1, ny = -1;
        for (int i = 0; i < Nx; i++) {
            for (int j = 0; j < Ny; j++) {
                if ((i * xstep < point.x) && (point.x < (i + 1) * xstep)) {
                    if ((j * ystep < point.y) && (point.y < (j + 1) * ystep)) {
                        nx = i;
                        ny = j;
                        break;
                    }
                }
            }
            if ((0 <= nx) && (nx < Nx)) {
                if ((0 <= ny) && (ny < Ny)) {
                    break;
                }
            }
        }
        //if (NETWORK) {
        /*
            Stone stn = new Stone(ny * Nx + nx, BATSU);
            if (game.getBoard().canPut(stn)) {
                game.getBoard().setStones(stn);
                client.out.println("slot " + String.valueOf(ny * Nx + nx));
                client.out.println("message " + "あなたの番です");
                client.out.flush();
                frame.setTitle("相手の手を待っています。。。");
                game.nextTurn();
            }
         */
        //} else {
        char[] a = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        char[] n = {'1', '2', '3', '4', '5', '6', '7', '8'};
        String s = String.valueOf(a[nx]) + String.valueOf(n[ny]);
        game.getPlayer().setCoord(s);
        //}
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
