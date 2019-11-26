package Chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String name;

    public static void main(String[] args) {
        new Client().createClient();
    }

    private void createClient() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите адрес сервера:  ");
        String host = scanner.next();
        System.out.print("Введите порт сервера: ");
        int port = scanner.nextInt();
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            Thread rm = new Thread() {
                String tmp;

                @Override
                public void run() {
                    try {
                        while (true) {
                            tmp = in.readLine();
                            System.out.println(tmp);
                        }
                    } catch (IOException e) {
                        System.out.println("Соединение потеряно");
                    }
                }
            };

            Thread sm = new Thread() {
                String tmp;

                @Override
                public void run() {
                    try {
                        System.out.print("Введите имя: ");
                        name = scanner.nextLine();
                        out.write(name);
                        while (true) {
                            tmp = scanner.nextLine();
                            if (tmp.equals("стоп")) {
                                out.write("стоп\n");
                                break;
                            }
                            out.write(tmp + "\n");
                            out.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            sm.start();
            rm.start();
            sm.join();
            rm.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}