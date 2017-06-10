package ru.nstu.cinema.client.gui;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nstu.cinema.client.storage.DataStorage;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Hall;
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
class HallPanel extends JPanel {

    private final DataStorage storage;
    private final Timer reLoader;
    private final ArrayList<SeatButtonInfo> buttons = new ArrayList<>();

    HallPanel(DataStorage storage) {
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

    void updateSession(Session session) {
        Objects.requireNonNull(session, "session");

        Film film = session.getFilm();
        Hall hall = session.getHall();
        final ArrayList<SeatButtonInfo> selectedButtons = new ArrayList<>();

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

        final JButton payButton = new JButton("Забронировать выбранные места") {{
            setFont(getFont().deriveFont(11f));
            setEnabled(false);
            addActionListener(event -> {
                int seatCount = selectedButtons.size();
                int summaryCoast = seatCount * session.getPrice();
                StringBuilder message = new StringBuilder("Подтвердите бронирование мест на сеанс: ");
                message.append(film.getName()).append(", ");
                message.append(session.getTime().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")));
                selectedButtons.forEach(seat -> message.append(", ряд: ").append(seat.getRowId())
                        .append(" место: ").append(seat.getSeatId()));
                message.append(". Стоимость: ").append(summaryCoast).append(" руб");

                int dialogResult = JOptionPane.showConfirmDialog(HallPanel.this, message.toString(),
                        "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        selectedButtons.forEach(seat -> {
                            storage.storeSeat(session, seat.getRowId(), seat.getSeatId());
                            setSeatDisabled(seat.getSeatButton());
                        });
                        StringBuilder inform = new StringBuilder("Дата: ");
                        inform.append(session.getTime().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")));
                        inform.append("\nФильм: ");
                        inform.append(film.getName());
                        inform.append("\nЗал: ");
                        inform.append(hall.getName());
                        inform.append("\nСтоимость: ");
                        inform.append(summaryCoast);
                        inform.append("\nМеста: ");
                        selectedButtons.forEach(seat -> inform.append("\n    Ряд ").append(seat.getRowId())
                                .append(", место ").append(seat.getSeatId()));
                        JOptionPane.showMessageDialog(HallPanel.this, inform, "Квитанция",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (RuntimeException e) {
                        JOptionPane.showMessageDialog(HallPanel.this,
                                "Произошла проблема при выборе мест, попробуйте выбрать другие", "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    this.setEnabled(false);
                    selectedButtons.clear();
                }
            });
        }};
        add(Box.createVerticalStrut(10));
        add(new JPanel() {{
            add(Box.createHorizontalStrut(15));
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(payButton);
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

        JSONArray hallRows = (JSONArray) hall.getStructure().get("rows");
        hallRows.stream().sorted((o1, o2) -> {
            long idOfRow1 = (Long)((JSONObject) o1).get("id");
            long idOfRow2 = (Long)((JSONObject) o2).get("id");
            return (int)(idOfRow1 - idOfRow2);
        }).forEachOrdered(object -> {
            long rowId = (Long)((JSONObject) object).get("id");
            long seats = (Long)((JSONObject) object).get("seats");
            JPanel buttonPanel = new JPanel() {{setLayout(new BoxLayout(this, BoxLayout.X_AXIS));}};
            buttonPanel.add(new JLabel("Ряд " + rowId) {{setFont(getFont().deriveFont(10f));}});
            buttonPanel.add(Box.createGlue());
            for (int i = 0; i < seats; i++) {
                final int seatId = i + 1;
                JButton seatButton = new JButton(String.valueOf(seatId)) {{
                    setPreferredSize(new Dimension(50, 50));
                    setBackground(Color.green);
                    setFont(getFont().deriveFont(8f));
                    addActionListener((event) -> {
                        SeatButtonInfo seatInfo = new SeatButtonInfo(this, (int)rowId, seatId);
                        if (this.getBackground() == Color.orange) {
                            selectedButtons.remove(seatInfo);
                            this.setBackground(Color.green);
                        } else {
                            selectedButtons.add(seatInfo);
                            this.setBackground(Color.orange);
                        }
                        payButton.setEnabled(!selectedButtons.isEmpty());
                        this.revalidate();
                        this.repaint();
                    });
                }};
                buttons.add(new SeatButtonInfo(seatButton, (int)rowId, seatId));
                buttonPanel.add(seatButton);
            }
            buttonPanel.add(Box.createGlue());
            add(buttonPanel);
        });
        add(Box.createGlue());

        for (ActionListener listener : reLoader.getActionListeners()) {
            reLoader.removeActionListener(listener);
        }
        reLoader.addActionListener((event) -> storage.loadStoredSeats(session).forEach(seat -> buttons.stream()
                    .filter(button -> seat.getRow() == button.getRowId() && seat.getSeat() == button.getSeatId())
                    .forEach(button -> setSeatDisabled(button.getSeatButton()))));
        reLoader.start();

        revalidate();
        repaint();
    }

    private void setSeatDisabled(JButton seat) {
        if (seat.getBackground() != Color.red) {
            seat.setBackground(Color.red);
            seat.setEnabled(false);
            seat.revalidate();
            seat.repaint();
        }
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

        int getRowId() {
            return rowId;
        }

        int getSeatId() {
            return seatId;
        }

        JButton getSeatButton() {
            return seatButton;
        }

        @Override
        public boolean equals(Object obj) {
            Objects.requireNonNull(obj);
            return obj instanceof SeatButtonInfo && seatButton.equals(((SeatButtonInfo) obj).getSeatButton());
        }

        @Override
        public int hashCode() {
            return seatButton.hashCode();
        }
    }
}
