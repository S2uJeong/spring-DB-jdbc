package hello.jdbc.connection.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnCheckedTest {
    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }
    @Test
    void unchecked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(()-> service.callThrow())
                .isInstanceOf(MyUnCheckedException.class);
    }

    /**
     * 1. RuntimeException을 상속받은 예외는 언체크 예외가 된다.
     */
    static class MyUnCheckedException extends RuntimeException {
        public MyUnCheckedException(String message) {
            super(message);
        }
    }

    static class Service {
        Repository repository = new Repository();
        public void callCatch() {
            try {
                repository.call();
            } catch (MyUnCheckedException e){
                log.info("예외 처리, message = {}", e.getMessage(), e);
            }
        }

        public void callThrow() {
             repository.call();
        }

    }

    /**
     *  2. 예외 발생 근원지에서, throws 하지 않아도 에러가 나지 않는다.
     */
    static class Repository {
        public void call() {
            throw new MyUnCheckedException("ex");
        }
    }
}

