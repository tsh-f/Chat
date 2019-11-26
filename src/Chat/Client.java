package Chat;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private UIChat ui;

    public static void main(String[] args) {
        new Thread(new Client()).start();
    }


    @Override
    public void run() {
        ui = new UIChat();


        ui.submit.addActionListener(e -> {
            synchronized (this) {
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
                for (ActionListener e : ui.submit.getActionListeners()) {
                    ui.submit.removeActionListener(e);
                }

                try (Socket socket = new Socket(host, port);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                    Thread rm = new Thread(() -> {
                        try {
                            while (true) {
                                ui.text.append(in.readLine() + "\n");
                            }
                        } catch (IOException e) {
                            ui.text.append("\nСоединение потеряно\n");
                        }
                    });

                    Thread sm = new Thread() {
                        @Override
                        public void run() {

                            ui.submit.addActionListener(e -> {
                                synchronized (this) {
                                    notifyAll();
                                }
                            });

                            try {
                                synchronized (this) {
                                    ui.text.setText("Введите имя: ");
                                    wait();
                                    out.write(ui.message.getText() + "\n");
                                    ui.text.setText("");
                                    ui.message.setText("");
                                    while (true) {
                                        wait();
                                        out.write(ui.message.getText() + "\n");
                                        out.flush();
                                        ui.message.setText("");
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