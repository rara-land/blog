package rara.board.repository;

import rara.board.domain.MemRegistryCheck;
import rara.board.domain.MemUpdateCheck;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class MemberDto {

    public MemberDto() {}

    public MemberDto(Long id, String name, String memberId) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
    }

    private Long id;

    @NotBlank(message = "이름은 필수 입력 항목입니다.", groups = {MemRegistryCheck.class, MemUpdateCheck.class})
    private String name;
    @NotBlank(message = "아이디는 필수 입력 항목입니다.", groups = MemRegistryCheck.class)
    @Pattern(regexp = "(?=.*[a-z])([-_]*+)(?=.*[0-9])(?=\\S+)[a-z\\d-_]{6,20}",
            message = "아이디는 영어소문자와 숫자를 포함하여 6~20자리 이내로 입력해 주세요. (특수문자는 _-만 가능)",
            groups = MemRegistryCheck.class)
    private String memberId;
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.", groups = MemRegistryCheck.class)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[~!@#$%^&*-_])(?=\\S+$)[0-9a-zA-Z~!@#$%^&*_-]{8,20}",
            message = "비밀번호는 영문자,숫자,특수문자를 최소 1자리씩 포함하여 8~20자리 이내로 입력해 주세요.",
            groups = MemRegistryCheck.class)
    private String password;
    @NotBlank(message = "비밀번호를 한 번 더 입력해 주세요.", groups = MemRegistryCheck.class)
    private String checkPassword;

    private String email;
}
