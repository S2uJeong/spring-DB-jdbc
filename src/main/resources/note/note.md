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



# 파일 설명
- V0
  - 순수 데이터 접근 방법
- V1
  - 커넥션 풀 사용 with Datasource인터페이스 