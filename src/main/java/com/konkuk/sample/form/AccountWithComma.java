package com.konkuk.sample.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountWithComma {
    private Long id;
    private String bankName; // 은행 이름
    private String accountNumber; // 계좌 번호
    private String balance; // 잔액

    public static AccountWithComma createAccountWithComma(Long id, String bankName, String accountNumber, Long balance){
        AccountWithComma account = new AccountWithComma();
        account.setId(id);
        account.setBankName(bankName);
        account.setAccountNumber(accountNumber);
        account.setBalanceWithComma(account, balance);
        return account;
    }

    public static void setBalanceWithComma(AccountWithComma account, Long balance){
        String newStringBalance = "";
        String stringBalance = String.valueOf(balance);
        int index = 0;
        int len = stringBalance.length() - 1;
        newStringBalance += stringBalance.substring(0, len % 3 + 1);
        index += len % 3 + 1;
        for (int i = 0; i < len / 3; i++) {
            newStringBalance += "," + stringBalance.substring(index, index + 3);
            index += 3;
        }
        account.setBalance(newStringBalance);
    }

    public static void main(String[] args) {
        // 테스트 코드
        System.out.println(createAccountWithComma(0L, "","",0L).getBalance());
        System.out.println(createAccountWithComma(0L, "","",90L).getBalance());
        System.out.println(createAccountWithComma(0L, "","",890L).getBalance());
        System.out.println(createAccountWithComma(0L, "","",7890L).getBalance());
        System.out.println(createAccountWithComma(0L, "","",67890L).getBalance());
        System.out.println(createAccountWithComma(0L, "","",567890L).getBalance());
        System.out.println(createAccountWithComma(0L, "","",4567890L).getBalance());
        System.out.println(createAccountWithComma(0L, "","",34567890L).getBalance());
        System.out.println(createAccountWithComma(0L, "","",234567890L).getBalance());
        System.out.println(createAccountWithComma(0L, "","",1234567890L).getBalance());
    }
}
