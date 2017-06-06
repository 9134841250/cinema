package ru.nstu.cinema.client.gui;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nstu.cinema.client.storage.DataStorage;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Hall;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Панель, отвечающая за отображение свободных мест в зале для сеанса
 */
public class HallPanel extends JPanel {

    private final DataStorage storage;
    private final Timer reLoader;
    private final java.util.List<SeatButtonInfo> buttons = new ArrayList<>();

    public HallPanel(DataStorage storage) {
        super();
        this.storage = storage;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(900, 700));

        add(Box.createGlue());
        add(new JPanel(){{
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(Box.createGlue());
            add(new JLabel("Выберите сеанс", JLabel.CENTER) {{setFont(getFont().deriveFont(24f));}});
            add(Box.createGlue());
        }});
        add(Box.createGlue());

        reLoader = new Timer(10000, null){{
            setInitialDelay(0);
            setRepeats(true);
        }};
    }

    public void updateSession(Session session) {
        Objects.requireNonNull(session, "session");

        Film film = session.getFilm();
        Hall hall = session.getHall();

        removeAll();
        reLoader.stop();
        buttons.clear();

        add(new JPanel() {{
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(new JLabel("Зал: " + hall.getName() + ", фильм: " + film.getName() + ", сеанс: "
                    + session.getTime().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")), JLabel.LEFT));
            add(Box.createGlue());
        }});
        add(new JPanel() {{
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(new JLabel("Цена билета: " + session.getPrice() + " руб.", JLabel.LEFT));
            add(Box.createGlue());
        }});
        add(Box.createGlue());

        add(new JPanel(){{
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(Box.createGlue());
            add(new JLabel("Экран", JLabel.CENTER) {{setFont(getFont().deriveFont(8f));}});
            add(Box.createGlue());
        }});

        add(Box.createGlue());

        ((JSONArray) hall.getStructure().get("rows")).stream().sorted((o1, o2) -> {
            int idOfRow1 = (Integer)((JSONObject) o1).get("id");
            int idOfRow2 = (Integer)((JSONObject) o2).get("id");
            return idOfRow1 - idOfRow2;
        }).forEachOrdered(object -> {
            int rowId = (Integer)((JSONObject) object).get("id");
            int seats = (Integer)((JSONObject) object).get("seats");
            JPanel buttonPanel = new JPanel() {{setLayout(new BoxLayout(this, BoxLayout.X_AXIS));}};
            buttonPanel.add(new JLabel("Ряд " + rowId) {{setFont(getFont().deriveFont(10f));}});
            buttonPanel.add(Box.createGlue());
            for (int i = 0; i < seats; i++) {
                final int seatId = i + 1;
                JButton seatButton = new JButton(String.valueOf(seatId)) {{
                    setPreferredSize(new Dimension(50, 50));
                    setBackground(Color.green);
                    setFont(getFont().deriveFont(8f));
                    addActionListener((event) ->{
                        int dialogResult = JOptionPane.showConfirmDialog(HallPanel.this,
                                "Вы действительно хотите забронировать билет на сеанс: " + film.getName() + ", "
                                        + session.getTime().format(DateTimeFormatter.ofPattern("dd.MM HH:mm"))
                                        + ", ряд: " + rowId + ", место: " + seatId + "?", "Подтверждение",
                                JOptionPane.YES_NO_OPTION);
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            try {
                                storage.storeSeat(session, rowId, seatId);
                                ((JButton)event.getSource()).setEnabled(false);
                                ((JButton)event.getSource()).setBackground(Color.red);
                            } catch (RuntimeException e) {
                                JOptionPane.showMessageDialog(HallPanel.this,
                                        "Произошла проблема при выборе места, попробуйте выбрать другое", "Ошибка",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                }};
                buttons.add(new SeatButtonInfo(seatButton, rowId, seatId));
                buttonPanel.add(seatButton);
            }
            buttonPanel.add(Box.createGlue());
            add(buttonPanel);
        });
        add(Box.createGlue());

        for (ActionListener listener : reLoader.getActionListeners()) {
            reLoader.removeActionListener(listener);
        }
        reLoader.addActionListener((event) -> {
            java.util.List<Seat> seats = storage.loadStoredSeats(session);
            seats.forEach(seat -> buttons.stream()
                        .filter(button -> seat.getRow() == button.getRowId() && seat.getSeat() == button.getSeatId())
                        .forEach(button -> {
                            if (button.getSeatButton().isEnabled()) {
                                button.getSeatButton().setBackground(Color.red);
                                button.getSeatButton().setEnabled(false);
                                button.getSeatButton().revalidate();
                                button.getSeatButton().repaint();
                            }
                        }));
        });
        reLoader.start();

        revalidate();
        repaint();
    }

    public void destroy() {
        reLoader.stop();
    }

    private static class SeatButtonInfo {
        private final JButton seatButton;
        private final int rowId;
        private final int seatId;

        SeatButtonInfo(JButton seatButton, int rowId, int seatId) {
            this.seatButton = seatButton;
            this.rowId = rowId;
            this.seatId = seatId;
        }

        public int getRowId() {
            return rowId;
        }

        public int getSeatId() {
            return seatId;
        }

        public JButton getSeatButton() {
            return seatButton;
        }
    }
}
