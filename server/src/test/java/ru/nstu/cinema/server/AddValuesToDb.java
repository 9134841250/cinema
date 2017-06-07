package ru.nstu.cinema.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.nstu.cinema.common.entity.Film;
import ru.nstu.cinema.common.entity.Hall;
import ru.nstu.cinema.common.entity.Seat;
import ru.nstu.cinema.common.entity.Session;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ksuhan on 06.06.17.
 */
public class AddValuesToDb {
    public static void main(String[] args) {
        CinemaStorage cinemaStorage = new CinemaStorage();

        Film film1 = new Film();
        film1.setName("Wonder women");
        film1.setDescr(new JSONObject() {{
            put("country", "USA");
            put("time", 217);
        }});
        film1=cinemaStorage.addFilm(film1);

        Film film2 = new Film();
        film2.setName("Harry Potter and the Sorcerer's Stone");
        film2.setDescr(new JSONObject() {{
            put("country", "USA");
            put("time", 152);
        }});
        film2=cinemaStorage.addFilm(film2);

        Hall hall1 = new Hall();
        hall1.setName("Big hall");
        hall1.setDescription("");
        hall1.setStructure(new JSONObject() {{
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
        hall1=cinemaStorage.addHall(hall1);

        Hall hall2 = new Hall();
        hall2.setName("VIP hall");
        hall2.setDescription("super hall");
        hall2.setStructure(new JSONObject() {{
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
        hall2=cinemaStorage.addHall(hall2);

        Hall hall3 = new Hall();
        hall3.setName("Hall №1");
        hall3.setDescription("Hall with super soft sofa");
        hall3.setStructure(new JSONObject() {{
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
        hall3=cinemaStorage.addHall(hall3);

        Session session = new Session();
        session.setFilm(film1);
        session.setHall(hall1);
        session.setPrice(300);
        session.setTime(LocalDateTime.of(2017, 6, 7, 15, 0));
        cinemaStorage.addSession(session);

        session = new Session();
        session.setFilm(film1);
        session.setHall(hall1);
        session.setPrice(500);
        session.setTime(LocalDateTime.of(2017, 6, 7, 21, 0));
        cinemaStorage.addSession(session);

        session = new Session();
        session.setFilm(film2);
        session.setHall(hall2);
        session.setPrice(1500);
        session.setTime(LocalDateTime.of(2017, 6, 7, 19, 0));
        cinemaStorage.addSession(session);

        session = new Session();
        session.setFilm(film2);
        session.setHall(hall2);
        session.setPrice(2500);
        session.setTime(LocalDateTime.of(2017, 6, 7,21 , 0));
        cinemaStorage.addSession(session);

        session = new Session();
        session.setFilm(film2);
        session.setHall(hall3);
        session.setPrice(500);
        session.setTime(LocalDateTime.of(2017, 6, 7,12 , 0));
        cinemaStorage.addSession(session);


    }
}
