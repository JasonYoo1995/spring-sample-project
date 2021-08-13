package com.konkuk.sample.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MemberToMember { // 자주 송금하는 회원
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registering_id")
    private Member registeringMember; // 등록한 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registered_id")
    private Member registeredMember; // 등록 당한 회원

    /** 생성 메서드 */
    public static MemberToMember createMemberToMember(Member registeringMember, Member registeredMember){
        MemberToMember memberToMember = new MemberToMember();
        memberToMember.setRegisteringMember(registeringMember);
        memberToMember.setRegisteredMember(registeredMember);
        return memberToMember;
    }
}
