package ru.nstu.cinema.client.storage;

import ru.nstu.cinema.common.Command;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class DataStorageImpl implements DataStorage {

//    private final Socket socket;
    private final String host;
    private final int port;

    public DataStorageImpl(String host, int port) throws IOException {
//        socket = new Socket(host, port);
//        socket.setKeepAlive(true);
        this.host = host;
        this.port = port;
    }

    private Object sendCommand(Command command) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(host, port)) {
            try (OutputStream out = socket.getOutputStream();
                 InputStream is = socket.getInputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(out);
                 ObjectInputStream ois = new ObjectInputStream(is)) {
                oos.writeObject(command);
                oos.flush();
                if ("STORE_SEAT".equals(command.getCommand())) {
                    return null;
                }
                return ois.readObject();
//                return null;
            }
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
            return (List<Seat>) sendCommand(new Command("GET_SEATS", session));
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

//    @Override
//    public void close() throws Exception {
//        socket.close();
//    }
}
