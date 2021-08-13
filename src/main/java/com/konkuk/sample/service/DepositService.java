package com.konkuk.sample.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepositService {

    // ATM 입금 : 특정 회원의 특정 계좌에 특정 금액 만큼 입금
    public void atmDeposit(String accountNumber, Long money){

    }

    // 특정 회원에 대하여 3가지 이벤트 참여에 대한 보상 지급
    public void participateEvent(String event, String accountNumber){

    }
}
