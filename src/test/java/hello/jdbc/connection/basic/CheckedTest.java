package hello.jdbc.connection.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Service;

@Slf4j
public class CheckedTest {
    @Test
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }
    @Test
    void checked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow()).isInstanceOf(MyCheckedException.class);
    }
        /**
         * Exception을 상속받은 예외는 체크 예외가 된다.
         */

        static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    static class Service {
        Repository repository = new Repository();

            /**
             예외잡기
             */
            public void callCatch() {
                try {
                    repository.call();
                } catch (MyCheckedException e) {
                    log.info("예외 처리, message = {}", e.getMessage(), e);
                }
            }
            /**
             예외던지기
             */
            public void callThrow() throws MyCheckedException {
                repository.call();
            }
        }

        static class Repository {
            public void call() throws MyCheckedException {
                // 체크 예외는 던지거나 처리하지 않으면 빨간 줄이 쳐지며 컴파일이 안될것을 알 수 있음
                throw new MyCheckedException("ex");
            }
        }
    }

