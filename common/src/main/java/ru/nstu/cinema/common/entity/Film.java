package ru.nstu.cinema.common.entity;

import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * Объект описания фильма
 */
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private JSONObject descr;

    public Film() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getDescr() {
        return descr;
    }

    public void setDescr(JSONObject descr) {
        this.descr = descr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Film film = (Film) o;

        return id == film.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", descr=" + descr +
                '}';
    }
}
