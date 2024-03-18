# JDBC
- Java DataBase Connentivity
- 각 DBMS 드라이버에 표준인터페이스 제공
  - java.sql.Connection : 연결
  - java.sql.Statement : SQL을 담은 내용
  - java.sql.ResultSet : SQL 요청 응답
- 데이터베이스마다 SQL 사용법이 달라서, JDBC 관련 코드는 변경하지 않아고 되지만 SQL은 바꿔줘야 한다. 
  - 하지만 이것도 JPA가 대부분 해결해준다. 
  - SQL Mapper
    - SQL만 직접 작성하면 나머지 번거로운 일은 SQL Mapper가 대신 해결
    - JdbcTemplate, MyBatis
  - ORM
    - SQL 작성 자체를 안한다.
    - JPA

## 커넥션풀
- 커넥션 생성 과정이 길어질 수 있기 때문에, 커넥션을 미리 만들어주고 쓰자는 아이디어 
  - note/DB 커넥션 생성 과정.png
- 커넥션 풀에 들어 있는 커넥션은 TCP/IP로 DB와 커넥션이 연결되어 있는 상태이기 때문에 언제든지 즉시 SQL을 DB에 전달 가능
- **애플리케이션 로직에서 DB드라이버를 통해서 획득하는게 아니라 풀을 통해 생성되어 있는 커넥션을 참조로 그냥 가져다 쓸 수 있다.**
- 쓰고 난 커넥션은 풀에 넣어 두고 다시 사용 (커넥션을 종료하지 않는다.)
- 커넥션 풀 오픈소스 : `commons-dbcp2`, `tomcat-jdbc poil`, `HikariCP`

### DataSource
- 커넥션을 획득하는 방법을 추상화
- 이 인터페이스의 핵심 기능은 **커넥션 조회** 이다.
  ```java
  public interface DataSource {
    Connection getConnetion() throws SQLException;
  }
  ```
- 덕분에 커넥션 풀 구현기술을 변경하거나 DriverManage로 직접 생성 하고 싶으면 구현체를 갈아끼우기만 하면 된다.  
  - DriverManagerDataSource
- 설정과 사용의 분리했다. 
  - DriverManager.getConnection(url,username,password)
  - DataSourceDriverManager.getConnection()
  - 필요한 데이터를 DataSource가 만들어지는 시점에 미리 넣어두고, 사용하는 곳에선 호출만한다. 
    - 그렇게 되면, repository는 DataSource에만 의존하고, 이런 속성을 몰라도 되는것이다!

## 트랜잭션
- 트랜잭션 동작
  - 커밋을 호출하기 전까지는 해당 트랜잭션을 시작한 세션(사용자)에게만 변경 데이터가 보이고 다른 세션에서는 변경 데이터가 보이지 않음

### 데이터베이스 연결 구조와 DB 세션 
  - note/데이터베이스 연결 구조.png
  - 웹 어플리케이션 서버(WAS)나 DB 접근 툴 같은 클라이언트에서 **요청이 오면, DB 서버는 내부에 세션을 만든다**.
  - 그리고 앞으로 해당 커넥션을 통한 모든 요청은 이 세션을 통해서 실행한다. 
  - 개발자가 클라이언트를 통해 SQL을 전달하면 현재 커넥션에 연결된 **세션이 SQL을 실행**한다. 

### DB 락 
- 락 대기 시간 설정 가능
- 락을 가졌던 세션이 커밋을 수행하면 락이 반납된다. 

### 애플리케이션서 트랜잭션을 어떤 계층에 걸어야 할까? - 트랜잭션을 어디서 시작하고, 어디서 커밋해야할까
- **비즈니스 로직이 있는 서비스 계층에서 트랙잭션을 시작**해야 한다. 
  - 트랜잭션을 시작하려면 커넥션이 필요하다.
  - 결국 서비스 계층에서 커넥션을 만들고, 트랜잭션 커밋 이후에 커넥션을 종료해야 한다. 
  - 애플리케이션에서 DB 트랜잭션을 사용하려면 **트랜잭션을 사용하는 동안 같은 커넥션을 유지**해야한다. (그래야 같은 세션을 이용할 수 있다.)
    - 파라미터를 통해 유지시키는 것을 방법으로 한다.

# 스프링의 트랜잭션
- 애플리케이션 계층
  1. 프레젠테이션 계층
     - UI와 관련된 처리 담당
     - 사용자 요청 검증
     - 주 사용 기술 : 서블릿과 HTTP 같은 웹 기술, 스프링 MVC
  2. 서비스 계층
     - 비즈니스 로직을 담당
     - 주 사용 기술 : 가급적 특정 기술에 의존하지 않고, 순수 자바 코드로 작성
  3. 데이터 접근 계층
     - 실제 데이터베이스에 접근하는 코드
     - 주 사용 기술 : JDBC, JPA, File, Redis, Mongo ...

---
# 파일 설명
- MemberReposirotyV0
  - 순수 데이터 접근 방법
  - JDBC - DriverManager 사용
- MemberReposirotyV1
  - 커넥션 풀 사용 with Datasource인터페이스 
- MemberServiceV1 
  - 트랜잭션 사용 안 한 서비스 로직
- MemberReposirotyV2,MemberServiceV2
  - Connection 유지를 위한 코드 변경 
  - Connection 파라미터화, 서비스계층에서 close