package com.konkuk.sample.service;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Member;
import com.konkuk.sample.domain.Remit;
import com.konkuk.sample.domain.RemitType;
import com.konkuk.sample.repository.AccountRepository;
import com.konkuk.sample.repository.RemitRepository;
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
    @Autowired
    RemitRepository remitRepository;

    // 특정 계좌 조회
    public Account getAccount(Long accountId){
        return accountRepository.readOne(accountId);
    }

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

    // 특정 계좌에 대하여 모든 입출금 내역 출력
    public List<Remit> getRemitList(Long accountId){
        Account account = accountRepository.readOne(accountId);
        return account.getRemitList();
    }

    // 특정 계좌에 대하여 입금 내역만 출력
    public List<Remit> getDepositList(Long accountId){
        Account account = accountRepository.readOne(accountId);
        return remitRepository.readByRemitType(account, RemitType.DEPOSIT);
    }

    // 특정 계좌에 대하여 출금 내역만 출력
    public List<Remit> getWithdrawList(Long accountId){
        Account account = accountRepository.readOne(accountId);
        return remitRepository.readByRemitType(account, RemitType.WITHDRAW);
    }

    // 송금
    public void remit(Long fromAccountId, String toAccountNumber, Long money, String content){
        Account fromAccount = accountRepository.readOne(fromAccountId);
        Account toAccount = accountRepository.readOneByAccountNumber(toAccountNumber);
        fromAccount.withdraw(money, content);
        toAccount.deposit(money, content);
    }
}
