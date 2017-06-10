package ru.nstu.cinema.client.storage;

import ru.nstu.cinema.common.Command;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataStorageImpl implements DataStorage {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public DataStorageImpl(String host, int port) throws IOException {
        socket = new Socket(host, port);
        socket.setKeepAlive(true);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    private Object sendCommand(Command command) throws IOException, ClassNotFoundException {
        outputStream.writeObject(command);
        outputStream.flush();
        if ("STORE_SEAT".equals(command.getCommand())) {
            return null;
        }
        return inputStream.readObject();
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
            List<?> result = (List<?>) sendCommand(new Command("GET_SEATS", session));
            Objects.requireNonNull(result, "seats");
            return result.stream().map(object -> (Seat)object).collect(Collectors.toList());
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
            List<?> result = (List<?>) sendCommand(new Command("GET_SESSIONS"));
            Objects.requireNonNull(result, "sessions");
            return result.stream().map(obj -> (Session) obj).collect(Collectors.toList());
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
