package com.konkuk.sample.form;

import com.konkuk.sample.domain.RemitType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class RemitWithCommaForm {
    private String date;
    private RemitType type;
    private String money;
    private String content;

    public static RemitWithCommaForm createRemitWithComma(LocalDateTime date, RemitType type, Long money, String content){
        RemitWithCommaForm remit = new RemitWithCommaForm();
        convertDate(remit, date);
        remit.setType(type);
        convertMoney(remit, money, type);
        remit.setContent(content);
        return remit;
    }

    public static void convertDate(RemitWithCommaForm remit, LocalDateTime date){
        remit.setDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
    }

    public static void convertMoney(RemitWithCommaForm remit, Long money, RemitType type){
        String newStringMoney = "";
        String stringMoney = String.valueOf(money);
        int index = 0;
        int len = stringMoney.length() - 1;
        newStringMoney += stringMoney.substring(0, len % 3 + 1);
        index += len % 3 + 1;
        for (int i = 0; i < len / 3; i++) {
            newStringMoney += "," + stringMoney.substring(index, index + 3);
            index += 3;
        }
        if(type.equals(RemitType.WITHDRAW)) remit.setMoney("-"+newStringMoney);
        else if(type.equals(RemitType.DEPOSIT)) remit.setMoney("+"+newStringMoney);
    }
}
