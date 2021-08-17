package com.konkuk.sample.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountForm {
    private String bankName; // 은행 이름
    private String accountNumber; // 계좌 번호
    private Long balance; // 잔액

}
