import java.net.*;
import java.io.*;

public class Server implements Runnable {
    public DataInputStream in;
    public DataOutputStream out;
    Socket socket;

    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(12345); // создаем сокет сервера и привязываем его к вышеуказанному порту
        System.out.println("Ожидание клиента");
        while (true) {
            Socket socket = ss.accept();
            new Server(socket).run();
        }
    }

    public Server(Socket s) throws Throwable {
        this.socket = s;
        // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
        InputStream sin = socket.getInputStream();
        OutputStream sout = socket.getOutputStream();

        // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
        this.in = new DataInputStream(sin);
        this.out = new DataOutputStream(sout);
    }

    public void run() {
        try {
            out.writeUTF("Привет!");
            out.flush();

            while (true) {
                String line = null;

                line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
                System.out.println("Клиент прислал строку : " + line);
                System.out.println("Отправляю обратно");
                out.writeUTF(line); // отсылаем клиенту обратно ту самую строку текста.
                out.flush(); // заставляем поток закончить передачу данных.
                System.out.println("Ждем следующую строку");
            }
        } catch (Throwable x) {
            System.out.println("Произошла ошибка " + x);
        }
    }
}
