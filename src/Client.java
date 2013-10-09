import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    public DataInputStream in;
    public DataOutputStream out;

    public static void main(String[] args) {
        int serverPort = 12345; // здесь обязательно нужно указать порт к которому привязывается сервер.
        String address = "127.0.0.1"; // это IP-адрес компьютера, где исполняется наша серверная программа.
        // Здесь указан адрес того самого компьютера где будет исполняться и клиент.

        try {
            InetAddress ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
            System.out.println("Подключаюсь к " + address + ":" + serverPort);
            Socket socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
            System.out.println("Подключились");

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            Client client = new Client();
            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            client.in = new DataInputStream(sin);
            client.out = new DataOutputStream(sout);

            while (true) {
                client.do_it();
            }
        } catch (Exception x) {
            System.out.println("Произошла ошибка " + x);
        }
    }

    public void do_it() throws Exception {
        Scanner keyboard = new Scanner(System.in);
        String line = null;
        line = in.readUTF(); // ждем пока сервер отошлет строку текста.
        System.out.println(line);
        line = keyboard.next(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
        out.writeUTF(line); // отсылаем введенную строку текста серверу.
        out.flush(); // заставляем поток закончить передачу данных.
    }
}