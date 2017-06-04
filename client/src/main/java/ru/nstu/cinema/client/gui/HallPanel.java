package ru.nstu.cinema.client.gui;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Hall;
import ru.nstu.cinema.common.entity.Session;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Панель, отвечающая за отображение свободных мест в зале для сеанса
 */
public class HallPanel extends JPanel {

    public HallPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(600, 400));

        add(Box.createGlue());
        add(new JPanel(){{
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(Box.createGlue());
            add(new JLabel("Выберите сеанс", JLabel.CENTER) {{setFont(getFont().deriveFont(24f));}});
            add(Box.createGlue());
        }});
        add(Box.createGlue());
    }

    public void updateSession(Session session) {
        Objects.requireNonNull(session, "session");

        Film film = session.getFilm();
        Hall hall = session.getHall();

        removeAll();

        JLabel headerLabel = new JLabel("Зал: " + hall.getName() + ", фильм: " + film.getName()
                + ", сеанс: " + session.getTime().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")), JLabel.LEFT);
        JLabel priceLabel = new JLabel("Цена билета: " + session.getPrice() + " руб.", JLabel.LEFT);

        add(headerLabel);
        add(priceLabel);
        add(Box.createGlue());

        JLabel screenLabel = new JLabel("Экран", JLabel.CENTER);
        screenLabel.setFont(screenLabel.getFont().deriveFont(8f));

        add(screenLabel);
        add(Box.createGlue());

        ((JSONArray) hall.getStructure().get("rows")).stream().sorted((o1, o2) -> {
            int idOfRow1 = (Integer)((JSONObject) o1).get("id");
            int idOfRow2 = (Integer)((JSONObject) o2).get("id");
            return idOfRow1 - idOfRow2;
        }).forEachOrdered(object -> {
            int seats = (Integer)((JSONObject) object).get("seats");
            JPanel buttonPanel = new JPanel() {{setLayout(new BoxLayout(this, BoxLayout.X_AXIS));}};
            buttonPanel.add(Box.createGlue());
            for (int i = 0; i < seats; i++) {
                buttonPanel.add(new JButton() {{
                    setSize(new Dimension(20, 20));
                    setBackground(Color.green);
                }});
            }
            buttonPanel.add(Box.createGlue());
            add(buttonPanel);
        });
        add(Box.createGlue());

        revalidate();
        repaint();
    }
}
