package com.konkuk.sample.service;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Event;
import com.konkuk.sample.domain.Member;
import com.konkuk.sample.repository.EventRepository;
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
    @Autowired
    EventRepository eventRepository;

    // ATM 입금 : 특정 회원의 특정 계좌에 특정 금액 만큼 입금
    public void atmDeposit(Long accountId, Long money){
        Account account = accountService.getAccount(accountId);
        account.deposit(money, "ATM 입금");
    }

    // 특정 회원에 대하여 3가지 이벤트 참여에 대한 보상 지급
    public void participateEvent(String eventName, Long memberId, Long accountId){
        // 입금
        Account account = accountService.getAccount(accountId);
        account.deposit(1000L, "Event 참여 보상");

        // 이벤트 참여 여부 업데이트
        Member member = memberService.getMember(memberId);
        Event event = eventRepository.readOneByMember(member);
        switch (eventName){
            case "첫 입금":
                event.setFirstDeposit(true);
                break;
            case "첫 송금":
                event.setFirstRemit(true);
                break;
            case "첫 공유":
                event.setFirstShare(true);
                break;
            default:
                break;
        }
        eventRepository.update(event);
    }
}
