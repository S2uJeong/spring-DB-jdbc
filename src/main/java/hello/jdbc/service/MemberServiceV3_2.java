package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 템플릿
 *  : 서비스계층에서 트랜잭션을 시작하는 코드가 없어진다.
 */
@Slf4j
public class MemberServiceV3_2 {

 //   private final PlatformTransactionManager transactionManager;
    private final TransactionTemplate txTemplate;
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.txTemplate = new TransactionTemplate(transactionManager); // 매니저를 주입 받고, TransactionTemplate을 생성한다.   (TransactionTemplate은 유연성이 없기 때문 단지 클래스임)
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) {
        txTemplate.executeWithoutResult((status) -> {
                    try {
                        bizLogic(fromId, toId, money);
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                });
        /*
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            //비즈니스 로직
            bizLogic(fromId, toId, money);
            transactionManager.commit(status); // 성공시 커밋
        } catch (Exception e) {
            transactionManager.rollback(status); // 실패시 롤백
            throw new IllegalStateException(e);
        }
         */
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }
    /*
    private static void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);  // auto 커밋 기본값으로 다시 돌려준다.
                con.close();
            } catch (Exception e) {
                log.info("error",e);
            }
        }
    }
    */

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
