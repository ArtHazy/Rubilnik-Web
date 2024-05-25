import java.io.IOException;
import java.sql.SQLException;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class App {
    public static JTextArea log = new JTextArea();
    public static void log(String message) {
        log.append("\n"+message);
        System.out.println(message);
    }
    public static void main(String[] args) throws Exception {

        var mainFrame = new JFrame("Rubilnik");
        mainFrame.setSize(250, 150);
        mainFrame.setLayout(new GridLayout(0,2));
 
        var logScrollPane = new JScrollPane(new JPanel(new GridLayout(0,1)).add(log));
        log.setFocusable(false);
        mainFrame.add(logScrollPane);

        var controllPanel = new JPanel(new GridLayout(0,1));

        mainFrame.add(controllPanel);
        controllPanel.setLayout(new BoxLayout(controllPanel, 1));

        var startServerB = new JButton("Start");
        controllPanel.add(startServerB);
        startServerB.addActionListener((l)->{
            try {Server.start();log("Start");} catch (Exception e) {log("Start server failed: " + e.getMessage()); System.err.println(e);}
        });
        var stopServerB = new JButton("Stop");
        controllPanel.add(stopServerB);
        stopServerB.addActionListener((l)->{
            try {Server.stop(); log("Stop");} catch (IOException e) {log("Stop server failed: " + e.getMessage());}
        });

        var connectDB = new JButton("Connect DB");
        controllPanel.add(connectDB);
        connectDB.addActionListener((l)->{
            try {DB.connect(); log("DB connecting");} catch (Exception e) {log("DB Connect failed: " + e.getMessage());}
        });
        var disconnectDB = new JButton("Disconnect DB");
        controllPanel.add(disconnectDB);
        disconnectDB.addActionListener((l)->{
            try {DB.disconnect(); log("DB disconnect");} catch (SQLException e) {log("DB disconnect failed: " + e.getMessage());}
        });
        
        mainFrame.setVisible(true);
        log.append("Hello, World!");
    }
}
