package ru.nstu.cinema.client.gui;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nstu.cinema.client.storage.TestStorage;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Hall;
import ru.nstu.cinema.common.entity.Session;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class HallPanelTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Панель выбора мест");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(400,300));

            final HallPanel panel = new HallPanel(new TestStorage());
            frame.add(panel);

            frame.pack();
            frame.setVisible(true);

            new Timer(5000, e -> {
                Film film = new Film();
                film.setId(1);
                film.setName("Wonder women");
                film.setDescr(new JSONObject() {{
                    put("country", "USA");
                    put("time", 217);
                    //bla bla
                }});

                Hall hall = new Hall();
                hall.setId(1);
                hall.setName("Большой зал");
                hall.setDescription("Огромный зал с серебряным экраном");
                hall.setStructure(new JSONObject() {{
                    JSONArray rows = new JSONArray();
                    for (int i = 0; i < 15; i++) {
                        final int id = i + 1;
                        rows.add(new JSONObject() {{
                            put("id", id);
                            put("seats", 15 - (id > 10 ? 0 : id > 5 ? 1 : 2));
                        }});
                    }
                    put("rows", rows);
                }});

                Session session = new Session();
                session.setFilm(film);
                session.setHall(hall);
                session.setPrice(300);
                session.setTime(LocalDateTime.of(2017, 6, 7, 15, 0));

                panel.updateSession(session);
            }).start();
        });
    }
}
