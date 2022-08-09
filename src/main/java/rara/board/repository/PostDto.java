package rara.board.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostDto {

    private Long id;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요")
    private String content;
    private Long writer;

}


