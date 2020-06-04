package me.rsk.hiroshima;

import javax.swing.*;


public class Framer extends JFrame {
    public Framer() {
        this.setTitle("Hiroshima Verification");
        this.setDefaultCloseOperation(2);
        this.setLocationRelativeTo(null);
        String message = "Hiroshima Does Not Recognize This ID!" + "\n" ;
        JOptionPane.showMessageDialog(this, message, "Hiroshima Verification", -1, UIManager.getIcon("OptionPane.errorIcon"));
    }

}