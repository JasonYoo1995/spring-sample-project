package com.konkuk.sample.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RemitService {

    // 특정 회원의 특정 계좌에 대하여 잔액 표시
    public void getBalanceByAccount(String accountNumber){

    }

    // 특정 계좌에 대하여 모든 입출금 내역 출력
    public void getRemitList(String accountNumber){

    }

    // 특정 계좌에 대하여 입금 내역만 출력
    public void getDepositList(String accountNumber){

    }

    // 특정 계좌에 대하여 출금 내역만 출력
    public void getWithdrawList(String accountNumber){

    }

    // 송금
    public void remit(String fromAccountNumber, String ToAccountNumber, Long money){

    }
}
