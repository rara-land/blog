package rara.board;

import rara.board.domain.Member;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class Util {
    public static Member getSessionMember() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = requestAttributes.getRequest().getSession(true);

        return (Member)session.getAttribute("member");
    }
}
