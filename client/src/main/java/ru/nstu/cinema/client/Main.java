package ru.nstu.cinema.client;

import ru.nstu.cinema.client.gui.CinemaFrame;
import ru.nstu.cinema.client.storage.DataStorage;
import ru.nstu.cinema.client.storage.DataStorageImpl;

import javax.swing.*;
import java.io.IOException;

/**
 * Main class starting gui
 */
public class Main {

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(() -> {
            try (DataStorageImpl storage = new DataStorageImpl("127.0.0.1", 6666)) {
                new CinemaFrame(storage).createGUI();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
