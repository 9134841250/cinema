package ru.nstu.cinema.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Объект описания сеанса
 */
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Film film;
    private Hall hall;
    private LocalDateTime time;
    private int price;

    public Session() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        return id == session.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", film=" + film +
                ", hall=" + hall +
                ", time=" + time +
                ", price=" + price +
                '}';
    }
}
