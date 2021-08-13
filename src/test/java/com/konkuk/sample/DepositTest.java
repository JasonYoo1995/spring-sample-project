package com.konkuk.sample;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Event;
import com.konkuk.sample.domain.Member;
import com.konkuk.sample.repository.AccountRepository;
import com.konkuk.sample.repository.EventRepository;
import com.konkuk.sample.repository.MemberRepository;
import com.konkuk.sample.service.DepositService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DepositTest {
    @Autowired
    DepositService depositService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EventRepository eventRepository;

    @Test
    @Rollback(true)
    // ATM 입금 : 특정 회원의 특정 계좌에 특정 금액 만큼 입금
    public void atmDepositTestInDepositService(){
        // GIVEN
        Account account = Account.createAccount("신한", "110-123-456789", 1000000L, null);
        accountRepository.create(account);

        // WHEN
        depositService.atmDeposit(account.getId(), 5555L);

        // THEN
        Assertions.assertThat(account.getBalance()).isEqualTo(1000000L + 5555L);
    }

    @Test
    @Rollback(true)
    // 특정 회원에 대하여 3가지 이벤트 참여에 대한 보상 지급
    public void participateEventTestInDepositService(){
        // GIVEN
        Event event = Event.createEvent();
        eventRepository.create(event);

        Member member = Member.createMember("990909", "김건국", event);
        memberRepository.create(member);

        Account account = Account.createAccount("신한", "110-123-456789", 10000L, member);
        accountRepository.create(account);

        // WHEN
        depositService.participateEvent("첫 입금", member.getId(), account.getId());

        // THEN
        Assertions.assertThat(account.getBalance()).isEqualTo(10000L + 1000L);
        Assertions.assertThat(eventRepository.readOneByMember(member).isFirstDeposit()).isEqualTo(true);

        // WHEN
        depositService.participateEvent("첫 공유", member.getId(), account.getId());

        // THEN
        Assertions.assertThat(account.getBalance()).isEqualTo(10000L + 1000L + 1000L);
        Assertions.assertThat(eventRepository.readOneByMember(member).isFirstShare()).isEqualTo(true);
    }
}

