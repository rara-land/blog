package rara.board.interceptor;

import rara.board.constant.SessionConst;
import rara.board.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();

        if (session == null || session.getAttribute(SessionConst.LOGIN_SESS_ID) == null) {
            log.info("로그인 필요");
            response.sendRedirect("/member/login?redirectUrl=" + requestURI);
            return false;
        } else {
            Member member = (Member)session.getAttribute(SessionConst.LOGIN_SESS_ID);
            session.setAttribute("member", member);
            log.info("로그인 회원 = {}", member.toString());
        }

        return true;
    }
}
