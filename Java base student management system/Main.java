import javax.swing.*;

import GUI.LoginFrame;
import database.DBConnection;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                DBConnection.closeConnection();
            }));
            new LoginFrame();
        });
    }
}