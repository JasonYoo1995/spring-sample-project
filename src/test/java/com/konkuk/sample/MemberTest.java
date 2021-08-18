package com.konkuk.sample;

import com.konkuk.sample.domain.Member;
import com.konkuk.sample.domain.MemberToMember;
import com.konkuk.sample.exception.MemberNotFoundException;
import com.konkuk.sample.repository.EventRepository;
import com.konkuk.sample.repository.MemberRepository;
import com.konkuk.sample.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberTest {
	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	EventRepository eventRepository;

	@Test
	@Rollback(true)
	// 생년월일과 이름을 입력하여 회원 가입
	// 특정 회원을 조회
	public void createAndReadOneTestInMemberRepository() {
		// GIVEN
		Member createMember = Member.createMember("990909", "김건국");

		// WHEN
		Long id = memberRepository.create(createMember);
		Member readMember = memberRepository.readOne(id);

		// THEN
		Assertions.assertThat(readMember.getId()).isEqualTo(createMember.getId());
		Assertions.assertThat(readMember.getBirth()).isEqualTo(createMember.getBirth());
		Assertions.assertThat(readMember.getName()).isEqualTo(createMember.getName());
		Assertions.assertThat(readMember).isEqualTo(createMember); // JPA 엔티티 동일성 보장
	}

	@Test(expected = MemberNotFoundException.class)
	@Rollback(true)
	public void getMemberByNameInMemberService(){
		// GIVEN
		Member createMember = Member.createMember("990909", "김건국");
		Long id = memberRepository.create(createMember);

		// WHEN : 있는 회원 조회
		Member existingMember = memberService.getMemberByName("김건국");

		// THEN
		Assertions.assertThat(existingMember).isNotNull();

		// WHEN : 없는 회원 조회
		Member notExistingMember = memberService.getMemberByName("나건국");

		// THEN
		fail("해당되는 회원이 존재하지 않습니다.");
	}

	@Test
	@Rollback(true)
	// Member 회원 가입 시 Event도 함께 생성
	public void signUpTestInMemberService() {
		// GIVEN
		String birth = "990909";
		String name = "김건국";

		// WHEN
		memberService.signUp(birth, name);

		// THEN
		Assertions.assertThat(eventRepository.readAll().size()).isEqualTo(1);
	}

	@Test
	@Rollback(true)
	// 모든 회원 목록을 조회
	public void readAllTestInMemberRepository() {
		// GIVEN
		int before = memberRepository.readAll().size();
		int num = 5;
		Member members[] = new Member[num];
		for (int i = 0; i < num; i++) {
			members[i] = Member.createMember("99090" + (i + 1), "name" + (i + 1));
			memberRepository.create(members[i]);
		}

		// WHEN
		List<Member> readMembers = memberRepository.readAll();

		// THEN
		int after = memberRepository.readAll().size();
		Assertions.assertThat(num).isEqualTo(after - before);
		for (int i = 0; i < num; i++) {
			Assertions.assertThat(readMembers.get(i + before)).isEqualTo(members[i]);
		}
	}

	@Test
	@Rollback(true)
	// 생년월일과 이름을 수정하여 저장 가능
	public void updateTestInMemberRepository() {
		// GIVEN
		Member createMember = Member.createMember("990909", "김건국");
		Long id = memberRepository.create(createMember);

		// WHEN
		Member updateMember = Member.createMember("850102", "최대학");
		updateMember.setId(id);
		memberRepository.update(updateMember);
		Member readMember = memberRepository.readOne(id);

		// THEN
		Assertions.assertThat(readMember.getBirth()).isEqualTo("850102");
		Assertions.assertThat(readMember.getName()).isEqualTo("최대학");
	}

	@Test
	@Rollback(true)
	// 자주 송금하는 회원 이름 등록 및 조회
	public void frequentMemberRegisterAndGetTestInMemberService() {
		// GIVEN
		Member registeringMember = Member.createMember("990909", "김건국");
		Long registeringMemberId = memberRepository.create(registeringMember);
		Member registeredMember1 = Member.createMember("940412", "나건국");
		Long registeredMemberId1 = memberRepository.create(registeredMember1);
		Member registeredMember2 = Member.createMember("961122", "이건국");
		Long registeredMemberId2 = memberRepository.create(registeredMember2);

		// WHEN
		MemberToMember.createMemberToMember(registeringMember, registeredMember1);
		memberService.registerFrequentMember(registeringMemberId, registeredMemberId1);
		MemberToMember.createMemberToMember(registeringMember, registeredMember1);
		memberService.registerFrequentMember(registeringMemberId, registeredMemberId2);

		List<Member> frequentMemberList = memberService.getFrequentMembers(registeringMember.getId());

		// THEN
		Assertions.assertThat(frequentMemberList.get(0)).isEqualTo(registeredMember1);
		Assertions.assertThat(frequentMemberList.get(1)).isEqualTo(registeredMember2);
	}
}