package vn.edu.eaut.lab5.util;
import javax.swing.*;
public class MessageUtil {
    public static void info(java.awt.Component c,String s){JOptionPane.showMessageDialog(c,s,"MiniShop",JOptionPane.INFORMATION_MESSAGE);}
    public static void error(java.awt.Component c,String s){JOptionPane.showMessageDialog(c,s,"Loi",JOptionPane.ERROR_MESSAGE);}
    public static boolean confirm(java.awt.Component c,String s){return JOptionPane.showConfirmDialog(c,s,"Xac nhan",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;}
}
