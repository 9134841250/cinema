package ru.nstu.cinema.client.storage;

import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;

import java.util.ArrayList;
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
}
