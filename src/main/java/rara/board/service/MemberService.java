package rara.board.service;

import rara.board.auth.Kakao;
import rara.board.auth.KakaoUserInfo;
import rara.board.constant.MemberConst;
import rara.board.domain.Member;
import rara.board.repository.MemberDto;
import rara.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final Kakao kakao;

    public Member save(MemberDto memberDto) {
        Member member = new Member();
        member.setMemberId(memberDto.getMemberId());
        member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        member.setName(memberDto.getName());
        member.setLevel(MemberConst.MEMBER_BASIC_LEVEL);
        member.setRegDate(LocalDateTime.now());
        return memberRepository.save(member);
    }

    public Member update(Long id, String name, String password) {
        Member findMember = memberRepository.findById(id);
        findMember.setName(name);

        if (!password.isEmpty()) {
            findMember.setPassword(passwordEncoder.encode(password));
        }

        return findMember;
    }

    public Boolean checkDuplicateMember(String memberId) {
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        if (member.isEmpty()) {
            // 동일 ID 존재 X
            return true;
        } else {
            // 동일 ID 존재
            return false;
        }
    }
    
    public Boolean checkPassword(String inputPassword, String memberPassword) {
        return passwordEncoder.matches(inputPassword, memberPassword);
    }


    public void kakaoLogin(String code) {
        KakaoUserInfo userInfo = kakao.getUserInfo(code);

        /**
         * todo
         * 1. member에 sns type과 sns id (long type) 컬럼 추가 필요
         * 2. userinfo 의 아이디가 이미 등록되어있는지 확인 필요.
         */
    }
}
