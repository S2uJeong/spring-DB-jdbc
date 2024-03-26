package hello.jdbc.repository.ex;

/**
 * 이 예외는 특정 기술에 종속적이지 않다. 따라서 이 예외를 사용하더라고 서비스 계층의 순수성을 유지할 수 있다.
 */
public class MyDuplicateKeyException extends MyDBException{

    public MyDuplicateKeyException() {
    }

    public MyDuplicateKeyException(String message) {
        super(message);
    }

    public MyDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
