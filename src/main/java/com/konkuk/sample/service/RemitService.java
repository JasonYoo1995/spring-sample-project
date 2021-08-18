package com.konkuk.sample.service;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Remit;
import com.konkuk.sample.domain.RemitType;
import com.konkuk.sample.repository.RemitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RemitService {
    @Autowired
    RemitRepository remitRepository;

    // 특정 계좌에 대하여 입금 내역만 출력
    public List<Remit> getDepositList(Account account){
        return remitRepository.readByRemitType(account, RemitType.DEPOSIT);
    }

    // 특정 계좌에 대하여 출금 내역만 출력
    public List<Remit> getWithdrawList(Account account){
        return remitRepository.readByRemitType(account, RemitType.WITHDRAW);
    }
    
    // 입출금 내역 생성
    public Long createRemit(Remit remit){
        return remitRepository.create(remit);
    }
}
