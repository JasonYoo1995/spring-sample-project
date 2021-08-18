package com.konkuk.sample.controller;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Member;
import com.konkuk.sample.form.EventForm;
import com.konkuk.sample.form.RemitForm;
import com.konkuk.sample.service.AccountService;
import com.konkuk.sample.service.DepositService;
import com.konkuk.sample.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class DepositController {
    private final AccountService accountService;
    private final DepositService depositService;
    private final MemberService memberService;

    @PostMapping(value = "/atm")
    public String atm(RemitForm remitForm) {
        accountService.remit(null, remitForm.getAccountNumber(), remitForm.getMoney(), "ATM 입금");
        return "redirect:/atm";
    }

    @PutMapping(value = "/event")
    public void showRemitList(HttpServletRequest request, EventForm eventForm) {
        Long memberId = (Long) request.getSession().getAttribute("memberId");
        Member member = memberService.getMember(memberId);
        Account account = member.getAccountList().get(0);
        depositService.participateEvent(eventForm, memberId, account.getId());
    }
}
