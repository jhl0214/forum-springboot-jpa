package com.forum.forum.service;

import com.forum.forum.domain.Member;
import com.forum.forum.dto.MemberDTO;
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
    public Long join(MemberDTO memberDTO) {
        validateDuplicateMember(memberDTO);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        memberDTO.setPassword(encoder.encode(memberDTO.getPassword()));

        return memberRepository.save(Member.builder()
                .name(memberDTO.getName().toLowerCase())
                .username(memberDTO.getUsername())
                .password(memberDTO.getPassword())
                .email(memberDTO.getEmail())
                .auth(memberDTO.getAuth()).build());
    }

    private void validateDuplicateMember(MemberDTO memberDTO) {
        Member findMember = memberRepository.findByUserName(memberDTO.getUsername());
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
