package com.konkuk.sample.controller;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Member;
import com.konkuk.sample.domain.Remit;
import com.konkuk.sample.form.*;
import com.konkuk.sample.service.AccountService;
import com.konkuk.sample.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final MemberService memberService;

    @GetMapping(value = "/account/create")
    public String createAccountForm(Model model) {
        AccountForm form = new AccountForm();
        model.addAttribute("form", form);
        return "remittance/account_create";
    }

    @PostMapping(value = "/account/create")
    public String createAccount(HttpServletRequest request, AccountForm form) {
        Long memberId = (Long) request.getSession().getAttribute("memberId");
        accountService.createAccount(form.getBankName(), form.getAccountNumber(), form.getBalance(), memberId);
        return "redirect:/account";
    }

    @GetMapping(value = "/account/retrieve/{id}")
    public String showRemitList(@PathVariable("id") Long accountId, Model model) {
        List<Remit> remitList = accountService.getRemitList(accountId);
        List<RemitWithCommaForm> RemitWithCommaList = new ArrayList<>();
        for(Remit remit : remitList){
            RemitWithCommaForm remitWithComma = RemitWithCommaForm.createRemitWithComma(remit.getDate(), remit.getType(), remit.getMoney(), remit.getContent());
            RemitWithCommaList.add(remitWithComma);
        }
        model.addAttribute("remitList", RemitWithCommaList);
        model.addAttribute("account_id", accountId);
        return "remittance/remit_list";
    }

    @GetMapping(value = "/account/retrieve/{id}/deposit")
    public String showDepositRemitList(@PathVariable("id") Long accountId, Model model) {
        List<Remit> remitList = accountService.getDepositList(accountId);
        List<RemitWithCommaForm> RemitWithCommaList = new ArrayList<>();
        for(Remit remit : remitList){
            RemitWithCommaForm remitWithComma = RemitWithCommaForm.createRemitWithComma(remit.getDate(), remit.getType(), remit.getMoney(), remit.getContent());
            RemitWithCommaList.add(remitWithComma);
        }
        model.addAttribute("remitList", RemitWithCommaList);
        model.addAttribute("account_id", accountId);
        return "remittance/remit_list";
    }

    @GetMapping(value = "/account/retrieve/{id}/withdraw")
    public String showWithdrawRemitList(@PathVariable("id") Long accountId, Model model) {
        List<Remit> remitList = accountService.getWithdrawList(accountId);
        List<RemitWithCommaForm> RemitWithCommaList = new ArrayList<>();
        for(Remit remit : remitList){
            RemitWithCommaForm remitWithComma = RemitWithCommaForm.createRemitWithComma(remit.getDate(), remit.getType(), remit.getMoney(), remit.getContent());
            RemitWithCommaList.add(remitWithComma);
        }
        model.addAttribute("remitList", RemitWithCommaList);
        model.addAttribute("account_id", accountId);
        return "remittance/remit_list";
    }

    @GetMapping(value = "/account/remit/{id}")
    public String remittanceForm(HttpServletRequest request, @PathVariable("id") Long accountId, Model model) {
        // 송금 내용 폼
        RemitForm remitForm = new RemitForm();
        model.addAttribute("remitForm", remitForm);

        // 자주 송금하는 회원 추가 폼
        MemberForm memberForm = new MemberForm();
        model.addAttribute("memberForm", memberForm);

        // 계좌 id 전달
        model.addAttribute("accountId", accountId);

        // 자주 송금하는 회원 계좌 목록 전달
        Long memberId = (Long) request.getSession().getAttribute("memberId");
        List<Member> registeredMemberList = memberService.getFrequentMembers(memberId);
        List<FrequentMemberForm> frequentMemberList = new ArrayList<>();
        for(Member member : registeredMemberList){
            FrequentMemberForm frequentMember = new FrequentMemberForm();
            frequentMember.setMemberName(member.getName());
            List<Account> accountList = accountService.getAllAccounts(member.getId());
            if(!accountList.isEmpty()){
                frequentMember.setAccountNumber(accountList.get(0).getAccountNumber());
                frequentMemberList.add(frequentMember);
            }
        }
        model.addAttribute("frequentMemberList", frequentMemberList);

        // 모든 회원 목록들 중 자주 송금하지 않는 회원 목록 전달
        List<Member> allMemberList = memberService.getAllMembers();
        for(Member registeredMember : registeredMemberList){
            for (int i = 0; i < allMemberList.size(); i++) {
                Member allMember = allMemberList.get(i);
                if(allMember.getId() == registeredMember.getId()){
                    allMemberList.remove(allMember);
                    continue;
                }
            }
        }
        model.addAttribute("memberList", allMemberList);

        return "remittance/remit";
    }

    @PostMapping(value = "/account/remit/{id}")
    public String remittance(@PathVariable("id") Long accountId, RemitForm remitForm) {
        accountService.remit(accountId, remitForm.getAccountNumber(), remitForm.getMoney(), remitForm.getContent());
        return "redirect:/account";
    }

    @PostMapping(value = "/account/remit/{id}/register")
    public String registerFrequentMember(HttpServletRequest request, MemberForm registeredMemberForm) {
        Long fromMemberId = (Long) request.getSession().getAttribute("memberId");
        Member toMember = memberService.getMemberByName(registeredMemberForm.getName());
        List<Account> accountList = accountService.getAllAccounts(toMember.getId());
        if(!accountList.isEmpty()){ // 해당 회원의 계좌가 있으면
            memberService.registerFrequentMember(fromMemberId, toMember.getId()); // 자주 송금하는 회원으로 추가
        }
        return "redirect:/account/remit/{id}";
    }

    @GetMapping(value = "/account/delete/{id}")
    public String deleteAccount(@PathVariable("id") Long accountId, Model model) {
        accountService.removeAccount(accountId);
        return "redirect:/account";
    }
}