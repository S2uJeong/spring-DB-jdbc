package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV0Test {
    MemberRepositoryV0 repository = new MemberRepositoryV0();
    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV3", 10000);
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("member == findMember : {} ", member == findMember);
        log.info("member equals findMember : {}", member.equals(findMember)); // @Data가 @EqualsAndHashCode를 가짐
        Assertions.assertThat(findMember).isEqualTo(member); // isEqualTo()는 내부에서 equals()을 사용한다.

        // update - money : 10000 -> 20000
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        // Assertions.assertThat(updatedMember.getMoney()).isEqualTo(member.getMoney());
        Assertions.assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        repository.delete(member.getMemberId());
        Assertions.assertThatThrownBy( () -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
