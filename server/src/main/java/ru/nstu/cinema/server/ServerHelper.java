package ru.nstu.cinema.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by ksuhan on 08.06.17.
 */
public class ServerHelper extends Thread {

    private Socket socket = null;

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
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            String line;
            while (true) {
                line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
                System.out.println("The dumb client just sent me this line : " + line);
                System.out.println("I'm sending it back...");
                out.writeUTF(line); // отсылаем клиенту обратно ту самую строку текста.
                out.flush(); // заставляем поток закончить передачу данных.
                System.out.println("Waiting for the next line...");
                System.out.println();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }

    }
    //implement your methods here

}