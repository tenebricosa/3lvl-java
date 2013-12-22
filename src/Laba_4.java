import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import freemarker.template.*;


public class Laba_4 extends HTTP {
    Configuration config;
    String request;
    String url;
    String method;
    List<Bio> bio = Arrays.asList(
            new Bio(1, 2010, 12, "note1"),
            new Bio(2, 2009, 4, "note2"));
//    Map<String, Bio> bio = new HashMap<String, Bio>();

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
//        config.setObjectWrapper( new DefaultObjectWrapper() );
        config.setDefaultEncoding("UTF-8");


    }

    public void run() {
        try {
            make_request();
            if (url.contains("static")) {
                String path = getClass().getResource("").getPath();
                path += "../../../src/";
                path += url.replace("\\", "/");
                read_file(path);
            } else if (url.equals("/api")) {
                proceed_api();
            } else {
                if (url.equals("/")) {
                    url = "index.html";
                }
                String template = render_template(url);
                make_response(template);
            }

        } catch (Throwable x) {
            System.out.println("Произошла ошибка " + x);
        }
    }

    public String proceed_api() throws Exception {
        if (method.equals("GET")) {
            String result = "";
            make_response(result);

        } else if (method.equals("POST")) {
//            Bio o = new Bio(1, 2013, 1, "oLOLOLo");
//            bio.put("bio", o);
//            bio.put("qwe", o);
        } else {

        }
        return "";
    }

    public String make_request() throws Exception {
        request = super.make_request();
        String first_line = request.split("\r\n")[0];
        method = first_line.split(" ")[0];
        url = first_line.split(" ")[1];
        System.out.println(url);
        return request;
    }

    public String render_template(String path) throws Exception {
        Writer out = new StringWriter();
        Template template = config.getTemplate(path);
        Map root = new HashMap();
        root.put("bio", bio);
        template.process(root, out);
        return out.toString();
    }

    public void make_response(String html) throws Exception {
        OutputStream sout = socket.getOutputStream();
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: OloloServer/2013-10-13\r\n" +
                "Content-Length: " + html.getBytes().length + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + html;
        sout.write(result.getBytes());
        sout.flush();
    }

    public void read_file(String path) throws Throwable {
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

    }
}
