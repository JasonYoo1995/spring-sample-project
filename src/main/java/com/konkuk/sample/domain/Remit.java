package com.konkuk.sample.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Remit {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime date; // 날짜 & 시간

    @Enumerated(EnumType.STRING)
    private RemitType type; // 입금[DEPOSIT] vs 출금[WITHDRAW]

    private Long money; // 금액

    private String content; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id") // Remit 테이블에서 외래키 이름 생성
    private Account account; // 계좌

    /** 생성 메서드 */
}