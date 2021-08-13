package com.konkuk.sample.service;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Member;
import com.konkuk.sample.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MemberService memberService;

    // 특정 회원의 모든 계좌 목록 조회
    public List<Account> getAllAccounts(Long memberId){
        return accountRepository.readAllByMember(memberId);
    }

    // 특정 회원에 대하여 신규 계좌 생성
    public void createAccount(String bankName, String accountNumber, Long balance, Long memberId){
        Member member = memberService.getMember(memberId);
        Account account = Account.createAccount(bankName, accountNumber, balance, member);
        accountRepository.create(account);
    }

    // 특정 회원에 대하여 특정 계좌 삭제
    public void removeAccount(Long accountId){
        accountRepository.delete(accountId);
    }

    // 특정 회원의 모든 계좌에 대하여 잔액 표시
    public List<Long> getBalanceByMember(Long memberId){
        return accountRepository.readAllBalanceByMember(memberId);
    }
}
