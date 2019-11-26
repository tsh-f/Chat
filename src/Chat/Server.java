package Chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Scanner;

class Server {
    private LinkedList<SampleServer> serverList;
    private ServerSocket server;
    private Socket socket;

    public static void main(String[] args) {
        System.out.print("Введите порт сервера: ");
        new Server().createServer(new Scanner(System.in).nextInt());
    }

    private void createServer(int port) {
        try {
            server = new ServerSocket(port);
            serverList = new LinkedList<>();
            while (true) {
                socket = server.accept();
                new Thread(new SampleServer(socket)).start();
                System.out.println(socket.getInetAddress().getHostName() + " connected!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SampleServer implements Runnable {
        private Socket socket;
        private BufferedWriter out;
        private BufferedReader in;

        SampleServer(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }


        @Override
        public void run() {
            serverList.add(this);
            String tmp;

            try {
                String name = in.readLine();
                while (true) {
                    tmp = in.readLine();
                    if (tmp.equals("стоп")) {
                        break;
                    }

                    for (SampleServer ss : serverList) {
                        ss.out.write("(" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ") " +  name + ": " + tmp + "\n");
                        ss.out.flush();
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection reset " + socket.getInetAddress().getHostName());
            } finally {
                try {
                    serverList.remove(this);
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}