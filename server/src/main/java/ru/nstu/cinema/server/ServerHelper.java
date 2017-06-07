package ru.nstu.cinema.server;

import ru.nstu.cinema.common.Command;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Created by ksuhan on 08.06.17.
 */
public class ServerHelper extends Thread {

    private Socket socket = null;
    private CinemaStorage cinemaStorage = new CinemaStorage();
    public ServerHelper(Socket socket) {

        super("ServerHelper");
        this.socket = socket;

    }

    public void run() {
        //Read input and process here
        // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
        try {
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            ObjectInputStream in = new ObjectInputStream(sin);
            ObjectOutputStream out = new ObjectOutputStream(sout);

            Object object;
            while (true) {
                object = in.read(); // ожидаем пока клиент пришлет строку текста.
                if(object instanceof Command) {
                    switch (((Command) object).getCommand()) {
                        case "GET_SESSIONS":
                            List<Session> sessions = cinemaStorage.retrieveSessions();
                            out.writeObject(sessions);
                            System.out.println("Отправили доступные сеансы");
                            break;
                        case "STORE_SEAT":
                            if (((Command) object).getObject() instanceof Seat) {
                                Seat seat = ((Seat) ((Command) object).getObject());
                                cinemaStorage.addSeat(seat);
                                System.out.println("Записали в БД место");
                            }
                            break;
                        default: //GET_SEATS
                            if (((Command) object).getObject() instanceof Session) {
                                Session session = (Session) ((Command) object).getObject();
                                List<Seat> seats = cinemaStorage.retrieveSeats(session);
                                out.writeObject(seats);
                                System.out.println("Отправили места");
                            }
                            break;
                    }

                    out.flush(); // заставляем поток закончить передачу данных.
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }

    }
    //implement your methods here

}