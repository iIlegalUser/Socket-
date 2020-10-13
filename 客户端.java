
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.net.*;

class Client {
    JFrame jFrame = new JFrame("客户端");
    JPanel inputPanel = new JPanel();
    TitledBorder tb = new TitledBorder("客户端设置");
    JTextField tf1 = new JTextField("localhost",10);
    JTextField tf2 = new JTextField("8888",10);
    JTextField tf3 = new JTextField(20);
    TextArea ta = new TextArea(10, 50);
    JButton b1 = new JButton("Connect");
    JButton b2 = new JButton("Say");
    JLabel l1 = new JLabel("Server IP:");
    JLabel l2 = new JLabel("Server Port:");
    JLabel l3 = new JLabel("Say:");
    Socket s1;

    public void init() {
        Box hbox = Box.createVerticalBox();
        Box vtemp = Box.createVerticalBox();
        Box htemp1 = Box.createHorizontalBox();
        Box htemp2 = Box.createHorizontalBox();
        Box htemp3 = Box.createHorizontalBox();
        tb.setTitleFont(new Font("SimSun", Font.BOLD, 15));
        inputPanel.setBorder(tb);

        htemp1.add(l1);
        htemp1.add(Box.createHorizontalStrut(10));
        tf1.setPreferredSize(new Dimension(20, 10));
        htemp1.add(tf1);
        htemp1.add(Box.createHorizontalStrut(10));
        htemp1.add(l2);
        htemp1.add(Box.createHorizontalStrut(10));
        tf2.setPreferredSize(new Dimension(20, 10));
        htemp1.add(tf2);
        htemp1.add(Box.createHorizontalStrut(10));
        htemp1.add(b1);

        htemp2.add(l3);
        htemp2.add(Box.createHorizontalStrut(10));
        tf3.setPreferredSize(new Dimension(50, 10));
        htemp2.add(tf3);
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
                String IP = tf1.getText();
                int port = Integer.parseInt(tf2.getText());
                s1 = new Socket(IP, port);
                ta.append("Connect to server…\n");
                messeage m = new messeage();
                m.start();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        b2.addActionListener(e -> {
            try {
                OutputStream os = s1.getOutputStream();
                os.write(tf3.getText().getBytes());
                ta.append("Client says:" + tf3.getText() + "\r\n");
                tf3.setText("");
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
                        ta.append("Server says:"+new String(bytes,0,len) + "\r\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
public class TCPClient {
    public static void main(String[] args) {
        Client c = new Client();
        c.init();
        c.listen();
    }
}