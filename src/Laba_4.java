import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;

import freemarker.template.*;


public class Laba_4 extends HTTP {
    Configuration config;
    String request;
    String url;
    String method;
    int code = 200;
    Map<String, String> headers;

    List<Bio> bioList = new ArrayList<Bio>(10);

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
        config.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);

    }

    public void run() {
        read_database();
        try {
            make_request();
            if (url.contains("static")) {
                String path = "C:\\Users\\пользователь\\Documents\\GitHub\\internet\\src";
                path += url.replace("\\", "\\\\");
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
            code = 500;
            x.printStackTrace(new PrintStream(System.out));
            try {
                make_response("Server error");
            } catch (Exception e) {
                System.out.println("Произошла ошибка " + e);
            }

        }
    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    public void proceed_api() throws Exception {
        if (method.equals("GET")) {
            String result = "Need POST method!";
            make_response(result);

        } else if (method.equals("POST")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            StringBuilder requestContent = new StringBuilder();
            for (int i = 0; i < contentLength; i++) {
                requestContent.append((char) br.read());
            }
            String result = URLDecoder.decode(requestContent.toString(), "UTF-8");
            Map<String, String> params = getQueryMap(result);
            int id = Integer.parseInt(params.get("id"));
            String year = params.get("year");
            String month = params.get("month");
            String text = params.get("text");
            Bio bi = new Bio(id, Integer.parseInt(year), Integer.parseInt(month), text);
            Bio b = null;

            for (Bio o : bioList) {
                if (o.id == id) {
                    b = o;
                }
            }
            if (b != null) {
                bioList.remove(b);
            }
            bioList.add(bi);

        } else if (method.equals("DELETE")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            StringBuilder requestContent = new StringBuilder();
            for (int i = 0; i < contentLength; i++) {
                requestContent.append((char) br.read());
            }
            String result = URLDecoder.decode(requestContent.toString(), "UTF-8");
            Map<String, String> params = getQueryMap(result);
            int id = Integer.parseInt(params.get("id"));
            Bio b = null;
            for (Bio o : bioList) {
                if (o.id == id) {
                    b = o;
                }
            }
            if (b != null) {
                bioList.remove(b);
            }
        } else {
        }
        save_database();
        make_response("ololo");
    }

    public void save_database() {
        try {
            PrintWriter out = new PrintWriter("database.txt");
            for (Bio b : bioList) {
                System.out.println(b.getRepresentation());
                out.println(b.getRepresentation());
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void read_database() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("database.txt"));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    if (line.length() > 1) {
                        bioList.add(Bio.parseBio(line));
                    }
                    sb.append(line);
                    sb.append('\n');
                    line = br.readLine();
                }
                String everything = sb.toString();
            } finally {
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String make_request() throws Exception {
        request = super.make_request();
        String first_line = request.split("\r\n")[0];
        method = first_line.split(" ")[0];
        url = first_line.split(" ")[1];
        headers = new HashMap<String, String>();
        String[] head;
        for (String line : request.split("\r\n")) {
            head = line.split(":");
            if (head.length > 1) {
                headers.put(head[0], head[1].trim());
            }
        }

        return request;
    }

    public String render_template(String path) throws Exception {
        Writer out = new StringWriter();
        Template template = config.getTemplate(path);
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("bioList", bioList);
        template.process(root, out);
        return out.toString();
    }

    public void make_response(String html) throws Exception {
        OutputStream sout = socket.getOutputStream();
        String response = "HTTP/1.1 " + code + " OK\r\n" +
                "Server: OloloServer/2013-10-13\r\n" +
                "Content-Length: " + html.getBytes().length + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + html;
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

        } catch (FileNotFoundException a) {
            code = 404;
            make_response("File not found");
        }
    }
}
