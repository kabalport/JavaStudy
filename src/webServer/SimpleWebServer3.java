package webServer; /**
 * 정적파일을 서빙하는 기능을 추가한 웹 서버의 코드를 작성
 * 이 기능은 웹서버가 정적파일(예: HTML,CSS, 이미지 등)을 제공할 수 있게 해 줍니다.
 * 이 코드에서는 www 디렉토리에 저장된 정적파일을 제공합니다. 웹 서버의 루트 디렉토리 www라는 이름의 폴더를 생성하고 그 안에 정적 파일을 추가합니다.
 * 웹 서버를 실행한 후 웹 브라우저에서 `http://localhost:8080/index.html`과 같이
 * 정적 파일의 경로를 입력하면 해당 파일이 제공됩니다. 만약 파일이 존재하지 않는 경우 "404 Not Found"메시지를 반환합니다.
 * 이 구현에서는 요청된 정적 파일의 MIME 타입을 추측하여 Content-Type 헤더를 설정합니다. 이를 위해
 * `Files.probeContentType()` 메서드를 사용하여 파일의 MIME 타입을 확인합니다.
 * 코드의 주요 부분들에 대한 간단한 설명은 다음과 같습니다.
 * 1. handleRequest() : HTTP 요청을 처리하는 메소드로, GET 요청의 경우 `serveStaticFile()`메서드를 호출하여 해당 파일을 반환합니다.
 * 2. serveStaticFile() : 요청받은 파일을 찾아서 읽고 HTTP 응답 헤더와 함께 반환합니다. 파일이 없는 경우 "404 Not Found"메시지를 반환합니다.
 * 3. guessContentType() : 파일의 MIME 타입을 추측하여 반환합니다. 추측할 수 없는 경우 "application/octet-stream"을 반환합니다.
 * 4. joinBypeArrays() : 두 바이트 배열을 연결하여 반환합니다. 이 메소드는 HTTP 응답 헤더와 파일 내용을 하나의 바이트 배열로 결합할 때 사용됩니다.
 * 위 코드는 정적파일 서빙을 추가한 웹 서버의 간단한 예제입니다.
 *
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

public class SimpleWebServer3 {
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
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}
