package com.konkuk.sample.service;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Event;
import com.konkuk.sample.domain.Member;
import com.konkuk.sample.form.EventForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepositService {
    @Autowired
    AccountService accountService;
    @Autowired
    MemberService memberService;

    // 특정 회원에 대하여 3가지 이벤트 참여에 대한 보상 지급
    public void participateEvent(EventForm eventForm, Long memberId, Long accountId){
        // 입금
        Account account = accountService.getAccount(accountId);
        account.deposit(1000L, "Event 참여 보상");

        // 이벤트 참여 여부 업데이트
        Member member = memberService.getMember(memberId);
        Event event = member.getEvent();
        event.setFirstDeposit(eventForm.isFirstDeposit());
        event.setFirstRemit(eventForm.isFirstRemit());
        event.setFirstShare(eventForm.isFirstShare());
    }
}
