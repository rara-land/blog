package rara.board.repository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchCond {
    private String context;

    public SearchCond(String context) {
        this.context = context;
    }
}
