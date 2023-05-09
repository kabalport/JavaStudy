package webServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * 목적 : 자바를 사용하여 웹서버를 만들어보기
 * java.net을 사용하여 간단한 웹서버 만들기
 * com.sun.net.httpserver 패키지를 사용하여 기본적인 http요청에 응답하는 웹서버를 만드는 방법
 * 8000번 포트에서 동작하여 루트 경로롤 요청이 오면 "Hello World!"라는 응답을 반환합니다.
 * Java 웹 프레임워크 사용하기
 *
 * server.setExecutor(null);은 기본 쓰레드 풀을 사용하도록 설정하는 부분입니다.
 * null을 전달함으로써 기본 쓰레드 풀을 사용하도록 합니다.
 * 기본 쓰레드 풀은 HttpServer 클래스 내부에서 관리되며, 서버의 설정에 따라 최적의 크기로 쓰레드 풀이 생성됩니다.
 */

public class SimpleWebServer {
    public static void main(String[] args) throws IOException {
        // HTTP 서버 인스턴스 생성 (8000번 포트로 바인딩)
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // '/' 경로에 대한 핸들러 등록
        server.createContext("/", new MyHandler());

        // 기본 쓰레드 풀 사용
        server.setExecutor(null);

        // 서버 시작
        server.start();
        System.out.println("서버가 시작되었습니다.");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            // 응답으로 보낼 내용
            String response = "Hello, World!";

            // HTTP 응답 헤더 설정 (상태 코드: 200 OK, 내용 길이)
            httpExchange.sendResponseHeaders(200, response.getBytes().length);

            // 응답 바디에 내용 작성
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());

            // 응답 전송 완료 및 리소스 해제
            os.close();
        }
    }
}
