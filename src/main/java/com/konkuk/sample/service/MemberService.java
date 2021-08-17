package com.konkuk.sample.service;

import com.konkuk.sample.domain.Event;
import com.konkuk.sample.domain.Member;
import com.konkuk.sample.domain.MemberToMember;
import com.konkuk.sample.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    // 특정 회원을 조회
    public Member getMember(Long memberId){
        return memberRepository.readOne(memberId);
    }

    public Member getMemberByName(String memberName){
        return memberRepository.readOneByName(memberName);
    }

    // 모든 회원 목록을 조회
    public List<Member> getAllMembers(){
        return memberRepository.readAll();
    }

    // 생년월일과 이름을 입력하여 회원 가입
    public void signUp(String birth, String name){
        Event event = Event.createEvent();
        Member member = Member.createMember(birth, name, event);
        memberRepository.create(member);
    }

    // 생년월일과 이름을 수정하여 저장 가능
    public void modifyMemberInfo(String birth, String name, Long memberId){
        Member member = memberRepository.readOne(memberId);
        member.setBirth(birth);
        member.setName(name);
        memberRepository.update(member);
    }

    // 자주 송금하는 회원 이름 등록
    public void registerFrequentMember(Long registeringMemberId, Long registeredMemberId){
        Member registeringMember = memberRepository.readOne(registeringMemberId);
        Member registeredMember = memberRepository.readOne(registeredMemberId);
        MemberToMember memberToMember = MemberToMember.createMemberToMember(registeringMember, registeredMember);
        memberRepository.createMemberToMember(memberToMember);
    }

    // 자주 송금하는 회원 이름 조회
    public List<Member> getFrequentMembers(Long registeringMemberId){
        Member member = memberRepository.readOne(registeringMemberId);
        return memberRepository.readRegisteredMembers(member);
    }
}
