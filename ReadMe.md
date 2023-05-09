# 자바 공부
## 웹서버 공부
### webServer.SimpleWebServer
- 단일 쓰레드로 동작하는 웹서버
  - 이 코드는 단일 쓰레드로 동작하는 웹 서버 코드입니다. 즉. 동시에 여러 클라이언트 요청을 처리하지는 않습니다.
  - 해당 코드는 단일 쓰레드로 요청이 들어올 때마다 순차적으로 처리하는 방식으로 동작합니다.
[src/webServer.SimpleWebServer.java](#src/SimpleWebServer.java)
### webServer.SimpleWebServer2
- 멀티 쓰레드로 동작하는 웹서버
- 웹서버는 동시에 여러 클라이언트 요청을 처리해야하는 경우가 많기 때문에 멀티 쓰레딩을 사용하는 것이 일반적이다.
- 멀티 쓰레딩을 사용하면 여러 쓰레드가 동시에 작업을 처리할 수 있으므로 동시성을 활용하여 빠른 응답 및 효율적인 작업 처리르 할 수 있습니다.
[src/webServer.SimpleWebServer2.java](#src/SimpleWebServer2.java)
### webServer.SimpleWebServer3
- 정적파일을 서빙하는 기능을 추가한 웹 서버의 코드를 작성
  [src/webServer.SimpleWebServer3.java](#src/SimpleWebServer3.java)
### webServer.SimpleWebServer4
- 클라이언트로부터 POST 요청을 받아 처리하는 기능을 추가한 웹 서버 코드를 작성
[src/webServer.SimpleWebServer4.java](#src/SimpleWebServer4.java)

### net.IpAddressExam
- ip 확인하기
### net.VerySimpleWebServer
- 서버 응답받아보는 단순한 웺서버