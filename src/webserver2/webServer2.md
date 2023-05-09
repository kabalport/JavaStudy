# 네트워크 프로그래밍(자바)

# ip 주소와 port

- 컴퓨터를 구분하는 주소 : ip
- 컴퓨터 안에 있는 서버들을 구분하는 값 : port

- 윈도우

```
ipconfig
```

- 맥

```
ifconfig
```

# ip주소와 port

| 구분 | 범위 | 설명 |
| --- | --- | --- |
| Well Known Port 번호 | 0~1023 | 국제 인터넷 주소 관리기구(ICANN)에서 미리 예약해 둔 포트 |
| Registered Port 번호 | 1024~49151 | 개인 또는 회사에서 사용하는 포트 |
| Dynamic 또는 Private Port 번호 | 49152 ~ 65535 | 운영체제가 부여하는 동적 포트 또는 개인적인 목적으로 사용할 수 있는 포트 |

# 127.0.0.1

- 컴퓨터 자신의 IP

# 도메인 (Domain) 주소

[https://www.naver.com](https://www.naver.com) 은 도메인 주소이다.

# 도메인 네임 서버(Domain Name Server: DNS)

- 도메인 주소를 IP로 변환한다.
- nslookup 도메인주소
    - 위의 명령으로 도메인에 해당하는 IP주소를 알아낼 수 있다.

# localhost

- 컴퓨터 자신의 도메인

# IP주소 알아내기

- InetAddress로 알아낸다.
- 사용자 컴퓨터의 IP주소 알아내기

```java
InetAddress ia = InetAddress.getLocalHost();
System.out.println(ia.getHostAddress());
```

- google의 IP주소 알아내기

```java
InetAddress[] iaArr = InetAddress.getAllByName("www.google.com");
for(InetAddress ia : iaArr){
	System.out.println(ia.getHostAddress());
}
```

### 소스코드

src/net/IpNetworkExam

# Client & Server 프로그래밍

- Socket : Server 에 접속을 하는 역할
- ServerSocket : Client가 접속 요청을 기다리는 역할
    - Client 요청을 기다리다가 접속을 하면 Socket을 반환합니다.
- Socket과 Socket간에는 IO객체를 이용하여 통신할 수 있다.

브라우저에다가 주소를 적으면 [www.naver.com](http://www.naver.com) 엔터를 적으면 브라우저는 서버에 접속을 한다.

브라우저는 서버한테 요청정보를 보내게되고 서버는 응답정보를 보낸다. 브라우저는 응답정보를 받고 접속을 닫는다.

이렇게 통신하겠다는것을 약속이라하며 http프로토콜 규약이라고 한다.

자바로 클라이언트 서버를 어떻게 하는건가?

1. serversocket을 생성한다. serversocket(port)를 받아서 인스턴스를 생성한다.
2. ss = ServerSocket(port)라고 하면 ss.accept(); 는 클라이언트를 기다린다. 서버는 여기서 멈춘다.
3. 클라이언트는 Socket이 필요한데 Socket은 Ip와 Port를 받는다. Socket(Ip, 5000)이라 하고 인스턴스를 생성하면 접속을 할 수 있다.
4. 서버소켓은 접속을 하면 Socket을 리턴한다. 소켓과 서버소켓은 이어져 연결되어있고 InputStream, OutputStream을 둘다 얻을 수 있다.
5. 즉 소켓을 생성해주면 접속이 일어나는데 socketServer로 가서 소켓을 리턴하여 InputStream, OutputStream을 얻을 수 있다.

동시에 뭔가 주고 받고싶다면?

→ 쓰레드를 사용해야한다.

# 브라우저 요청 결과를 출력하는 Server 프로그램 작성하기

- http://ip:port 주소로 브라우저는 요청을 보낼 수 있다.
- ServerSocket은 특정 port로 접속 요청을 기다릴 수 있다.
- 브라우저는 서버와 연결이 되면 요청 정보를 전송한다.
- 서버는 브라우저가 보내는 요청정보를 읽어들인 후, 그 결과를 출력할 수 있다.

# VerySimpleWebServer 만들기

src/net/VerySimpleWebServer

# 웹서버의 동작

- 동시에 여러번 동작한다.
    - 클라이언트가 접속할때까지 대기하기
    - 클라이언트가 접속을 하면 연결된다.
    - 클라이언트가 보내주는 정보를 읽어들인다. (빈줄까지!!)
        - 첫줄(GET /)
        - 헤더들(여러줄)헤더명:헤더값
        - 빈줄
    - 서버는 응답을
        - 첫줄(200OK)
        - 헤더들 (Body의 크기)
        - 빈줄
        - Body내용이 전달된다.
    - 연결이 끊어진다.


# 서버를 안만들어도 서버를 실행할 수 있다.

- 이미 누군가가 만든 서버가 있다.
- apache, nginx같은 서버
- 누가 실행할까
    - 내 홈페이지를 만들어서 누군가에게 보여주고 싶다.
    - 컴퓨터에 서버를 실행하고, 그 서버에 내가 만든 (html, css) 홈페이지를 서비스.