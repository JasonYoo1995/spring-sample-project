package com.konkuk.sample.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    private boolean firstDeposit; // 첫 입금

    private boolean firstRemit; // 첫 송금

    private boolean firstShare; // 친구에게 앱 공유하기

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // Event 테이블에서 외래키 이름 생성
    private Member member; // 회원

    /** 생성 메서드 */
    public static Event createEvent(){
        Event event = new Event();
        event.setFirstDeposit(false);
        event.setFirstRemit(false);
        event.setFirstShare(false);
        return event;
    }
}