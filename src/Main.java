import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main (String[] args) {
        try {
            HttpServer server = makeServer();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static HttpServer makeServer() throws IOException {

        // укажем на каком сетевом адресе и порте

        // мы будем создавать наш сервер

        String host = "localhost";

        InetSocketAddress address = new InetSocketAddress(host, 9889);


        String msg = "запускаем сервер по адресу" + " http://%s:%s/%n";

        System.out.printf(msg, address.getHostName(), address.getPort());

        // пробуем создать наш сервер по этому адресу

        HttpServer server = HttpServer.create(address, 50);
        initRoutes(server);
        System.out.println("  удачно!");   return server;

    }
    private static void initRoutes(HttpServer server) {

        // создаём контекст к пути "/", который будет

        // обрабатываться через метод handleRoot

        server.createContext("/", Main::handleMainRequest);

        server.createContext("/apps/", Main::handleSecondRequest);

        server.createContext("/apps/profile",Main::handleThirdRequest);
        server.createContext("/index.html",Main::handleMainRequest);

    }

    private static void handleThirdRequest(HttpExchange exchange) {
        try {
            exchange.getResponseHeaders().add("Content-Type", "multipart/form-data; charset=utf-8");
            int responseCode = 500;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);
            try (PrintWriter writer = getWriterFrom(exchange)) {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext()
                        .getPath();
                write(writer, "HTTP Метод", method);
                write(writer, "Запрос", uri.toString());
                write(writer, "Обработан через", ctxPath);
                writeHeaders(writer, "Заголовки запроса",
                        exchange.getRequestHeaders());
                writer.flush();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static void handleMainRequest(HttpExchange exchange) {
        try {
// укажем, что тип нашего содержимого - это
// простой текст в кодировке utf-8. Без этой строки
// браузер не будет знать как отображать те данные,
// которые он получит.
// text/plain - это MIME описание содержимого
// помогающее получателю понять, что мы
// отправляем просто текст
            exchange.getResponseHeaders().add("Content-Type", "Test text/plain; charset=utf-8");

// укажем, что мы удачно обработали запрос,
// отправив ответ с кодом 200. Так же установим
// длину ответа в 0, что означает, что отправляй
// ответ пока мы не закроем поток
            int responseCode = 200;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);
// получаем экземпляр класса PrintWriter, который
// умеет записывать в поток текстовые данные
            try (PrintWriter writer = getWriterFrom(exchange)) {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext()
                        .getPath();
                write(writer, "HTTP ", method);
                write(writer, "Запрос", uri.toString());
                write(writer, "Путь", ctxPath);
                writeHeaders(writer, "Заголовки ",
                        exchange.getRequestHeaders());
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleSecondRequest(HttpExchange exchange) {
        try {
            exchange.getResponseHeaders().add("Content-Type", "OSI; charset=utf-8");
            int responseCode = 100;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);
            try (PrintWriter writer = getWriterFrom(exchange)) {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext()
                        .getPath();
                write(writer, "HTTP Метод", method);
                write(writer, "Запрос", uri.toString());
                write(writer, "Обработан через", ctxPath);
                writeHeaders(writer, "Заголовки запроса",
                        exchange.getRequestHeaders());
                writer.flush();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PrintWriter getWriterFrom(HttpExchange exchange) {

        // создаём экземпляр класса PrintWriter, который

        // умеет записывать в поток текстовые данные

        // Если вы хотите записывать данные из файла,

        // то вы можете записывать их напрямую в

        // exchange.getResponseBody(); не используя этот метод

        OutputStream output = exchange.getResponseBody();

        Charset charset = StandardCharsets.UTF_8;

        return new PrintWriter(output, false, charset);

    }

    private static void write(Writer writer, String msg, String method) {

        String data = String.format("%s: %s%n%n", msg, method);

        try {

            writer.write(data);

        } catch (IOException e) {

            e.printStackTrace();

        } }

    private static void writeHeaders(Writer writer, String type, Headers headers) {

        write(writer, type, "");

        headers.forEach((k, v) -> write(writer, "\t" + k, v.toString()));

    }

    private static void handleCssRequest(HttpExchange httpExchange) {
        // get full URI locasdf/css/hjk.css
        // get file nae
        // load faile
        // write file
    }


}