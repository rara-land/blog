package rara.board.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@ToString
public class KakaoUserInfo {
    private Long id;
    private String name;
    private String email;

    public KakaoUserInfo() {}

    public KakaoUserInfo(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
