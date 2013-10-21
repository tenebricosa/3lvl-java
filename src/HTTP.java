import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class HTTP extends Server {
    Socket socket;

    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(8080); // создаем сокет сервера и привязываем его к вышеуказанному порту
        while (true) {
            Socket socket = ss.accept();
            new Thread(new HTTP(socket)).start();
        }
    }

    private HTTP(Socket s) throws Throwable {
        super(s);
        socket = s;
    }

    public void run() {
        try {
            String request = make_request();
            String url = request.split("\r\n")[0];
            request = parse_calc(url);
            make_response(request);
        } catch (Throwable x) {
            System.out.println("Произошла ошибка " + x);
        }
    }

    public String make_request() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String text = "";
        String buff = "";

        while (true) {
            buff = br.readLine();
            text += buff + "\r\n";
            if (buff == null || buff.trim().length() == 0) {
                break;
            }
        }
        return text;
    }

    public void make_response(String str) throws Exception {
        OutputStream sout = socket.getOutputStream();
        String html = "<!DOCTYPE HTML><html><body><h1 style='color: red'>" + str + "</h1></body></html>";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: OloloServer/2013-10-13\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + (html.length() + 0) + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + html;
        sout.write(result.getBytes());
        sout.flush();
    }

    public String parse_calc(String str) {
        String[] lines = str.split(" ");
        if (lines.length != 3) {
            return "Формат: http://localhost:8080/5+5";
        }
        String method = lines[0];
        String url = lines[1];
        url = url.substring(1);
        String protocol = lines[2];
        String[] operators = "+ - * /".split(" ");
        for (int i = 0; i < operators.length; i++) {
            String o = operators[i];
            String[] os = url.split(Pattern.quote(o));
            if (os.length == 2) {
                Calculator calc = new Calculator();
                try {
                    String out;
                    Double o1 = calc.read_double(os[0]);
                    Double o2 = calc.read_double(os[1]);
                    Double result = calc.get_result(o1, o2, o);
                    out = o1 + " " + o + " " + o2 + " = " + result;
                    return out;
                } catch (Message m) {
                    return m.toString();
                }
            }
        }
        return "Формат: http://localhost:8080/5+5";
    }
}
