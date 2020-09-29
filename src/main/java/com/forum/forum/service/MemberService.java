package com.forum.forum.service;

import com.forum.forum.domain.Member;
import com.forum.forum.dto.MemberDto;
import com.forum.forum.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * Join
     */
    @Transactional
    public Long join(MemberDto memberDto) {
        validateDuplicateMember(memberDto);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        memberDto.setPassword(encoder.encode(memberDto.getPassword()));

        return memberRepository.save(Member.builder()
                .name(memberDto.getName())
                .username(memberDto.getUsername())
                .password(memberDto.getPassword())
                .email(memberDto.getEmail())
                .auth(memberDto.getAuth()).build());
    }

    private void validateDuplicateMember(MemberDto memberDto) {
        System.out.println("=================");
        System.out.println(memberDto.getUsername());
        Member findMember = memberRepository.findByUserName(memberDto.getUsername());
        if (findMember != null) {
            throw new IllegalStateException("User already exists.");
        }
    }

    /**
     * Get all members
     */

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findById(Long memberId) {
        return memberRepository.find(memberId);
    }

    public Member findByUserName(String username) {
        return memberRepository.findByUserName(username);
    }

    public Member findByUserNameAndPassword(String username, String password) {
        return memberRepository.findByUserNameAndPassword(username, password);
    }

    @Override
    public Member loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserName(username);

        if (member == null) {
            throw new UsernameNotFoundException((username));
        }
        return member;

    }
}
