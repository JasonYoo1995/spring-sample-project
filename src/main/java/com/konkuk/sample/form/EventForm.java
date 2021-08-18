package com.konkuk.sample.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventForm {
    private boolean firstDeposit; // 첫 입금
    private boolean firstRemit; // 첫 송금
    private boolean firstShare; // 친구에게 앱 공유하기
}
