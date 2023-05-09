package webServer;
/**
 * 공부용 간단한 자바 웹서버 만들기
 * ServerSocket과 Socket 클래스를 사용하여 웹 서버를 구축할 수 있다.
 * 포트 8080에서 웹 서버를 실행하며, 들어오는 요청을 처리하여 "Hello, World!"라는 메시지를 포함한 HTTP응답을 반환합니다.
 * 실행 후 웹 브라우저 에서 접속하면 Hello World 메시지가 표시됩니다.
 *
 * 멀티쓰레드 도입
 * ExecutorService를 사용하여 고정 크기의 스레드 풀을 생성하고 각 요청이 도입할 때마다 새로운 작업을 스레드 풀에 추가합니다.
 * 단일쓰레드에 비해 동시에 여러 요청을 처리할 수 있게 되었습니다. 이를 통해 서버의 성능과 응답시간이 개선된다.
 */


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleWebServer2 {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        int numThreads = 10; // 스레드 풀 크기 설정

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Listening for connections on port " + port);

        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads); // 고정 크기 스레드 풀 생성

        while (true) {
            Socket socket = serverSocket.accept();
            threadPool.execute(() -> handleRequest(socket)); // 스레드 풀에 요청 처리 작업 추가
        }
    }

    private static void handleRequest(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                System.out.println(line);
            }

            long startTime = System.currentTimeMillis();


            byte[] httpResponse = createHttpResponse("Hello, World!");
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(httpResponse);
            outputStream.close();
            socket.close(); // 요청 처리 후 소켓을 닫습니다.

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Request processed in " + elapsedTime + "ms");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] createHttpResponse(String content) {
        String httpResponseTemplate = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html; charset=utf-8\r\n" +
                "Content-Length: %d\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                "%s";
        String httpResponseText = String.format(httpResponseTemplate, content.getBytes(StandardCharsets.UTF_8).length, content);
        return httpResponseText.getBytes(StandardCharsets.UTF_8);
    }
}

