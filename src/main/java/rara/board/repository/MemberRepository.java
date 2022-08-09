package rara.board.repository;

import rara.board.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);

        return member;
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public Optional<Member> findByMemberId(String memberId) {
        return em.createQuery("select m from Member m where m.memberId = :memberId order by m.id asc", Member.class)
                .setParameter("memberId", memberId)
                .getResultList()
                .stream().findFirst();
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
