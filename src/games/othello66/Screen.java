package othello66;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.geom.Ellipse2D;
import static othello66.Constants.*;

class Screen extends Thread implements MouseListener {

    static Frame frame;
    static Canvas canvas;
    static WindowClosing winalt;
    
    public Screen() {    //コンストラクタ     
        frame = new Frame();
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        canvas = new Canvas();
        canvas.setSize(GWIDTH, GHEIGHT);  //大きさの設定
        canvas.addMouseListener(this);  //マウスのイベントリスナをこのクラス内に
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
        frame.setTitle("高岡工芸高校コンピュータ研究部(66)");
        winalt = new WindowClosing(frame);
        frame.addWindowListener((WindowListener)winalt);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        (new Thread(this)).start();
        winalt.popUp("スタート！[ " + WIDTH + " x "+WIDTH+" オセロ] あなたは黒石");
        //game.start(this);
    }
    
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
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
            g.fillRect(0, 0, GWIDTH, GHEIGHT);
            g.setColor(Color.WHITE);
            int dely = GHEIGHT / WIDTH;
            for (int j = 0; j < WIDTH; j++) {
                int y = j * dely;
                //int y = j * dely + dely/2;
                g.drawLine(0, y, GWIDTH - 1, y);
            }
            int delx = GWIDTH / WIDTH;
            for (int i = 0; i < WIDTH; i++) {
                int x = i * delx;
                //int x = i * delx + delx/2;
                g.drawLine(x, 0, x, GHEIGHT - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        boardState();
    }
    
    void boardState() {
        //盤面の状態を表示
        for (int y = 0; y < WIDTH; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int cl = game.getBoard().getStone(y * WIDTH + x).getColor();
                if (cl == BLACK) {
                    putDisc(x, y, BLACK);
                }
                if (cl == WHITE) {
                    putDisc(x, y, WHITE);
                }
                //System.out.println("x=" + x + ",y=" + y + ",cl=" + cl);
            }
        }
    }

    private void putDisc(int nx, int ny, int nc) {
        //System.out.println("nx=" + nx + ",ny=" + ny + ",nc=" + nc);
        int diameter = (GWIDTH / WIDTH) * 2 / 5;
        int xstep = GWIDTH / WIDTH;
        int ystep = GHEIGHT / WIDTH;
        int x = nx * xstep;
        int y = ny * ystep;
        Graphics2D g2d = (Graphics2D) canvas.getGraphics();
        Ellipse2D.Double circle = new Ellipse2D.Double(x + (xstep - diameter) / 2, y + (ystep - diameter) / 2, diameter, diameter);
        Color cl = Color.WHITE;
        if (nc == BLACK) {
            cl = Color.BLACK;
        }
        if (nc == WHITE) {
            cl = Color.WHITE;
        }
        g2d.setColor(cl);
        g2d.fill(circle);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!game.getPlayer().getHuman()) {
            return;
        }
        java.awt.Point point = e.getPoint();
        //System.out.println("x:" + point.x + ",y:" + point.y);
        int xstep = GWIDTH / WIDTH;
        int ystep = GHEIGHT / WIDTH;
        int nx = -1, ny = -1;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if ((i * xstep < point.x) && (point.x < (i + 1) * xstep )) {
                    if ((j * ystep < point.y) && (point.y < (j + 1) * ystep )) {
                        nx = i + 1;
                        ny = j + 1;
                        break;
                    }
                }
            }
            if ((0 <= nx) && (0 <= ny)) {
                break;
            }
        }
        nx--;
        ny--;
        /*
        char cx, cy;
        if (nx < 10) {
            cx = (char) ('1' + (nx - 1));
        } else {
            cx = (char) ('a' + (nx - 10));
        }
        if (ny < 10) {
            cy = (char) ('1' + (ny - 1));
        } else {
            cy = (char) ('a' + (ny - 10));
        }
        String ss = String.valueOf(cx) + String.valueOf(cy);
        */
        String ss = game.getPlayer().toStringCoord(nx, ny);
        //System.out.println("yoko="+cx+", tate="+cy+", s="+ss);
        game.getPlayer().setCoord(ss);
        repaint();
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