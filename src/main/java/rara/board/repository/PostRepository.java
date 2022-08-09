package rara.board.repository;

import rara.board.Paging;
import rara.board.domain.Post;
import rara.board.domain.DelStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostRepository {

//    @PersistenceContext
    private final EntityManager em;

    public Post save(Post post) {
        em.persist(post);

        return post;
    }

    public Post delete(Long id) {
        Post findPost = em.find(Post.class, id);

        findPost.setUpdateDate(LocalDateTime.now());
        findPost.setDel(DelStatus.Y);

        return findPost;
    }

    public Optional<Post> findById(Long id) {
        return em.createQuery("select p from Post p where p.id = :id", Post.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst();
    }

    public List<Post> findAll(Paging paging, SearchCond search) {
        String query = "select p from Post p where p.del = :del";

        log.info("search = {}", search.toString());
        if (search.getContext() != null) {
            query += " and (p.title like :title or p.content like :content)";
        }

        query += " order by p.id desc";

        TypedQuery<Post> typedQuery = em.createQuery(query, Post.class)
                .setParameter("del", DelStatus.N)
                .setFirstResult(paging.getOffset())
                .setMaxResults(paging.getLimit());

        if (search.getContext() != null) {
            typedQuery = typedQuery
                    .setParameter("title", "%"+search.getContext()+"%")
                    .setParameter("content", "%"+search.getContext()+"%");
        }

        return typedQuery.getResultList();
    }

    public Long getTotalCount(SearchCond search) {
        String query = "select count(p)  from Post p where p.del = :del";

        if (search.getContext() != null) {
            query += " and (p.title like :title or p.content like :content)";
        }

        TypedQuery<Long> typedQuery = em.createQuery(query, Long.class)
                .setParameter("del", DelStatus.N);

        if (search.getContext() != null) {
            typedQuery = typedQuery
                    .setParameter("title", "%"+search.getContext()+"%")
                    .setParameter("content", "%"+search.getContext()+"%");
        }

        return typedQuery.getSingleResult();
    }

    public Optional<Post> findPrevious(Long id) {
        return em.createQuery("select p from Post p where p.id < :id and p.del = :del order by p.id desc", Post.class)
                .setParameter("id", id)
                .setParameter("del", DelStatus.N)
                .setMaxResults(1)
                .getResultList().stream().findFirst();
    }

    public Optional<Post> findNext(Long id) {
        return em.createQuery("select p from Post p where p.id > :id and p.del = :del order by p.id desc", Post.class)
                .setParameter("id", id)
                .setParameter("del", DelStatus.N)
                .setMaxResults(1)
                .getResultList().stream().findFirst();
    }
}
