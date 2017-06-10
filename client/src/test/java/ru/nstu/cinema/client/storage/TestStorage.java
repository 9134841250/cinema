package ru.nstu.cinema.client.storage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Hall;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestStorage implements DataStorage {

    private List<Seat> seats = new ArrayList<>();
    private Random random = new Random();

    @Override
    public void storeSeat(Session session, int row, int seat) {
        seats.add(new Seat() {{
            setRow(row);
            setSeat(seat);
            setSession(session);
        }});
    }

    @Override
    public List<Seat> loadStoredSeats(Session session) {
        int row = random.nextInt(15);
        int seatId = random.nextInt(10);
        seats.add(new Seat() {{
            setRow(row);
            setSeat(seatId);
            setSession(session);
        }});
        return seats;
    }

    @Override
    public List<Film> loadFilms() {
        return null;
    }

    @Override
    public List<Session> loadSessionsByFilm(Film film) {
        return null;
    }

    @Override
    public List<Session> loadSessions() {
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
        return Collections.singletonList(session);
    }

    @Override
    public void close() throws Exception {

    }
}
