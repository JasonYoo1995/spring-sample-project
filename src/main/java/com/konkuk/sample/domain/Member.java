package com.konkuk.sample.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    private String birth; // 생년월일

    private String name; // 회원 이름

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Event event; // 이벤트 참여

    @OneToMany(mappedBy = "member") // Account 클래스의 필드들 중, Account 테이블의 외래키에 대응되는 필드의 이름
    private List<Account> accountList = new ArrayList<>(); // 계좌 목록

    @OneToMany(mappedBy = "registeringMember")
    private List<MemberToMember> registeringMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "registeredMember")
    private List<MemberToMember> registeredMemberList = new ArrayList<>();

    /** 생성 메서드 */
    public static Member createMember(String birth, String name){
        Member member = new Member();
        member.setBirth(birth);
        member.setName(name);
        return member;
    }

    public static Member createMember(String birth, String name, Event event){
        Member member = new Member();
        member.setBirth(birth);
        member.setName(name);
        member.setEvent(event);
        event.setMember(member);
        return member;
    }
}