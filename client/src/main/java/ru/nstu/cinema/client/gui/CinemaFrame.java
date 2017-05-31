package ru.nstu.cinema.client.gui;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class CinemaFrame extends JFrame {

    public CinemaFrame() {
        super("Онлайн кинотеатр");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400,300));
    }

    public void createGUI() {
        JLabel label = new JLabel("You are successfully running a Swing applet!");
        label.setPreferredSize(new Dimension(175, 100));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        getContentPane().add(label, BorderLayout.CENTER);

        //Display the window.
        pack();
        setVisible(true);
    }
}
