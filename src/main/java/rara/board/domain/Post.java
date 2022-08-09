package rara.board.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
public class Post {

    public Post() {}

    @Id @GeneratedValue
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;
    private Long writer;
    private LocalDateTime updateDate;
    private LocalDateTime regDate;

    @Enumerated(value = EnumType.STRING)
    private DelStatus del;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public void setWriter(Long writer) {
        this.writer = writer;
    }

    public void setDel(DelStatus del) {
        this.del = del;
    }
}
