package rara.board.controller;

import rara.board.constant.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_SESS_ID) == null){
            return "home/index";
        }

        return "redirect:/loginHome";
    }

    @GetMapping("/loginHome")
    public String loginHome() {
        return "home/loginIndex";
    }
}
