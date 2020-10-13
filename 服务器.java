
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.net.*;

import static java.lang.Integer.parseInt;

class Server {
    JFrame jFrame = new JFrame("服务器");
    TitledBorder tb = new TitledBorder("服务器设置");
    JTextField tf1 = new JTextField("8888", 10);
    JTextField tf2 = new JTextField( 30);
    TextArea ta = new TextArea(10, 50);
    JButton b1 = new JButton("Start");
    JButton b2 = new JButton("Say");
    JLabel l1 = new JLabel("Port:");
    JLabel l2 = new JLabel("Say:");
    ServerSocket Server;
    Socket s1;

    public void init() {
        JPanel inputPanel = new JPanel();
        Box hbox = Box.createVerticalBox();
        Box vtemp = Box.createVerticalBox();
        Box htemp1 = Box.createHorizontalBox();
        Box htemp2 = Box.createHorizontalBox();
        Box htemp3 = Box.createHorizontalBox();
        tb.setTitleFont(new Font("SimSun", Font.BOLD, 15));
        inputPanel.setBorder(tb);

        htemp1.add(l1);
        htemp1.add(Box.createHorizontalStrut(10));
        tf1.setPreferredSize(new Dimension(50, 10));
        htemp1.add(tf1);
        htemp1.add(Box.createHorizontalStrut(10));
        htemp1.add(b1);

        htemp2.add(l2);
        htemp2.add(Box.createHorizontalStrut(10));
        tf2.setPreferredSize(new Dimension(50, 10));
        htemp2.add(tf2);
        htemp2.add(Box.createHorizontalStrut(10));
        htemp2.add(b2);

        vtemp.add(htemp1);
        vtemp.add(Box.createVerticalStrut(20));
        ta.setPreferredSize(new Dimension(80, 50));
        vtemp.add(ta);
        vtemp.add(Box.createVerticalStrut(20));
        vtemp.add(htemp2);
        vtemp.add(Box.createVerticalStrut(20));
        vtemp.add(htemp3);

        inputPanel.add(vtemp);
        hbox.add(inputPanel);
        jFrame.add(hbox);

        jFrame.setResizable(false);
        jFrame.setLocation(300, 300);
        jFrame.setPreferredSize(new Dimension(600, 350));
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void listen() {
        b1.addActionListener(e -> {
            try {
                b1.setEnabled(false);
                int port = parseInt(tf1.getText());
                ta.append("Server starting…\r\n");
                Server = new ServerSocket(port);
                s1 = Server.accept();
                ta.append("Client connected…\r\n");
                messeage m = new messeage();
                m.start();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        b2.addActionListener(e -> {
            try {
                OutputStream os = s1.getOutputStream();
                os.write(tf2.getText().getBytes());
                ta.append("Server says:" + tf2.getText() + "\r\n");
                tf2.setText("");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    class messeage extends Thread {
        public void run() {
            try {
                InputStream is = s1.getInputStream();
                int len;
                byte[] bytes = new byte[1024];
                while (true) {
                    while ((len = is.read(bytes)) != -1)
                        ta.append("Client says:" + new String(bytes, 0, len) + "\r\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class TCPServe {
    public static void main(String[] args) {
        Server s = new Server();
        s.init();
        s.listen();
    }
}