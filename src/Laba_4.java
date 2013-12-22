import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;

import freemarker.template.*;
import logic.Bio;

import java.sql.SQLException;
import java.util.List;

import DAO.Factory;


public class Laba_4 extends HTTP {
    Configuration config;
    String request;
    String url;
    String method;
    int code = 200;
    Map<String, String> headers;

    List<Bio> bioList = Factory.getInstance().getBioDAO().getAllBios();

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

    public Map<String, String> getQueryMap() throws Exception {
        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        StringBuilder requestContent = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            requestContent.append((char) br.read());
        }
        String result = URLDecoder.decode(requestContent.toString(), "UTF-8");
        String[] params = result.split("&");
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
            Map<String, String> params = getQueryMap();
            Bio b = new Bio();
            b.setYear(Integer.parseInt(params.get("year")));
            b.setMonth(Integer.parseInt(params.get("month")));
            b.setText(params.get("text"));
            if (params.containsKey("id")) {
                int id = Integer.parseInt(params.get("id"));
                b.setId(id);
                Factory.getInstance().getBioDAO().updateBio(b);

            } else {
                Factory.getInstance().getBioDAO().addBio(b);
            }
            make_response("" + b.getId());

        } else if (method.equals("DELETE")) {
            Map<String, String> params = getQueryMap();
            int id = Integer.parseInt(params.get("id"));
            Bio b = Factory.getInstance().getBioDAO().getBioById(id);
            Factory.getInstance().getBioDAO().deleteBio(b);
            make_response("ok");
        } else {
            make_response("ok");
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
