package rara.board.controller;

import rara.board.Util;
import rara.board.constant.SessionConst;
import rara.board.domain.MemRegistryCheck;
import rara.board.domain.MemUpdateCheck;
import rara.board.domain.Member;
import rara.board.repository.MemberDto;
import rara.board.repository.MemberRepository;
import rara.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new MemberDto());
        return "home/register";
    }

    @PostMapping("/register")
    public String register(@Validated(MemRegistryCheck.class) @ModelAttribute("form") MemberDto memberDto, BindingResult bindingResult) {
        log.info("binding result = {}", bindingResult);
        if (bindingResult.hasErrors()) {
            return "home/register";
        }

        if (!memberService.checkDuplicateMember(memberDto.getMemberId())) {
            log.info("이미 존재하는 ID : {}", memberDto.getMemberId());
            bindingResult.addError(new FieldError("form", "memberId", "이미 존재하는 아이디입니다."));
            return "home/register";
        }

        if (!memberDto.getPassword().equals(memberDto.getCheckPassword())) {
            bindingResult.addError(new FieldError("form", "checkPassword", "비밀번호가 다릅니다."));
            log.info("비밀번호가 다릅니다.");
            return "home/register";
        }
        memberService.save(memberDto);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model, @RequestParam(defaultValue = "/") String redirectUrl) {
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("form", new MemberDto());
        return "home/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public Map<String, Object> login(@ModelAttribute MemberDto memberDto, HttpServletRequest request,
                                     @RequestParam(defaultValue = "/") String redirectUrl) {
        Optional<Member> findMember = memberRepository.findByMemberId(memberDto.getMemberId());
        HashMap<String, Object> result = new HashMap<>();

        if (memberDto.getMemberId().isEmpty() || memberDto.getPassword().isEmpty()) {
            result.put("result", false);
            result.put("msg", "아이디와 비밀번호 모두 입력해 주세요.");
            return result;
        }

        if (findMember.isEmpty()) {
            result.put("result", false);
            result.put("msg", "존재하지 않는 아이디입니다.");
            return result;

        } else {
            Member member = findMember.get();

            if (!memberService.checkPassword(memberDto.getPassword(), member.getPassword())) {
                result.put("result", false);
                result.put("msg", "틀린 비밀번호입니다.");
                return result;
            }

            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_SESS_ID, member);
        }

        result.put("result", true);
        result.put("redirectUrl", redirectUrl);
        result.put("msg", "로그인 성공!");

        return result;
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("/register/checkDuplicateId")
    public Boolean checkDuplicateId(@RequestParam("id") String id) {
        log.info("duplicate id = {}", id);

        return memberService.checkDuplicateMember(id);
    }

    @GetMapping("/myInfo")
    public String myInfoForm(Model model) {
        Member sessionMember = Util.getSessionMember();

        // 세션 저장 이후 데이터가 바뀔 수가 있으므로 한 번 더 가져옴
        Member member = memberRepository.findById(sessionMember.getId());

        MemberDto memberDto = new MemberDto(member.getId(), member.getName(), member.getMemberId());

        model.addAttribute("form", memberDto);

        return "member/myInfo";
    }

    @PostMapping("/myInfo")
    public String myInfo(@Validated(MemUpdateCheck.class) @ModelAttribute("form") MemberDto memberDto,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/myInfo";
        }

        if (!memberDto.getPassword().isEmpty() || !memberDto.getCheckPassword().isEmpty()) {
            if (memberDto.getPassword().isEmpty() || memberDto.getCheckPassword().isEmpty()) {
                bindingResult.addError(new FieldError("form","password", "비밀번호와 비밀번호 확인 모두 입력해 주세요"));
            } else if (!memberDto.getPassword().equals(memberDto.getCheckPassword())) {
                bindingResult.addError(new FieldError("form","password", "비밀번호와 비밀번호 확인의 입력값이 다릅니다."));
            } else if (!checkRegexPassword(memberDto.getPassword())) {
                bindingResult.addError(new FieldError("form","password", "비밀번호는 영문자,숫자,특수문자를 최소 1자리씩 포함하여 8~20자리 이내로 입력해 주세요."));
            }

            return "member/myInfo";
        }

        Member sessionMember = Util.getSessionMember();

        memberService.update(sessionMember.getId(), memberDto.getName(), memberDto.getPassword());

        return "redirect:/member/myInfo";
    }

    private boolean checkRegexPassword(String password) {
        String pattern = "(?=.*[a-z])([-_]*+)(?=.*[0-9])(?=\\S+)[a-z\\d-_]{6,20}";
        return Pattern.matches(pattern, password);
    }
}
