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

## note.md 위치
src/main/resources/note/note.md
