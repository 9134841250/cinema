package ru.nstu.cinema.client.storage;

import ru.nstu.cinema.common.Command;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class DataStorageImpl implements DataStorage, AutoCloseable {

    private final Socket socket;

    public DataStorageImpl(String host, int port) throws IOException {
        socket = new Socket(host, port);
        socket.setKeepAlive(true);
    }

    private Object sendCommand(Command command) throws IOException, ClassNotFoundException {
        try (OutputStream out = socket.getOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(command);
        }
        if ("STORE_SEAT".equals(command.getCommand())) {
            return null;
        }
        try (InputStream is = socket.getInputStream();
             ObjectInputStream ois = new ObjectInputStream(is)) {
            return ois.readObject();
        }
    }

    @Override
    public void storeSeat(Session session, int row, int seat) {
        try {
            sendCommand(new Command("STORE_SEAT", new Seat(0, session, row, seat)));
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Seat> loadStoredSeats(Session session) {
        try {
            return (List<Seat>) sendCommand(new Command("GET_SEATS"));
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Film> loadFilms() {
        throw new NotImplementedException();
    }

    @Override
    public List<Session> loadSessionsByFilm(Film film) {
        throw new NotImplementedException();
    }

    @Override
    public List<Session> loadSessions() {
        try {
            return (List<Session>) sendCommand(new Command("GET_SESSIONS"));
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }
}
