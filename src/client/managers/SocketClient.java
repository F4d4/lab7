package client.managers;

import client.commands.Execute_script;
import client.tools.Ask;
import global.facility.Request;
import global.facility.Response;
import global.facility.Ticket;
import global.tools.MyConsole;
import server.Main;
import server.rulers.CollectionRuler;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SocketClient {
    MyConsole console = new MyConsole();
    private String host;
    private int port;
    public SocketClient(String host, int port){
        this.port=port;
        this.host=host;
    }

    public void start() throws Exception {
        boolean connected = false;
        SocketChannel socketChannel = null;

        // Попытка подключения с задержкой
        while (!connected) {
            try {
                socketChannel = SocketChannel.open();
                socketChannel.connect(new InetSocketAddress(host, port));
                socketChannel.configureBlocking(false);
                connected = true; // Успешное подключение
            } catch (IOException e) {
                console.println("Не удалось подключиться к серверу. Попробуйте снова через 5 секунд...");
                try {
                    Thread.sleep(5000); // Задержка перед следующей попыткой
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        console.println("Подключение к серверу успешно установлено.");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            String[] tokens = (command.trim() + " ").split(" ", 2);
            String command1 = tokens[0];

            if (command1.equals("exit")) {
                console.println("Завершение сеанса");
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    console.println("Ошибка при закрытии соединения: " + e.getMessage());
                }
                System.exit(1);
            }

            if (command1.equals("save")) {
                console.println("Вам недоступна данная команда");
            }

            if (command1.equals("add") || command1.equals("update") || command1.equals("add_if_min")) {
                Ticket ticket = Ask.askTicket(console);
                Request request = new Request(command, ticket);
                sendRequest(request, socketChannel);
            } else if (command1.equals("execute_script")) {
                Execute_script.execute(command, socketChannel);
            } else if(!command1.equals("save")){
                Request request = new Request(command, null);
                sendRequest(request, socketChannel);
            }
        }
    }

    public static void sendRequest(Request request , SocketChannel channel) throws IOException, ClassNotFoundException, InterruptedException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        objectOutputStream.close();
        ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());

        while(buffer.hasRemaining()){
            channel.write(buffer);
        }
        System.out.println(getAnswer(channel));
    }

    public static Object getAnswer(SocketChannel channel) throws IOException, ClassNotFoundException, InterruptedException {
        Selector selector = Selector.open();
        channel.register(selector, channel.validOps());
        ByteBuffer buffer = ByteBuffer.allocate(1024); // Меньший размер буфера для частых проверок
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < 10000) {
            int readyChannels = selector.select(1000); // Таймаут для select, чтобы не блокировать навсегда

            if (readyChannels == 0) {
                continue;
            }

            while (true) {
                int bytesRead = channel.read(buffer);
                if (bytesRead == -1) {
                    break; // Конец потока данных
                }
                if (bytesRead == 0) {
                    break; // Нет доступных данных для чтения
                }

                buffer.flip();
                byteArrayOutputStream.write(buffer.array(), 0, buffer.limit());
                buffer.clear();
            }

            byte[] responseBytes = byteArrayOutputStream.toByteArray();
            if (responseBytes.length > 0) {
                try (ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(responseBytes))) {
                    Response answer = (Response) oi.readObject();
                    return answer.getMessage();
                } catch (EOFException | StreamCorruptedException e) {
                    // Не удалось десериализовать объект, возможно, не все данные получены
                    // Продолжаем чтение данных
                }
            }
        }
        return null;
    }

}
