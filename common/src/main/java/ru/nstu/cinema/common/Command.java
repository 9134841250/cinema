package ru.nstu.cinema.common;

import java.io.Serializable;

public class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * GET_SESSIONS|STORE_SEAT|GET_SEATS
     */
    private String command;
    /**
     * STORE_SEAT=Seat; GET_SEATS=Session
     */
    private Serializable object;

    public Command() {}

    public Command(String command) {
        this.command = command;
    }

    public Command(String command, Serializable object) {
        this.command = command;
        this.object = object;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setObject(Serializable object) {
        this.object = object;
    }

    public Serializable getObject() {
        return object;
    }

    public String getCommand() {
        return command;
    }
}
