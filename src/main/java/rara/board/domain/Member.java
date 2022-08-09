package rara.board.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class Member {

    public Member() {}

    @Id @GeneratedValue
    private Long id;
    private String name;
    private Integer level;
    private String memberId;
    private String password;
    private LocalDateTime regDate;

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", memberId='" + memberId + '\'' +
                ", password='" + password + '\'' +
                ", regDate=" + regDate +
                '}';
    }
}
