package ru.nstu.cinema.client.storage;

import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;

import java.util.List;

/**
 * Интерфес работы с данными
 */
public interface DataStorage extends AutoCloseable {

    void storeSeat(Session session, int row, int seat);

    List<Seat> loadStoredSeats(Session session);

    List<Film> loadFilms();

    List<Session> loadSessionsByFilm(Film film);

    List<Session> loadSessions();
}
