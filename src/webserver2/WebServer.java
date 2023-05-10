package webserver2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 이 간단한 웹서버는 클라이언트의 연결을 받아들이고, 클라이언트의 요청을 처리한 후 응답을 반환하는 기본적인 기능을 수행합니다.
 * 주요 구성 요소와 작동방식은 다음과 같습니다.
 * 1.ServerSocket 객체를 생성하여 10000번 포트에서 클라이언트 연결을 수신하도록 설정합니다.
 * 2. while 루프를 사용하여 무한히 클라이언트의 연결을 수신하고, 각 연결에 대해 별도의 ClientThread를 생성하여 처리합니다.
 * 3. ClientThread는 스레드를 상속하며 각 클라이언트의 연결을 처리하는 역할을 수행합니다.
 * 이 스레드의 run() 메소드는 다음 작업을 수행합니다.
 * - 클라이언트 소켓의 입력 스트림과 출력 스트림을 가져옵니다.
 * - 클라이언트로부터 요청 헤더를 읽어들입니다. 요청의 첫 번째 줄이 "/hello" 또는 "/hi"를 포함하는지
 * 확인하여 해당 문자열을 msg 변수에 저장합니다.
 * 클라이언트 HTTP 응답 헤더를 전송합니다. 이 경우 200 OK 상태 코드와 함께 응답이 전송됩니다.
 * 클라이언트에게 HTML 형식의 응답 본문을 전송합니다. 이 예제에서는 첫 번째 요청 줄과 느낌표를 포함한 간단한 HTML 문서를 반환합니다.
 * 마지막으로 리소스를 닫고 클라이언트 소켓을 종료합니다.
 *
 * 
 */
public class WebServer {
    public static void main(String[] args) throws Exception {
        // 클라이언트가 접속할때까지 대기할때 필요한 객체가 ServerSocket
        // 서버 소켓 객체 생성, 포트 10000 사용
        ServerSocket serverSocket = new ServerSocket(10000);
        System.out.println("1");

    try {
        while (true) {
            // 클라이언트의 연결 요청을 기다립니다.
            // 클라이언트가 연결되면 통신용 Socket 객체를 반환합니다.
            Socket clientSocket = serverSocket.accept(); //대기한다. 클라이언트가 접속하면 클라이언트와 통신하는 Socket이 반환된다.
            System.out.println("2");

            // 새 클라이언트 스레드를 생성하고 시작합니다.
            ClientThread ct = new ClientThread(clientSocket);
            ct.start();
        }
    }finally {
        // 서버 소켓을 닫습니다.
        serverSocket.close();
    }
    }



}

class ClientThread extends Thread{
    private Socket clientSocket;
    public ClientThread(Socket clientSocket){
        this.clientSocket = clientSocket;
    }



    public void run() {
        try {
            // 클라이언트 소켓으로부터 입력 스트림을 가져옵니다.
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            // 클라이언트 소켓으로부터 출력 스트림을 가져옵니다.
            OutputStream outputStream = clientSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));

            // http://localhost:10000/hello
            // http://localhost:10000/hi
            // GET /hello HTTP/1.1
            // 클라이언트로부터 첫 번째 요청 줄을 읽습니다.
            String firstLine = br.readLine();
            String msg = "";
            if (firstLine.indexOf("/hello") >= 0) {
                msg = "hello";
            } else if (firstLine.indexOf("/hi") >= 0) {
                msg = "hi";
            }
            System.out.println(firstLine);

            String line = null;
            while (!(line = br.readLine()).equals("")) {
                System.out.println(line);
            }
            // 빈줄까지 읽어들이면 끝
            // 빈줄이 나올 때 까지 클라이언트의 요청 헤더를 읽습니다.

            System.out.println("3 - 응답을한다.");

            // HTTP 응답 헤더를 클라이언트에 전송합니다.
            pw.println("HTTP/1.1 200 OK"); //응답
            pw.println("name: csoftwareengineer");
            pw.println("email: kabalport@gmail.com");
            pw.println();
            pw.flush();

            // GET /hello HTTP/1.1 의 요청이 왔을때 /pf.png에 대한 파일을 읽어서 출력한다.
            // /hello를 어디서 읽어들이냐? 서버입장으로 봤을때 어디 경로에 있는 파일을 읽어들이지?
            // HTTP 응답 본문을 클라이언트에 전송합니다.
            pw.println("<html>"); // body부분
            pw.println(firstLine + " !!!");
            pw.println("</html>");
            pw.flush();

            // 리소스를 닫고 클라이언트 소켓을 종료합니다.
            pw.close();
            br.close();
            clientSocket.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
