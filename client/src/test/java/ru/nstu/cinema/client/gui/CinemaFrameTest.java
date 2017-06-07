package ru.nstu.cinema.client.gui;


import ru.nstu.cinema.client.storage.TestStorage;

import javax.swing.*;

public class CinemaFrameTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CinemaFrame(new TestStorage()).createGUI());
    }
}
