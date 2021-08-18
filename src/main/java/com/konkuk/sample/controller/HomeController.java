package com.konkuk.sample.controller;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.form.AccountWithCommaForm;
import com.konkuk.sample.form.RemitForm;
import com.konkuk.sample.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final AccountService accountService;

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
}
