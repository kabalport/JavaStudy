package webServer; /**
 * 클라이언트로부터 POST 요청을 받아 처리하는 기능을 추가한 웹 서버 코드를 작성
 * 이 기능은 클라이언트로부터 데이터를 받아 웹 서버에서 처리할 수 있게 해줍니다.
 * 이 예제에서는 간단한 폼 데이터를 받아서, 서버에서 출력하는 기능을 구현
 * 이 코드에서는 POST요청을 받으면 요청 헤더에서 "Content-Length"를 찾아서, 클라이언트로부터 전송받은 데이터의 길이를 파악합니다.
 * 그런 다음 그 길이만큼 데이터를 읽어서 출력합니다.
 * 웹 서버를 테스트하기 위해 index2.html 파일을 웹 브라우저에서 열고 폼을 제출합니다.
 * 폼을 제출하면 서버 콘솔에 입력한 이름이 출력됩니다.
 * 이처럼 POST 요청을 처리하는 기능을 추가하여 웹 서버가 클라이언트로부터 데이터를 받아 처리할 수 있게 되었습니다.
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleWebServer4 {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        int numThreads = 10;

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Listening for connections on port " + port);

        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);

        while (true) {
            Socket socket = serverSocket.accept();
            threadPool.execute(() -> handleRequest(socket));
        }
    }

    private static void handleRequest(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String requestLine = reader.readLine();
            System.out.println(requestLine);

            String[] requestParts = requestLine.split(" ");
            String requestMethod = requestParts[0];
            String requestPath = requestParts[1];

            if (requestMethod.equalsIgnoreCase("GET")) {
                byte[] httpResponse = serveStaticFile(requestPath);
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(httpResponse);
            } else if (requestMethod.equalsIgnoreCase("POST")) {
                int contentLength = 0;
                String line;
                while (!(line = reader.readLine()).isEmpty()) {
                    if (line.startsWith("Content-Length: ")) {
                        contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
                    }
                }

                if (contentLength > 0) {
                    char[] postData = new char[contentLength];
                    reader.read(postData);
                    System.out.println("POST Data: " + new String(postData));
                }

                byte[] httpResponse = createHttpResponse("POST request received");
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(httpResponse);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ... (serveStaticFile, guessContentType, joinByteArrays 메소드는 이전 예제와 동일)
    private static byte[] serveStaticFile(String requestPath) throws IOException {
        Path filePath = Paths.get("www", requestPath.substring(1));
        byte[] fileContent;

        if (Files.exists(filePath)) {
            fileContent = Files.readAllBytes(filePath);
        } else {
            fileContent = "404 Not Found".getBytes(StandardCharsets.UTF_8);
        }

        String httpResponseTemplate = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: %s; charset=utf-8\r\n" +
                "Content-Length: %d\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        String contentType = guessContentType(filePath);
        String httpResponseHeader = String.format(httpResponseTemplate, contentType, fileContent.length);
        byte[] httpResponse = joinByteArrays(httpResponseHeader.getBytes(StandardCharsets.UTF_8), fileContent);

        return httpResponse;
    }

    private static String guessContentType(Path filePath) throws IOException {
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    private static byte[] joinByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
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
