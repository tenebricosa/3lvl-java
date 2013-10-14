import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HTTP extends Server {
    Socket socket;
    String s;

    public static void main(String[] args) {
        Server server = new HTTP();
        server.start(8080);
    }


    @Override
    public void do_it(Socket socket) throws Exception {
        this.socket = socket;
        make_request();
//        BufferedReader file = new BufferedReader(new FileReader("src/index.html"));
        make_response("Hello");
    }

    public void make_request() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            s = br.readLine();
            System.out.println(s);
            if (s == null || s.trim().length() == 0) {
                break;
            }

        }
    }

    public void make_response(String html) throws Exception {
        OutputStream sout = socket.getOutputStream();
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: OloloServer/2013-10-13\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + html.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + html;
        sout.write(result.getBytes());
        sout.flush();
    }
}
