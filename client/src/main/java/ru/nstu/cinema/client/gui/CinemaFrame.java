package ru.nstu.cinema.client.gui;

import ru.nstu.cinema.client.storage.DataStorage;
import ru.nstu.cinema.common.entity.Session;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class CinemaFrame extends JFrame {

    private final DataStorage storage;

    public CinemaFrame(DataStorage storage) {
        super("Онлайн кинотеатр");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400,300));
        setPreferredSize(new Dimension(1100, 700));
        this.storage = storage;
    }

    public void createGUI() {
        HallPanel hallPanel = new HallPanel(storage);

        JList<Session> sessionList = new JList<Session>() {{
            setLayoutOrientation(JList.VERTICAL);
            setVisibleRowCount(0);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }};
        sessionList.addListSelectionListener((event) -> {
            Session session = sessionList.getSelectedValue();
            if (session != null) {
                hallPanel.updateSession(session);
            }
        });
        new Timer(60000, (event) -> sessionList.setListData(storage.loadSessions().toArray(new Session[0]))){{
            setInitialDelay(0);
            setRepeats(true);
            start();
        }};

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(sessionList) {{setPreferredSize(new Dimension(200, 700));}},
                new JScrollPane(hallPanel) {{setPreferredSize(new Dimension(900, 700));}});
        splitPane.setResizeWeight(0.2);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);

        getContentPane().add(splitPane, BorderLayout.CENTER);

        //Display the window.
        pack();
        setVisible(true);
    }
}
