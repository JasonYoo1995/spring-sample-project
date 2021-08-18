package com.konkuk.sample.domain;

import com.konkuk.sample.exception.ZeroBalanceException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    private String bankName; // 은행 이름

    private String accountNumber; // 계좌 번호

    private Long balance; // 잔액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // Account 테이블에서 외래키 이름 생성
    private Member member; // 회원

    @OneToMany(mappedBy = "account", cascade = CascadeType.PERSIST) // Remit 클래스의 필드들 중, Remit 테이블의 외래키에 대응되는 필드의 이름
    private List<Remit> remitList = new ArrayList<>(); // 송금 내역

    @PreRemove // ON DELETE SET NULL와 같은 효과
    private void preRemoveRemits(){
        for(Remit remit : remitList){
            remit.setAccount(null);
        }
    }

    /** 생성 메서드 */
    public static Account createAccount(String bankName, String accountNumber, Long balance, Member member){
        Account account = new Account();
        account.setBankName(bankName);
        account.setAccountNumber(accountNumber);
        account.setBalance(balance);
        account.setMember(member);
        return account;
    }

    /** 비즈니스 로직 */
    // 입금
    public Remit deposit(Long money, String content){
        balance += money;
        List<Remit> remitList = this.getRemitList();
        Remit remit = Remit.createRemit(RemitType.DEPOSIT, money, content, this);
        remitList.add(remit);
        return remit;
    }

    // 출금
    public Remit withdraw(Long money, String content){
        if(balance - money < 0) throw new ZeroBalanceException("잔액가 부족하여 출금할 수 없습니다.");
        balance -= money;
        List<Remit> remitList = this.getRemitList();
        Remit remit = Remit.createRemit(RemitType.WITHDRAW, money, content, this);
        remitList.add(remit);
        return remit;
    }
}
