package com.konkuk.sample.controller;

import com.konkuk.sample.form.RemitForm;
import com.konkuk.sample.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class DepositController {
    private final AccountService accountService;

    @PostMapping(value = "/atm")
    public String atm(RemitForm remitForm) {
        accountService.remit(null, remitForm.getAccountNumber(), remitForm.getMoney(), "ATM 입금");
        return "redirect:/atm";
    }
}
