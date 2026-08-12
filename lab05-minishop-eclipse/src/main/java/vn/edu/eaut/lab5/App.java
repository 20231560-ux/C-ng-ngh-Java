package vn.edu.eaut.lab5;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.ui.LoginFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args){
        DBHelper.testConnection();
        SwingUtilities.invokeLater(()->new LoginFrame().setVisible(true));
    }
}
