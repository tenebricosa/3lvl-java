import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;
import freemarker.template.Configuration;


public class Laba_4 extends HTTP {
    Configuration config;
    String url;

    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(8080); // создаем сокет сервера и привязываем его к вышеуказанному порту

        while (true) {
            Socket socket = ss.accept();
            new Thread(new Laba_4(socket)).start();
        }
    }

    private Laba_4(Socket s) throws Throwable {
        super(s);
        socket = s;
        config = new Configuration();
        config.setClassForTemplateLoading(Laba_4.class, "templates");
        config.setDefaultEncoding("UTF-8");


    }

    public void run() {
        try {
            String request = make_request();

            System.out.println(url);
            url = url.split(" ")[1];
            String template;

            if (url.contains("static")) {
//                String path = getClass().getResource(url.replace("\\", "\\\\")).getPath();
                String path = "C:\\Users\\пользователь\\Documents\\GitHub\\internet\\src";
                path += url.replace("\\", "\\\\");
                read_file(path);
            } else {
                if (url.equals("/")) {
                    url = "index.html";
                }
                template = render_template(url);
                make_response(template);
            }

        } catch (Throwable x) {
            System.out.println("Произошла ошибка " + x);
        }
    }

    public void process_request(String request) {
         url = request.split("\r\n")[0];
    }

    public String render_template(String path) throws Exception {
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("bio", new Bio(2013, 1, "oLOLOLo"));
        Writer out = new StringWriter();
        Template template = config.getTemplate(path);
        template.process(input, out);
        return out.toString();
    }

    public void make_response(String html) throws Exception {
        OutputStream sout = socket.getOutputStream();
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: OloloServer/2013-10-13\r\n" +
//                "Content-Type: text/html\r\n" +
                "Content-Length: " + html.getBytes().length + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + html;
        sout.write(result.getBytes());
        sout.flush();
    }

    public void make_error(String message, int code) throws IOException {
        OutputStream sout = socket.getOutputStream();
        String response = "HTTP/1.1 +" + code + " OK\r\n" +
                "Server: OloloServer/2013-10-13\r\n" +
//                "Content-Type: text/html\r\n" +
                "Content-Length: " + message.getBytes().length + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + message;
        sout.write(result.getBytes());
        sout.flush();
    }

    public void read_file(String path) throws Throwable {
        try {
            FileReader file = new FileReader(path);
            BufferedReader br = new BufferedReader(file);
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append('\n');
                    line = br.readLine();
                }
                String content = sb.toString();
                make_response(content);

            } finally {
                br.close();
            }
        } catch (FileNotFoundException e) {
            make_error(e.getMessage(), 404);
        }
    }
}
