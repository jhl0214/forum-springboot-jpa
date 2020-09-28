package com.forum.forum.service;

import com.forum.forum.domain.Member;
import com.forum.forum.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * Join
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByUserName(member.getUsername());
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

}
