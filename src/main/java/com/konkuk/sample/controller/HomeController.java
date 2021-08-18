package com.konkuk.sample.controller;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Event;
import com.konkuk.sample.domain.Member;
import com.konkuk.sample.exception.AccountNotFoundException;
import com.konkuk.sample.form.AccountWithCommaForm;
import com.konkuk.sample.form.EventForm;
import com.konkuk.sample.form.RemitForm;
import com.konkuk.sample.service.AccountService;
import com.konkuk.sample.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AccountService accountService;
    private final MemberService memberService;

    @GetMapping(value = "/home")
    public String showMenu() {
        return "initial_page/home";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping(value = "/account")
    public String showAccountList(HttpServletRequest request, Model model) {
        Long memberId = (Long) request.getSession().getAttribute("memberId");
        List<Account> accountList = accountService.getAllAccounts(memberId);
        List<AccountWithCommaForm> accountWithCommaList = new ArrayList<>();
        for(Account account : accountList){
            accountWithCommaList.add(AccountWithCommaForm.createAccountWithComma(
                    account.getId(), account.getBankName(), account.getAccountNumber(), account.getBalance())
            );
        }
        model.addAttribute("accounts", accountWithCommaList);
        return "remittance/account";
    }

    @GetMapping(value = "/atm")
    public String atm(HttpServletRequest request, Model model) {
        Long memberId = (Long) request.getSession().getAttribute("memberId");
        List<Account> allAccounts = accountService.getAllAccounts(memberId);
        model.addAttribute("accountList", allAccounts);

        RemitForm remitForm = new RemitForm();
        model.addAttribute("remitForm", remitForm);
        return "deposit/atm";
    }

    @GetMapping(value = "/event")
    public String event(HttpServletRequest request, Model model) {
        Long memberId = (Long) request.getSession().getAttribute("memberId");
        List<Account> allAccounts = accountService.getAllAccounts(memberId);
        Account account;
        try{
            account = allAccounts.get(0);
        }
        catch(Exception e){
            throw new AccountNotFoundException("이벤트 보상을 받을 계좌가 존재하지 않습니다.");
        }

        Member member = memberService.getMember(memberId);
        Event event = member.getEvent();
        EventForm eventForm = new EventForm();
        if(event.isFirstDeposit()) eventForm.setFirstDeposit(true);
        if(event.isFirstRemit()) eventForm.setFirstRemit(true);
        if(event.isFirstShare()) eventForm.setFirstShare(true);
        model.addAttribute("eventForm", eventForm);

        return "deposit/event";
    }
}
