package com.konkuk.sample;

import com.konkuk.sample.domain.Account;
import com.konkuk.sample.domain.Member;
import com.konkuk.sample.domain.Remit;
import com.konkuk.sample.domain.RemitType;
import com.konkuk.sample.repository.AccountRepository;
import com.konkuk.sample.repository.MemberRepository;
import com.konkuk.sample.repository.RemitRepository;
import com.konkuk.sample.service.AccountService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccountTest {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	AccountService accountService;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	RemitRepository remitRepository;

	@Test
	@Rollback(true)
	// 특정 회원에 대하여 신규 계좌 생성
	public void createAndReadOneTestInAccountRepository() {
		// GIVEN
		Member member = Member.createMember("990909", "김건국");
		memberRepository.create(member);
		Account createAccount = Account.createAccount("신한", "110-123-456789", 0L, member);

		// WHEN
		Long id = accountRepository.create(createAccount);
		Account readAccount = accountRepository.readOne(id);

		// THEN
		Assertions.assertThat(readAccount).isEqualTo(createAccount); // JPA 엔티티 동일성 보장
	}

	@Test
	@Rollback(true)
	// 특정 회원의 모든 계좌 목록 조회
	public void readAllByMemberTestInAccountRepository() {
		// GIVEN : member1은 2개의 account를 개설하고, member2는 3개의 account를 개설했을 때
		Member member1 = Member.createMember("990909", "김건국");
		memberRepository.create(member1);

		Member member2 = Member.createMember("921231", "최대학");
		memberRepository.create(member2);

		int num1 = 2;
		Account accounts1[] = new Account[num1];
		for (int i = 0; i < num1; i++) {
			accounts1[i] = Account.createAccount("은행" + i, "110-123-45678" + i, 0L, member1);
			accountRepository.create(accounts1[i]);
		}

		int num2 = 3;
		Account accounts2[] = new Account[num2];
		for (int i = 0; i < num2; i++) {
			accounts2[i] = Account.createAccount("은행1" + i, "110-123-45679" + i, 0L, member2);
			accountRepository.create(accounts2[i]);
		}

		// WHEN : 두 member의 모든 계좌를 조회하면
		List<Account> readAccounts1 = accountRepository.readAllByMember(member1.getId());
		List<Account> readAccounts2 = accountRepository.readAllByMember(member2.getId());

		// THEN : 모든 계좌가 조회되어야 한다
		Assertions.assertThat(num1).isEqualTo(readAccounts1.size());
		for (int i = 0; i < num1; i++) {
			Assertions.assertThat(readAccounts1.get(i).getMember()).isEqualTo(member1);
		}

		Assertions.assertThat(num2).isEqualTo(readAccounts2.size());
		for (int i = 0; i < num2; i++) {
			Assertions.assertThat(readAccounts2.get(i).getMember()).isEqualTo(member2);
		}
	}

	@Test
	@Rollback(true)
	// 특정 회원에 대하여 특정 계좌 삭제
	public void deleteTestInAccountRepository() {
		// GIVEN : 1명의 member가 1개의 account를 개설했을 때
		Member member = Member.createMember("990909", "김건국");
		memberRepository.create(member);
		Account createAccount = Account.createAccount("신한", "110-123-456789", 0L, member);
		Long id = accountRepository.create(createAccount);

		// WHEN : account를 삭제하면
		accountRepository.delete(id);

		// THEN : 연관된 member는 삭제되지 않아야 한다
		Assertions.assertThat(member).isEqualTo(memberRepository.readOne(member.getId()));

		// WHEN : account가 삭제된 뒤 해당 account를 조회하면
		Account readAccount = accountRepository.readOne(id);

		// THEN : 조회할 수 없어야 한다
		Assertions.assertThat(readAccount).isNull();
	}

	@Test
	@Rollback(true)
	// 특정 회원의 모든 계좌에 대하여 잔액 표시
	public void readAllBalanceByMemberTestInAccountRepository(){
		// GIVEN
		Member member = Member.createMember("990909", "김건국");
		memberRepository.create(member);
		Account account1 = Account.createAccount("신한", "110-123-456789", 1000000L, member);
		Account account2 = Account.createAccount("기업", "321-123456-00-000", 300000L, member);
		accountRepository.create(account1);
		accountRepository.create(account2);

		// WHEN
		List<Long> balanceList = accountRepository.readAllBalanceByMember(member.getId());

		// THEN
		Assertions.assertThat(balanceList.get(0)).isEqualTo(1000000L);
		Assertions.assertThat(balanceList.get(1)).isEqualTo(300000L);
	}

	@Test
	@Rollback(true)
	// 특정 계좌에 대하여 모든 입출금 내역 출력
	public void getRemitListTestInAccountService(){
		// GIVEN
		Account account = Account.createAccount("신한", "110-123-456789", 1000000L, null);
		Long accountId = accountRepository.create(account);
		Remit remit1 = Remit.createRemit(RemitType.DEPOSIT, 10000L, "송금 테스트 1", account);
		Remit remit2 = Remit.createRemit(RemitType.WITHDRAW, 2000L, "송금 테스트 2", account);
		remitRepository.create(remit1);
		remitRepository.create(remit2);

		// WHEN
		List<Remit> remitList = accountService.getRemitList(accountId);

		// THEN
		Assertions.assertThat(remitList.get(0)).isEqualTo(remit1);
		Assertions.assertThat(remitList.get(1)).isEqualTo(remit2);
	}

	@Test
	@Rollback(true)
	// 특정 계좌에 대하여 입금 내역만 출력
	public void getDepositListTestInAccountService(){
		// GIVEN
		Account account = Account.createAccount("신한", "110-123-456789", 1000000L, null);
		Long accountId = accountRepository.create(account);
		Remit depositRemit1 = Remit.createRemit(RemitType.DEPOSIT, 30000L, "입금1", account);
		Remit depositRemit2 = Remit.createRemit(RemitType.DEPOSIT, 50000L, "입금2", account);
		Remit withdrawRemit = Remit.createRemit(RemitType.WITHDRAW, 2000L, "출금", account);
		remitRepository.create(depositRemit1);
		remitRepository.create(depositRemit2);
		remitRepository.create(withdrawRemit);

		// WHEN
		List<Remit> remitList = accountService.getDepositList(accountId);

		// THEN
		Assertions.assertThat(remitList.size()).isEqualTo(2);
		Assertions.assertThat(remitList.get(0)).isEqualTo(depositRemit1);
		Assertions.assertThat(remitList.get(1)).isEqualTo(depositRemit2);
	}

	@Test
	@Rollback(true)
	// 특정 계좌에 대하여 출금 내역만 출력
	public void getWithdrawListTestInAccountService(){
		// GIVEN
		Account account = Account.createAccount("신한", "110-123-456789", 1000000L, null);
		Long accountId = accountRepository.create(account);
		Remit depositRemit = Remit.createRemit(RemitType.DEPOSIT, 30000L, "입금", account);
		Remit withdrawRemit1 = Remit.createRemit(RemitType.WITHDRAW, 5000L, "출금1", account);
		Remit withdrawRemit2 = Remit.createRemit(RemitType.WITHDRAW, 2000L, "출금2", account);
		remitRepository.create(depositRemit);
		remitRepository.create(withdrawRemit1);
		remitRepository.create(withdrawRemit2);

		// WHEN
		List<Remit> remitList = accountService.getWithdrawList(accountId);

		// THEN
		Assertions.assertThat(remitList.size()).isEqualTo(2);
		Assertions.assertThat(remitList.get(0)).isEqualTo(withdrawRemit1);
		Assertions.assertThat(remitList.get(1)).isEqualTo(withdrawRemit2);
	}

	@Test
	@Rollback(true)
	// 송금
	public void remitTestInAccountService(){
		// GIVEN
		Account fromAccount = Account.createAccount("신한", "110-123-456789", 10000L, null);
		Account toAccount = Account.createAccount("신한", "110-123-000000", 50000L, null);
		Long fromAccountId = accountRepository.create(fromAccount);
		accountRepository.create(toAccount);

		// WHEN
		accountService.remit(fromAccountId, "110-123-000000", 888L, "송금 테스트");

		// THEN
		Assertions.assertThat(fromAccount.getBalance()).isEqualTo(10000L-888L);
		Assertions.assertThat(toAccount.getBalance()).isEqualTo(50000L+888L);
		Assertions.assertThat(fromAccount.getRemitList().get(0).getType()).isEqualTo(RemitType.WITHDRAW);
		Assertions.assertThat(toAccount.getRemitList().get(0).getType()).isEqualTo(RemitType.DEPOSIT);
	}
}