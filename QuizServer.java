
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QuizServer {

    static ArrayList<Question> questions = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
            new InetSocketAddress(8082), 0
        );

        server.createContext("/api/questions", exchange -> {
            addCors(exchange);

            StringBuilder json = new StringBuilder("[");
            synchronized (questions) {
                for (int i = 0; i < questions.size(); i++) {
                    Question q = questions.get(i);

                    if (i > 0) json.append(",");

                    json.append("{")
                        .append("\"id\":").append(i).append(",")
                        .append("\"question\":\"").append(escape(q.question)).append("\",")
                        .append("\"options\":[")
                        .append("\"").append(escape(q.a)).append("\",")
                        .append("\"").append(escape(q.b)).append("\",")
                        .append("\"").append(escape(q.c)).append("\",")
                        .append("\"").append(escape(q.d)).append("\"]")
                        .append("}");
                }
            }
            json.append("]");

            send(exchange, 200, json.toString());
        });

        server.createContext("/api/addQuestion", exchange -> {
            addCors(exchange);

            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                send(exchange, 405, "{\"error\":\"POST required\"}");
                return;
            }

            Map<String, String> data = readForm(exchange);

            String question = data.get("question");
            String a = data.get("option1");
            String b = data.get("option2");
            String c = data.get("option3");
            String d = data.get("option4");
            String answer = data.get("correctAnswer");

            if (question == null || a == null || b == null ||
                c == null || d == null || answer == null ||
                question.isBlank() || a.isBlank() || b.isBlank() ||
                c.isBlank() || d.isBlank() ||
                !answer.matches("[1-4]")) {

                send(exchange, 400, "{\"error\":\"Please fill all fields correctly\"}");
                return;
            }

            synchronized (questions) {
                if (questions.size() >= 60) {
                    send(exchange, 400, "{\"error\":\"Maximum 60 questions allowed\"}");
                    return;
                }

                questions.add(new Question(
                    questions.size(), question, a, b, c, d,
                    Integer.parseInt(answer)
                ));
            }

            send(exchange, 200, "{\"message\":\"Question added successfully\"}");
        });

        server.createContext("/api/submit", exchange -> {
            addCors(exchange);

            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                send(exchange, 405, "{\"error\":\"POST required\"}");
                return;
            }

            Map<String, String> data = readForm(exchange);
            String answersText = data.get("answers");

            if (answersText == null) {
                send(exchange, 400, "{\"error\":\"Answers are missing\"}");
                return;
            }

            String[] answers = answersText.split(",", -1);

            int total;
            int correct = 0;

            synchronized (questions) {
                total = questions.size();

                if (total == 0) {
                    send(exchange, 400, "{\"error\":\"No questions available\"}");
                    return;
                }

                if (answers.length != total) {
                    send(exchange, 400, "{\"error\":\"Answer count does not match question count\"}");
                    return;
                }

                for (int i = 0; i < total; i++) {
                    try {
                        int selected = Integer.parseInt(answers[i]);

                        if (selected == questions.get(i).correctAnswer) {
                            correct++;
                        }
                    } catch (NumberFormatException ignored) {
                        // Unanswered questions are counted as wrong.
                    }
                }
            }

            int wrong = total - correct;
            double percentage = (correct * 100.0) / total;

            String result = String.format(
                Locale.US,
                "{\"total\":%d,\"correct\":%d,\"wrong\":%d,\"percentage\":%.2f}",
                total, correct, wrong, percentage
            );

            send(exchange, 200, result);
        });

        server.setExecutor(null);
        server.start();

        System.out.println("Quiz Application Server Started!");
        System.out.println("Server running at http://localhost:8082");
    }

    static Map<String, String> readForm(HttpExchange exchange)
            throws IOException {

        String body = new String(
            exchange.getRequestBody().readAllBytes(),
            StandardCharsets.UTF_8
        );

        Map<String, String> map = new HashMap<>();

        for (String part : body.split("&")) {
            String[] pair = part.split("=", 2);

            if (pair.length == 2) {
                map.put(
                    URLDecoder.decode(pair[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(pair[1], StandardCharsets.UTF_8)
                );
            }
        }

        return map;
    }

    static String escape(String value) {
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    static void addCors(HttpExchange exchange) {
        exchange.getResponseHeaders().set(
            "Access-Control-Allow-Origin", "*"
        );
        exchange.getResponseHeaders().set(
            "Access-Control-Allow-Methods", "GET, POST, OPTIONS"
        );
        exchange.getResponseHeaders().set(
            "Access-Control-Allow-Headers", "Content-Type"
        );
    }

    static void send(
        HttpExchange exchange, int status, String response
    ) throws IOException {

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
            "Content-Type", "application/json; charset=UTF-8"
        );

        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    static class Question {
        int id;
        String question, a, b, c, d;
        int correctAnswer;

        Question(int id, String question, String a, String b,
                 String c, String d, int correctAnswer) {
            this.id = id;
            this.question = question;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.correctAnswer = correctAnswer;
        }
    }
}
