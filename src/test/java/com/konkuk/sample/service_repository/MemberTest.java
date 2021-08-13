package com.konkuk.sample.service_repository;

import com.konkuk.sample.domain.Member;
import com.konkuk.sample.domain.MemberToMember;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberTest {
	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;

	@Test
	@Rollback(true)
	// 생년월일과 이름을 입력하여 회원 가입
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

	@Test
	@Rollback(true)
	// 모든 회원 목록을 조회
	public void readAllTestInMemberRepository() {
		// GIVEN
		int num = 5;
		Member members[] = new Member[num];
		for (int i = 0; i < num; i++) {
			members[i] = Member.createMember("99090" + (i + 1), "name" + (i + 1));
			memberRepository.create(members[i]);
		}

		// WHEN
		List<Member> readMembers = memberRepository.readAll();

		// THEN
		Assertions.assertThat(num).isEqualTo(readMembers.size());
		for (int i = 0; i < num; i++) {
			Assertions.assertThat(readMembers.get(i)).isEqualTo(members[i]);
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