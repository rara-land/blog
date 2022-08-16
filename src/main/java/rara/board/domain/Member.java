package rara.board.domain;

import lombok.Getter;

import javax.persistence.*;
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

    @Enumerated(value = EnumType.STRING)
    private SnsType snsType;
    
    private String email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSnsType(SnsType snsType) {
        this.snsType = snsType;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", memberId='" + memberId + '\'' +
                ", password='" + password + '\'' +
                ", snsType=" + snsType +
                ", email='" + email + '\'' +
                ", regDate=" + regDate +
                '}';
    }
}
