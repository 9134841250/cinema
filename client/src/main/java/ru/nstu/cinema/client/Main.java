package ru.nstu.cinema.client;

import ru.nstu.cinema.client.gui.CinemaFrame;
import ru.nstu.cinema.client.storage.DataStorageImpl;

import javax.swing.*;
import java.io.IOException;

/**
 * Main class starting gui
 */
public class Main {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                DataStorageImpl storage = new DataStorageImpl("192.168.0.104", 6669);
                new CinemaFrame(storage).createGUI();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
