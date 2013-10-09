import java.net.*;
import java.io.*;

public class Server {
    public DataInputStream in;
    public DataOutputStream out;
    protected Server self;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        int port = 12345; // случайный порт (может быть любое число от 1025 до 65535)
        try {
            ServerSocket ss = new ServerSocket(port); // создаем сокет сервера и привязываем его к вышеуказанному порту
            System.out.println("Ожидание клиента");

            Socket socket = ss.accept(); // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
            System.out.println("Клиент подключился");

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.


            in = new DataInputStream(sin);
            out = new DataOutputStream(sout);


            do_it();

        } catch (Exception x) {
            System.out.println("Произошла ошибка " + x);
        }
    }


    public void do_it() throws Exception {
        while (true) {
            String line = null;
            line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
            System.out.println("Клиент прислал строку : " + line);
            System.out.println("Отправляю обратно");
            out.writeUTF(line); // отсылаем клиенту обратно ту самую строку текста.
            out.flush(); // заставляем поток закончить передачу данных.
            System.out.println("Ждем следующую строку");
            System.out.println();
        }
    }
}