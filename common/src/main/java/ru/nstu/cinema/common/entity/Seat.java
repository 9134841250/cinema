package ru.nstu.cinema.common.entity;

import java.io.Serializable;

/**
 * Объект описания купленного места
 */
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Session session;
    private int row;
    private int seat;

    public Seat() {
    }

    public Seat(int id,Session session,int row,int seat) {
        this.id=id;
        this.session=session;
        this.row=row;
        this.seat=seat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seat seat = (Seat) o;

        return id == seat.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", session=" + session +
                ", row=" + row +
                ", seat=" + seat +
                '}';
    }
}
