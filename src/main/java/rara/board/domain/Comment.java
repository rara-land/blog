package rara.board.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Comment {

    public Comment() {}

    @Id @GeneratedValue
    private Long id;

    private String commentContent;
    private String commentWriter;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    @Enumerated(value = EnumType.STRING)
    private DelStatus del;

}
