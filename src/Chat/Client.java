package Chat;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private String name;

    public static void main(String[] args){
        new Thread(new Client()).start();
    }


    @Override
    public void run() {
        UIChat ui = new UIChat();

        ui.submit.addActionListener(e -> {
                synchronized (this){
                    notify();
                }
        });

        ui.text.setText("Введите адрес сервера:  ");
        synchronized (this) {
            try {
                wait();
                String host = ui.message.getText();
                ui.message.setText("");
                ui.text.setText("Введите порт сервера: ");
                wait();
                int port = Integer.parseInt(ui.message.getText());
                ui.message.setText("");

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
                                    ui.text.append(tmp + "\n");
                                }
                            } catch (IOException e) {
                                ui.text.append("\nСоединение потеряно\n");
                            }
                        }
                    };

                    Thread sm = new Thread() {
                        Object lock = this;
                        String tmp;

                        @Override
                        public void run() {

                            ui.submit.addActionListener(e -> {
                                synchronized (lock){
                                    notify();
                                }
                            });

                            try {
                                synchronized (lock) {
                                    ui.text.setText("Введите имя: ");
                                    wait();
                                    name = ui.message.getText();
                                    ui.text.setText("");
                                    ui.message.setText("");
                                    out.write(name + "\n");
                                    while (true) {
                                        wait();
                                        tmp = ui.message.getText();
                                        ui.message.setText("");
                                        if (tmp.equals("стоп")) {
                                            out.write("стоп\n");
                                            break;
                                        }
                                        out.write(tmp + "\n");
                                        out.flush();
                                    }
                                }
                            } catch (InterruptedException | IOException e) {
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
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}